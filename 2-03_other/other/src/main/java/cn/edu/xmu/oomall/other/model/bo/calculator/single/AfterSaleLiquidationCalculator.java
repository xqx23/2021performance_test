package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */

public class AfterSaleLiquidationCalculator extends CompositeSingleLiquidationCalculator {

    public AfterSaleLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected List<SingleLiquidationCalculator> getCalculators() {
        return List.of(
                new BasicInfoFromOrderItemLiquidationCalculator(info),
                new AfterSaleItemLiquidationCalculator(info),
                new RefundIdLiquidationCalculator(info)
        );
    }
}
