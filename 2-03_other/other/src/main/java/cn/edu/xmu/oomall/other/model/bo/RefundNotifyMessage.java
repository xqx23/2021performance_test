package cn.edu.xmu.oomall.other.model.bo;

import cn.edu.xmu.oomall.other.constant.RefundState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:李智樑
 * @time:2021/12/20 10:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundNotifyMessage {
    private Byte documentType;
    private String documentId;
    private RefundState refundState;
}
