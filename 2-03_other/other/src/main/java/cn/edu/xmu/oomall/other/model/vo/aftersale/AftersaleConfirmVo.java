package cn.edu.xmu.oomall.other.model.vo.aftersale;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author jxy
 * @create 2021/12/11 11:13 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleConfirmVo {
    @NotBlank
    @ApiModelProperty(name = "处理结果", value = "true")
    private Boolean confirm;

    @Min(value = 0)
    @NotNull
    private Long price;

    @NotBlank
    @ApiModelProperty(name = "处理意见", value = "no")
    private String conclusion;
    @NotBlank
    @ApiModelProperty(name = "类型", value = "no")
    private Byte type;

}
