package cn.edu.xmu.oomall.otherlistener.service.mq;

import cn.edu.xmu.oomall.core.util.JacksonUtil;

import cn.edu.xmu.oomall.otherlistener.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.otherlistener.microservice.CouponActivityService;
import cn.edu.xmu.oomall.otherlistener.model.CouponPo;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 消费者
 *
 * @author:李智樑
 * @time:2021/12/12 10:33
 **/
@Service
@RocketMQMessageListener(topic = "coupon-topic", consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.BROADCASTING, consumeThreadMax = 10, consumerGroup = "coupon-group")
public class CouponConsumerListener implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(CouponConsumerListener.class);

    @Autowired
    CouponPoMapper couponPoMapper;

    @Autowired
    CouponActivityService couponActivityService;

    @Override
    public void onMessage(String message) {
        CouponPo po = JacksonUtil.toObj(message, CouponPo.class);
        logger.info("onMessage: got message coupon =" + po + " time = " + LocalDateTime.now());
        couponPoMapper.insert(po);
        couponActivityService.decreaseCoupons(po.getActivityId());
        System.out.println("finished");
    }

}
