package cn.edu.xmu.oomall.other.model.vo.aftersale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author 李智樑
 * @create 2021/12/4 20:00
 */
@ApiModel(description = "售后传值对象")
@Data
public class AfterSaleVo {
    @Max(2)
    @Min(0)
    @NotNull(message = "售后类别不能为空")
    @ApiModelProperty(name = "售后类别", value = "0")
    private Byte type;

    @Min(1)
    @NotNull
    @ApiModelProperty(name = "数量", value = "1")
    private Long quantity;

    @ApiModelProperty(name = "原因", value = "无法使用")
    private String reason;

    @Min(0)
    @NotNull
    @ApiModelProperty(name = "区域ID", value = "1")
    private Long regionId;

    @ApiModelProperty(name = "详细地址", value = "厦大翔安校区")
    private String detail;

    @NotBlank
    @ApiModelProperty(name = "联系人", value = "李某")
    private String consignee;

    @NotBlank
    @Pattern(regexp = "[0-9]{11}")
    @ApiModelProperty(name = "手机号", value = "13608000000")
    private String mobile;
}
