package util;

import cn.edu.xmu.oomall.other.microservice.bo.CouponActivity;
import cn.edu.xmu.oomall.other.microservice.vo.CouponActivityDetailRetVo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author:李智樑
 * @time:2021/12/14 0:36
 **/

public class CouponsFactory {
    private static CouponsFactory instance = null;

    public static CouponsFactory getInstance() {
        if (instance == null) {
            synchronized (CouponsFactory.class) {
                if (instance == null) {
                    instance = new CouponsFactory();
                }
            }
        }
        return instance;
    }

    public CouponActivityDetailRetVo getNoCouponActivity() {
        CouponActivityDetailRetVo couponActivityDetailRetVo = new CouponActivityDetailRetVo();
        couponActivityDetailRetVo.setQuantity(-1);

        return couponActivityDetailRetVo;
    }

    public CouponActivityDetailRetVo getCouponActivity_type_0() {
        CouponActivityDetailRetVo couponActivityDetailRetVo = new CouponActivityDetailRetVo();
        couponActivityDetailRetVo.setName("test");
        couponActivityDetailRetVo.setState(CouponActivity.State.ONLINE.getCode());
        couponActivityDetailRetVo.setId(1L);
        couponActivityDetailRetVo.setNumKey(10);
        couponActivityDetailRetVo.setQuantityType((byte) 0);
        couponActivityDetailRetVo.setQuantity(2);
        couponActivityDetailRetVo.setValidTerm((byte) 0);
        couponActivityDetailRetVo.setBeginTime(ZonedDateTime.of(2021, 10, 10, 10, 10, 10, 10, ZoneId.systemDefault()));
        couponActivityDetailRetVo.setCouponTime(ZonedDateTime.of(2021, 11, 11, 12, 10, 10, 10, ZoneId.systemDefault()));
        couponActivityDetailRetVo.setEndTime(ZonedDateTime.of(2021, 12, 31, 12, 12, 10, 10, ZoneId.systemDefault()));

        return couponActivityDetailRetVo;
    }

    public CouponActivityDetailRetVo getCouponActivity_type_1() {
        CouponActivityDetailRetVo couponActivityDetailRetVo = new CouponActivityDetailRetVo();
        couponActivityDetailRetVo.setName("test");
        couponActivityDetailRetVo.setState(CouponActivity.State.ONLINE.getCode());
        couponActivityDetailRetVo.setId(1L);
        couponActivityDetailRetVo.setNumKey(10);
        couponActivityDetailRetVo.setQuantityType((byte) 1);
        couponActivityDetailRetVo.setQuantity(2);
        couponActivityDetailRetVo.setValidTerm((byte) 0);
        couponActivityDetailRetVo.setBeginTime(ZonedDateTime.of(2021, 10, 10, 10, 10, 10, 10, ZoneId.systemDefault()));
        couponActivityDetailRetVo.setCouponTime(ZonedDateTime.of(2021, 11, 11, 12, 10, 10, 10, ZoneId.systemDefault()));
        couponActivityDetailRetVo.setEndTime(ZonedDateTime.of(2021, 12, 31, 12, 12, 10, 10, ZoneId.systemDefault()));

        return couponActivityDetailRetVo;
    }
}
