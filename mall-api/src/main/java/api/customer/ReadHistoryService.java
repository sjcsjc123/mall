package api.customer;

import common.vo.ReadHistoryVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 消费者浏览记录操作（mongodb）
 */
public interface ReadHistoryService {

    /**
     * 创建浏览记录
     */
    void createHistory(String username,Long goodsId);

    /**
     * 查询浏览记录
     * @return
     */
    Page<ReadHistoryVo> findByIdAndTime(String username, int pageNum,
                                        int pageSize);

    /**
     * 批量删除浏览记录
     */
    void deleteByIds(List<String> id);

    /**
     * 清空浏览记录
     */
    void clear(String username);
}
