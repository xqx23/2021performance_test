package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */
public class RevenueItemLiquidationCalculator extends CompositeSingleLiquidationCalculator {

    public RevenueItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected List<SingleLiquidationCalculator> getCalculators() {
        return List.of(
                new BasicInfoFromRevenueItemLiquidationCalculator(info),
                new AccumulatorsFromRevenueItemLiquidationCalculator(info),
                new RefundIdLiquidationCalculator(info)
        );
    }
}
