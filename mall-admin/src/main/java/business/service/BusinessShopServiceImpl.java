package business.service;

import api.business.BusinessShopService;
import business.mapper.BusinessShopMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import common.domian.BusinessShop;
import common.util.IDWorker;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DubboService
@Component
public class BusinessShopServiceImpl implements BusinessShopService {

    @Autowired
    private BusinessShopMapper businessShopMapper;
    @Autowired
    private IDWorker idWorker;

    @Override
    public void insert(BusinessShop businessShop) {
        businessShop.setShopId(idWorker.nextId());
        businessShopMapper.insert(businessShop);
    }

    @Override
    public void update(BusinessShop businessShop) {
        businessShopMapper.updateById(businessShop);
    }

    @Override
    public void delete(Long shopId) {
        businessShopMapper.deleteById(shopId);
    }

    @Override
    public BusinessShop select(Long shopId) {
        return businessShopMapper.selectById(shopId);
    }

    @Override
    public IPage<BusinessShop> selectAllByBusinessName(String username) {
        IPage<BusinessShop> page = new Page<>();
        page.setPages(10);
        page.setSize(10);
        QueryWrapper<BusinessShop> wrapper = new QueryWrapper<>();
        wrapper.eq("business_name",username);
        return businessShopMapper.selectPage(page, wrapper);

    }
}
