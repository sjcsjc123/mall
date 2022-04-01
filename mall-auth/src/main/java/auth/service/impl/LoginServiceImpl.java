package auth.service.impl;

import auth.mapper.CustomerMapper;
import auth.mapper.CustomerRoleMapper;
import api.auth.LoginService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import common.exception.constant.MyProjectExceptionEnum;
import common.domian.Customer;
import common.domian.CustomerRole;
import common.exception.MyProjectException;
import common.redisUtils.RedisService;
import common.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CustomerRoleMapper customerRoleMapper;

    @Override
    public void login(String username, String password) {
        /*UserDetails userDetails = loadUserByUsername(username);
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
        return token;*/
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_name",username);
        Customer customer = customerMapper.selectOne(wrapper);
        String password1 = customer.getCustomerPassword();
        if (customer == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        if (!password.equals(password1)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
    }

    @Override
    public void register(Customer customer) {
        QueryWrapper<Customer> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("customer_phone",customer.getCustomerPhone());
        int countPhone = customerMapper.selectCount(wrapper1);
        QueryWrapper<Customer> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("customer_name",customer.getCustomerName());
        int countUsername = customerMapper.selectCount(wrapper1);
        if (customer.getCustomerName().isEmpty()){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_USER_NULL);
        }
        if (countUsername >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.USER_NO_ONLY);
        }
        if (customer.getCustomerPhone().length()!=11) {
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_LENGTH);
        }
        String phoneConfig = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\\\d{8})$";
        if (customer.getCustomerPhone().matches(phoneConfig)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PHONE_DATA_TYPE);
        }
        if (countPhone >= 1){
            throw new MyProjectException(MyProjectExceptionEnum.PHONE_NO_ONLY);
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
        if (!customer.getCustomerPassword().matches(regex)){
            throw new MyProjectException(MyProjectExceptionEnum.INVALID_PASSWORD);
        }
        customer.setCustomerMoney(0f);
        customer.setCustomerRegTime(new Date());
        customer.setCustomerLastLogin(new Date());
        customer.setCustomerStatus(1);
        customer.setCustomerScore(0);
        customer.setStatus(1);
        CustomerRole customerRole = new CustomerRole();
        customerRole.setCustomerName(customer.getCustomerName());
        customerRole.setRole("common");
        customerRoleMapper.insert(customerRole);
        customerMapper.insert(customer);
    }

    /*public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_name",username);
        Customer customer = customerMapper.selectOne(wrapper);
        String password = customer.getCustomerPassword();
        if (customer == null){
            throw new MyProjectException(MyProjectExceptionEnum.USERNAME_IS_EMPTY);
        }
        customer.setCustomerLastLogin(new Date());
        customerMapper.update(customer,wrapper);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("common"));
        return new User(username,passwordEncoder.encode(password),authorities);
    }*/
}
