package order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.TradeOrder;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeOrderMapper extends BaseMapper<TradeOrder> {

    @Update("update from trade_order set order_status = 2 where order_id = " +
            "#{order_id}")
    void chancelNoPaid(Long orderId);

    @Update("update from trade_order set order_status = 4 where order_id = " +
            "#{order_id}")
    void chancelPaid(Long orderId);
}
