package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.model.VoObject;
import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.oomall.other.model.bo.Customer;
import cn.edu.xmu.oomall.other.model.vo.customer.*;
import cn.edu.xmu.oomall.other.service.CustomerService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author 李智樑
 * @date 2021/12/3
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private CustomerService customerService;

    /***
     * 获得买家的所有状态
     * @return Object
     */
    @GetMapping("/customers/states")
    public Object getUserState() {
        ReturnObject<List> returnObject = customerService.getCustomerStates();
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/5 10:50 AM
     * 注册用户
     */

    @ApiOperation(value = "注册用户", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserSignUpVo", name = "vo", value = "可填写的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 613, message = "用户名已被注册"),
            @ApiResponse(code = 612, message = "邮箱已被注册"),
            @ApiResponse(code = 611, message = "电话已被注册")
    })
    @PostMapping("/customers")
    public Object signUpUser(@Validated @RequestBody CustomerSignUpVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);
            return object;
        }
        ReturnObject returnObject = customerService.signCustomer(vo);
        if (returnObject.getData() != null) {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        }
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 买家查看自己信息
     *
     * @param UserId 用户id
     * @return Object
     * @author 李智樑
     */
    @ApiOperation(value = "买家查看自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/self")
    public Object getSelfInfo(@LoginUser Long UserId) {
        ReturnObject<VoObject> returnObject = customerService.getCustomerById(UserId);
        return Common.decorateReturnObject(returnObject);
    }

    /***
     * 买家修改自己的信息
     * @param userId 用户id
     * @return Object
     */
    @ApiOperation(value = "买家修改自己信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyVo", name = "vo", value = "可修改的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/self")
    public Object changeMyselfInfo(@LoginUser Long userId, @RequestBody CustomerModifyVo vo) {
        Customer customer = cloneVo(vo, Customer.class);

        ReturnObject ret = customerService.modifyCustomerById(userId, customer);

        return Common.decorateReturnObject(ret);
    }


    /***
     * 用户重置密码
     * @author jxy
     * @return Object
     */
    @ApiOperation(value = "用户重置密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserResetPasswordVo", name = "vo", value = "邮箱和用户名", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 608, message = "用户名/邮箱/电话不存在")
    })
    @PutMapping("/password/reset")
    public Object resetUserSelfPassword(@RequestBody CustomerResetPasswordVo vo, BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (o != null) {
            return o;
        }

        ReturnObject returnObject = customerService.resetPassword(vo);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/4 5:17 PM
     */

    @ApiOperation(value = "用户修改密码", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = 741, message = "不能与旧密码相同"),
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/password")
    @ResponseBody
    public Object modifyPassword(@Validated @RequestBody CustomerModifyPasswordVo vo, BindingResult bindingResult, HttpServletResponse httpServletResponse, @LoginUser Long loginUser, @LoginName String loginName) {

        /* 处理参数校验错误 */
        Object o = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (o != null) {
            return o;
        }
        ReturnObject returnObject = customerService.modifyPassword(vo, loginUser, loginName);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/4 5:26 PM
     * 平台管理员获取所有用户列表
     */
    @ApiOperation(value = "平台管理员获取所有用户列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "userName", value = "testuser"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "email", value = "test@test.com"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "mobile", value = "12300010002"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "1"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "10")
    })
    @GetMapping("/shops/{id}/customers")
    @Audit(departName = "shops")
    public Object getAllCustomers(
            @PathVariable Long id,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (id != 0) {
            ReturnObject returnObject = new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            return Common.decorateReturnObject(returnObject);
        }
        ReturnObject returnObject = customerService.getAllCustomers(userName, email, mobile, page, pageSize);
        logger.debug("fingUserById: getUsers = " + returnObject);
        returnObject = Common.getPageRetVo(returnObject, SimpleCustomerRetVo.class);
        return Common.decorateReturnObject(returnObject);
    }


    /**
     * @author jxy
     * @create 2021/12/5 1:20 PM
     * 用户名密码登录
     */

    @ApiOperation(value = "用户名密码登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "vo", value = "用户名和密码", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或密码错误"),
            @ApiResponse(code = 702, message = "用户被禁止登录")
    })
    @PostMapping("/login")
    public Object loginCustomer(@Validated @RequestBody CustomerLoginVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            logger.debug("Validate failed");
            logger.debug("UserLoginVo:" + vo);

            return object;
        }

        ReturnObject<Object> returnObject = customerService.login(vo);
        if(returnObject.getCode().equals(ReturnNo.OK))
        {
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
        }
        return Common.decorateReturnObject(returnObject);

    }


    /***
     *@author jxy
     *@create 2021/12/5 3:58 PM
     */
    @ApiOperation(value = "用户登出", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/logout")
    @Audit
    public Object logoutCustomer(@LoginUser Long id, @LoginName String loginName) {
        ReturnObject ret = customerService.logout(id);
        return Common.decorateReturnObject(ret);

    }


    /**
     * @author jxy
     * @create 2021/12/4 8:11 PM
     * 平台管理员查看任意买家信息
     */

    @ApiOperation(value = "平台管理员查看任意买家信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 505, message = "操作的资源id不存在")
    })
    @GetMapping("/shops/{shopId}/customers/{id}")
    @Audit(departName = "shops")
    public Object getCustomerById(@PathVariable Long shopId, @PathVariable("id") Long id) {
        if (shopId != 0L) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        ReturnObject ret = customerService.getCustomerById(id);

        return Common.decorateReturnObject(ret);
    }


    /**
     * @author jxy
     * @create 2021/12/4 10:39 PM
     * 平台管理员封禁买家
     */

    @ApiOperation(value = "平台管理员封禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @Audit(departName = "shops")
    @PutMapping("/shops/{did}/customers/{id}/ban")
    public Object banCustomer(@PathVariable Long did, @PathVariable("id") Long id, @LoginUser Long loginUser, @LoginName String loginName) {

        if (did != 0L) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        ReturnObject ret = customerService.banCustomer(id, loginUser, loginName);

        return Common.decorateReturnObject(ret);

    }

    /**
     * @author jxy
     * @create 2021/12/5 10:04 AM
     * 平台解禁买家
     */

    @ApiOperation(value = "平台管理员解禁买家", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "用户id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 504, message = "操作的资源id不存在")
    })
    @Audit(departName = "shops")
    @PutMapping("/shops/{did}/customers/{id}/release")
    public Object releaseCustomer(@PathVariable Long did, @PathVariable("id") Long id, @LoginUser Long loginUser, @LoginName String loginName) {

        if (did != 0L) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }
        ReturnObject ret = customerService.releaseCustomer(id, loginUser, loginName);

        return Common.decorateReturnObject(ret);

    }

    /**
     * @author jxy
     * @create 2021/12/11 10:13 PM
     */
    @ApiOperation(value = "修改分享返点", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyVo", name = "vo", value = "可修改的用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @PutMapping("/internal/point/{customerId}")
    public Object changeCustomerPoint(@PathVariable Long customerId, @RequestBody CustomerModifyPointsVo vo) {

        ReturnObject ret = customerService.modifyCustomerPointsById(customerId, vo.getPoints());
        return Common.decorateReturnObject(ret);
    }

    /**
     * 获取用户的简要信息（id、name）
     *
     * @author 李智樑
     */
    @GetMapping("/internal/customers/{id}")
    public Object getCustomerById(@PathVariable("id") Long id) {
        return Common.decorateReturnObject(new ReturnObject<>(cloneVo(customerService.
                getCustomerById(id).getData(), SimpleObject.class)));
    }


}
