package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.microservice.vo.CouponActivityDetailRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.ValidCouponActivityRetVo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author jxy
 * @create 2021/12/5 7:32 PM
 */
@FeignClient(name = "coupon-service")
public interface CouponActivityService {
    //集成初步完成，可能需要对取到的list再判断一下时间
    @GetMapping("products/{id}/couponactivities")
    InternalReturnObject<SimplePageInfo<ValidCouponActivityRetVo>> listCouponActivitiesByProductId(@ApiParam(value = "货品ID", required = true) @PathVariable("id") Long productId,
                                                                                                   @ApiParam(value = "页码") @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber,
                                                                                                   @ApiParam(value = "每页数目") @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize);

    //已完成并初步通过集成测试
    @GetMapping("/internal/couponactivities/{id}")
    InternalReturnObject<CouponActivityDetailRetVo> getCouponActivityById(@PathVariable Long id);

    // 已完成并初步 通过集成，但是可能有并发问题
    @PutMapping("/internal/couponactivities/{id}/derc")
    InternalReturnObject decreaseCoupons(@PathVariable Long id);


}
