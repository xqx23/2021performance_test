package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.aop.Mask;
import cn.edu.xmu.oomall.other.microservice.OrderService;
import cn.edu.xmu.oomall.other.microservice.vo.AftersaleOrderItemRecVo;
import cn.edu.xmu.oomall.other.microservice.vo.AftersaleRecVo;
import cn.edu.xmu.oomall.other.model.bo.AfterSale;
import cn.edu.xmu.oomall.other.model.vo.aftersale.*;
import cn.edu.xmu.oomall.other.service.AfterSaleService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author 李智樑
 * @create 2021/12/4 19:10
 */
@RestController /*Restful的Controller对象*/
@RefreshScope
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class AfterSaleController {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private AfterSaleService afterSaleService;

    @ApiOperation(value = "获得售后单的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @GetMapping("/aftersales/states")
    public Object getAfterSaleAllStates() {
        ReturnObject returnObject = afterSaleService.getAfterSaleAllStates();
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/10 3:15 PM
     */
    @ApiOperation(value = "买家提交售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "订单明细id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AftersaleVo", name = "body", value = "售后服务信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PostMapping("/orderItems/{id}/aftersales")
    public Object createAftersale(@LoginUser Long userId, @LoginName String loginName, @Validated @RequestBody AfterSaleVo vo, BindingResult bindingResult, @PathVariable("id") Long orderItemId) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);

        if (null != object) {
            return object;
        }
        ReturnObject ret = afterSaleService.newAfterSale(vo, orderItemId, userId, loginName);
        return Common.decorateReturnObject(ret);
    }

    @ApiOperation(value = "买家查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "type", value = "售后类型"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/aftersales")
    public Object queryAllReturnOrder(
            @LoginUser Long userId,
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer state) {
        // 校验query合法性
        ReturnObject ret = checkQueryValidity(beginTime, endTime, type, state);
        if (ret.getCode() != ReturnNo.OK) {
            return Common.decorateReturnObject(ret);
        }
        return Common.decorateReturnObject(afterSaleService.getAfterSales(userId, null, beginTime, endTime, page, pageSize, type, state, true));
    }

    @ApiOperation(value = "管理员查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "type", value = "售后类型"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping("/shops/{id}/aftersales")
    public Object GetAllAfterSales(
            @PathVariable("id") Long shopId,
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
            @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer state) {
        // 校验query合法性
        ReturnObject ret = checkQueryValidity(beginTime, endTime, type, state);
        if (ret.getCode() != ReturnNo.OK) {
            return Common.decorateReturnObject(ret);
        }
        return Common.decorateReturnObject(afterSaleService.getAfterSales(null, shopId, beginTime, endTime, page, pageSize, type, state, false));
    }

    @ApiOperation(value = "买家根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}")
    public Object getOneAllAftersaleOrder(@LoginUser Long userId, @PathVariable("id") Long afterSaleId) {
        return Common.decorateReturnObject(afterSaleService.getAfterSaleById(userId, afterSaleId));
    }

    @ApiOperation(value = "买家修改售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AfterSaleModifyVo", name = "body", value = "买家可修改的信息：地址，售后商品的数量，申请售后的原因，联系人以及联系电话", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}")
    public Object changeAfterSale(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long aftersaleId, @Validated @RequestBody AfterSaleModifyVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.FIELD_NOTVALID));
        }
        ReturnObject returnObject = afterSaleService.modifyAfterSaleById(userId, loginName, aftersaleId, vo);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/11 9:22 AM
     */
    @ApiOperation(value = "买家取消售后单和逻辑删除售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @DeleteMapping("/aftersales/{id}")
    public Object deleteAftersaleById(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id) {
        ReturnObject returnObject = afterSaleService.deleteAftersaleById(userId, loginName, id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/11 9:49 AM
     */
    @ApiOperation(value = "买家填写售后的运单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AftersaleSendbackVo", name = "body", value = "运单号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/sendback")
    public Object addWayBillNumber(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id, @Validated @RequestBody AftersaleSendbackVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return object;
        }

        ReturnObject returnObject = afterSaleService.addWayBillNumber(userId, loginName, id, vo.getLogSn());

        return Common.decorateReturnObject(returnObject);

    }

    /**
     * @author jxy
     * @create 2021/12/11 10:33 AM
     */

    @ApiOperation(value = "买家确认售后单结束", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/confirm")
    public Object confirmAftersaleEnd(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id) {
        ReturnObject returnObject = afterSaleService.confirmAftersaleEnd(userId, loginName, id);
        return Common.decorateReturnObject(returnObject);

    }

    /**
     * @author jxy
     * @create 2021/12/11 10:48 AM
     */
    @ApiOperation(value = "管理员根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @Mask
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public Object adminGetAftersaleById(@PathVariable("shopId") Long shopId, @PathVariable("id") Long id) {
        ReturnObject returnObject = afterSaleService.adminGetAftersaleById(shopId, id);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/11 11:12 AM
     */
    @ApiOperation(value = "管理员同意/不同意（退款，换货，维修）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AftersaleConfirmVo", name = "body", value = "处理意见", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public Object adminConfirmAftersale(
            @LoginUser Long userId,
            @LoginName String loginName,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestBody AftersaleConfirmVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return object;
        }

        ReturnObject returnObject = afterSaleService.adminConfirm(id, shopId, vo, userId, loginName);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/11 7:19 PM
     */
    @ApiOperation(value = "店家确认收到买家的退（换）货", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AftersaleReceiveVo", name = "body", value = "处理意见", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    public Object adminReceive(
            @LoginUser Long userId,
            @LoginName String loginName,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody AftersaleReceiveVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return object;
        }

        ReturnObject returnObject = afterSaleService.adminReceive(id, shopId, vo, userId, loginName);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/11 7:56 PM
     */
    @ApiOperation(value = "店家寄出货物", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "AftersaleDeliverVo", name = "body", value = "运单号", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @PutMapping("/shops/{shopId}/aftersales/{id}/deliver")
    public Object adminDeliver(
            @LoginUser Long userId,
            @LoginName String loginName,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody AftersaleDeliverVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != object) {
            return object;
        }

        ReturnObject returnObject = afterSaleService.adminDeliver(id, shopId, vo == null ? "" : vo.getShopLogSn(), userId, loginName);

        return Common.decorateReturnObject(returnObject);
    }

    /**
     * @author jxy
     * @create 2021/12/14 9:32 AM
     */

    @ApiOperation(value = "获取售后单的支付信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "售后单id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}/payments")
    public Object GetPaymentByAftersaleId(@PathVariable("id") Long aftersaleId) {
        ReturnObject returnObject = afterSaleService.getPaymentsByAftersaleId(aftersaleId);
        return Common.decorateReturnObject(returnObject);
    }


    private ReturnObject checkQueryValidity(ZonedDateTime beginTime, ZonedDateTime endTime, Integer type, Integer state) {
        // 判断开始结束时间是否合法
        if (beginTime != null && endTime != null && beginTime.isAfter(endTime)) {
            return new ReturnObject(ReturnNo.LATE_BEGINTIME);
        }
        // 判断state和type是否合法
        if (type != null) {
            if (type < AfterSale.Type.EXCHANGE.getCode() || type > AfterSale.Type.MAINTAIN.getCode()) {
                return new ReturnObject(ReturnNo.FIELD_NOTVALID);
            }
        }
        if (state != null) {
            if (state < AfterSale.State.NEW.getCode() || state > AfterSale.State.CANCELED.getCode()) {
                return new ReturnObject(ReturnNo.FIELD_NOTVALID);
            }
        }
        return new ReturnObject();
    }
}
