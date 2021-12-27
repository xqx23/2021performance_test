package cn.edu.xmu.oomall.other.model.bo.calculator.single;

import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import lombok.Data;

/**
 * @author Gao Yanfeng
 * @date 2021/12/13
 */
@Data
public class PointLiquidationCalculator extends SingleLiquidationCalculator {

    public PointLiquidationCalculator(LiquidationInfo info) {
        super(info);
    }

    @Override
    public LiquidationResultItem calculate() {
        var resultItem = new LiquidationResultItem();
        resultItem.setSharerId(info.getPointsRet().getSharerId());
        resultItem.setPoint(info.getPointsRet().getPoints());
        resultItem.setShopRevenue(-info.getPointsRet().getPoints());
        return resultItem;
    }
}
