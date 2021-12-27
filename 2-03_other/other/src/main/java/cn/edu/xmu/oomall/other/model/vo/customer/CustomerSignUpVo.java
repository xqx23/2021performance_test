package cn.edu.xmu.oomall.other.model.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author jxy
 * @create 2021/12/5 10:52 AM
 */

@ApiModel
@Data
public class CustomerSignUpVo {
    @NotBlank
    @Pattern(regexp = "[0-9]{11}")
    @ApiModelProperty(name = "手机号", value = "12300000000")
    private String mobile;

    @NotBlank
    @Email
    @ApiModelProperty(name = "邮箱", value = "testuser@test.com")
    private String email;

    @NotBlank
    @ApiModelProperty(name = "用户名", value = "testuser")
    private String userName;

    @NotBlank
    @ApiModelProperty(name = "密码", value = "123456")
    private String password;

    @NotBlank
    @ApiModelProperty(name = "真实姓名", value = "陈xx")
    private String name;

}
