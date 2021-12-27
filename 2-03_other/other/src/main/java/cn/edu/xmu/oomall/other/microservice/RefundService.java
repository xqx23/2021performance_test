package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleRefundRetVo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */

@FeignClient(name = "refund")
public interface RefundService {
    @GetMapping("/shops/{shopId}/refund")
    InternalReturnObject<SimplePageInfo<SimpleRefundRetVo>> listRefund(
            @RequestParam(value = "documentId", required = false) String documentId,
            @RequestParam(value = "state", required = false) Byte state,
            @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
            @RequestParam(value = "endTime", required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

}
