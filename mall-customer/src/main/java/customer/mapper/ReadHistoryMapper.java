package customer.mapper;

import common.vo.ReadHistoryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReadHistoryMapper extends MongoRepository<ReadHistoryVo,Long> {

    /**
     * 根据用户id查找记录
     */
    Page<ReadHistoryVo> findAllByCustomerNameOrderByCreateTimeDesc(String customerName,
                                                                   Pageable pageable);

    /**
     * 根据用户id删除记录
     */
    void deleteAllByCustomerName(String customerName);

    /**
     * 根据用户id以及商品id查找记录
     */
    ReadHistoryVo findByGoodsIdAndCustomerName(Long goodsId, String customerName);
}
