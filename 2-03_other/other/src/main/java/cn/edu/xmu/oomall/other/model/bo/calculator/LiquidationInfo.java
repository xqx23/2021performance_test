package cn.edu.xmu.oomall.other.model.bo.calculator;

import cn.edu.xmu.oomall.other.microservice.vo.OrderItemRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.OrderRetVo;
import cn.edu.xmu.oomall.other.model.po.AfterSalePo;
import cn.edu.xmu.oomall.other.model.po.RevenueItemPo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.PointsRetVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiquidationInfo {
    private OrderRetVo order;
    private OrderItemRetVo orderItem;
    private Integer commissionRatio;
    private PointsRetVo pointsRet;
    private RevenueItemPo revenueItem;
    private AfterSalePo afterSale;
    private Long paymentId;
    private Long refundId;
    private List<LiquidationInfo> liquidationInfoList;
}
