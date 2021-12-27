package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

import java.util.List;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public class OrderItemLiquidationCalculator extends CompositeSingleLiquidationCalculator {

    public OrderItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected List<SingleLiquidationCalculator> getCalculators() {
        return List.of(
                new BasicInfoFromOrderItemLiquidationCalculator(info),
                new AmountLiquidationCalculator(info),
                new CommissionLiquidationCalculator(info),
                new PointLiquidationCalculator(info),
                new PaymentIdLiquidationCalculator(info)
        );
    }
}
