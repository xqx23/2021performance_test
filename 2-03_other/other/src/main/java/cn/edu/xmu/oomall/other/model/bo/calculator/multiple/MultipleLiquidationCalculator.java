package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class MultipleLiquidationCalculator {

    protected LiquidationInfo info;

    public abstract List<LiquidationResultItem> calculate();
}
