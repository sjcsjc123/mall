package common.vo;

import lombok.Data;

/**
 * 未登录状态下的临时用户
 */
@Data
public class TempUserVo {

    private Long id;
    private String userKey;
    private boolean first;
}
