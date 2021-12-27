package cn.edu.xmu.oomall.other.microservice.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author Lu Zhang
 * @create 2021/12/13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "简单对象,包括id和name")
public class SimpleObject {
    private Long id;
    private String name;
}
