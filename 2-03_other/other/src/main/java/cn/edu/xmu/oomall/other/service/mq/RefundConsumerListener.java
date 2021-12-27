package cn.edu.xmu.oomall.other.service.mq;

import cn.edu.xmu.oomall.core.util.JacksonUtil;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.constant.RefundState;
import cn.edu.xmu.oomall.other.dao.AfterSaleDao;
import cn.edu.xmu.oomall.other.mapper.AfterSalePoMapper;
import cn.edu.xmu.oomall.other.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
import cn.edu.xmu.oomall.other.model.bo.AfterSale;
import cn.edu.xmu.oomall.other.model.bo.Customer;
import cn.edu.xmu.oomall.other.model.bo.RefundNotifyMessage;
import cn.edu.xmu.oomall.other.model.po.AfterSalePo;
import cn.edu.xmu.oomall.other.model.po.AfterSalePoExample;
import cn.edu.xmu.oomall.other.model.po.CouponPo;
import cn.edu.xmu.oomall.other.service.AfterSaleService;
import cn.edu.xmu.oomall.other.service.CustomerService;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author:李智樑
 * @time:2021/12/16 15:49
 **/
@Service
@RocketMQMessageListener(topic = "refund-type2-notify-topic", consumeMode = ConsumeMode.ORDERLY, consumerGroup = "aftersales-refund-group")
public class RefundConsumerListener implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(RefundConsumerListener.class);

    @Autowired
    AfterSaleService afterSaleService;

    @Autowired
    AfterSaleDao afterSaleDao;

    @Autowired
    CustomerService customerService;

    @Override
    public void onMessage(String message) {
        RefundNotifyMessage refundNotifyMessage = JSONObject.parseObject(message, RefundNotifyMessage.class);
        logger.info("onMessage: got message refundMessage =" + refundNotifyMessage + " time = " + LocalDateTime.now());
        // 退款成功
        if (refundNotifyMessage.getRefundState().getCode().equals(RefundState.FINISH_REFUND.getCode())) {
            // 根据售后单序号去查售后单
            ReturnObject returnObject = afterSaleDao.getAfterSaleBySn(refundNotifyMessage.getDocumentId());

            if (returnObject.getData() != null) {
                AfterSalePo afterSalePo = (AfterSalePo) returnObject.getData();
                afterSalePo.setState(AfterSale.State.OVER.getCode().byteValue());
                // 采用系统默认用户修改
                afterSaleDao.updateAfterSale(afterSalePo, 1L, "admin");
                //退还积点
                Long points = afterSaleService.calculateReturnPoints(afterSalePo);
                customerService.modifyCustomerPointsById(afterSalePo.getCustomerId(), points);
            }
        }
    }

}
