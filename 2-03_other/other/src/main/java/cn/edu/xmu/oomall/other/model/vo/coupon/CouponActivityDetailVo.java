package cn.edu.xmu.oomall.other.model.vo.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/24 1:01
 **/
@Data
public class CouponActivityDetailVo implements Serializable {
    private Long id;
    private String name;
    private LocalDateTime couponTime;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Byte quantityType;
    private Byte validTerm;
    private String imageUrl;
    private Integer numKey;
    private String strategy;
    private Byte state;
}
