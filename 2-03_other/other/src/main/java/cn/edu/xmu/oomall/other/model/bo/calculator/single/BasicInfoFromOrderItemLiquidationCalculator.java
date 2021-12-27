package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */

@Data
public class BasicInfoFromOrderItemLiquidationCalculator extends SingleLiquidationCalculator {

    public BasicInfoFromOrderItemLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        var orderItem = info.getOrderItem();
        resultItem.setShopId(orderItem.getShopId());
        resultItem.setOrderId(orderItem.getOrderId());
        resultItem.setOrderItemId(orderItem.getId());
        resultItem.setProductId(orderItem.getProductId());
        resultItem.setProductName(orderItem.getName());
        resultItem.setQuantity(orderItem.getQuantity());
        return resultItem;
    }

}
