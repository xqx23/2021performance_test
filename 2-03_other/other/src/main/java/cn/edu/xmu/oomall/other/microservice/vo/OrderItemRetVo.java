package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author jxy
 * @create 2021/12/10 10:14 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRetVo {
    private Long id;
    private Long orderId;
    private Long shopId;
    private Long productId;
    private Long onsaleId;
    private Long quantity;
    private Long price;
    private Long discountPrice;
    private Long point;
    private String name;
    private Long couponActivityId;
    private Long couponId;
    private Long customerId;
}
