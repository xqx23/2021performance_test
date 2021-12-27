//package cn.edu.xmu.oomall.other.controller;
//
//import cn.edu.xmu.oomall.core.util.JacksonUtil;
//import cn.edu.xmu.oomall.other.microservice.*;
//import cn.edu.xmu.oomall.other.microservice.vo.*;
//import cn.edu.xmu.oomall.other.model.vo.aftersale.AfterSaleRetVo;
//import cn.edu.xmu.oomall.other.service.RocketMQService;
//import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
//import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
//import cn.edu.xmu.privilegegateway.annotation.util.bloom.BloomFilter;
//import io.swagger.models.auth.In;
//import org.checkerframework.checker.units.qual.A;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
//import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
//import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
//import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
//import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @Author jxy
// * @create 2021/12/18 3:06 PM
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//public class combineTest {
//    private static String customerToken_1;
//    private static String customerToken_2;
//    private static String customerToken;
//    private static String customerToken2;
//    private static String shopToken_1;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private CouponActivityService couponActivityService;
//    @Autowired
//    private GoodsService goodsService;
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private PaymentService paymentService;
//    @Autowired
//    private ShareActivityService shareActivityService;
//    @MockBean
//    private RocketMQService rocketMQService;
//
//
//    @MockBean
//    private BloomFilter<Long> longBloomFilter;
//
//    @MockBean
//    private RedisUtil redisUtil;
//
//    @BeforeAll
//    private static void login() {
//        JwtHelper jwtHelper = new JwtHelper();
//        customerToken_1 = jwtHelper.createToken(1L, "lzl", -1L, 3, 3600);
//        customerToken_2 = jwtHelper.createToken(2L, "abc", -1L, 3, 3600);
//        customerToken = jwtHelper.createToken(1L, "lzl", 0L, 3, 3600);
//        customerToken2 = jwtHelper.createToken(3L, "lzl", 0L, 3, 3600);
//        shopToken_1 = jwtHelper.createToken(1L, "admin", 1L, 2, 3600);
//
//    }
//    @Test
//    @Transactional
//    public void getCart_noMock() throws Exception {
//        String responseString = this.mvc.perform(get("/carts?page=1&pageSize=50")
//                .header("authorization", customerToken2))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":50,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void addCart_NoMock() throws Exception {
//        String requestJson = "{\n" +
//                "  \"productId\": 1550,\n" +
//                "  \"quantity\": 2\n" +
//                "}";
//        String responseString = this.mvc.perform(post("/carts").contentType("application/json;charset=UTF-8")
//                .header("authorization", customerToken).content(requestJson))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//    }
//    @Test
//    @Transactional
//    public void getAddresses() throws Exception {
//        String responseString = this.mvc.perform(get("/addresses")
//                        .contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getCoupons() throws Exception {
//        String responseString = this.mvc.perform(get("/coupons")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//    /* 成功，本身就有分享 */
//    @Test
//    @Transactional
//    public void createShareLink_1() throws Exception {
//
//        String responseString = this.mvc.perform(post("/onsale/1/shares")
//                        .contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void getCommissionRate()
//    {
//        InternalReturnObject internalReturnObject=goodsService.getCommissionRate(1550L);
//        System.out.println(internalReturnObject.getData());
//    }
//
//    @Test
//    @Transactional
//    public void getShareActivityById() throws Exception {
//
//        String responseString = this.mvc.perform(get("/test/1")
//                        .contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void adminReceive() throws Exception {
////
////        AftersaleOrderItemRecVo orderitemRecVo=new AftersaleOrderItemRecVo();
////        orderitemRecVo.setProductId(1550L);
////        orderitemRecVo.setOnsaleId(1L);
////        orderitemRecVo.setQuantity(5L);
////        AftersaleRecVo aftersaleRecVo=new AftersaleRecVo();
////        aftersaleRecVo.setOrderItem(orderitemRecVo);
////        aftersaleRecVo.setCustomerId(1L);
////        aftersaleRecVo.setConsignee("222");
////        aftersaleRecVo.setRegionId(5L);
////        aftersaleRecVo.setMobile("13056766288");
//
////        String response = this.mvc.perform(MockMvcRequestBuilders.post("/internal/shops/10/orders")
////                        .header("authorization",shopToken_1)
////                        .contentType("application/json;charset=UTF-8")
////                        .content(request))
////                .andExpect(status().isOk())
////                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
////                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
////        String expected = "{\"errno\":0,\"data\":{\"orderSn\":null,\"customer\":{\"id\":1,\"name\":\"李智樑\"},\"shop\":{\"id\":10,\"name\":\"商铺10\"},\"pid\":0,\"state\":201,\"confirmTime\":null,\"discountPrice\":0,\"originPrice\":0,\"point\":0,\"expressFee\":null,\"consignee\":\"222\",\"regionId\":5,\"address\":null,\"mobile\":\"13056766288\",\"message\":null,\"advancesaleId\":null,\"grouponId\":null,\"shipmentSn\":null,\"aftersaleOrderitemVo\":{\"productId\":1550,\"name\":\"欢乐家久宝桃罐头\",\"quantity\":5,\"price\":0}},\"errmsg\":\"成功\"}";
////        JSONAssert.assertEquals(expected, response, false);
//
//        String requestJson = "{\n" +
//                "  \"confirm\": true,\n" +
//                "  \"conclusion\": \"hh\"\n" +
//                "}";
//System.out.println(shopToken_1);
//      String  responseString = this.mvc.perform(put("/shops/1/aftersales/15/receive").contentType("application/json;charset=UTF-8")
//                        .header("authorization", shopToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//
//    }
//
//
//}
