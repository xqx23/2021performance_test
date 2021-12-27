package cn.edu.xmu.oomall.other.model.vo.liquidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Gao Yanfeng
 * @date 2021/12/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleLiquidationInfoVo {
    Long id;
    SimpleShopVo shop;
    LocalDateTime liquidDate;
    Long expressFee;
    Long commission;
    Long shopRevenue;
    Long point;
    Byte state;
}
