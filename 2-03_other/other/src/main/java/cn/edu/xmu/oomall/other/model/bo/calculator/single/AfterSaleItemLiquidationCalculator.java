package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */

@Data
public class AfterSaleItemLiquidationCalculator extends SingleLiquidationCalculator {

    public AfterSaleItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        var afterSale = info.getAfterSale();
        var revenueItem = info.getRevenueItem();
        var amount = -afterSale.getPrice();
        var point = -Math.round((afterSale.getQuantity().doubleValue() / revenueItem.getQuantity()) * revenueItem.getPoint());
        resultItem.setQuantity(afterSale.getQuantity());
        resultItem.setAmount(amount);
        resultItem.setPoint(point);
        resultItem.setShopRevenue(amount - point);
        return resultItem;
    }
}
