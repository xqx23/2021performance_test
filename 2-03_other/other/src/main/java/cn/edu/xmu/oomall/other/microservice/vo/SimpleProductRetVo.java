package cn.edu.xmu.oomall.other.microservice.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author jxy
 * @create 2021/12/5 5:31 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleProductRetVo {
    @ApiModelProperty(value = "货品id")
    private Long id;
    @ApiModelProperty(value = "货品名称")
    private String name;
    @ApiModelProperty(value = "图片链接")
    private String imageUrl;
}
