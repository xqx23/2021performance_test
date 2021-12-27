package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SingleLiquidationCalculator {

    protected LiquidationInfo info;

    public abstract LiquidationResultItem calculate();
}
