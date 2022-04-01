package business.filter;

import common.exception.constant.MyProjectExceptionEnum;
import common.exception.MyProjectException;
import common.redisUtils.RedisService;
import common.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest.getRequestURI().contains("business")) {
            log.info("请求路径判定为可以直接放行");
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }else {
            String token =
                    httpServletRequest.getHeader(JwtTokenUtil.AUTH_HEADER_KEY);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            String redisToken =
                    (String) redisService.get(JwtTokenUtil.AUTH_HEADER_KEY + username);
            log.info("路径判断不通过，开始校验token");
            if (!redisToken.equals(token) || jwtTokenUtil.isTokenExpire(redisToken) ||
                    jwtTokenUtil.isTokenExpire(token) || username == null){
                log.info("登录已失效，请重新登陆");
                throw new MyProjectException(MyProjectExceptionEnum.TOKEN_IS_EXPIRE);
            }
            log.info("token校验成功,开始校验用户");
            log.info("此时校验用户为:"+username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("给用户授予权限:"+username);
                System.out.println(authenticationToken);
            }
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
}
