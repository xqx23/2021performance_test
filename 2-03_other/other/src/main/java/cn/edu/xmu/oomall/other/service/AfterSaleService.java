package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.AfterSaleDao;
import cn.edu.xmu.oomall.other.microservice.OrderService;
import cn.edu.xmu.oomall.other.microservice.PaymentService;
import cn.edu.xmu.oomall.other.microservice.RegionService;
import cn.edu.xmu.oomall.other.microservice.vo.*;
import cn.edu.xmu.oomall.other.model.bo.AfterSale;
import cn.edu.xmu.oomall.other.model.po.AfterSalePo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.oomall.other.model.vo.aftersale.*;
import cn.edu.xmu.oomall.other.model.vo.customer.CustomerRetVo;
import cn.edu.xmu.oomall.other.model.vo.customer.SimpleCustomerRetVo;
import cn.edu.xmu.privilegegateway.annotation.util.Common;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.*;

/**
 * @author jxy
 * @create 2021/12/11 9:22 AM
 */

@Service
public class AfterSaleService {
    private static final Logger logger = LoggerFactory.getLogger(AfterSaleService.class);

    @Autowired
    private AfterSaleDao afterSaleDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    public ReturnObject getAfterSaleAllStates() {
        return afterSaleDao.getAfterSaleStates();
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject newAfterSale(AfterSaleVo vo, Long orderItemId, Long userId, String loginName) {
        // 校验订单
        InternalReturnObject internalReturnObject = orderService.getOrderItemById(orderItemId, userId);
        if (!internalReturnObject.getErrno().equals(ReturnNo.OK.getCode()))
            return new ReturnObject(ReturnNo.getByCode(internalReturnObject.getErrno()), internalReturnObject.getErrmsg());
        OrderItemRetVo orderItemRetVo = (OrderItemRetVo) internalReturnObject.getData();

        //校验地区
        internalReturnObject = regionService.getSimpleRegionById(vo.getRegionId());
        if (!internalReturnObject.getErrno().equals(ReturnNo.OK.getCode()))
            return new ReturnObject(ReturnNo.getByCode(internalReturnObject.getErrno()), internalReturnObject.getErrmsg());

        AfterSalePo afterSalePo = cloneVo(vo, AfterSalePo.class);
        afterSalePo.setOrderItemId(orderItemId);
        afterSalePo.setState(AfterSale.State.NEW.getCode().byteValue());
        afterSalePo.setServiceSn(genSeqNum(1));
        afterSalePo.setCustomerId(orderItemRetVo.getCustomerId());
        afterSalePo.setShopId(orderItemRetVo.getShopId());

        afterSalePo = (AfterSalePo) afterSaleDao.insertAftersale(afterSalePo, userId, loginName).getData();
        AfterSaleSimpleRetVo afterSaleSimpleRetVo = cloneVo(afterSalePo, AfterSaleSimpleRetVo.class);
        return new ReturnObject(afterSaleSimpleRetVo);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getAfterSales(Long userId, Long shopId, ZonedDateTime beginTime, ZonedDateTime endTime,
                                      Integer page, Integer pageSize, Integer type, Integer state, Boolean isTimeDesc) {
        return afterSaleDao.getAllAftersales(userId, shopId, beginTime, endTime, page, pageSize, type, state, isTimeDesc);
    }

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject getAfterSaleById(Long userId, Long afterSaleId) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(afterSaleId);

        if (ret.getData() == null) {
            return ret;
        } else {
            AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
            // 检验与用户id是否一致
            if (!afterSalePo.getCustomerId().equals(userId)) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            AfterSaleRetVo afterSaleRetVo = Common.cloneVo(afterSalePo, AfterSaleRetVo.class);
            // 根据customerId查找Customer并填充RetVo
            CustomerRetVo customerRetVo = (CustomerRetVo) customerService.getCustomerById(userId).getData();
            afterSaleRetVo.setCustomer(Common.cloneVo(customerRetVo, SimpleCustomerRetVo.class));
            // 根据regionId查找Region并填充RetVo
            SimpleObject regionSimpleRetVo = (SimpleObject) regionService.getSimpleRegionById(afterSalePo.getRegionId()).getData();
            afterSaleRetVo.setRegion(regionSimpleRetVo);

            return new ReturnObject(afterSaleRetVo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject modifyAfterSaleById(Long userId, String loginName, Long afterSaleId, AfterSaleModifyVo vo) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(afterSaleId);
        if (ret.getData() == null) {
            return ret;
        } else {
            AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
            // 检验与用户id是否一致
            if (!afterSalePo.getCustomerId().equals(userId)) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
            // 检验State必须为新建态
            if (!afterSalePo.getState().equals(AfterSale.State.NEW.getCode().byteValue())) {
                // TODO 状态码不清楚
                return new ReturnObject(ReturnNo.STATENOTALLOW);
            }
            // update 传值
            if (vo.getConsignee() != null && !vo.getConsignee().isBlank()) {
                afterSalePo.setConsignee(vo.getConsignee());
            }
            if (vo.getDetail() != null && !vo.getDetail().isBlank()) {
                afterSalePo.setDetail(vo.getDetail());
            }
            if (vo.getMobile() != null && !vo.getMobile().isBlank()) {
                afterSalePo.setMobile(vo.getMobile());
            }
            if (vo.getQuantity() != null) {
                afterSalePo.setQuantity(vo.getQuantity());
            }
            if (vo.getReason() != null && !vo.getReason().isBlank()) {
                afterSalePo.setQuantity(vo.getQuantity());
            }
            if (vo.getRegionId() != null) {
                afterSalePo.setRegionId(vo.getRegionId());
            }

            return afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
        }
    }

    /**
     * @author jxy
     * @create 2021/12/11 9:24 AM
     */
    @Transactional(rollbackFor = Exception.class)
    public ReturnObject deleteAftersaleById(Long userId, String loginName, Long id) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(id);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (afterSalePo.getBeDeleted().equals((byte) 1))
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);

        if (!afterSalePo.getCustomerId().equals(userId))
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        if (afterSalePo.getState().equals(AfterSale.State.NEW.getCode().byteValue()) || afterSalePo.getState().equals(AfterSale.State.WAIT_CUSTOMER_DELIVER.getCode().byteValue())) {
            afterSalePo.setState(AfterSale.State.CANCELED.getCode().byteValue());
            afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
            return new ReturnObject<>();
        }

        if (afterSalePo.getState().equals(AfterSale.State.OVER.getCode().byteValue()) || afterSalePo.getState().equals(AfterSale.State.CANCELED.getCode().byteValue())) {
            afterSalePo.setBeDeleted((byte) 1);
            afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
            return new ReturnObject<>();
        }

        return new ReturnObject<>(ReturnNo.STATENOTALLOW);
    }

    /**
     * @author jxy
     * @create 2021/12/11 9:53 AM
     */
    public ReturnObject addWayBillNumber(Long userId, String loginName, Long id, String logSn) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(id);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (afterSalePo.getBeDeleted().equals((byte) 1))
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);

