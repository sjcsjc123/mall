package coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.TradeCoupon;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMapper extends BaseMapper<TradeCoupon> {
}
