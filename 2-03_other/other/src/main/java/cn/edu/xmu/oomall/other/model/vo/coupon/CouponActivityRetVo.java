package cn.edu.xmu.oomall.other.model.vo.coupon;

import cn.edu.xmu.oomall.other.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/9 16:12
 **/
@Data
public class CouponActivityRetVo {
    private Long id;
    private String name;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime endTime;
    private Long quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", timezone = "GMT+8")
    private ZonedDateTime couponTime;
}
