package cn.edu.xmu.oomall.otherlistener.dao;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.otherlistener.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.otherlistener.model.CouponPo;
import cn.edu.xmu.oomall.otherlistener.model.CouponPoExample;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.edu.xmu.oomall.core.util.Common.getAvgArray;

/**
 * @author:李智樑
 * @time:2021/12/9 15:03
 **/
@Repository
public class CouponDao {
    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    @Autowired
    private CouponPoMapper couponPoMapper;

    public ReturnObject insertCoupon(CouponPo couponPo) {
        try {
            couponPoMapper.insert(couponPo);
            return new ReturnObject();
        } catch (Exception e) {
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }

}
