package cn.edu.xmu.oomall.other.model.vo.coupon;

import cn.edu.xmu.oomall.other.constant.Constants;
import cn.edu.xmu.oomall.other.model.vo.SimpleUserRetVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/12 15:22
 **/
@Data
public class CouponDetailRetVo {
    private Long id;
    private CouponActivityRetVo activity;
    private Long customerId;
    private String name;
    private String couponSn;
    private Byte state;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime endTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtModified;
    private SimpleUserRetVo creator;
    private SimpleUserRetVo modifier;
}
