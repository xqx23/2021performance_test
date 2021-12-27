package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */
@Data
public class RefundIdLiquidationCalculator extends SingleLiquidationCalculator {

    public RefundIdLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        resultItem.setRefundId(info.getRefundId());
        return resultItem;
    }
}
