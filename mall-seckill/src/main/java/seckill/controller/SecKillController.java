package seckill.controller;

import api.seckill.SecKillService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.redisUtils.RedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import seckill.domain.SecKillGoods;
import seckill.mapper.SecKillGoodsMapper;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/secKill")
public class SecKillController {

    @Autowired
    private SecKillService secKillService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SecKillGoodsMapper secKillGoodsMapper;
    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/begin")
    public void begin(@RequestParam("goods_id") Long goodsId) throws InterruptedException {
        /**
         * 应该在上架秒杀商品之前统计商品库存并放入redis中
         */
        /**/
        secKillService.beginSecKill(goodsId);
    }

    @RequestMapping("/before/{goods_id}")
    public void before(@PathVariable("goods_id") Long goodsId){
        int goodsNumber = secKillService.selectGoodsNumber(goodsId);
        System.out.println("此时数量为"+goodsNumber);
        redisService.set("goodsNumber",goodsNumber);
        int goodsNumber1 = (int) redisService.get("goodsNumber");
        System.out.println(goodsNumber1);
    }

    @RequestMapping("/after/{goods_id}")
    public void after(@PathVariable("goods_id") Long goodsId){
        int goodsNumber = (int) redisService.get("goodsNumber");
        QueryWrapper<SecKillGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId);
        SecKillGoods secKillGoods = secKillGoodsMapper.selectOne(wrapper);
        secKillGoods.setGoodsNumber(goodsNumber);
        secKillGoodsMapper.update(secKillGoods,wrapper);
    }

    /**
     * 上架最近三天的商品
     */
    @Scheduled(cron = "*/30 * * * * *")
    @RequestMapping("/upload")
    public String upload(){
        RLock lock = redissonClient.getLock("secKill-lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            secKillService.uploadSecKillGoods();
        }catch (Exception e){
        }finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return "上架成功";
    }

}
