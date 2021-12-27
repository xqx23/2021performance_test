package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import cn.edu.xmu.oomall.other.model.bo.calculator.single.SingleLiquidationCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public abstract class ListLiquidationCalculator extends MultipleLiquidationCalculator {

    public ListLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public List<LiquidationResultItem> calculate() {
        List<LiquidationResultItem> list = new ArrayList<>();
        for (var itemInfo : info.getLiquidationInfoList()) {
            var calculator = getCalculator(itemInfo);
            list.add(calculator.calculate());
        }
        return list;
    }

    protected abstract SingleLiquidationCalculator getCalculator(LiquidationInfo info);
}
