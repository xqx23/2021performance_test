package cn.edu.xmu.oomall.other.dao;


import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.CustomerPoMapper;
import cn.edu.xmu.oomall.other.model.bo.Customer;
import cn.edu.xmu.oomall.other.model.po.CustomerPo;
import cn.edu.xmu.oomall.other.model.po.CustomerPoExample;
import cn.edu.xmu.oomall.other.model.vo.customer.*;
import cn.edu.xmu.privilegegateway.annotation.util.Common;
import cn.edu.xmu.privilegegateway.annotation.util.RandomCaptcha;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
import cn.edu.xmu.privilegegateway.annotation.util.bloom.BloomFilter;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;
import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoModifiedFields;


/**
 * @author 李智樑
 * @date 2021/12/3
 */
@Repository
public class CustomerDao implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDao.class);

    @Autowired
    private CustomerPoMapper customerPoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    BloomFilter<String> stringBloomFilter;

    final String EMAILFILTER = "NewUserEmailBloomFilter";
    final String MOBILEFILTER = "NewUserMobileBloomFilter";
    final String NAMEFILTER = "NewUserNameBloomFilter";

    @Value("${privilegeservice.bloomfilter.new-user-email.error-rate}")
    private double emailError;
    @Value("${privilegeservice.bloomfilter.new-user-email.capacity}")
    private int emailCapacity;

    @Value("${privilegeservice.bloomfilter.new-user-name.error-rate}")
    private double nameError;
    @Value("${privilegeservice.bloomfilter.new-user-name.capacity}")
    private int nameCapacity;

    @Value("${privilegeservice.bloomfilter.new-user-mobile.error-rate}")
    private double mobileError;
    @Value("${privilegeservice.bloomfilter.new-user-mobile.capacity}")
    private int mobileCapacity;
    /**
     * 验证码的redis key: cp_id
     */
    private final static String CAPTCHAKEY = "cp_%s";

    public ReturnObject getCustomerState() {
        List<Map<String, Object>> stateList = new ArrayList<>();
        for (Customer.State states : Customer.State.values()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("code", states.getCode());
            temp.put("name", states.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    /**
     * 初始化布隆过滤器
     *
     * @throws Exception createdBy: LiangJi3229 2020-11-10 18:41
     *                   modifiedBy: Ming Qiu 2021-11-21 05:42
     */
    @Override
    public void afterPropertiesSet() throws Exception {
//        stringBloomFilter.deleteFilter(EMAILFILTER);
//        stringBloomFilter.deleteFilter(NAMEFILTER);
//        stringBloomFilter.deleteFilter(MOBILEFILTER);
//        if (!stringBloomFilter.checkFilter(EMAILFILTER)) {
//            stringBloomFilter.newFilter(EMAILFILTER, emailError, emailCapacity);
//        }
//
//        if (!stringBloomFilter.checkFilter(NAMEFILTER)) {
//            stringBloomFilter.newFilter(NAMEFILTER, nameError, nameCapacity);
//        }
//
//        if (!stringBloomFilter.checkFilter(MOBILEFILTER)) {
//            stringBloomFilter.newFilter(MOBILEFILTER, mobileError, mobileCapacity);
//        }
//        CustomerPoExample customerPoExample = new CustomerPoExample();
//        List<CustomerPo> customerPos = null;
//        customerPos = customerPoMapper.selectByExample(customerPoExample);
//        if (customerPos != null) {
//            int i=0;
//            for (CustomerPo customerPo : customerPos) {
//                if (customerPo.getUserName() != null) {
//                    stringBloomFilter.addValue(NAMEFILTER, customerPo.getUserName());
//                }
//                if (customerPo.getEmail() != null) {
//                    stringBloomFilter.addValue(EMAILFILTER, customerPo.getEmail());
//                }
//                if (customerPo.getMobile() != null) {
//                    stringBloomFilter.addValue(MOBILEFILTER, customerPo.getMobile());
//                }
//                System.out.println(i);
//                i++;
//            }
//            System.out.println("finished");
//        }

    }

    /**
     * @author jxy
     * @create 2021/12/4 4:36 PM
     */


    public ReturnObject<Object> resetPassword(CustomerResetPasswordVo vo) {

        //验证邮箱、手机号

        CustomerPoExample customerPoExample = new CustomerPoExample();
        List<CustomerPo> customerPos = null;

        try {
            CustomerPoExample.Criteria criteria_email = customerPoExample.createCriteria();
            criteria_email.andEmailEqualTo(vo.getName());
            CustomerPoExample.Criteria criteria_phone = customerPoExample.createCriteria();
            criteria_phone.andMobileEqualTo(vo.getName());
            CustomerPoExample.Criteria criteria_username = customerPoExample.createCriteria();
            criteria_username.andUserNameEqualTo(vo.getName());
            customerPoExample.or(criteria_phone);
            customerPoExample.or(criteria_username);
            customerPos = customerPoMapper.selectByExample(customerPoExample);
            if (customerPos.isEmpty()) {
                return new ReturnObject<>(ReturnNo.CUSTOMERID_NOTEXIST);
            }

        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }

        //随机生成验证码
        String captcha = RandomCaptcha.getRandomString(6);
        Long id = customerPos.get(0).getId();
        String key = String.format(CAPTCHAKEY, captcha);
        redisUtil.set(key, id, 30L);

        return new ReturnObject<>(new ResetPwdRetVo(captcha));
    }

    /**
     * @author jxy
     * @create 2021/12/4 4:37 PM
     */
    public ReturnObject<Object> modifyPassword(CustomerModifyPasswordVo modifyPwdVo, Long loginUser, String loginName) {


        String key = String.format(CAPTCHAKEY, modifyPwdVo.getCaptcha());
        //通过验证码取出id
        if (!redisUtil.hasKey(key))
            return new ReturnObject<>(ReturnNo.CUSTOMERID_NOTEXIST);
        Long id = null;
        Serializable serializable = redisUtil.get(key);
        if (serializable == null) id = null;
        else {
            id = Long.valueOf(serializable.toString());
        }
        ReturnObject returnObject=getCustomerPoById(id);
        if (returnObject.getCode() != ReturnNo.OK)
            return returnObject;

        CustomerPo customerPo = (CustomerPo) returnObject.getData();

        //新密码与原密码相同
        if (customerPo.getPassword().equals(modifyPwdVo.getNewPassword()))
            return new ReturnObject<>(ReturnNo.CUSTOMER_PASSWORDSAME);
        customerPo.setPassword(modifyPwdVo.getNewPassword());
        //更新数据库
        try {
            customerPoMapper.updateByPrimaryKeySelective(customerPo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
        return new ReturnObject<>(ReturnNo.OK);
    }


    public ReturnObject createNewCustomerByPo(CustomerPo customerPo) {

        ReturnObject returnObject = null;
//        if (returnObject.getCode() != ReturnNo.OK) return returnObject;
        try {
            if (customerPo.getEmail() != null) {
                CustomerPoExample example = new CustomerPoExample();
                CustomerPoExample.Criteria criteria = example.createCriteria();
                criteria.andEmailEqualTo(customerPo.getEmail());
                List<CustomerPo> results = customerPoMapper.selectByExample(example);
                if (results.size() != 0) {
                    return new ReturnObject(ReturnNo.CUSTOMER_EMAILEXIST);
                }
            }
            if (customerPo.getUserName() != null) {
                CustomerPoExample example = new CustomerPoExample();
                CustomerPoExample.Criteria criteria = example.createCriteria();
                criteria.andUserNameEqualTo(customerPo.getUserName());
                List<CustomerPo> results = customerPoMapper.selectByExample(example);
                if (results.size() != 0) {
                    return new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST);
                }
            }

            if (customerPo.getMobile() != null) {
                CustomerPoExample example = new CustomerPoExample();
                CustomerPoExample.Criteria criteria = example.createCriteria();
                criteria.andMobileEqualTo(customerPo.getMobile());
                List<CustomerPo> results = customerPoMapper.selectByExample(example);
                if (results.size() != 0) {
                    return new ReturnObject(ReturnNo.CUSTOMER_MOBILEEXIST);
                }
            }
//            stringBloomFilter.addValue(NAMEFILTER, customerPo.getUserName());
//            stringBloomFilter.addValue(EMAILFILTER, customerPo.getEmail());
//            stringBloomFilter.addValue(MOBILEFILTER, customerPo.getMobile());
            customerPo.setState((byte) 0);
            customerPo.setGmtCreate(LocalDateTime.now());
            customerPoMapper.insert(customerPo);
            returnObject = new ReturnObject<>(customerPo);
            logger.debug("success trying to insert newUser");
        }
        //catch exception by unique index
//        catch (DuplicateKeyException e) {
//            logger.debug("failed trying to insert newUser");
//            //e.printStackTrace();
//            String info = e.getMessage();
//            if (info.contains("user_name_uindex")) {
//                return new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST);
//            }
//            if (info.contains("email_uindex")) {
//                return new ReturnObject(ReturnNo.CUSTOMER_EMAILEXIST);
//            }
//            if (info.contains("mobile_uindex")) {
//                return new ReturnObject(ReturnNo.CUSTOMER_MOBILEEXIST);
//            }
//        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error("Internal error Happened:" + e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
        return returnObject;

    }

    public ReturnObject login(CustomerLoginVo customerLoginVo) {

        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(customerLoginVo.getUserName());
        try {
            if (customerPoMapper.selectByExample(example).size() == 0) {
                logger.debug("User not exist:" + customerLoginVo.getUserName() + "," + customerLoginVo.getPassword());
                return new ReturnObject<>(ReturnNo.CUSTOMER_INVALID_ACCOUNT);
            }

            CustomerPo returnCustomerPo = customerPoMapper.selectByExample(example).get(0);

            if (returnCustomerPo.getPassword().contentEquals(customerLoginVo.getPassword())) {
                if (Customer.State.getTypeByCode(Integer.valueOf(returnCustomerPo.getState())).equals(Customer.State.FORBID) || returnCustomerPo.getBeDeleted().equals((byte) 1)) {
                    logger.debug("User is forbidden");
                    logger.debug("Username:" + returnCustomerPo.getUserName());
                    return new ReturnObject<>(ReturnNo.CUSTOMER_FORBIDDEN);
                } else {
                    logger.debug("User login success");
                    logger.debug("Username:" + returnCustomerPo.getUserName());

                    return new ReturnObject<>(returnCustomerPo);
                }
            } else {
                logger.debug("Wrong login password, username:" + returnCustomerPo.getUserName());
                return new ReturnObject<>(ReturnNo.CUSTOMER_INVALID_ACCOUNT);
            }
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject<Object> getCustomerById(Long id) {
        CustomerPo customerPo;
        try {
            customerPo = customerPoMapper.selectByPrimaryKey(id);
            if (customerPo == null)
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR);
        }

        Customer customer = cloneVo(customerPo, Customer.class);

        return new ReturnObject<>(customer);
    }


    /**
     * @author jxy
     * @create 2021/12/4 4:59 PM
     */
    public ReturnObject<Object> getCustomerPoById(Long id) {
        CustomerPo customerPo;
        try {
            customerPo = customerPoMapper.selectByPrimaryKey(id);
            if (customerPo == null)
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR);
        }

        return new ReturnObject<>(customerPo);
    }

    public ReturnObject<Object> changeCustomerState(Long id, Long loginUser, String loginName, Customer.State state) {

        ReturnObject<Object> retObj = getCustomerPoById(id);
        if (retObj.getCode() != ReturnNo.OK)
            return retObj;

        CustomerPo customerPo = (CustomerPo) retObj.getData();
        customerPo.setState(state.getCode().byteValue());
        Common.setPoModifiedFields(customerPo, loginUser, loginName);

        int ret;
        try {
            ret = customerPoMapper.updateByPrimaryKeySelective(customerPo);
            if (ret == 0) {
                logger.info("用户不存在或已被删除：id = " + id);
                retObj = new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("用户 id = " + id + " 的状态修改为 " + state.getDescription());
                retObj = new ReturnObject<>();
            }
        } catch (DataAccessException e) {
            // 数据库错误
            logger.error("数据库错误：" + e.getMessage());
            retObj = new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        } catch (Exception e) {
            // 属未知错误
            logger.error("严重错误：" + e.getMessage());
            retObj = new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, String.format("发生了严重的未知错误：%s", e.getMessage()));
        }
        return retObj;
    }


    public ReturnObject updateCustomer(CustomerPo po) {
        try {
            customerPoMapper.updateByPrimaryKeySelective(po);
            return new ReturnObject(ReturnNo.OK);
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    /**
     * @author jxy
     * @create 2021/12/11 10:24 PM
     */


    public ReturnObject changePoints(Long userId, Long pointsChange) {
        ReturnObject<Object> retObj = getCustomerPoById(userId);
        if (retObj.getCode() != ReturnNo.OK) {
            return retObj;
        }

        CustomerPo customerPo = (CustomerPo) retObj.getData();
        Long point = customerPo.getPoint();
        Long temp = point + pointsChange;
        if (temp < 0) {
            /* 若积点不够，全部使用 */
            temp = 0L;
            pointsChange = -point;
        }
        customerPo.setPoint(temp);
        try {
            customerPoMapper.updateByPrimaryKey(customerPo);
            return new ReturnObject(new CustomerModifyPointsVo(pointsChange));
        } catch (Exception e) {
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }


    /**
     * @author jxy
     * @create 2021/12/4 8:09 PM
     */

    public ReturnObject selectAllCustomers(String userName, String email, String mobile, Integer page, Integer pageSize) {

        CustomerPoExample example = new CustomerPoExample();
        CustomerPoExample.Criteria criteria = example.createCriteria();

        if (userName != null) criteria.andUserNameEqualTo(userName);
        if (email != null) criteria.andEmailEqualTo(email);
        if (mobile != null) criteria.andMobileEqualTo(mobile);
        List<CustomerPo> customerPos = new ArrayList<>();
        PageHelper.startPage(page, pageSize);
        try {
            customerPos = customerPoMapper.selectByExample(example);
        } catch (DataAccessException e) {
            return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
        return new ReturnObject<>(customerPos);

    }

}
