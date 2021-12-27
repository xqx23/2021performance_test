package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
@NoArgsConstructor
public class DefaultLiquidationCalculator extends MultipleLiquidationCalculator {
    @Override
    public List<LiquidationResultItem> calculate() {
        return List.of();
    }
}
