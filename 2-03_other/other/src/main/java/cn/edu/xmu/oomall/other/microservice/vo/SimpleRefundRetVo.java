package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gao Yanfeng
 * @date 2021/12/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleRefundRetVo {
    private Long id;
    private String tradeSn;
    private Long patternId;
    private String documentId;
    private Long paymentId;
    private Long amount;
    private Byte state;
    private Byte documentType;
}

