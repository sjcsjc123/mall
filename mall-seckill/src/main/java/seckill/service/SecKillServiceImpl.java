package seckill.service;

import api.seckill.SecKillService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.redisUtils.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import seckill.domain.SecKillGoods;
import seckill.domain.SecKillSession;
import seckill.mapper.SecKillGoodsMapper;
import seckill.mapper.SecKillSessionMapper;
import seckill.vo.SecKillGoodsVo;
import seckill.vo.SecKillSessionGoodsVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@DubboService
@Component
@Slf4j
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    private SecKillGoodsMapper secKillGoodsMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SecKillSessionMapper secKillSessionMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 秒杀
     * 采用redis扣减库存，减到0之后再将MySQL里的库存设置为0以减少和MySQL的交互
     * 在秒杀之前先将秒杀信息存入单独的另外一张表（专门存放所有秒杀活动信息）中
     */
    /**
     * window单个redis，不存放抢到商品用户信息，此时吞吐量为632
     * Linux集群redis，不存放抢到商品用户信息，此时吞吐量为253
     * @param goodsId
     * @throws InterruptedException
     */
    @Override
    public void beginSecKill(Long goodsId) throws InterruptedException {
        RLock lock = redissonClient.getLock("lock");
        boolean b = lock.tryLock(10, TimeUnit.SECONDS);
        if (b){
            if (lock.isHeldByCurrentThread()){
                int goodsNumber = (int) redisService.get("goodsNumber");
                if (goodsNumber <= 0){
                    if (lock != null){
                        lock.unlock();
                    }
                }else {
                    redisService.set("goodsNumber",goodsNumber-1);
                    if (lock!= null){
                        lock.unlock();
                    }
                }
            }else {
                log.info("线程发生了冲突");
            }
        }
    }

    @Override
    public int selectGoodsNumber(Long goodsId) {
        QueryWrapper<SecKillGoods> wrapper =
                new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId);
        return secKillGoodsMapper.selectOne(wrapper).getGoodsNumber();
    }

    @Override
    public void uploadSecKillGoods() {
        //查询最近三天秒杀活动和秒杀商品
        QueryWrapper<SecKillSession> wrapper = new QueryWrapper<>();
        wrapper.between("start_time",startTime(),endTime());
        List<SecKillSession> secKillSessions =
                secKillSessionMapper.selectList(wrapper);
        //将秒杀活动信息存入redis中
        List<SecKillSessionGoodsVo> secKillSessionGoodsVos =
                saveSecKillSessions(secKillSessions);
        saveSecKillGoods(secKillSessionGoodsVos);
    }

    private void saveSecKillGoods(List<SecKillSessionGoodsVo> secKillSessionGoodsVos) {
        for (SecKillSessionGoodsVo secKillSessionGoodsVo :
                secKillSessionGoodsVos) {
            List<SecKillGoods> secKillGoods =
                    secKillSessionGoodsVo.getSecKillGoods();
            for (SecKillGoods secKillGood : secKillGoods) {
                String goodsKey = "skuId:"+secKillGood.getSkuId()+
                        "spuId:"+secKillGood.getSpuId();
                BoundHashOperations<String, String, String> ops =
                        redisTemplate.boundHashOps("secKill eventId:"+secKillSessionGoodsVo.getEventId()+
                                "placeId:"+secKillSessionGoodsVo.getPlaceId());
                if (ops.hasKey(goodsKey)){
                }else {
                    SecKillGoodsVo secKillGoodsVo = new SecKillGoodsVo();
                    secKillGoodsVo.setSecKillGoods(secKillGood);
                    String token = UUID.randomUUID().toString().replace("-"
                            , "");
                    secKillGoodsVo.setToken(token);
                    ops.put(goodsKey,JSON.toJSONString(secKillGoodsVo));
                    //将库存设为信号量分布式锁
                    RSemaphore semaphore = redissonClient.getSemaphore(
                            "semaphore"+token);
                    semaphore.trySetPermits(secKillGood.getGoodsNumber());
                    Date nowTime = new Date();
                    Date endTime = secKillSessionGoodsVo.getEndTime();
                    semaphore.expire(endTime.getTime()-nowTime.getTime(),
                            TimeUnit.SECONDS);
                }
            }
        }
    }

    private List<SecKillSessionGoodsVo> saveSecKillSessions(List<SecKillSession> secKillSessions) {
        List<SecKillSessionGoodsVo> secKillSessionGoodsVos = new ArrayList<>();
        for (SecKillSession secKillSession : secKillSessions) {
            Long placeId = secKillSession.getPlaceId();
            //根据场次id查找所有需要上架的商品
            QueryWrapper<SecKillGoods> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("place_id",placeId);
            wrapper1.eq("event_id",secKillSession.getEventId());
            List<SecKillGoods> secKillGoods =
                    secKillGoodsMapper.selectList(wrapper1);
            SecKillSessionGoodsVo secKillSessionGoodsVo = new SecKillSessionGoodsVo();
            secKillSessionGoodsVo.setSecKillGoods(secKillGoods);
            secKillSessionGoodsVo.setPlaceId(placeId);
            secKillSessionGoodsVo.setEventId(secKillSession.getEventId());
            secKillSessionGoodsVo.setEndTime(secKillSession.getEndTime());
            secKillSessionGoodsVos.add(secKillSessionGoodsVo);
            //将秒杀活动信息存入redis中
            BoundHashOperations<String, String, String> ops =
                    redisTemplate.boundHashOps("secKill event"+secKillSession.getEventId());
            if (ops.hasKey("place_id"+secKillSession.getPlaceId())){
            }else {
                ops.put("place_id"+secKillSession.getPlaceId(),
                        JSON.toJSONString(secKillSession));
            }
        }
        return secKillSessionGoodsVos;
    }

    /**
     * 最近三天的开始时间
     * @return
     */
    private String startTime() {
        LocalDate now = LocalDate.now();
        String start = LocalDateTime.of(now, LocalTime.MIN)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return start;
    }

    /**
     * 最近三天的结束时间
     * @return
     */
    private String endTime() {
        LocalDate after2Days = LocalDate.now().plusDays(2);
        String end = LocalDateTime.of(after2Days, LocalTime.MAX)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return end;
    }

}
