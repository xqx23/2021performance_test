package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AftersaleOrderItemRetVo {
    private Long productId;
    private String name;
    private Long quantity;
    private Long price;
}
