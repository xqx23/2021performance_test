package cn.edu.xmu.oomall.other.model.vo.customer;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author jxy
 * @version 创建时间：2021/12/3
 */
@Data
@ApiModel
public class CustomerResetPasswordVo {
    @NotBlank
    private String name;
}