        if (!afterSalePo.getCustomerId().equals(userId))
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);

        if (!afterSalePo.getState().equals(AfterSale.State.WAIT_CUSTOMER_DELIVER.getCode().byteValue()))
            return new ReturnObject<>(ReturnNo.STATENOTALLOW);

        afterSalePo.setState(AfterSale.State.CUSTOMER_DELIVERED.getCode().byteValue());
        afterSalePo.setCustomerLogSn(logSn);
        afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
        return new ReturnObject<>();
    }

    /**
     * @author jxy
     * @create 2021/12/11 10:39 AM
     */
    public ReturnObject confirmAftersaleEnd(Long userId, String loginName, Long id) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(id);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (afterSalePo.getBeDeleted().equals((byte) 1))
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);

        if (!afterSalePo.getCustomerId().equals(userId))
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);

        if (!afterSalePo.getState().equals(AfterSale.State.WAIT_SHOP_REFUND.getCode().byteValue()) && !afterSalePo.getState().equals(AfterSale.State.SHOP_DELIVERED.getCode().byteValue()))
            return new ReturnObject<>(ReturnNo.STATENOTALLOW);
        afterSalePo.setState(AfterSale.State.OVER.getCode().byteValue());
        afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
        return new ReturnObject<>();
    }

    /**
     * @author jxy
     * @create 2021/12/11 10:52 AM
     */
    public ReturnObject adminGetAftersaleById(Long shopId, Long id) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(id);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (!afterSalePo.getShopId().equals(shopId) && shopId != 0L)
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        FullAfterSaleRetVo fullAfterSaleRetVo = Common.cloneVo(afterSalePo, FullAfterSaleRetVo.class);
        // 根据customerId查找Customer并填充RetVo
        CustomerRetVo customerRetVo = (CustomerRetVo) customerService.getCustomerById(afterSalePo.getCustomerId()).getData();
        fullAfterSaleRetVo.setCustomer(Common.cloneVo(customerRetVo, SimpleCustomerRetVo.class));
        // 根据regionId查找Region并填充RetVo
        SimpleObject regionSimpleRetVo = (SimpleObject) regionService.getSimpleRegionById(afterSalePo.getRegionId()).getData();
        fullAfterSaleRetVo.setRegion(regionSimpleRetVo);
        return new ReturnObject<>(fullAfterSaleRetVo);
    }

    /**
     * @author jxy
     * @create 2021/12/11 11:16 AM
     */
    public ReturnObject adminConfirm(Long aftersaleId, Long shopId, AftersaleConfirmVo vo, Long userId, String loginName) {

        ReturnObject ret = afterSaleDao.getAfterSaleById(aftersaleId);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (!afterSalePo.getShopId().equals(shopId) && shopId != 0L)
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        if (!afterSalePo.getState().equals(AfterSale.State.NEW.getCode().byteValue()))
            return new ReturnObject(ReturnNo.STATENOTALLOW);

        if (vo.getConfirm()) {
            copyAttribute(vo, afterSalePo);
            afterSalePo.setState(AfterSale.State.WAIT_CUSTOMER_DELIVER.getCode().byteValue());
//            afterSalePo.setConclusion(vo.getConclusion());
//            afterSalePo.setType(vo.getType());
//            if (!vo.getType().equals(AfterSale.Type.RETURN.getCode().byteValue()))
//                afterSalePo.setPrice(vo.getPrice());
//            else
//                afterSalePo.setPrice(calculateRefund(afterSalePo));
        } else {
            afterSalePo.setState(AfterSale.State.CANCELED.getCode().byteValue());
        }

        afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
        return new ReturnObject<>();
    }

    /**
     * @author jxy
     * @create 2021/12/11 3:33 PM
     */

    public Long calculateRefund(AfterSalePo afterSalePo) {
        //todo:计算退货的退款金额,可能是这么算的吧
        InternalReturnObject internalReturnObject = orderService.getOrderItemById(afterSalePo.getOrderItemId(), afterSalePo.getCustomerId());
        OrderItemRetVo orderItemRetVo = (OrderItemRetVo) internalReturnObject.getData();
        Long price = afterSalePo.getQuantity() * (orderItemRetVo.getPrice() - orderItemRetVo.getDiscountPrice() - orderItemRetVo.getPoint());
        return price;
    }

    /**
     * @author jxy
     * @create 2021/12/13 5:04 PM
     */
    public Long calculateReturnPoints(AfterSalePo afterSalePo) {
        //todo:计算退货的退还积点,可能是这么算的吧
        InternalReturnObject internalReturnObject = orderService.getOrderItemById(afterSalePo.getOrderItemId(), afterSalePo.getCustomerId());
        OrderItemRetVo orderItemRetVo = (OrderItemRetVo) internalReturnObject.getData();
        Long points = afterSalePo.getQuantity() * orderItemRetVo.getPoint();
        return points;
    }

    /**
     * @author jxy
     * @create 2021/12/11 7:22 PM
     */

    public ReturnObject adminReceive(Long aftersaleId, Long shopId, AftersaleReceiveVo vo, Long userId, String loginName) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(aftersaleId);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (!afterSalePo.getShopId().equals(shopId) && shopId != 0L)
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        if (!afterSalePo.getState().equals(AfterSale.State.CUSTOMER_DELIVERED.getCode().byteValue()))
            return new ReturnObject(ReturnNo.STATENOTALLOW);


        if (vo.getConclusion() != null && !vo.getConclusion().isBlank())
            afterSalePo.setConclusion(vo.getConclusion());
        if (vo.getConfirm().equals(true)) {
            if (afterSalePo.getType().equals(AfterSale.Type.RETURN.getCode().byteValue())) {

                //退款
                Long refundPrice = calculateRefund(afterSalePo);
                SimplePaymentRetVo paymentRetVo = orderService.getPaymentByOrderItemId(afterSalePo.getOrderItemId()).getData();
                RefundVo refundVo = cloneVo(paymentRetVo, RefundVo.class);
                refundVo.setPaymentId(paymentRetVo.getId());
                refundVo.setAmount(refundPrice);
                paymentService.requestRefund(refundVo);
                afterSalePo.setState(AfterSale.State.WAIT_SHOP_REFUND.getCode().byteValue());
            } else if (afterSalePo.getType().equals(AfterSale.Type.EXCHANGE.getCode().byteValue())) {
             //新建售后订单
                ret=new ReturnObject(orderService.getOrderItemById(afterSalePo.getOrderItemId(),null));
                if (ret.getCode() != ReturnNo.OK)
                    return ret;
                OrderItemRetVo orderItemRetVo=(OrderItemRetVo) ret.getData();
                AftersaleOrderItemRecVo aftersaleOrderItemRecVo=cloneVo(orderItemRetVo,AftersaleOrderItemRecVo.class);
                AftersaleRecVo aftersaleRecVo=cloneVo(afterSalePo,AftersaleRecVo.class);
                aftersaleRecVo.setOrderItem(aftersaleOrderItemRecVo);
                aftersaleRecVo.setCustomerId(orderItemRetVo.getCustomerId());
                InternalReturnObject internalReturnObject=orderService.createAftersaleOrder(afterSalePo.getShopId(),aftersaleRecVo);
                if (internalReturnObject.getErrno() !=0)
                    return new ReturnObject(internalReturnObject);
                AfterSaleRetVo afterSaleRetVo=(AfterSaleRetVo)internalReturnObject.getData();

                afterSalePo.setPrice(0L);
                afterSalePo.setOrderId(afterSaleRetVo.getOrderId());
                afterSalePo.setState(AfterSale.State.SHOP_DELIVERED.getCode().byteValue());
            }
            else if(afterSalePo.getType().equals(AfterSale.Type.MAINTAIN.getCode().byteValue()))
            {
                PaymentVo paymentVo=new PaymentVo();
                paymentVo.setDocumentId(afterSalePo.getServiceSn());
                paymentVo.setAmount(afterSalePo.getPrice());
                afterSalePo.setState(AfterSale.State.WAIT_PAY.getCode().byteValue());
                paymentService.requestPayment(paymentVo);
                afterSalePo.setState(AfterSale.State.WAIT_PAY.getCode().byteValue());
            }

        } else {
            afterSalePo.setState(AfterSale.State.WAIT_CUSTOMER_DELIVER.getCode().byteValue());
        }

        afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);

        return new ReturnObject();
    }


    /**
     * @author jxy
     * @create 2021/12/11 8:01 PM
     */
    public ReturnObject adminDeliver(Long aftersaleId, Long shopId, String logSn, Long userId, String loginName) {
        ReturnObject ret = afterSaleDao.getAfterSaleById(aftersaleId);
        if (ret.getCode() != ReturnNo.OK)
            return ret;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        if (!afterSalePo.getShopId().equals(shopId) && shopId != 0L)
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        if (!afterSalePo.getState().equals(AfterSale.State.WAIT_SHOP_DELIVER.getCode().byteValue()))
            return new ReturnObject(ReturnNo.STATENOTALLOW);

        afterSalePo.setState(AfterSale.State.SHOP_DELIVERED.getCode().byteValue());
        if (logSn != null || !logSn.isBlank())
            afterSalePo.setShopLogSn(logSn);
        afterSaleDao.updateAfterSale(afterSalePo, userId, loginName);
        return new ReturnObject();
    }
    /**
    * @author jxy
    * @create 2021/12/14 9:35 AM
    */
    //todo：还没做完
    public ReturnObject getPaymentsByAftersaleId(Long aftersaleId)
    {
        String serviceSn=getServiceSnByAfterSaleId(aftersaleId);
        InternalReturnObject<SimplePageInfo<SimplePaymentRetVo>> internalReturnObject=paymentService.listPaymentInternal(serviceSn,null,null,null,1,10);
        if (internalReturnObject.getErrno() != 0)
            return new ReturnObject<>(internalReturnObject);
        SimplePaymentRetVo paymentRetVo =internalReturnObject.getData().getList().get(0);
        return new ReturnObject<>(paymentRetVo);
    }
    /**
    * @author jxy
    * @create 2021/12/17 3:30 PM
     * 通过售后单id查询售后单sn
    */

    public String getServiceSnByAfterSaleId(Long aftersaleId)
    {
        ReturnObject ret = afterSaleDao.getAfterSaleById(aftersaleId);
        if (ret.getCode() != ReturnNo.OK)
            return null;
        AfterSalePo afterSalePo = (AfterSalePo) ret.getData();
        return afterSalePo.getServiceSn();
    }

}
