package cn.edu.xmu.oomall.other.controller;


import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.address.AddressRetVo;
import cn.edu.xmu.oomall.other.model.vo.address.AddressVo;
import cn.edu.xmu.oomall.other.service.AddressService;
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

/**
 * @author jxy
 * @create 2021/12/9 9:43 AM
 */

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class AddressController {
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @ApiOperation(value = "买家新增地址", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AddressVo", name = "addressVo", value = "addressInfo", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PostMapping("/addresses")
    public Object addAddress(@LoginUser Long userId, @LoginName String loginName, @RequestBody @Validated AddressVo addressVo, BindingResult result) {
        Object object = Common.processFieldErrors(result, httpServletResponse);
        if (null != object) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return object;
        }
        ReturnObject returnObj = addressService.addAddress(userId, loginName, addressVo);
        return Common.decorateReturnObject(returnObj);

    }

    /**
     * 用户查看已有地址
     *
     * @author jxy
     * @create 2021/12/10 9:10 AM
     */

    @ApiOperation(value = "买家查看所有已有的地址信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/addresses")
    public Object getAddresses(@LoginUser Long userId) {
        ReturnObject returnObject = addressService.getAddresses(userId);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 买家设置默认地址
     *
     * @author jxy
     * @create 2021/12/10 9:40 AM
     */

    @ApiOperation(value = "买家设置默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/addresses/{id}/default")
    public Object updateDefaultAddress(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id) {
        ReturnObject returnObject = addressService.updateDefaultAddress(userId, loginName, id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 用户修改自己的地址信息
     *
     * @author jxy
     * @create 2021/12/10 10:05 AM
     */


    @ApiOperation(value = "买家修改自己的地址信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
            @ApiImplicitParam(paramType = "body", dataType = "AddressVo", name = "addressVo", value = "可修改的地址信息")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/addresses/{id}")
    public Object updateAddress(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id, @Validated @RequestBody AddressVo addressVo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return object;
        }
        ReturnObject returnObject = addressService.updateAddress(userId, loginName, id, addressVo);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 买家删除地址
     *
     * @author jxy
     * @create 2021/12/10 11:36 AM
     */

    @ApiOperation(value = "买家删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "地址id"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @DeleteMapping("/addresses/{id}")
    public Object deleteAddress(@LoginUser Long userId, @PathVariable Long id) {
        ReturnObject returnObject = addressService.deleteAddress(userId, id);
        return Common.decorateReturnObject(returnObject);
    }

}
