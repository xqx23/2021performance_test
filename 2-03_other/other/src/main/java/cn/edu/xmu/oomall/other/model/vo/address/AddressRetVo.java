package cn.edu.xmu.oomall.other.model.vo.address;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import lombok.Data;

/**
 * @author Ji Cao
 * @version 创建时间：2020/12/3 下午18:57
 */
@Data
public class AddressRetVo {
    private Long id;
    private SimpleObject region;
    private String detail;
    private String consignee;
    private String mobile;
    private Boolean beDefault;
}
