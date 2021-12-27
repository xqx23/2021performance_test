package cn.edu.xmu.oomall.other.model.vo.shoppingcart;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jxy
 * @create 2021/12/5 9:04 PM
 */
@Data
@ApiModel
public class ShoppingCartVo {

    @NotNull(message = "id 不能为空")
    @ApiModelProperty(name = "商品Id", value = "0")
    private Long productId;

    @ApiModelProperty(name = "数量", value = "1")
    private Long quantity;
}
