package common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 消费者购物车系统
 * 一个用户id对应多个商品id
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCartVo implements Serializable {

    private List<CustomerCartItemVo> customerCartItemVos;
    private String userKey;

}
