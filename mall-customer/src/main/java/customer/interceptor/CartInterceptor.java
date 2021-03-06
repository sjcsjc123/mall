package customer.interceptor;

import common.util.JwtTokenUtil;
import common.vo.TempUserVo;
import customer.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 购物车拦截器，实现未登录和登录的区分
 */
public class CartInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<TempUserVo> threadLocal = new ThreadLocal<>();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TempUserVo tempUserVo = new TempUserVo();
        String token = request.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
        if (token != null){
            //表示用户已经登录
            String username = jwtTokenUtil.getUsernameFromToken(token);
            tempUserVo.setId(customerMapper.selectIdByUsername(username));
        }
        //用户未登录
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-key")){
                    tempUserVo.setUserKey(cookie.getValue());
                }
            }
        }
        //即未登录也无user-key
        if (tempUserVo.getUserKey()== null){
            String userKey = UUID.randomUUID().toString().replace("-", "");
            tempUserVo.setUserKey(userKey);
            //标记第一次访问
            tempUserVo.setFirst(true);
        }
        threadLocal.set(tempUserVo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        TempUserVo tempUserVo = threadLocal.get();
        //如果是第一次访问
        if (tempUserVo.isFirst()){
            String userKey = tempUserVo.getUserKey();
            Cookie cookie = new Cookie("user-key", userKey);
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
        }
    }
}
