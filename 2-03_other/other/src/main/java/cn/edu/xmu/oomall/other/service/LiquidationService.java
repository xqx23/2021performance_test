package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.constant.LiquidationState;
import cn.edu.xmu.oomall.other.dao.AfterSaleDao;
import cn.edu.xmu.oomall.other.dao.CustomerDao;
import cn.edu.xmu.oomall.other.dao.LiquidationDao;
import cn.edu.xmu.oomall.other.microservice.*;
import cn.edu.xmu.oomall.other.microservice.vo.SimplePaymentRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleRefundRetVo;
import cn.edu.xmu.oomall.other.model.bo.LiquidationResultItem;
import cn.edu.xmu.oomall.other.model.bo.LiquidationResultMap;
import cn.edu.xmu.oomall.other.model.bo.calculator.LiquidationInfo;
import cn.edu.xmu.oomall.other.model.bo.calculator.multiple.*;
import cn.edu.xmu.oomall.other.model.po.AfterSalePo;
import cn.edu.xmu.oomall.other.model.po.ExpenditureItemPo;
import cn.edu.xmu.oomall.other.model.po.LiquidationPo;
import cn.edu.xmu.oomall.other.model.po.RevenueItemPo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.*;
import cn.edu.xmu.oomall.other.util.PageUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;
import static cn.edu.xmu.privilegegateway.annotation.util.Common.setPoCreatedFields;

/**
 * @author Gao Yanfeng
 * @date 2021/12/10
 */
@Service
public class LiquidationService {

    @Autowired
    private LiquidationDao liquidationDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private AfterSaleDao afterSaleDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShareService shareService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RefundService refundService;

    public ReturnObject getLiquidationStates() {
        return new ReturnObject(Arrays.stream(LiquidationState.values()).map(
                state -> new StateVo(state.getCode(), state.getName())
        ).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public ReturnObject getLiquidations(Long shopId, ZonedDateTime beginDate, ZonedDateTime endDate, Boolean state, Integer page, Integer pageSize) {
        return liquidationDao.getLiquidations(shopId, beginDate, endDate, state, page, pageSize);
    }

    @Transactional(readOnly = true)
    public ReturnObject getLiquidationById(Long shopId, Long id) {
        return liquidationDao.getLiquidationById(shopId, id);
    }

    public ReturnObject getExpenditureByInfo(Long liquidationId, Long shopId, Long orderId, Long productId, Integer page, Integer pageSize) {
        return getExpenditureItem(liquidationId, shopId, orderId, productId, null, null, null, page, pageSize, null);

    }

    public ReturnObject getRevenueItemByInfo(Long liquidationId, Long shopId, Long orderId, Long productId, Integer page, Integer pageSize) {
        return getRevenueItem(liquidationId, shopId, orderId, productId, null, null, null, page, pageSize, null);

    }

    public ReturnObject getRevenueItem(Long liquidationId, Long shopId, Long orderId, Long productId,
                                       Long sharerId, ZonedDateTime beginDate,
                                       ZonedDateTime endDate, Integer page,
                                       Integer pageSize, Boolean desc) {
        ReturnObject<PageInfo<RevenueItemPo>> ret = liquidationDao.getRevenueItem
                (liquidationId, shopId, orderId, productId, sharerId, beginDate, endDate, page, pageSize, desc);
        PageInfo<RevenueItemPo> revenueItemPoPageInfo = ret.getData();

        List<RevenueItemPo> list = revenueItemPoPageInfo.getList();
        List<Long> shopIds = list.stream().map(RevenueItemPo::getShopId).collect(Collectors.toList());
        List<GeneralLedgersRetVo> generalLedgersRetVos = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            Long id = shopIds.get(i);
            SimpleShopVo simpleShopVo = shopService.getSimpleShopById(id).getData();
            GeneralLedgersRetVo generalLedgersRetVo = cloneVo(list.get(i), GeneralLedgersRetVo.class);
            generalLedgersRetVo.setShop(simpleShopVo);
            generalLedgersRetVos.add(generalLedgersRetVo);
        }

        PageInfo<GeneralLedgersRetVo> pageInfo = PageUtils.PageInfo2PageInfoVo(revenueItemPoPageInfo, generalLedgersRetVos);
        return new ReturnObject(pageInfo);
    }

    public ReturnObject getExpenditureItem(Long liquidationId, Long shopId, Long orderId, Long productId,
                                           Long sharerId, ZonedDateTime beginDate,
                                           ZonedDateTime endDate, Integer page,
                                           Integer pageSize, Boolean desc) {
        ReturnObject<PageInfo<ExpenditureItemPo>> ret = liquidationDao.getExpenditureItem
                (liquidationId, shopId, orderId, productId, sharerId, beginDate, endDate, page, pageSize, desc);
        PageInfo<ExpenditureItemPo> expenditureItemPoPageInfoPoPageInfo = ret.getData();

        List<ExpenditureItemPo> list = expenditureItemPoPageInfoPoPageInfo.getList();
        List<Long> shopIds = list.stream().map(ExpenditureItemPo::getShopId).collect(Collectors.toList());
        List<GeneralLedgersRetVo> generalLedgersRetVos = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            Long id = shopIds.get(i);
            SimpleShopVo simpleShopVo = shopService.getSimpleShopById(id).getData();
            GeneralLedgersRetVo generalLedgersRetVo = cloneVo(list.get(i), GeneralLedgersRetVo.class);
            generalLedgersRetVo.setShop(simpleShopVo);
            generalLedgersRetVos.add(generalLedgersRetVo);
        }

        PageInfo<GeneralLedgersRetVo> pageInfo = PageUtils.PageInfo2PageInfoVo(expenditureItemPoPageInfoPoPageInfo, generalLedgersRetVos);
        return new ReturnObject(pageInfo);
    }

