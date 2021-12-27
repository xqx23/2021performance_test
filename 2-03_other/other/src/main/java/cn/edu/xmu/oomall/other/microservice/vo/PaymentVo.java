package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author jxy
 * @create 2021/12/24 4:15 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVo {
    private String patternId;
    private String documentId;
    private Byte documentType;
    private Long amount;
    private String descr;
    private Long prepaidId;

}
