package cn.edu.xmu.oomall.other.model.vo.coupon;

import cn.edu.xmu.oomall.other.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/24 14:52
 **/
@Data
public class CouponRetVoForReceive {
    private Long id;
    private Long customerId;
    private Long activityId;
    private String name;
    private String couponSn;
    private Byte state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime endTime;
}
