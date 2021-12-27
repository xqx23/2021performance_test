package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.microservice.GoodsService;
import cn.edu.xmu.oomall.other.microservice.OrderService;
import cn.edu.xmu.oomall.other.microservice.ShareActivityService;
import cn.edu.xmu.oomall.other.microservice.vo.*;
import cn.edu.xmu.oomall.other.model.bo.PercentageRebate;
import cn.edu.xmu.oomall.other.model.bo.Share;
import cn.edu.xmu.oomall.other.model.bo.SuccessfulShare;
import cn.edu.xmu.oomall.other.model.po.SuccessfulSharePo;
import cn.edu.xmu.oomall.other.model.po.SharePo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.PointsRetVo;
import cn.edu.xmu.oomall.other.model.vo.share.ShareRetVo;
import cn.edu.xmu.oomall.other.model.vo.share.SuccessfulShareRetVo;
import cn.edu.xmu.oomall.other.util.PageUtils;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.*;

/**
 * @author Lu Zhang
 * @create 2021/12/6
 */
@Service
public class ShareService {

    @Autowired
    private ShareDao shareDao;

    @Autowired
    private OrderService orderItemService;

    @Autowired
    private ShareActivityService shareActivityService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CustomerService customerService;

    /**
     * @author jxy
     * @create 2021/12/13 9:32 AM
     * 计算分享返点
     */

    public ReturnObject calculateSharePoints(Long orderItemId) {
        InternalReturnObject internalReturnObject = orderItemService.getOrderItemById(orderItemId, null);

        if (!internalReturnObject.getErrno().equals(ReturnNo.OK.getCode())) {
            return new ReturnObject(ReturnNo.getByCode(internalReturnObject.getErrno()), internalReturnObject.getErrmsg());
        }
        OrderItemRetVo orderItemRetVo = (OrderItemRetVo) internalReturnObject.getData();

        ReturnObject ret = getEarliestSuccessfulShare(orderItemRetVo.getOnsaleId(), orderItemRetVo.getCustomerId());
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        SuccessfulSharePo successfulSharePo = (SuccessfulSharePo) ret.getData();

        ret = shareDao.getShareByShareId(successfulSharePo.getShareId());
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        SharePo sharePo = (SharePo) ret.getData();
        RetShareActivityInfoVo retShareActivityInfoVo = shareActivityService.getShareActivityById(sharePo.getShareActId()).getData();

        PercentageRebate percentageRebate = new PercentageRebate(sharePo.getQuantity(), orderItemRetVo.getQuantity(), orderItemRetVo.getPrice() - orderItemRetVo.getDiscountPrice(), 1, retShareActivityInfoVo.getStrategy());
        Long points = percentageRebate.calculate();

        return new ReturnObject<>(new PointsRetVo(points, sharePo.getSharerId()));
    }

    /**
     * @author jxy
     * @create 2021/12/13 9:32 AM
     * 获取最早分享成功记录
     */
    public ReturnObject getEarliestSuccessfulShare(Long onSaleId, Long customerId) {

        ReturnObject ret = shareDao.getEarliestSuccessfulShare(onSaleId, customerId);
        return ret;
    }
    
