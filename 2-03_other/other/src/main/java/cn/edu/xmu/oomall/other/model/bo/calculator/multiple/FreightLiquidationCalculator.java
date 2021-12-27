package cn.edu.xmu.oomall.other.model.bo.calculator.multiple;

import cn.edu.xmu.oomall.other.microservice.vo.OrderRetVo;
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
public class FreightLiquidationCalculator extends MultipleLiquidationCalculator {

    public FreightLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public List<LiquidationResultItem> calculate() {
        var resultItem = new LiquidationResultItem();
        resultItem.setExpressFee(info.getOrder().getFreightPrice());
        resultItem.setPaymentId(info.getPaymentId());
        resultItem.setOrderItemId(0L);
        return List.of(resultItem);
    }
}
