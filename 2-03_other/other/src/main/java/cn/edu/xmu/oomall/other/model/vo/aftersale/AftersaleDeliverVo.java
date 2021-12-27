package cn.edu.xmu.oomall.other.model.vo.aftersale;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jxy
 * @create 2021/12/11 7:57 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleDeliverVo {
    @ApiModelProperty(name = "运单号", value = "333")
//    @NotNull
    private String shopLogSn;
}
