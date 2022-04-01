package goods.service;

import api.goods.GoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.domian.TradeGoods;
import common.domian.TradeGoodsNumberLog;
import goods.mapper.GoodsMapper;
import goods.mapper.GoodsNumberLogMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsNumberLogMapper goodsNumberLogMapper;

    @Override
    public void insert(TradeGoods goods) {
        goodsMapper.insert(goods);
    }

    @Override
    public void update(TradeGoods goods) {
        QueryWrapper<TradeGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goods.getGoodsId());
        goodsMapper.update(goods,wrapper);
    }

    @Override
    public void delete(Long goodsId) {
        QueryWrapper<TradeGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId);
        goodsMapper.delete(wrapper);
    }

    @Override
    public void reduceGoodsNumber(TradeGoodsNumberLog tradeGoodsNumberLog) {
        goodsMapper.reduceGoodsNumber(tradeGoodsNumberLog.getGoodsNumber(),
                tradeGoodsNumberLog.getGoodsId());
        goodsNumberLogMapper.insert(tradeGoodsNumberLog);
    }

    @Override
    public void chancelReduce(Long goodsId,int goodsNumber) {
        goodsMapper.reduceGoodsNumber(-goodsNumber,goodsId);
        QueryWrapper<TradeGoodsNumberLog> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId);
        goodsNumberLogMapper.delete(wrapper);
    }

    @Override
    public TradeGoods selectOne(Long goodsId) {
        QueryWrapper<TradeGoods> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id",goodsId);
        return goodsMapper.selectOne(wrapper);
    }
}
