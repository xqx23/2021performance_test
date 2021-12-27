package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.other.microservice.GoodsService;
import cn.edu.xmu.oomall.other.microservice.OrderService;
import cn.edu.xmu.oomall.other.microservice.ShareActivityService;
import cn.edu.xmu.oomall.other.microservice.vo.*;
import cn.edu.xmu.oomall.other.model.po.SuccessfulSharePo;
import cn.edu.xmu.oomall.other.model.vo.liquidation.PointsRetVo;
import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author:李智樑
 * @time:2021/12/13 21:15
 **/
@SpringBootTest
@AutoConfigureMockMvc
public class ShareControllerTest {
    private static String customerToken_1;
    private static String customerToken_2;
    private static String shopToken_1;
    @Autowired
    private ShareService shareService;

    @MockBean
    private OrderService orderItemService;

    @MockBean
    private ShareActivityService shareActivityService;

    @MockBean
    private GoodsService goodsService;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    private static void login() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken_1 = jwtHelper.createToken(1L, "lzl", -1L, 3, 3600);
        customerToken_2 = jwtHelper.createToken(2L, "lzl", -1L, 3, 3600);
        shopToken_1 = jwtHelper.createToken(1L, "admin", 1L, 2, 3600);
    }

    @Test
    @Transactional
    public void getEarliestSuccessfulShare() throws Exception {
        System.out.println((SuccessfulSharePo) (shareService.getEarliestSuccessfulShare(1L, 1L).getData()));
    }

    @Test
    @Transactional
    public void calculatePoints_1() throws Exception {
        OrderItemRetVo orderItemRetVo = new OrderItemRetVo();
        orderItemRetVo.setCustomerId(14723L);
        orderItemRetVo.setOnsaleId(33L);
        orderItemRetVo.setPrice(10000L);
        orderItemRetVo.setDiscountPrice(800L);
        orderItemRetVo.setQuantity(1L);

        RetShareActivityInfoVo retShareActivityInfoVo = new RetShareActivityInfoVo();
        List<StrategyVo> strategy = new ArrayList<>();
        strategy.add(new StrategyVo(5, 1));
        strategy.add(new StrategyVo(20, 2));
        strategy.add(new StrategyVo(-1, 3));
        retShareActivityInfoVo.setStrategy(strategy);


        Mockito.when(orderItemService.getOrderItemById(1L, null))
                .thenReturn(new InternalReturnObject<>(orderItemRetVo));
        Mockito.when(shareActivityService.getShareActivityById(Mockito.anyLong()))
                .thenReturn(new InternalReturnObject<>(retShareActivityInfoVo));

        PointsRetVo pointsRetVo = (PointsRetVo) (shareService.calculateSharePoints(1L).getData());

    }

    // ---------------------------------
    // 买家查看自己的分享记录

    /* beginTime晚于endTime */
    @Test
    public void getOwnShares_1() throws Exception {
        String responseString = this.mvc.perform(get("/shares")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1)
                .queryParam("beginTime", "2021-11-10T10:00:00.000+08:00")
                .queryParam("endTime", "2021-11-09T10:00:00.000+08:00"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":947,\"errmsg\":\"开始时间不能晚于结束时间\"}", responseString, false);
    }

    /* 获取到分享记录 */
    @Test
    @Transactional
    public void getOwnShares_2() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setId(1L);
        productRetVo.setName("test");
        productRetVo.setImageUrl("image");

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );

        String responseString = this.mvc.perform(get("/shares")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    // ------------------------------
    // 管理员查询商品分享记录

    /* product不为该shop所有 */
    @Test
    @Transactional
    public void getSharesByShopId_1() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setShop(new SimpleObject(2L, "test"));

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );

        String responseString = this.mvc.perform(get("/shops/1/products/1/shares")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
    }

    /* 找到 */
    @Test
    @Transactional
    public void getSharesByShopId_2() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setShop(new SimpleObject(1L, "test"));

        OnSaleDetailsRetVo onSaleDetailsRetVo = new OnSaleDetailsRetVo();
        onSaleDetailsRetVo.setPrice(11L);

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );


        String responseString = this.mvc.perform(get("/shops/1/products/1/shares")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    // ------------------------------
    // 查看商品的详细信息

    /* 找不到sid对应share */
    @Test
    @Transactional
    public void getGoodsDetails_1() throws Exception {
        String responseString = this.mvc.perform(get("/shares/10000/products/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
    }

    /* share里的productId与传的不一致 */
    @Test
    @Transactional
    public void getGoodsDetail_2() throws Exception {
        String responseString = this.mvc.perform(get("/shares/24574/products/158")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
    }

    /* 找到 */
    @Test
    @Transactional
    public void getGoodsDetail_3() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setQuantity(11);
        productRetVo.setOriginalPrice(1L);

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );

        String responseString = this.mvc.perform(get("/shares/24574/products/1588")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

    }


    // --------------------------------
    // 生成分享链接

    /* 成功，本身就有分享 */
    @Test
    @Transactional
    public void createShareLink_1() throws Exception {
        OnSaleDetailsRetVo onSaleDetailsRetVo = new OnSaleDetailsRetVo();
        onSaleDetailsRetVo.setShareActId(1L);
        onSaleDetailsRetVo.setProduct(new SimpleProductRetVo(1550L, "欢乐家久宝桃罐头", null));
        Mockito.when(goodsService.selectFullOnsale(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<OnSaleDetailsRetVo>(onSaleDetailsRetVo)
        );


        String responseString = this.mvc.perform(post("/onsales/2/shares")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    /* 成功生成分享记录 */
    @Test
    @Transactional
    public void createShareLink_2() throws Exception {
        OnSaleDetailsRetVo onSaleDetailsRetVo = new OnSaleDetailsRetVo();
        onSaleDetailsRetVo.setPrice(59337L);
        onSaleDetailsRetVo.setProduct(new SimpleProductRetVo(1550L, "欢乐家久宝桃罐头", null));

        Mockito.when(goodsService.selectFullOnsale(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<OnSaleDetailsRetVo>(onSaleDetailsRetVo)
        );

        String responseString = this.mvc.perform(post("/onsales/1/shares")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":650,\"errmsg\":\"货品不可分享\"}", responseString, false);
    }

    // ---------------------------------
    // 获取分享成功记录
    @Test
    @Transactional
    public void getBeShared_1() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setName("test");
        productRetVo.setImageUrl("img");

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );

        String responseString = this.mvc.perform(get("/beshared")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void getBeSharedByShopId() throws Exception {
        ProductRetVo productRetVo = new ProductRetVo();
        productRetVo.setName("test");
        productRetVo.setImageUrl("img");
        productRetVo.setShop(new SimpleObject(1L, "hh"));

        Mockito.when(goodsService.getProductDetails(Mockito.anyLong())).thenReturn(
                new InternalReturnObject<ProductRetVo>(productRetVo)
        );

        String responseString = this.mvc.perform(get("/shops/1/products/1/beshared")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", shopToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }
}
