package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gao Yanfeng
 * @date 2021/12/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleOrderItemVo {
    private Long id;
    private Long productId;
    private Long shopId;
    private String name;
    private Integer quantity;
    private Long price;
    private Long discountPrice;
}
