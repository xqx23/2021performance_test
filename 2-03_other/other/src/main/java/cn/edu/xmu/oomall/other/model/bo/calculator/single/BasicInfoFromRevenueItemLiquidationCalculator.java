package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */
@Data
public class BasicInfoFromRevenueItemLiquidationCalculator extends SingleLiquidationCalculator {

    public BasicInfoFromRevenueItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        var revenueItem = info.getRevenueItem();
        resultItem.setShopId(revenueItem.getShopId());
        resultItem.setRevenueId(revenueItem.getId());
        resultItem.setOrderId(revenueItem.getOrderId());
        resultItem.setOrderItemId(revenueItem.getOrderitemId());
        resultItem.setProductId(revenueItem.getProductId());
        resultItem.setProductName(revenueItem.getProductName());
        resultItem.setQuantity(revenueItem.getQuantity().longValue());
        resultItem.setSharerId(revenueItem.getSharerId());
        return resultItem;

    }
}
