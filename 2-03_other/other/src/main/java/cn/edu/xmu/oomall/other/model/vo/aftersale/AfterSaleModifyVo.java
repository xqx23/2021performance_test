package cn.edu.xmu.oomall.other.model.vo.aftersale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@author 李智樑
 *@create 2021/12/5 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfterSaleModifyVo {

    private Long quantity;

    private String reason;

    private Long regionId;

    private String detail;

    private String consignee;

    //    @Pattern(regexp = "^[0-9]{11}$")
    private String mobile;
}
