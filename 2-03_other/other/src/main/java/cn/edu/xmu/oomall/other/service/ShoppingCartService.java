package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.ShoppingCartDao;
import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
import cn.edu.xmu.oomall.other.microservice.GoodsService;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleCouponActivityRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleOnSaleRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleProductRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.ValidCouponActivityRetVo;
import cn.edu.xmu.oomall.other.model.po.ShoppingCartPo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.ShoppingCartRetVo;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.ShoppingCartVo;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.SimpleCartRetVo;
import cn.edu.xmu.oomall.other.util.PageUtils;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.privilegegateway.annotation.util.Common.cloneVo;

/**
 * @author 李智樑
 * @create 2021/12/5 21:30
 */
@Service
public class ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CouponActivityService couponActivityService;

    public ReturnObject clearCart(Long userId) {
        return shoppingCartDao.clearCart(userId);
    }

    public ReturnObject deleteCart(Long userId, Long cartId) {
        ReturnObject ret = shoppingCartDao.judgeCart(userId, cartId);
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        return shoppingCartDao.deleteCart(cartId);
    }


    public ReturnObject getCarts(Long userId, Integer page, Integer pageSize) {
        ReturnObject ret = shoppingCartDao.getCartByUserId(userId, page, pageSize);
        PageInfo<ShoppingCartPo> pageInfo = (PageInfo<ShoppingCartPo>) ret.getData();

        List<ShoppingCartPo> shoppingCartPos = pageInfo.getList();
        List<Long> productIds = shoppingCartPos.stream().map(ShoppingCartPo::getProductId).collect(Collectors.toList());
        List<ShoppingCartRetVo> shoppingCartRetVos = new ArrayList<>(shoppingCartPos.size());
        for (int i = 0; i < shoppingCartPos.size(); i++) {
            Long id = productIds.get(i);
            ShoppingCartRetVo shoppingCartRetVo = cloneVo(shoppingCartPos.get(i), ShoppingCartRetVo.class);
            //获取商品详情
            InternalReturnObject internalProductDetailsReturnObject = goodsService.getProductDetails(id);
            if (internalProductDetailsReturnObject.getErrno() != 0)
                return new ReturnObject(internalProductDetailsReturnObject);
            SimpleProductRetVo simpleProductRetVo = cloneVo(internalProductDetailsReturnObject.getData(), SimpleProductRetVo.class);
            //获取优惠活动列表
            InternalReturnObject<SimplePageInfo<ValidCouponActivityRetVo>> internalReturnObject = couponActivityService.listCouponActivitiesByProductId(id, page, pageSize);
            if (!internalReturnObject.getErrno().equals(ReturnNo.OK.getCode())) {
                return new ReturnObject(internalReturnObject);
            }
            List<ValidCouponActivityRetVo> validCouponActivityRetVos = internalReturnObject.getData().getList();
            //todo:可能需要判断validCouponActivityRetVos里的时间是不是符合当前时间
            List<SimpleCouponActivityRetVo> simpleCouponActivityRetVos =
                    (List<SimpleCouponActivityRetVo>) Common.getListRetVo(new ReturnObject(validCouponActivityRetVos), SimpleCouponActivityRetVo.class).getData();

            shoppingCartRetVo.setProduct(simpleProductRetVo);
            shoppingCartRetVo.setCouponActivity(simpleCouponActivityRetVos);

            shoppingCartRetVos.add(shoppingCartRetVo);

        }

        PageInfo<ShoppingCartRetVo> shoppingCartRetVoPageInfo = PageUtils.PageInfo2PageInfoVo(pageInfo, shoppingCartRetVos);

        return new ReturnObject<>(shoppingCartRetVoPageInfo);
    }

    public ReturnObject addToCart(Long userId, String loginName, ShoppingCartVo vo) {

        SimpleOnSaleRetVo simpleOnSaleRetVo = goodsService.getValidNowOnsaleByProductId(vo.getProductId()).getData();
        ReturnObject ret = shoppingCartDao.addToCart(userId, loginName, vo.getProductId(), vo.getQuantity(), simpleOnSaleRetVo.getPrice());
        if (!ret.getCode().equals(ReturnNo.OK)) {
            return ret;
        }
        // 区分大负数情况
        if (ret.getData() == null) {
            return ret;
        } else {
            SimpleCartRetVo simpleCartRetVo = cloneVo(ret.getData(), SimpleCartRetVo.class);
            return new ReturnObject<>(simpleCartRetVo);
        }
    }

    public ReturnObject modifyCart(Long cartId, ShoppingCartVo vo, Long userId, String loginName) {
        ReturnObject ret = shoppingCartDao.judgeCart(userId, cartId);
        if (!ret.getCode().equals(ReturnNo.OK))
            return ret;
        SimpleOnSaleRetVo simpleOnSaleRetVo = goodsService.getValidNowOnsaleByProductId(vo.getProductId()).getData();
        ShoppingCartPo shoppingCartPo = (ShoppingCartPo) ret.getData();
        shoppingCartPo.setPrice(simpleOnSaleRetVo.getPrice());
        return shoppingCartDao.modifyCart(shoppingCartPo, vo, userId, loginName);
    }
}
