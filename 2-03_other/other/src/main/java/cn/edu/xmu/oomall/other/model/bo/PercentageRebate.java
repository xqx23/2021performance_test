package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.oomall.other.microservice.vo.StrategyVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static java.lang.Math.round;

/**
 * @author jxy
 * @create 2021/12/12 8:23 PM
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PercentageRebate extends BaseShareRebate {

    //初始数量
    private Long originalQuantity;
    //新增数量
    private Long additionalQuantity;
    //orderitem价格
    private Long price;
    private Integer unit;
    private List<StrategyVo> shareStrategies;

    @Override
    public Long calculate() {
        Long points = 0L;
        int left = originalQuantity.intValue();
        int right;

        for (int i = 0; i < shareStrategies.size(); i++) {
            StrategyVo shareStrategy = shareStrategies.get(i);
            right = shareStrategy.getQuantity();
            /* 如果超过当前门槛数，跳过 */
            if (right != -1 && originalQuantity >= right) {
                continue;
            }
            /* 计算当前区间点数 */
            points += calculatePart(left, right, originalQuantity + additionalQuantity, unit, price, shareStrategy.getPercentage());
            left = right;
        }
        points = round(points / 1000.0);
        return points;
    }
}
