package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.JacksonUtil;
import cn.edu.xmu.oomall.other.model.po.CouponPo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:李智樑
 * @time:2021/12/12 10:27
 **/
@Service
public class RocketMQService {
    private static final Logger logger = LoggerFactory.getLogger(RocketMQService.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendCouponMessage(CouponPo couponPo) {
        String json = JacksonUtil.toJson(couponPo);
        Message message = MessageBuilder.withPayload(json).build();
        logger.info("sendCouponMessage: message = " + message);
        rocketMQTemplate.sendOneWay("coupon-topic", message);
    }
}
