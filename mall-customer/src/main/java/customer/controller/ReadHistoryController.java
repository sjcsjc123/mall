package customer.controller;

import api.customer.ReadHistoryService;
import common.util.JwtTokenUtil;
import common.vo.ReadHistoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/history")
public class ReadHistoryController {

    @Autowired
    private ReadHistoryService readHistoryService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/create")
    public String create(@RequestParam("goods_id") Long goodsId,
                         HttpServletRequest request){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        readHistoryService.createHistory(username,goodsId);
        return "创建浏览记录成功";
    }

    @PostMapping("/select")
    public Page<ReadHistoryVo> select(HttpServletRequest request,
                                      @RequestParam("pageNum") int pageNum,
                                      @RequestParam("pageSize") int pageSize){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return readHistoryService.findByIdAndTime(username,pageNum,pageSize);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("ids") List<String> ids){
        readHistoryService.deleteByIds(ids);
        return "删除成功";
    }

    @PostMapping("/clear")
    public String clear(HttpServletRequest request){
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        readHistoryService.clear(username);
        return "清空浏览记录成功";
    }
}
