package cn.edu.xmu.oomall.other.model.vo.coupon;

import cn.edu.xmu.oomall.other.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/9 16:24
 **/
@Data
public class CouponRetVo {
    private Long id;
    private Long activityId;
    private String name;
    private String couponSn;
    private Byte state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime endTime;
}
