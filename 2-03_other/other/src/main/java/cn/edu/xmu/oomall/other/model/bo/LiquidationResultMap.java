package cn.edu.xmu.oomall.other.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gao Yanfeng
 * @date 2021/12/11
 */
@Data
@AllArgsConstructor
public class LiquidationResultMap {
    private Map<Long, LiquidationResultItem> resultItemMap;

    public LiquidationResultMap() {
        resultItemMap = new HashMap<>();
    }

    public void updateBy(LiquidationResultItem item) {
        var cur = resultItemMap.get(item.getShopId());
        cur.updateBy(item);
        resultItemMap.put(item.getShopId(), cur);
    }

    public void put(Long shopId, LiquidationResultItem item) {
        resultItemMap.put(shopId, item);
    }

    public LiquidationResultItem get(Long id) {
        return resultItemMap.get(id);
    }
}
