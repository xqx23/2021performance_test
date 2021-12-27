package cn.edu.xmu.oomall.other.model.vo.customer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李智樑
 * @date 2021/12/3
 */
@Data
@NoArgsConstructor
public class CustomerRetVo {
    private Long id;
    private String userName;
    private String name;
    private String mobile;
    private String email;
    private Byte state;
    private Long point;
}
