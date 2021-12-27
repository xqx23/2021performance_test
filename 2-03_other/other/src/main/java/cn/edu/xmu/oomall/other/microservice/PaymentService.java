package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.microservice.vo.PaymentVo;
import cn.edu.xmu.oomall.other.microservice.vo.RefundVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimplePaymentRetVo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.ZonedDateTime;

/**
 * @Author jxy
 * @create 2021/12/13 4:54 PM
 */

@FeignClient(name = "transaction-service")
public interface PaymentService {
    //todo:等支付那边做好
    @PostMapping("/internal/refunds")
    InternalReturnObject requestRefund(@RequestParam RefundVo refundVo);

    @PostMapping("/internal/payments")
    InternalReturnObject requestPayment(@RequestParam PaymentVo paymentVo);
    //初步完成集成测试
    @GetMapping("/internal/payment")
    InternalReturnObject<SimplePageInfo<SimplePaymentRetVo>> listPaymentInternal(@RequestParam(value = "documentId",required = false)String documentId,
                                                                                 @RequestParam(value = "state",required = false)Byte state,
                                                                                 @RequestParam(value = "beginTime",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
                                                                                 @RequestParam(value = "endTime",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
                                                                                 @RequestParam(value = "page",required = false)Integer page,
                                                                                 @RequestParam(value = "pageSize",required = false)Integer pageSize) ;

}