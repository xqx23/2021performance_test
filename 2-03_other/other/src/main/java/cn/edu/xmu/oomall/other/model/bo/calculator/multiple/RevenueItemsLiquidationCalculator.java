package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import cn.edu.xmu.oomall.other.model.bo.calculator.single.RevenueItemLiquidationCalculator;
import cn.edu.xmu.oomall.other.model.bo.calculator.single.SingleLiquidationCalculator;
import lombok.Data;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public class RevenueItemsLiquidationCalculator extends ListLiquidationCalculator {

    public RevenueItemsLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected SingleLiquidationCalculator getCalculator(LiquidationInfo info) {
        return new RevenueItemLiquidationCalculator(info);
    }
}
