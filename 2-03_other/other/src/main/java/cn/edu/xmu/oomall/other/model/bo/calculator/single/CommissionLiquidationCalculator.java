package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public class CommissionLiquidationCalculator extends SingleLiquidationCalculator {

    public CommissionLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        var amount = info.getOrderItem().getPrice() - info.getOrderItem().getDiscountPrice();
        var commission = Math.round(amount * (info.getCommissionRatio() / 100.0));
        resultItem.setCommission(commission);
        resultItem.setShopRevenue(-commission);
        return resultItem;
    }
}
