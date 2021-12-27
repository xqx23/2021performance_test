package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.oomall.other.util.Accumulator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * @author Gao Yanfeng
 * @date 2021/12/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiquidationResultItem {

    private Long id;

    private Long liquidId;

    private Long paymentId;

    private Long refundId;

    private Long revenueId;

    private Long shopId;

    private String shopName;

    private Long orderId;

    private Long orderItemId;

    private Long productId;

    private String productName;

    private Long sharerId;

    private LocalDateTime liquidDate;

    private Byte state = 0;

    @Accumulator
    private Long quantity = 0L;

    @Accumulator
    private Long amount = 0L;

    @Accumulator
    private Long expressFee = 0L;

    @Accumulator
    private Long commission = 0L;

    @Accumulator
    private Long point = 0L;

    @Accumulator
    private Long shopRevenue = 0L;

    public void updateBy(LiquidationResultItem item) {
        Field[] fields = getClass().getDeclaredFields();
        try {
            for (var field : fields) {
                if (field.get(item) != null) {
                    if (field.getAnnotation(Accumulator.class) != null) {
                        field.set(this, (Long) field.get(this) + (Long) field.get(item));
                    } else {
                        field.set(this, field.get(item));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
