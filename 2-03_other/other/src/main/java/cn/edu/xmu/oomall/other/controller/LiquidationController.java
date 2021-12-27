package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.constant.Constants;
import cn.edu.xmu.oomall.other.microservice.ShareActivityService;
import cn.edu.xmu.oomall.other.model.vo.liquidation.GeneralLedgersRetVo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.StartInfoVo;
import cn.edu.xmu.oomall.other.service.LiquidationService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

/**
 * @author Gao Yanfeng
 * @date 2021/12/10
 */
@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class LiquidationController {

    @Autowired
    LiquidationService liquidationService;
    @Autowired
    ShareActivityService shareActivityService;

    @ApiOperation(value = "获得清算单的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/liquidation/states")
    public Object getLiquidationStates(@LoginUser Long userId, @LoginName String userName) {
        return Common.decorateReturnObject(liquidationService.getLiquidationStates());
    }

    @ApiOperation(value = "平台管理员或商家获取符合条件的清算单简单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "beginDate", value = "开始日期"),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "endDate", value = "结束日期"),
            @ApiImplicitParam(paramType = "query", dataType = "Boolean", name = "state", value = "是否已打款"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/shops/{shopId}/liquidation")
    public Object getLiquidations(@PathVariable Long shopId,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginDate,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
                                  @RequestParam(required = false) Boolean state, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        var ret = liquidationService.getLiquidations(shopId, beginDate, endDate, state, page, pageSize);
        return Common.decorateReturnObject(ret);
    }

    @ApiOperation(value = "查询指定清算单详情", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "清算单Id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/shops/{shopId}/liquidation/{id}")
    public Object getLiquidationById(@PathVariable Long shopId, @PathVariable Long id) {
        var ret = liquidationService.getLiquidationById(shopId, id);
        return Common.decorateReturnObject(ret);
    }

    @ApiOperation(value = "用户获取自己因分享得到返点的记录（得）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "beginTime", value = "开始日期"),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "endTime", value = "结束日期"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/pointrecords/revenue")
    public Object getCustomerAcquirePointchange(@LoginUser Long userId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginDate,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
                                                @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        var ret = liquidationService.getAcquirePointChangeBySharerId(userId, beginDate, endDate, page, pageSize);
        return Common.decorateReturnObject(Common.getPageRetVo(ret, GeneralLedgersRetVo.class));
    }

    @ApiOperation(value = "用户获取自己因分享得到返点的记录（失）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "beginTime", value = "开始日期"),
            @ApiImplicitParam(paramType = "query", dataType = "LocalDateTime", name = "endTime", value = "结束日期"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping(value = "/pointrecords/expenditure")
    public Object getCustomerLostPointchange(@LoginUser Long userId,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginDate,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate,
                                             @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        var ret = liquidationService.getLostPointChangeBySharerId(userId, beginDate, endDate, page, pageSize);
        return Common.decorateReturnObject(Common.getPageRetVo(ret, GeneralLedgersRetVo.class));
    }


    /**
     * @author jxy
     * @create 2021/12/14 3:27 PM
     */

    @ApiOperation(value = "管理员按条件查某笔进账", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "orderId", value = "订单Id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "productId", value = "货品Id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "liquidationId", value = "清算单Id", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping(value = "/shops/{shopId}/revenue")
    public Object getRevenuesByInfo(@PathVariable Long shopId, @RequestParam(required = false) Long orderId,
                                    @RequestParam(required = false) Long productId, @RequestParam(required = false) Long liquidationId,
                                    @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        var ret = liquidationService.getRevenueItemByInfo(liquidationId, shopId, orderId, productId, page, pageSize);
        return Common.decorateReturnObject(Common.getPageRetVo(ret, GeneralLedgersRetVo.class));
    }


    /**
     * @author jxy
     * @create 2021/12/14 4:13 PM
     */

    @ApiOperation(value = "管理员按条件查某笔出账", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "orderId", value = "订单Id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "productId", value = "货品Id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "liquidationId", value = "清算单Id", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping(value = "/shops/{shopId}/expenditure")
    public Object getExpenditureByInfo(@PathVariable Long shopId, @RequestParam(required = false) Long orderId,
                                       @RequestParam(required = false) Long productId, @RequestParam(required = false) Long liquidationId,
                                       @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        var ret = liquidationService.getExpenditureByInfo(liquidationId, shopId, orderId, productId, page, pageSize);
        return Common.decorateReturnObject(Common.getPageRetVo(ret, GeneralLedgersRetVo.class));
    }

    @ApiOperation(value = "管理员按id查出账对应的进账条件", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "清算单Id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping(value = "/shops/{shopId}/expenditure/{id}/revenue")
    public Object getRevenueByExpenditureId(@PathVariable Long shopId, @PathVariable Long id) {
        var ret = liquidationService.getRevenueByExpenditureId(shopId, id);
        return Common.decorateReturnObject(ret);
    }

    @ApiOperation(value = "开始清算", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shopId", value = "商铺Id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "StartInfoVo", name = "startInfo", value = "开始清算所需信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 505, message = "shopId只能为0")
    })
    @Audit
    @PutMapping(value = "/shops/{shopId}/liquidation/start")
    public Object startLiquidation(@PathVariable Long shopId, @RequestBody StartInfoVo startInfo,
                                   @LoginUser Long userId, @LoginName String userName) {
        ReturnObject ret;
        if (shopId != 0L) {
            ret = new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE, "shopId只能为0");
        } else {
            ret = liquidationService.startLiquidation(startInfo, userId, userName);
        }
        return Common.decorateReturnObject(ret);
    }

    /**
    * @author jxy
    * @create 2021/12/18 8:39 PM
     * 拿来测试的不用管
    */


    @Audit
    @GetMapping(value = "/test/{id}")
    public Object testGetShareActivityById(@PathVariable Long id) {

          InternalReturnObject ret = shareActivityService.getShareActivityById(id);
        return ret;
    }
}
