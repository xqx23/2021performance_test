package cn.edu.xmu.oomall.otherlistener.microservice;

import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @Author jxy
 * @create 2021/12/5 7:32 PM
 */
@FeignClient(name = "coupon-service")
public interface CouponActivityService {
    @PutMapping("/internal/couponactivities/{id}/derc")
    InternalReturnObject decreaseCoupons(@PathVariable Long id);

}
