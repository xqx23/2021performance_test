package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
public abstract class CompositeMultipleLiquidationCalculator extends MultipleLiquidationCalculator {

    public CompositeMultipleLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    abstract protected List<MultipleLiquidationCalculator> getCalculators();

    @Override
    public List<LiquidationResultItem> calculate() {
        List<LiquidationResultItem> list = new ArrayList<>();
        for (var calculator : getCalculators()) {
            list.addAll(calculator.calculate());
        }
        return list;
    }
}
