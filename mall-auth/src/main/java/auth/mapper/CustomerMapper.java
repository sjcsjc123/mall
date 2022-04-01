package auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {
}
