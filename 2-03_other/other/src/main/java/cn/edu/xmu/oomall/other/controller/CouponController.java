package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.bo.Coupon;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponRetVo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponRetVoForReceive;
import cn.edu.xmu.oomall.other.service.CouponService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author:李智樑
 * @time:2021/12/8 17:46
 **/
@RestController /*Restful的Controller对象*/
@RefreshScope
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private CouponService couponService;

    @GetMapping("/coupons/states")
    public Object getCouponStates() {
        ReturnObject<List> returnObject = couponService.getCouponStates();
        return Common.decorateReturnObject(returnObject);
    }

    @ApiOperation(value = "买家查看优惠券列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "state", value = "状态"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/coupons")
    public Object showCoupons(
            @LoginUser Long userId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer state) {
        // 检验state的合法性
        if (state != null) {
            if (state < Coupon.State.RECEIVED.getCode() || state > Coupon.State.OUT_OF_DATE.getCode()) {
                return new ReturnObject(ReturnNo.FIELD_NOTVALID);
            }
        }

        ReturnObject returnObject = Common.getPageRetVo(couponService.getCoupons(userId, page, pageSize, state), CouponRetVo.class);

        return Common.decorateReturnObject(returnObject);
    }

    @ApiOperation(value = "买家领取活动优惠券", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "活动id")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 630, message = "未到优惠券领取时间"),
            @ApiResponse(code = 631, message = "优惠券领罄"),
            @ApiResponse(code = 932, message = "优惠券活动终止")
    })
    @Audit
    @PostMapping("/couponactivities/{id}/coupons")
    public Object receiveCoupon(@LoginUser Long userId, @LoginName String userName, @PathVariable Long id) {
        ReturnObject ret = Common.getListRetVo(couponService.receiveCoupons(userId, userName, id), CouponRetVoForReceive.class);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 退款后，修改优惠券状态为已领取
     */
    @PutMapping("/internal/coupons/{id}/refund")
    public Object refundCoupon(@RequestParam Long userId, @RequestParam String userName, @PathVariable Long id) {
        return Common.decorateReturnObject(couponService.modifyCouponState(
                id, Coupon.State.RECEIVED.getCode().byteValue(), userId, userName));
    }

    /**
     * 使用优惠券消费后，修改优惠券状态为已使用
     */
    @PutMapping("/internal/coupons/{id}/use")
    public Object useCoupon(@RequestParam Long userId, @RequestParam String userName, @PathVariable Long id) {
        return Common.decorateReturnObject(couponService.modifyCouponState(
                id, Coupon.State.USED.getCode().byteValue(), userId, userName));
    }

    /**
     * 修改优惠券状态为已失效
     */
    @PutMapping("/internal/coupons/{id}/ban")
    public Object banCoupon(@RequestParam Long userId, @RequestParam String userName, @PathVariable Long id) {
        return Common.decorateReturnObject(couponService.modifyCouponState(
                id, Coupon.State.OUT_OF_DATE.getCode().byteValue(), userId, userName));
    }

    /**
     * 查询优惠券是否存在且状态为未使用
     */
    @GetMapping("/internal/coupons/{id}/exists")
    public Object isCouponExists(@PathVariable Long id, @RequestParam Long userId) {
        return Common.decorateReturnObject(couponService.isCouponExists(id, userId));
    }
}
