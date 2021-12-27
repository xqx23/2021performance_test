package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public abstract class CompositeSingleLiquidationCalculator extends SingleLiquidationCalculator {

    public CompositeSingleLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    abstract protected List<SingleLiquidationCalculator> getCalculators();

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        for (var calculator : getCalculators()) {
            resultItem.updateBy(calculator.calculate());
        }
        return resultItem;
    }
}
