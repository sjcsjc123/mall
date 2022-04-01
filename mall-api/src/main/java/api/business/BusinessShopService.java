package api.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import common.domian.BusinessShop;

import java.util.List;

/**
 * 用于商家操作店铺的一系列操作
 */
public interface BusinessShopService {

    /**
     * 添加店铺
     */
    void insert(BusinessShop businessShop);

    /**
     * 更改店铺
     */
    void update(BusinessShop businessShop);

    /**
     * 下架店铺
     */
    void delete(Long shopId);

    /**
     * 查询店铺
     */
    BusinessShop select(Long shopId);

    /**
     * 查询商家名下所有店铺
     */
    IPage<BusinessShop> selectAllByBusinessName(String username);
}
