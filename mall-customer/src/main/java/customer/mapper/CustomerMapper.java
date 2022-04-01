package customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import common.domian.Customer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    @Update("update from customer set customer_money = customer_money - " +
            "#{moneyPaid} where id = #{id}")
    void updateCustomerMoney(@Param("moneyPaid") float moneyPaid,
                             @Param("id") Long id);

    @Select("select id from customer where customer_name = #{username}")
    Long selectIdByUsername(String username);
}
