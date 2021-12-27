package cn.edu.xmu.oomall.other.model.vo.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author jxy
 * @create 2021/12/9 9:11 PM
 */


@ApiModel(description = "地址传值对象")
@Data
public class AddressVo {
    //    @NotNull
    @Min(1)
    @ApiModelProperty(name = "区域ID", value = "0")
    private Long regionId;

    //    @NotBlank
    @ApiModelProperty(name = "详细地址", value = "思明区厦大学生公寓")
    private String detail;

    //    @NotBlank
    @ApiModelProperty(name = "联系人", value = "曹某")
    private String consignee;

    //    @NotBlank
    @Pattern(regexp = "[+]?[0-9*#]*")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;
}