    public ReturnObject getRevenueByExpenditureId(Long shopId, Long id) {
        ReturnObject ret = liquidationDao.getExpenditureById(shopId, id);
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        ExpenditureItemPo expenditureItemPo = (ExpenditureItemPo) ret.getData();
        ret = liquidationDao.getRevenueById(shopId, expenditureItemPo.getRevenueId());
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        RevenueItemPo revenueItemPo = (RevenueItemPo) ret.getData();
        SimpleShopVo simpleShopVo = shopService.getSimpleShopById(revenueItemPo.getShopId()).getData();
        GeneralLedgersRetVo generalLedgersRetVo = cloneVo(revenueItemPo, GeneralLedgersRetVo.class);
        generalLedgersRetVo.setShop(simpleShopVo);
        return new ReturnObject(generalLedgersRetVo);
    }

    public ReturnObject getAcquirePointChangeBySharerId(Long userId, ZonedDateTime beginDate, ZonedDateTime endDate, Integer page, Integer pageSize) {
        return getRevenueItem(null, null, null, null, userId, beginDate, endDate, page, pageSize, true);
    }

    public ReturnObject getLostPointChangeBySharerId(Long userId, ZonedDateTime beginDate, ZonedDateTime endDate, Integer page, Integer pageSize) {
        return getExpenditureItem(null, null, null, null, userId, beginDate, endDate, page, pageSize, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public ReturnObject startLiquidation(StartInfoVo startInfo, Long userId, String userName) {

        int page = 1;
        int pages = -1;
        var map = new LiquidationResultMap();

        do {
            var pageRet = paymentService.listPaymentInternal(null, (byte) 2, startInfo.getStartTime(), startInfo.getEndTime(), page, 10);
            if (pageRet.getErrno() != 0) {
                return new ReturnObject(ReturnNo.getByCode(pageRet.getErrno()), pageRet.getErrmsg());
            }
            if (pages == -1) {
                pages = pageRet.getData().getPages();
            }
            for (var payment : pageRet.getData().getList()) {
                liquidateBill(payment, map, userId, userName);
            }
        } while (page < pages);

        page = 1;
        pages = -1;
        do {
            var pageRet = refundService.listRefund(null, (byte) 2, startInfo.getStartTime(), startInfo.getEndTime(), page, 10);
            if (pageRet.getErrno() != 0) {
                return new ReturnObject(ReturnNo.getByCode(pageRet.getErrno()), pageRet.getErrmsg());
            }
            if (pages == -1) {
                pages = pageRet.getData().getPages();
            }
            for (var refund : pageRet.getData().getList()) {
                liquidateBill(refund, map, userId, userName);
            }
        } while (page < pages);

        for (var item : map.getResultItemMap().values()) {
            var shopRet = shopService.getSimpleShopById(item.getShopId());
            if (shopRet.getErrno() != 0) {
                return new ReturnObject(ReturnNo.getReturnNoByCode(shopRet.getErrno()), shopRet.getErrmsg());
            }
            item.setShopName(shopRet.getData().getName());
            liquidationDao.updateLiquidation(cloneVo(item, LiquidationPo.class));
        }

        return new ReturnObject<>();
    }

    private <T> ReturnObject liquidateBill(T bill, LiquidationResultMap map, Long userId, String userName) {
        var calculatorRet = createCalculator(bill);
        if (!calculatorRet.getCode().equals(ReturnNo.OK)) {
            return calculatorRet;
        }
        var calculator = (MultipleLiquidationCalculator) calculatorRet.getData();
        var liquidationResultList = calculator.calculate();
        for (var item : liquidationResultList) {
            if (map.get(item.getShopId()) == null) {
                var po = new LiquidationPo();
                setPoCreatedFields(po, userId, userName);
                var liquidationRet = liquidationDao.insertLiquidation(po);
                if (!liquidationRet.getCode().equals(ReturnNo.OK)) {
                    return liquidationRet;
                }
                var liquidationPo = (LiquidationPo) liquidationRet.getData();
                var liquidation = new LiquidationResultItem();
                liquidation.setId(liquidationPo.getId());
                liquidation.setLiquidDate(liquidationPo.getGmtCreate());
                liquidation.setShopId(0L);
                map.put(item.getShopId(), liquidation);
            }
            item.setLiquidId(map.get(item.getShopId()).getLiquidId());
            map.updateBy(item);
            customerDao.changePoints(item.getSharerId(), item.getPoint());
            if (bill instanceof SimplePaymentRetVo) {
                var po = cloneVo(item, RevenueItemPo.class);
                setPoCreatedFields(po, userId, userName);
                liquidationDao.insertRevenue(po);
            } else {
                var po = cloneVo(item, ExpenditureItemPo.class);
                setPoCreatedFields(po, userId, userName);
                liquidationDao.insertExpenditure(po);
            }
        }
        return new ReturnObject<>();
    }

    private <T> ReturnObject createCalculator(T bill) {
        if (bill instanceof SimplePaymentRetVo) {
            switch (((SimplePaymentRetVo) bill).getDocumentType()) {
                case 0:
                case 2:
                case 3:
                    return createOrderRevenueLiquidationCalculator((SimplePaymentRetVo) bill);
                default:
                    return createDefaultLiquidationCalculator();
            }
        } else {
            switch (((SimpleRefundRetVo) bill).getDocumentType()) {
                case 0:
                    return createOrderExpenditureLiquidationCalculator((SimpleRefundRetVo) bill);
                case 2:
                    return createAfterSaleExpenditureLiquidationCalculator((SimpleRefundRetVo) bill);
                default:
                    return createDefaultLiquidationCalculator();
            }
        }
    }

    private ReturnObject createOrderRevenueLiquidationCalculator(SimplePaymentRetVo payment) {
        var info = new LiquidationInfo();
        info.setPaymentId(payment.getId());
        var documentId = payment.getDocumentId();
        var orderIdRet = orderService.getOrderIdByOrderSn(documentId);
        if (orderIdRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderIdRet.getErrno()), orderIdRet.getErrmsg());
        }
        var orderId = orderIdRet.getData().getId();

        var orderRet = orderService.getOrderById(orderId);
        if (orderRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderRet.getErrno()), orderRet.getErrmsg());
        }
        info.setOrder(orderRet.getData());

        var orderItemsRet = orderService.getOrderItemsByOrderId(orderId);
        if (orderItemsRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderItemsRet.getErrno()), orderItemsRet.getErrmsg());
        }

        List<LiquidationInfo> list = new ArrayList<>();
        for (var orderItem : orderItemsRet.getData()) {
            var itemInfo = new LiquidationInfo();
            itemInfo.setOrderItem(orderItem);
            itemInfo.setPaymentId(payment.getId());

            var commissionRatioRet = goodsService.getCommissionRate(orderItem.getProductId());
            if (commissionRatioRet.getErrno() != 0) {
                return new ReturnObject(ReturnNo.getByCode(commissionRatioRet.getErrno()), commissionRatioRet.getErrmsg());
            }
            itemInfo.setCommissionRatio(commissionRatioRet.getData());

            var pointsRet = shareService.calculateSharePoints(orderItem.getId());
            if (!pointsRet.getCode().equals(ReturnNo.OK)) {
                return pointsRet;
            }
            itemInfo.setPointsRet((PointsRetVo) pointsRet.getData());
            list.add(itemInfo);
        }
        info.setLiquidationInfoList(list);

        var calculator = new OrderRevenueLiquidationCalculator(info);
        return new ReturnObject<>(calculator);
    }

    private ReturnObject createOrderExpenditureLiquidationCalculator(SimpleRefundRetVo refund) {
        var documentId = refund.getDocumentId();
        var paymentId = refund.getPaymentId();
        var refundId = refund.getId();
        var info = new LiquidationInfo();
        info.setPaymentId(paymentId);

        var orderIdRet = orderService.getOrderIdByOrderSn(documentId);
        if (orderIdRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderIdRet.getErrno()), orderIdRet.getErrmsg());
        }
        var orderId = orderIdRet.getData().getId();

        var orderRet = orderService.getOrderById(orderId);
        if (orderRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderRet.getErrno()), orderRet.getErrmsg());
        }
        info.setOrder(orderRet.getData());

        var orderItemsRet = orderService.getOrderItemsByOrderId(orderId);
        if (orderItemsRet.getErrno() != 0) {
            return new ReturnObject<>(ReturnNo.getByCode(orderItemsRet.getErrno()), orderItemsRet.getErrmsg());
        }

        List<LiquidationInfo> list = new ArrayList<>();
        for (var orderItem : orderItemsRet.getData()) {
            var itemInfo = new LiquidationInfo();
            itemInfo.setRefundId(refundId);

            var revenueRet = liquidationDao.getRevenueByPaymentIdAndOrderItemId(paymentId, orderItem.getId());
            if (!revenueRet.getCode().equals(ReturnNo.OK)) {
                return revenueRet;
            }
            itemInfo.setRevenueItem((RevenueItemPo) revenueRet.getData());

            list.add(itemInfo);
        }
        info.setLiquidationInfoList(list);

        var calculator = new OrderExpenditureLiquidationCalculator(info);
        return new ReturnObject<>(calculator);
    }

    private ReturnObject createAfterSaleExpenditureLiquidationCalculator(SimpleRefundRetVo refund) {
        var info = new LiquidationInfo();
        var itemInfo = new LiquidationInfo();
        var paymentId = refund.getPaymentId();
        var documentId = refund.getDocumentId();
        var refundId = refund.getId();
        itemInfo.setRefundId(refundId);

        var afterSaleRet = afterSaleDao.getAfterSaleBySn(documentId);
        if (!afterSaleRet.getCode().equals(ReturnNo.OK)) {
            return afterSaleRet;
        }
        var afterSale = (AfterSalePo) afterSaleRet.getData();
        itemInfo.setAfterSale(afterSale);
        var orderItemId = afterSale.getOrderItemId();

        var orderItemRet = orderService.getOrderItemById(orderItemId, null);
        if (orderItemRet.getErrno() != 0) {
            return new ReturnObject(ReturnNo.getReturnNoByCode(orderItemRet.getErrno()), orderItemRet.getErrmsg());
        }
        itemInfo.setOrderItem(orderItemRet.getData());

        var revenueRet = liquidationDao.getRevenueByPaymentIdAndOrderItemId(paymentId, orderItemId);
        if (!revenueRet.getCode().equals(ReturnNo.OK)) {
            return revenueRet;
        }
        itemInfo.setRevenueItem((RevenueItemPo) revenueRet.getData());

        info.setLiquidationInfoList(List.of(itemInfo));

        var calculator = new AfterSaleExpenditureLiquidationCalculator(info);
        return new ReturnObject<>(calculator);
    }

    private ReturnObject createDefaultLiquidationCalculator() {
        var calculator = new DefaultLiquidationCalculator();
        return new ReturnObject<>(calculator);
    }
}
