package cn.edu.xmu.oomall.other.model.vo.aftersale;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.oomall.other.model.vo.customer.SimpleCustomerRetVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:李智樑
 * @time:2021/12/5 16:24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AfterSaleRetVo {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private SimpleCustomerRetVo customer;
    private Long shopId;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long price;
    private Long quantity;
    private SimpleObject region;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
}
