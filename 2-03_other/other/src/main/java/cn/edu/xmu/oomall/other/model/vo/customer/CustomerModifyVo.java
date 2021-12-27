package cn.edu.xmu.oomall.other.model.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 李智樑
 * @date 2021/12/3
 */
@Data
@ApiModel
public class CustomerModifyVo {
    @ApiModelProperty(name = "真实姓名", value = "陈xx")
    private String name;
}
