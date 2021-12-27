package cn.edu.xmu.oomall.other.model.vo.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author jxy
 * @create 2021/12/15 10:26 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCartRetVo {
    private Long id;
    private Long quantity;
    private Long price;
}