    /**
     * 查询所有分享记录
     * @param userId
     * @param productId
     * @param beginTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ReturnObject listShares(Long userId, Long shopId, Long productId, ZonedDateTime beginTime, ZonedDateTime endTime, Integer pageNum, Integer pageSize) {
        ReturnObject ret = checkProductWithShopId(productId, shopId);
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        //获取所有分享记录
        ret = shareDao.listShares(userId, productId, beginTime, endTime, pageNum, pageSize);
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }

        PageInfo<SharePo> pageInfo = (PageInfo<SharePo>) ret.getData();
        List<SharePo> sharePos = pageInfo.getList();
        List<ShareRetVo> shareRetVos = new ArrayList<>(sharePos.size());
        for (SharePo sharePo : sharePos) {
            ShareRetVo shareRetVo = cloneVo(sharePo, ShareRetVo.class);
            SimpleObject sharer = cloneVo(customerService.getCustomerById(sharePo.getSharerId()).getData(), SimpleObject.class);
            ProductRetVo productRetVo = goodsService.getProductDetails(sharePo.getProductId()).getData();
            shareRetVo.setSharer(sharer);
            shareRetVo.setProduct(cloneVo(productRetVo,SimpleProductRetVo.class));

            shareRetVos.add(shareRetVo);
        }
        PageInfo<ShareRetVo> shareRetVoPageInfo = PageUtils.PageInfo2PageInfoVo(pageInfo, shareRetVos);
        return new ReturnObject<>(shareRetVoPageInfo);
    }

    /**
     * 查询所有分享成功记录
     *
     * @param shopId
     * @param productId
     * @param beginTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject listBeShared(Long userId, Long shopId, Long productId, ZonedDateTime beginTime,
                                     ZonedDateTime endTime, Integer pageNum, Integer pageSize) {

        ReturnObject ret = checkProductWithShopId(productId, shopId);
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }

        /* 获取所有分享成功记录 */
        ret = shareDao.listBeShared(userId, productId, beginTime, endTime, pageNum, pageSize);

        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }

        PageInfo<SuccessfulSharePo> pageInfo = (PageInfo<SuccessfulSharePo>) ret.getData();
        List<SuccessfulSharePo> successfulSharePos = pageInfo.getList();
        List<SuccessfulShareRetVo> successfulShareRetVos = new ArrayList<>(successfulSharePos.size());
        for (SuccessfulSharePo successfulSharePo : successfulSharePos) {
            SuccessfulShareRetVo successfulShareRetVo = cloneVo(successfulSharePo, SuccessfulShareRetVo.class);
            ProductRetVo productRetVo = goodsService.getProductDetails(successfulSharePo.getProductId()).getData();

            successfulShareRetVo.setProduct(cloneVo(productRetVo,SimpleProductRetVo.class));

            successfulShareRetVos.add(successfulShareRetVo);
        }
        PageInfo<SuccessfulShareRetVo> successfulShareRetVoPageInfo = PageUtils.PageInfo2PageInfoVo(pageInfo, successfulShareRetVos);
        return new ReturnObject<>(successfulShareRetVoPageInfo);
    }

    /**
     * 查看商品的详细信息
     *
     * @param sid
     * @param productId
     * @return
     */
    public ReturnObject getProductById(Long userId, String userName, Long sid, Long productId) {
        // 校验分享
        ReturnObject retObj = shareDao.getShareById(sid);
        if (retObj.getCode() != ReturnNo.OK) {
            return retObj;
        }
        SharePo sharePo = (SharePo) retObj.getData();

        if (!sharePo.getProductId().equals(productId)) {
            return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
        }

        /* 插入一条分享成功记录，对于一个share与一个customer只生成一次 */
        retObj = shareDao.getSuccessfulShareByShareIdAndProductId(sid, productId);
        if (retObj.getCode() != ReturnNo.OK) {
            return retObj;
        }
        List<SuccessfulSharePo> successfulSharePos = (List<SuccessfulSharePo>) retObj.getData();
        if (successfulSharePos.size() == 0) {
            SuccessfulShare successfulShare = new SuccessfulShare();
            successfulShare.setSharerId(sharePo.getSharerId());
            successfulShare.setShareId(sid);
            successfulShare.setCustomerId(userId);
            successfulShare.setProductId(sharePo.getProductId());
            successfulShare.setOnsaleId(sharePo.getOnsaleId());
            successfulShare.setState(SuccessfulShare.State.VALID.getCode());
            setPoCreatedFields(successfulShare, userId, userName);
            shareDao.insertSuccessfulShare(successfulShare);
        }

        //查找product
        InternalReturnObject<ProductRetVo> productObject = goodsService.getProductDetails(productId);

        ProductRetVo product = productObject.getData();

        return new ReturnObject(product);
    }

    /**
     * 分享者生成分享链接
     *
     * @param userId
     * @param userName
     * @param onsaleId
     * @return
     */
    public ReturnObject createShareLink(Long userId, String userName, Long onsaleId) {
        //查找onSale
        InternalReturnObject<OnSaleDetailsRetVo> onSaleObject = goodsService.selectFullOnsale(onsaleId);
        if (!onSaleObject.getErrno().equals(0)) {
            return new ReturnObject(ReturnNo.getByCode(onSaleObject.getErrno()), onSaleObject.getErrmsg());
        }

        OnSaleDetailsRetVo onsaleVo = onSaleObject.getData();

        // 判断 onSale 的shareActivityId存不存在
        if (onsaleVo.getShareActId() == null) {
            return new ReturnObject(ReturnNo.SHARE_UNSHARABLE);
        }

        SimpleObject sharer = new SimpleObject(userId, userName);

        List<SharePo> sharePos = (List<SharePo>) (shareDao.getShareBySharerIdAndOnsaleId(userId, onsaleId).getData());
        SharePo sharePo;
        if (sharePos.size() == 0) {
            /* 找不到说明第一次分享，需要生成一条分享记录 */
            sharePo = new SharePo();
            sharePo.setSharerId(userId);
            sharePo.setOnsaleId(onsaleId);
            sharePo.setShareActId(onsaleVo.getShareActId());
            sharePo.setProductId(onsaleVo.getProduct().getId());
            sharePo.setQuantity(0L);
            sharePo.setState(Share.State.VALID.getCode());
            setPoCreatedFields(sharePo, userId, userName);

            ReturnObject ret = shareDao.insertShare(sharePo);
            /* 插入失败 */
            if (ret.getData() == null) {
                return ret;
            }
        } else {
            sharePo = sharePos.get(0);
        }

        ShareRetVo shareRetVo = cloneVo(sharePo, ShareRetVo.class);
        shareRetVo.setSharer(sharer);

        shareRetVo.setProduct(onsaleVo.getProduct());

        return new ReturnObject(shareRetVo);
    }

    private ReturnObject checkProductWithShopId(Long productId, Long shopId) {
        if (shopId != null) {
            InternalReturnObject<ProductRetVo> productObject = goodsService.getProductDetails(productId);

            ProductRetVo product = productObject.getData();
            if (!shopId.equals(product.getShop().getId())) {
                return new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
        }
        return new ReturnObject();
    }

}
