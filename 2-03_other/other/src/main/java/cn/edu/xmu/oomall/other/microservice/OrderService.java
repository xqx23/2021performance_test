package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.core.config.OpenFeignConfig;
import cn.edu.xmu.oomall.other.microservice.vo.*;
import cn.edu.xmu.oomall.other.model.vo.aftersale.AfterSaleRetVo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author jxy
 * @create 2021/12/10 3:23 PM
 */
@FeignClient(name = "order-service",configuration = OpenFeignConfig.class)
public interface OrderService {
    //初步完成集成
    @GetMapping("internal/orderitems/{id}")
    InternalReturnObject<OrderItemRetVo> getOrderItemById(@PathVariable("id") Long id, @RequestParam(value = "customerId", required = false) Long customerId);
    //初步完成集成
    @GetMapping("internal/orderitems/{id}/payment")
    InternalReturnObject<SimplePaymentRetVo> getPaymentByOrderItemId(@PathVariable("id") Long id);
    //初步完成集成
    @PostMapping("/internal/shops/{shopId}/orders")
    InternalReturnObject<AfterSaleRetVo> createAftersaleOrder(@PathVariable("shopId") Long shopId, @RequestBody AftersaleRecVo orderVo);

    @GetMapping("/orders/{id}")
    InternalReturnObject<OrderRetVo> getOrderById(@PathVariable Long id);

    @GetMapping("/internal/orders/{id}/orderitems")
    InternalReturnObject<List<OrderItemRetVo>> getOrderItemsByOrderId(@PathVariable Long id);

    @GetMapping("/internal/orderid")
    InternalReturnObject<OrderIdRetVo> getOrderIdByOrderSn(@RequestParam(value = "orderSn") String orderSn);
}