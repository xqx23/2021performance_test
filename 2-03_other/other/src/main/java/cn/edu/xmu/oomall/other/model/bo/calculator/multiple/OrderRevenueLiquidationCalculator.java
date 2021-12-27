package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.microservice.vo.OrderRetVo;
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
public class OrderRevenueLiquidationCalculator extends CompositeMultipleLiquidationCalculator {

    public OrderRevenueLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    protected List<MultipleLiquidationCalculator> getCalculators() {
        return List.of(
                new FreightLiquidationCalculator(info),
                new OrderItemsLiquidationCalculator(info)
        );
    }
}
