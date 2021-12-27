package cn.edu.xmu.oomall.other.dao;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.other.model.po.CouponPo;
import cn.edu.xmu.oomall.other.model.po.CouponPoExample;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.edu.xmu.oomall.core.util.Common.getAvgArray;

/**
 * @author:李智樑
 * @time:2021/12/9 15:03
 **/
@Repository
public class CouponDao {
    private final static String COUPON_ACTIVITY_QUANTITY_GROUP_KEY = "coupon_activity_%d_quantity_group_%d";
    private final static String COUPON_ACTIVITY_SET_KEY = "coupon_activity_%d_set";

    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    private final static String DECREASE_PATH = "coupons/decrease.lua";
    private final static String LOAD_PATH = "coupons/load.lua";
    private final static String CHECK_PATH = "coupons/check.lua";

    @Autowired
    private CouponPoMapper couponPoMapper;

    @Autowired
    private RedisUtil redisUtil;

    public ReturnObject getCoupons(Long userId, Integer page, Integer pageSize, Integer state) {
        CouponPoExample couponPoExample = new CouponPoExample();
        CouponPoExample.Criteria criteria = couponPoExample.createCriteria();
        criteria.andCustomerIdEqualTo(userId);
        if (state != null) {
            criteria.andStateEqualTo(state.byteValue());
        }

        List<CouponPo> couponPoList;
        try {
            PageHelper.startPage(page, pageSize);
            couponPoList = couponPoMapper.selectByExample(couponPoExample);
            PageInfo<CouponPo> pageInfo = new PageInfo<>(couponPoList);
            return new ReturnObject(pageInfo);
        } catch (Exception e) {
            logger.error("getCoupons: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject getCouponById(Long id) {
        try {
            CouponPo couponPo = couponPoMapper.selectByPrimaryKey(id);
            return new ReturnObject(couponPo);
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }

    }

    public ReturnObject updateCoupon(CouponPo couponPo) {
        try {
            int ret = couponPoMapper.updateByPrimaryKeySelective(couponPo);
            if (ret == 0) {
                return new ReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST);
            } else {
                return new ReturnObject();
            }
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

    public ReturnObject insertCoupon(CouponPo couponPo) {
        try {
            couponPoMapper.insert(couponPo);
            return new ReturnObject();
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }


    /**
     * @param id            优惠活动id
     * @param quantity      领取优惠券数量
     * @param groupNum      桶的数量
     * @param wholeQuantity 优惠券总数量
     * @return
     */

    public ReturnObject decreaseCouponQuantity(Long id, Integer quantity, Integer groupNum, Integer wholeQuantity) {
        try {
            DefaultRedisScript<Boolean> scriptExists = new DefaultRedisScript<>();
            String setKey = String.format(COUPON_ACTIVITY_SET_KEY, id);

            scriptExists.setScriptSource(new ResourceScriptSource(new ClassPathResource(CHECK_PATH)));
            scriptExists.setResultType(Boolean.class);

            Boolean isExists = redisUtil.executeScript(scriptExists,
                    Stream.of(setKey).collect(Collectors.toList()));
            if (isExists != null && !isExists) {
                loadQuantity(setKey, id, groupNum, wholeQuantity);
            }

            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource(DECREASE_PATH)));
            script.setResultType(Long.class);

            long timeSeed = System.currentTimeMillis();

            Long res = redisUtil.executeScript(script,
                    Stream.of(setKey).collect(Collectors.toList()), quantity, timeSeed);

            if (res >= 0) {
                return new ReturnObject(ReturnNo.OK);
            }
            return new ReturnObject(ReturnNo.COUPON_FINISH);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
        }
    }

    public void loadQuantity(String setKey, Long id, Integer groupNum, Integer wholeQuantity) {
        int[] incr = getAvgArray(groupNum, wholeQuantity);
        Object[] args = new Object[incr.length];
        // int数组转Object数组
        for (int i = 0; i < groupNum; i++) {
            args[i] = incr[i];
        }
        DefaultRedisScript script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(LOAD_PATH)));

        List<String> key = new ArrayList<>(groupNum + 1);
        key.add(setKey);
        for (int i = 0; i < groupNum; i++) {
            key.add(String.format(COUPON_ACTIVITY_QUANTITY_GROUP_KEY, id, i));
        }

        redisUtil.executeScript(script, key, args);
    }
}
