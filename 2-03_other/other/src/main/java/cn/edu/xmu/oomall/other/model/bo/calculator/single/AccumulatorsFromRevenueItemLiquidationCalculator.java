package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */

@Data
public class AccumulatorsFromRevenueItemLiquidationCalculator extends SingleLiquidationCalculator {

    public AccumulatorsFromRevenueItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        var revenueItem = info.getRevenueItem();
        resultItem.setAmount(-revenueItem.getAmount());
        resultItem.setCommission(-revenueItem.getCommission());
        resultItem.setPoint(-revenueItem.getPoint());
        resultItem.setShopRevenue(-revenueItem.getShopRevenue());
        return resultItem;

    }
}
