package cn.edu.xmu.oomall.other.model.vo.aftersale;

import lombok.Data;

/**
 * @author:李智樑
 * @time:2021/12/5 10:18
 **/
@Data
public class AfterSaleSimpleRetVo {
    private Long id;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long price;
    private Long quantity;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
}
