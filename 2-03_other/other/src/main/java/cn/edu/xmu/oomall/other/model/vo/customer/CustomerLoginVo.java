package cn.edu.xmu.oomall.other.model.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author jxy
 * @create 2021/12/5 1:22 PM
 */


@Data
@ApiModel
public class CustomerLoginVo {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(name = "用户名", value = "testuser")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "密码", value = "123456")
    private String password;

}
