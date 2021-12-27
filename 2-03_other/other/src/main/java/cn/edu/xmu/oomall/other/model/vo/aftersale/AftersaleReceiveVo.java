package cn.edu.xmu.oomall.other.model.vo.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author jxy
 * @create 2021/12/11 7:20 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersaleReceiveVo {
    @NotNull
    private Boolean confirm;

    private String conclusion;
}
