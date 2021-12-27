package cn.edu.xmu.oomall.other.microservice.vo;

import cn.edu.xmu.oomall.other.model.vo.customer.SimpleCustomerRetVo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.SimpleShopVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRetVo {
    private Long id;
    private String orderSn;
    private SimpleCustomerRetVo customer;
    private SimpleShopVo shop;
    private Long pid;
    private Integer state;
    private LocalDateTime confirmTime;
    private Long originPrice;
    private Long discountPrice;
    private Long freightPrice;
    private Long point;
    private String message;
    private Integer regionId;
    private String address;
    private String mobile;
    private String consignee;
    private Long grouponId;
    private Long advancesaleId;
    private String shipmentSn;
    private List<SimpleOrderItemVo> orderItems;
}
