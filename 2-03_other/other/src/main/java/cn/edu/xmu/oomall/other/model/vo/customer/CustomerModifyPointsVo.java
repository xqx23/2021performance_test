package cn.edu.xmu.oomall.other.model.vo.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author jxy
 * @create 2021/12/4 4:33 PM
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModifyPointsVo {
    @NotBlank
    private Long points;
}
