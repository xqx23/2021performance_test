package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import cn.edu.xmu.oomall.other.model.bo.calculator.single.AfterSaleLiquidationCalculator;
import cn.edu.xmu.oomall.other.model.bo.calculator.single.SingleLiquidationCalculator;
import lombok.Data;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public class AfterSaleExpenditureLiquidationCalculator extends ListLiquidationCalculator {

    public AfterSaleExpenditureLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected SingleLiquidationCalculator getCalculator(LiquidationInfo info) {
        return new AfterSaleLiquidationCalculator(info);
    }

    @Override
    public List<LiquidationResultItem> calculate() {
        var list = super.calculate();
        var expressFee = list.get(0).getExpressFee();
        list.get(0).setExpressFee(-expressFee);
        return list;
    }
}
