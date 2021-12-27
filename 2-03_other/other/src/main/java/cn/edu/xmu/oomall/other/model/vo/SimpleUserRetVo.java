package cn.edu.xmu.oomall.other.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jxy
 * @create 2021/12/11 10:59 AM
 */

@Data
@NoArgsConstructor
public class SimpleUserRetVo {

    @ApiModelProperty(value = "管理员id")
    private Long id;

    @ApiModelProperty(value = "管理员名")
    private String name;

}
