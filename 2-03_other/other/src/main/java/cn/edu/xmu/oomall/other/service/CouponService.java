package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.CouponDao;
import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
import cn.edu.xmu.oomall.other.microservice.bo.CouponActivity;
import cn.edu.xmu.oomall.other.microservice.vo.CouponActivityDetailRetVo;
import cn.edu.xmu.oomall.other.model.bo.Coupon;
import cn.edu.xmu.oomall.other.model.bo.Customer;
import cn.edu.xmu.oomall.other.model.po.CouponPo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponActivityDetailVo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponActivityRetVo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponDetailRetVo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponRetVo;
import cn.edu.xmu.oomall.other.util.PageUtils;
import cn.edu.xmu.privilegegateway.annotation.util.Common;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
import cn.edu.xmu.privilegegateway.annotation.util.bloom.BloomFilter;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author:李智樑
 * @time:2021/12/8 18:01
 **/
@Service
public class CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);
    private final static String UNAVAILABLE_USER_SET = "Claimed_%d_";
    private final static String ACTIVITY_KEY = "micro_service_coupon_activity_%d";
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponActivityService couponActivityService;
    @Autowired
    private RocketMQService rocketMQService;
    @Autowired
    private BloomFilter<Long> longBloomFilter;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${other.bloomfilter.new-coupon.error-rate}")
    private double couponError = 0.01;
    @Value("${other.bloomfilter.new-coupon.capacity}")
    private int couponCapacity = 10000;

    private static int timeout = 3600;

    public ReturnObject getCouponStates() {
        List<Map<String, Object>> stateList = new ArrayList<>();
        for (Coupon.State states : Coupon.State.values()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("code", states.getCode());
            temp.put("name", states.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getCoupons(Long userId, Integer page, Integer pageSize, Integer state) {
        ReturnObject ret = couponDao.getCoupons(userId, page, pageSize, state);
        PageInfo<CouponPo> pageInfo = (PageInfo<CouponPo>) ret.getData();
//        List<CouponPo> couponPos = pageInfo.getList();
//        List<CouponRetVo> couponRetVos = new ArrayList<>(couponPos.size());
//        for (CouponPo couponPo : couponPos) {
//            CouponRetVo couponRetVo = cloneVo(couponPo, CouponRetVo.class);
//
//            CouponActivityRetVo couponActivityRetVo = Common.cloneVo(couponActivityService.
//                    getCouponActivityById(couponPo.getActivityId()).getData(), CouponActivityRetVo.class);
//            couponRetVo.setActivity(couponActivityRetVo);
//            couponRetVos.add(couponRetVo);
//        }
//        /* 转为PageInfo */
//        PageInfo<CouponRetVo> couponRetVoPageInfo = PageUtils.PageInfo2PageInfoVo(pageInfo, couponRetVos);

        return new ReturnObject<>(pageInfo);
    }

    public ReturnObject receiveCoupons(Long userId, String userName, Long activityId) {
        CouponActivityDetailVo couponActivityDetailVo;
        String activityKey = String.format(ACTIVITY_KEY, activityId);

        // 采用redis避免频繁调用微服务
        if (redisUtil.hasKey(activityKey)) {
            couponActivityDetailVo = (CouponActivityDetailVo) redisUtil.get(activityKey);
        } else {
            InternalReturnObject internalReturnObject = couponActivityService.getCouponActivityById(activityId);
            if (internalReturnObject.getData() == null) {
                return new ReturnObject(ReturnNo.getByCode(internalReturnObject.getErrno()), internalReturnObject.getErrmsg());
            }
            couponActivityDetailVo =
                    Common.cloneVo(internalReturnObject.getData(), CouponActivityDetailVo.class);
            redisUtil.set(activityKey, couponActivityDetailVo, timeout);
        }

        /* 若优惠活动quantity为-1，表示该优惠活动不需要优惠卷，返回 504 错误 */
        if (couponActivityDetailVo.getQuantity().equals(-1)) {
            return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
        }

        ReturnObject ret = getCouponsIfPermitted(userId, userName, activityId, couponActivityDetailVo);
        /*  若优惠券列表为空，直接返回*/
        if (null == ret.getData()) {
            return ret;
        }

        List<CouponPo> couponPos = (List<CouponPo>) ret.getData();
//        List<CouponDetailRetVo> couponDetailRetVos = new ArrayList<>(couponPos.size());

        Byte quantityType = couponActivityDetailVo.getQuantityType();
//        CouponActivityRetVo couponActivityRetVo = Common.cloneVo(couponActivityDetailVo, CouponActivityRetVo.class);

        for (CouponPo couponPo : couponPos) {
            /* 分两种情况，因为quantityType为0不需要修改quantity字段 */
            if (quantityType == 0) {
                couponDao.insertCoupon(couponPo);
            } else {
                rocketMQService.sendCouponMessage(couponPo);
            }

//            CouponDetailRetVo couponDetailRetVo = Common.cloneVo(couponPo, CouponDetailRetVo.class);
//            couponDetailRetVo.setActivity(couponActivityRetVo);
//            couponDetailRetVos.add(couponDetailRetVo);
        }

        return new ReturnObject(couponPos);
    }

    public ReturnObject getCouponsIfPermitted(Long userId, String userName, Long activityId, CouponActivityDetailVo couponActivityDetailVo) {
        List<CouponPo> couponPos = new ArrayList<>();
        if (null == couponActivityDetailVo) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "不存在该优惠活动");
        } else if (couponActivityDetailVo.getCouponTime().isAfter(LocalDateTime.now())) {
            return new ReturnObject(ReturnNo.COUPON_NOTBEGIN, "未到优惠券领取时间");
        } else if (!couponActivityDetailVo.getState().equals(CouponActivity.State.ONLINE.getCode())) {
            return new ReturnObject(ReturnNo.STATENOTALLOW, "活动未上线禁止领取");
        } else if (couponActivityDetailVo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject(ReturnNo.COUPON_END, "优惠卷活动终止");
        }
        /*
          判断quantityType的类型
          0: 每人数量（即每个人都能领到）
          1: 总数控制（即每个人不一定能抢到）
          */
        if (couponActivityDetailVo.getQuantityType().equals((byte) 0)) {
            /* quantity = 0的时候表示领完了 */
            if (couponActivityDetailVo.getQuantity() == 0) {
                return new ReturnObject(ReturnNo.COUPON_FINISH);
            }
            /* quantityType == 0: 每人只能领quantity张，故需加入bloom过滤器中防止反复领取 */
            /* 采用布隆过滤器查看用户是否领取过此优惠券 */
            // todo: 高并发时使用布隆过滤器可能有超领
            String key = String.format(UNAVAILABLE_USER_SET, activityId);
            if (!longBloomFilter.checkFilter(key)) {
                longBloomFilter.newFilter(key, couponError, couponCapacity);
            }
            if (longBloomFilter.checkValue(key, userId)) {
                return new ReturnObject(ReturnNo.COUPON_EXIST, "已领取该优惠券");
            }

            longBloomFilter.addValue(key, userId);
            /* 生成quantity张优惠券 */
            for (int i = 0; i < couponActivityDetailVo.getQuantity(); i++) {
                CouponPo couponPo = generateCoupon(userId, userName,
                        activityId, couponActivityDetailVo.getValidTerm(),
                        couponActivityDetailVo.getCouponTime(),
                        couponActivityDetailVo.getEndTime(),
                        couponActivityDetailVo.getName());

                couponPos.add(couponPo);
            }
        } else {
            /* quantityType == 1: 每人可以重复领，一次领一张 */
            Long groupNum = couponActivityDetailVo.getNumKey().longValue();
            ReturnObject returnObject = couponDao.decreaseCouponQuantity(activityId, 1, groupNum.intValue(), couponActivityDetailVo.getQuantity().intValue());

            if (!returnObject.getCode().equals(ReturnNo.OK)) {
                /* 优惠券数量不够或网络异常 */
                return returnObject;
            } else {
                /* 优惠券数量够，领取一张优惠券 */
                CouponPo couponPo = generateCoupon(userId, userName,
                        activityId, couponActivityDetailVo.getValidTerm(),
                        couponActivityDetailVo.getBeginTime(),
                        couponActivityDetailVo.getEndTime(),
                        couponActivityDetailVo.getName());

                couponPos.add(couponPo);
            }
        }

        return new ReturnObject(couponPos);
    }

    private CouponPo generateCoupon(Long userId, String userName, Long activityId, Byte validTerm,
                                    LocalDateTime activityBeginTime, LocalDateTime activityEndTime, String activityName) {
        CouponPo couponPo = new CouponPo();

        couponPo.setCouponSn(Common.genSeqNum(1));
        couponPo.setName(activityName + "的优惠券");
        couponPo.setCustomerId(userId);
        couponPo.setActivityId(activityId);
        if (validTerm == 0) {
            couponPo.setBeginTime(activityBeginTime);
            couponPo.setEndTime(activityEndTime);
        } else {
            couponPo.setBeginTime(LocalDateTime.now());
            couponPo.setEndTime(couponPo.getBeginTime().plusDays(validTerm).isAfter(activityEndTime) ?
                    activityEndTime : couponPo.getBeginTime().plusDays(validTerm));
        }
        couponPo.setState(Coupon.State.RECEIVED.getCode().byteValue());
        Common.setPoCreatedFields(couponPo, userId, userName);

        return couponPo;
    }

    public ReturnObject modifyCouponState(Long id, Byte state, Long userId, String userName) {
        CouponPo couponPo = new CouponPo();

        couponPo.setId(id);
        couponPo.setState(state);
        Common.setPoModifiedFields(couponPo, userId, userName);

        return couponDao.updateCoupon(couponPo);
    }

    public ReturnObject isCouponExists(Long id, Long userId) {
        ReturnObject ret = couponDao.getCouponById(id);
        if (ret.getData() == null) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
        }

        CouponPo couponPo = (CouponPo) ret.getData();
        if (!couponPo.getCustomerId().equals(userId)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }

        // 判断状态是否为未使用以及时间是否在范围内
        if (!couponPo.getState().equals(Coupon.State.RECEIVED.getCode().byteValue())) {
            return new ReturnObject(ReturnNo.STATENOTALLOW);
        }
        if (couponPo.getBeginTime().isAfter(LocalDateTime.now()) || couponPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject(ReturnNo.STATENOTALLOW);
        }

        return new ReturnObject(ReturnNo.OK, "存在此优惠券");
    }
}
