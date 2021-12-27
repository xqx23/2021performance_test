package cn.edu.xmu.oomall.other.model.vo.shoppingcart;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleCouponActivityRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleProductRetVo;
import lombok.Data;

import java.util.List;

/**
 * @author jxy
 * @create 2021/12/5 4:34 PM
 */

@Data
public class ShoppingCartRetVo {
    private Long id;
    private SimpleProductRetVo product;
    private Long quantity;
    private Long price;
    private List<SimpleCouponActivityRetVo> couponActivity;
}
