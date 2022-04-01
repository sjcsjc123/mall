package business.service;

import api.business.BusinessService;
import business.mapper.BusinessMapper;
import business.mapper.BusinessRoleMapper;
import business.security.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.Business;
import common.domian.BusinessRole;
import common.exception.MyProjectException;
import common.redisUtils.RedisService;
import common.util.JwtTokenUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService
@Component
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessMapper businessMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BusinessRoleMapper businessRoleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if (userDetails == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //放入线程池
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenUtil.createToken(username);
        redisService.set(JwtTokenUtil.AUTH_HEADER_KEY+username,token);
        return token;
    }

    @Override
    public void register(Business business) {
        QueryWrapper<Business> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("business_phone",business.getBusinessPhone());
        int countPhone = businessMapper.selectCount(wrapper1);
        QueryWrapper<Business> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("business_name",business.getBusinessName());
        int countUsername = businessMapper.selectCount(wrapper1);
        if (business.getBusinessName().isEmpty()){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_USER_NULL);
        }
        if (countUsername >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.USER_NO_ONLY);
        }
        if (business.getBusinessPhone().length()!=11) {
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_LENGTH);
        }
        String phoneConfig = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\\\d{8})$";
        if (business.getBusinessPhone().matches(phoneConfig)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_DATA_TYPE);
        }
        if (countPhone >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.PHONE_NO_ONLY);
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        if (!business.getBusinessPassword().matches(regex)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PASSWORD);
        }
        business.setBusinessRegTime(new Date());
        business.setBusinessStatus(1);
        business.setBusinessScore(0);
        business.setStatus(1);
        BusinessRole businessRole = new BusinessRole();
        businessRole.setBusinessName(business.getBusinessName());
        businessRole.setRole("common");
        businessRoleMapper.insert(businessRole);
        businessMapper.insert(business);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Business> wrapper = new QueryWrapper<>();
        wrapper.eq("business_name",username);
        Business business = businessMapper.selectOne(wrapper);
        String password = business.getBusinessPassword();
        if (business == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        business.setBusinessLastLogin(new Date());
        businessMapper.update(business,wrapper);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("common"));
        return new User(username,passwordEncoder.encode(password),authorities);
    }
}
