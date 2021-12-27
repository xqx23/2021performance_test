package cn.edu.xmu.oomall.other.model.vo.liquidation;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author:李智樑
 * @time:2021/12/14 14:59
 **/
@Data
@NoArgsConstructor
public class GeneralLedgersRetVo {
    private Long id;
    private SimpleShopVo shop;
    private SimpleObject product;
    private Long amount;
    private Long commission;
    private Long point;
    private Long shopRevenue;
    private Long expressFee;
    private SimpleAdminUserVo creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private SimpleAdminUserVo modifier;
}
