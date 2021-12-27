package cn.edu.xmu.oomall.other.model.vo.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author jxy
 * @create 2021/12/4 4:33 PM
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModifyPasswordVo {
    @NotBlank
    private String captcha;

    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{6,}$", message = "密码格式不正确")
    private String newPassword;
}
