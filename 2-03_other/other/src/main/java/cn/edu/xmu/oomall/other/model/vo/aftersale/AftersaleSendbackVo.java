package cn.edu.xmu.oomall.other.model.vo.aftersale;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author jxy
 * @create 2021/12/11 9:50 AM
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleSendbackVo {
    @NotBlank
    @ApiModelProperty(name = "运单号", value = "002")
    private String logSn;
}
