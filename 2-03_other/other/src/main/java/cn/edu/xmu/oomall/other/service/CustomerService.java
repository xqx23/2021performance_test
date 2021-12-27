package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.CustomerDao;
import cn.edu.xmu.oomall.other.model.bo.Customer;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import cn.edu.xmu.oomall.other.model.vo.customer.*;
import cn.edu.xmu.privilegegateway.annotation.util.Common;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * 顾客Service
 *
 * @author 李智樑
 * @date 2021/12/3
 */
@Service
public class CustomerService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private RedisUtil redisUtil;
    @Value("${privilegeservice.login.jwtExpire}")
    private Integer jwtExpireTime;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private JwtHelper jwtHelper = new JwtHelper();
    /**
     * 登录token的redis key: login_id
     */
    private final static String LOGINKEY = "login_%d";

    public ReturnObject getCustomerStates() {
        return customerDao.getCustomerState();
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getCustomerById(Long id) {
        ReturnObject returnObject = customerDao.getCustomerById(id);
        CustomerRetVo vo = cloneVo(returnObject.getData(), CustomerRetVo.class);
        return new ReturnObject(vo);
    }

    @Transactional
    public ReturnObject modifyCustomerById(Long userId, Customer customer) {
        Customer cus = (Customer) customerDao.getCustomerById(userId).getData();

        if (cus == null) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
        }

        CustomerPo po = cloneVo(customer, CustomerPo.class);
        po.setId(userId);

        ReturnObject ret = customerDao.updateCustomer(po);
        return ret;
    }

    /**
     * @author jxy
     * @create 2021/12/11 10:20 PM
     */
    @Transactional
    public ReturnObject modifyCustomerPointsById(Long customerId, Long points) {
        return customerDao.changePoints(customerId, points);
    }


    /**
     * @author jxy
     * @create 2021/12/4 11:31 上午
     */
    @Transactional
    public ReturnObject<Object> resetPassword(CustomerResetPasswordVo vo) {
        return customerDao.resetPassword(vo);
    }

    /**
     * @author jxy
     * @create 2021/12/4 4:35 PM
     */
    @Transactional
    public ReturnObject<Object> modifyPassword(CustomerModifyPasswordVo vo, Long loginUser, String loginName) {
        return customerDao.modifyPassword(vo, loginUser, loginName);
    }

    /**
     * @author jxy
     * @create 2021/12/4 7:03 PM
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getAllCustomers(String userName, String email, String mobile, Integer page, Integer pageSize) {
        List<CustomerPo> customerPos = (List<CustomerPo>) customerDao.selectAllCustomers(userName, email, mobile, page, pageSize).getData();
        PageInfo<CustomerPo> customerRetVoPageInfo = PageInfo.of(customerPos);
        return new ReturnObject<>(customerRetVoPageInfo);
    }

    /**
     * @author jxy
     * @create 2021/12/4 10:48 PM
     */
    @Transactional
    public ReturnObject banCustomer(Long id, Long loginUser, String loginName) {

        ReturnObject ret = customerDao.changeCustomerState(id, loginUser, loginName, Customer.State.FORBID);
        kickOutUser(id);
        return ret;

    }

    /**
     * @author jxy
     * @create 2021/12/5 10:05 AM
     */
    @Transactional
    public ReturnObject releaseCustomer(Long id, Long loginUser, String loginName) {

        ReturnObject ret = customerDao.changeCustomerState(id, loginUser, loginName, Customer.State.NORMAL);
        return ret;

    }

    /**
     * @author jxy
     * @create 2021/12/5 11:06 AM
     */

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject signCustomer(CustomerSignUpVo customerSignUpVo) {
        CustomerPo customerPo = Common.cloneVo(customerSignUpVo, CustomerPo.class);
        customerPo.setBeDeleted((byte) 0);
        customerPo.setPoint(0L);
        ReturnObject ret = customerDao.createNewCustomerByPo(customerPo);
        if (ret.getData() == null) {
            return ret;
        }
        customerPo = (CustomerPo) ret.getData();
        SimpleCustomerRetVo simpleCustomerRetVo = Common.cloneVo(customerPo, SimpleCustomerRetVo.class);

        return new ReturnObject(simpleCustomerRetVo);
    }

    /**
     * @author jxy
     * @create 2021/12/5 1:53 PM
     */

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject login(CustomerLoginVo vo) {

        //获得user对象
        ReturnObject retObj = customerDao.login(vo);
        if (retObj.getCode() != ReturnNo.OK) {
            return retObj;
        }
        CustomerPo customerPo = (CustomerPo) retObj.getData();
        String token = jwtHelper.createToken(customerPo.getId(), customerPo.getUserName(), customerPo.getId(), 0, jwtExpireTime);
        String key = String.format(LOGINKEY, customerPo.getId());
        redisUtil.set(key, token, jwtExpireTime);
        return new ReturnObject<>(token);
    }

    /**
     * @author jxy
     * @create 2021/12/8 7:37 PM
     */

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject logout(Long customerId) {
        String key = String.format(LOGINKEY, customerId);
        redisUtil.del(key);
        return new ReturnObject<>();
    }

    /**
     * @author jxy
     * @create 2021/12/8 7:41 PM
     */

    @Transactional(rollbackFor = Exception.class)
    public void kickOutUser(Long id) {
        String key = String.format(LOGINKEY, id);
        if (redisUtil.hasKey(key)) {
            Serializable serializable=redisUtil.get(key);
            String jwt = serializable.toString();
            redisUtil.del(key);
            /* 将旧JWT加入需要踢出的集合 */
        }
    }


}
