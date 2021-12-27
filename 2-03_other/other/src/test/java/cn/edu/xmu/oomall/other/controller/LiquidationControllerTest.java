//package cn.edu.xmu.oomall.other.controller;
//
//import cn.edu.xmu.oomall.core.util.ReturnObject;
//import cn.edu.xmu.oomall.other.microservice.*;
//import cn.edu.xmu.oomall.other.microservice.vo.*;
//import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
//import cn.edu.xmu.oomall.other.model.vo.liquidation.PointsRetVo;
//import cn.edu.xmu.oomall.other.model.vo.liquidation.SimpleShopVo;
//import cn.edu.xmu.oomall.other.service.ShareService;
//import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
//import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
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
//import java.time.LocalDateTime;
//import java.time.ZonedDateTime;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @author Gao Yanfeng
// * @date 2021/12/10
// */
//@SpringBootTest
//@AutoConfigureMockMvc
//public class LiquidationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
////    @Autowired
//    private ShopService shopService;
//
//    @MockBean
//    private GoodsService goodsService;
//
//    private static JwtHelper jwtHelper;
//    private static String customerToken;
//    private static String shopToken;
//    private static String adminToken;
//
//    @MockBean
//    private PaymentService paymentService;
//
//    @MockBean
//    private RefundService refundService;
//
//    @MockBean
//    private OrderService orderService;
//
//    @MockBean
//    private ShareService shareService;
//
//    @BeforeAll
//    public static void init() {
//        jwtHelper = new JwtHelper();
//        customerToken = jwtHelper.createToken(1L, "Pecco", -1L, 3, 3600);
//        shopToken = jwtHelper.createToken(1L, "shop_admin", 1L, 3, 3600);
//        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
//    }
//
//    @Test
//    public void getLiquidationStatesTest() throws Exception {
//        String expected = "{\"errno\":0,\"data\":[{\"code\":0,\"name\":\"未汇出\"},{\"code\":1,\"name\":\"已汇出\"}],\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/liquidation/states")
//                .header("authorization", customerToken))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void getLiquidationsTest() throws Exception {
//        String expected = "{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/shops/1/liquidation")
//                .header("authorization", shopToken)
//                .queryParam("state", "1")
//                .queryParam("beginDate", "2021-11-11T00:00:00.000+08:00")
//                .queryParam("endDate", "2023-11-11T00:00:00.000+08:00"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//
//    @Test
//    public void getLiquidationByIdTest_NOT_FOUND() throws Exception {
//        String expected = "{\"errno\":504,\"errmsg\":\"在指定店铺中未找到指定的清算单\"}";
//        String actual = mockMvc.perform(get("/shops/1/liquidation/1000")
//                .header("authorization", shopToken))
//                .andExpect(status().isNotFound())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void getLiquidationByIdTest_FOUND() throws Exception {
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/shops/1/liquidation/1")
//                .header("authorization", shopToken))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    public void getAcquirePointChange() throws Exception {
//        Mockito.when(shopService.getSimpleShopById(Mockito.anyLong())).thenReturn(
//                new InternalReturnObject<>(new SimpleShopVo(1L, "test")));
//
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/pointrecords/revenue")
//                .header("authorization", customerToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    public void getLostPointChange() throws Exception {
//        Mockito.when(shopService.getSimpleShopById(Mockito.anyLong())).thenReturn(
//                new InternalReturnObject<>(new SimpleShopVo(1L, "test")));
//
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/pointrecords/expenditure")
//                .header("authorization", customerToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    public void getRevenuesByInfo() throws Exception {
//        Mockito.when(shopService.getSimpleShopById(Mockito.anyLong())).thenReturn(
//                new InternalReturnObject<>(new SimpleShopVo(1L, "test")));
//
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/shops/1/revenue")
//                .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    public void getExpenditureByInfo() throws Exception {
//        Mockito.when(shopService.getSimpleShopById(Mockito.anyLong())).thenReturn(
//                new InternalReturnObject<>(new SimpleShopVo(1L, "test")));
//
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/shops/1/expenditure")
//                .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    public void getRevenueByExpenditureId() throws Exception {
//        Mockito.when(shopService.getSimpleShopById(Mockito.anyLong())).thenReturn(
//                new InternalReturnObject<>(new SimpleShopVo(1L, "test")));
//
//        String expected = "{\"errno\":0,\"errmsg\":\"成功\"}";
//        String actual = mockMvc.perform(get("/shops/1/expenditure/1/revenue")
//                .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//
//        expected = "{\"errno\":504,\"errmsg\":\"在指定店铺中未找到指定的出账明细\"}";
//        actual = mockMvc.perform(get("/shops/1/expenditure/2/revenue")
//                .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isNotFound())
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals(expected, actual, false);
//    }
//
//    @Test
//    @Transactional
//    public void startLiquidationTest() throws Exception {
//
//        var endTime = ZonedDateTime.parse("2021-12-16T00:00:00.000+08:00");
//
//        var simplePaymentRetVo1 = new SimplePaymentRetVo();
//        simplePaymentRetVo1.setId(1L);
//        simplePaymentRetVo1.setDocumentId("1");
//        simplePaymentRetVo1.setDocumentType((byte) 0);
//
//        var simplePaymentRetVo2 = new SimplePaymentRetVo();
//        simplePaymentRetVo2.setDocumentType((byte) 1);
//
//        var simplePaymentRetVo3 = new SimplePaymentRetVo();
//        simplePaymentRetVo3.setId(3L);
//        simplePaymentRetVo3.setDocumentId("2");
//        simplePaymentRetVo3.setDocumentType((byte) 0);
//
//        var orderRetVo1 = new OrderRetVo();
//        orderRetVo1.setId(1L);
//        orderRetVo1.setFreightPrice(1200L);
//
//        var orderRetVo2 = new OrderRetVo();
//        orderRetVo2.setId(2L);
//        orderRetVo2.setFreightPrice(1000L);
//
//        var orderItemsRetVo1 = new OrderItemRetVo();
//        orderItemsRetVo1.setId(1L);
//        orderItemsRetVo1.setShopId(1L);
//        orderItemsRetVo1.setOrderId(1L);
//        orderItemsRetVo1.setProductId(1L);
//        orderItemsRetVo1.setQuantity(3L);
//        orderItemsRetVo1.setPrice(600L);
//        orderItemsRetVo1.setDiscountPrice(100L);
//        orderItemsRetVo1.setPoint(6L);
//        orderItemsRetVo1.setName("苹果");
//
//        var orderItemsRetVo2 = new OrderItemRetVo();
//        orderItemsRetVo2.setId(2L);
//        orderItemsRetVo2.setShopId(2L);
//        orderItemsRetVo2.setOrderId(1L);
//        orderItemsRetVo2.setProductId(2L);
//        orderItemsRetVo2.setQuantity(1L);
//        orderItemsRetVo2.setPrice(10000L);
//        orderItemsRetVo2.setDiscountPrice(0L);
//        orderItemsRetVo2.setPoint(100L);
//        orderItemsRetVo2.setName("T恤");
//
//        var orderItemsRetVo3 = new OrderItemRetVo();
//        orderItemsRetVo3.setId(3L);
//        orderItemsRetVo3.setShopId(1L);
//        orderItemsRetVo3.setOrderId(2L);
//        orderItemsRetVo3.setProductId(3L);
//        orderItemsRetVo3.setQuantity(1L);
//        orderItemsRetVo3.setPrice(800L);
//        orderItemsRetVo3.setDiscountPrice(80L);
//        orderItemsRetVo3.setPoint(8L);
//        orderItemsRetVo3.setName("西瓜");
//
//        var simpleShopRet1 = new SimpleShopVo();
//        simpleShopRet1.setName("水果店");
//
//        var simpleShopRet2 = new SimpleShopVo();
//        simpleShopRet2.setName("OOMALL");
//
//        var simpleShopRet3 = new SimpleShopVo();
//        simpleShopRet3.setName("服装店");
//
//        var simpleRefundRetVo1 = new SimpleRefundRetVo();
//        simpleRefundRetVo1.setPaymentId(3L);
//        simpleRefundRetVo1.setDocumentType((byte) 0);
//        simpleRefundRetVo1.setDocumentId("2");
//
//        var simpleRefundRetVo2 = new SimpleRefundRetVo();
//        simpleRefundRetVo2.setPaymentId(1L);
//        simpleRefundRetVo2.setDocumentType((byte) 2);
//        simpleRefundRetVo2.setDocumentId("131a21aa");
//
//        Mockito.when(paymentService.listPaymentInternal(null, (byte) 2, null, endTime, 1, 10)).thenReturn(
//                new InternalReturnObject<>(0, "", new SimplePageInfo<>(1, 10, 1, 1, List.of(
//                        simplePaymentRetVo1, simplePaymentRetVo2, simplePaymentRetVo3
//                )))
//        );
//        Mockito.when(orderService.getOrderIdByOrderSn("1")).thenReturn(new InternalReturnObject<>(0, "", new OrderIdRetVo(1L)));
//        Mockito.when(orderService.getOrderIdByOrderSn("2")).thenReturn(new InternalReturnObject<>(0, "", new OrderIdRetVo(2L)));
//        Mockito.when(orderService.getOrderById(1L)).thenReturn(new InternalReturnObject<>(0, "", orderRetVo1));
//        Mockito.when(orderService.getOrderById(2L)).thenReturn(new InternalReturnObject<>(0, "", orderRetVo2));
//        Mockito.when(orderService.getOrderItemsByOrderId(1L)).thenReturn(new InternalReturnObject<>(0, "", List.of(
//                orderItemsRetVo1, orderItemsRetVo2
//        )));
//        Mockito.when(orderService.getOrderItemsByOrderId(2L)).thenReturn(new InternalReturnObject<>(0, "", List.of(
//                orderItemsRetVo3
//        )));
//        Mockito.when(orderService.getOrderItemById(1L, null)).thenReturn(new InternalReturnObject<>(0, "", orderItemsRetVo1));
//        Mockito.when(goodsService.getCommissionRate(1L)).thenReturn(new InternalReturnObject<>(0, "", 5));
//        Mockito.when(goodsService.getCommissionRate(2L)).thenReturn(new InternalReturnObject<>(0, "", 5));
//        Mockito.when(goodsService.getCommissionRate(3L)).thenReturn(new InternalReturnObject<>(0, "", 5));
//        Mockito.when(shareService.calculateSharePoints(1L)).thenReturn(new ReturnObject<>(new PointsRetVo(3L, 1001L)));
//        Mockito.when(shareService.calculateSharePoints(2L)).thenReturn(new ReturnObject<>(new PointsRetVo(70L, 1001L)));
//        Mockito.when(shareService.calculateSharePoints(3L)).thenReturn(new ReturnObject<>(new PointsRetVo(5L, 1002L)));
//        Mockito.when(shopService.getSimpleShopById(1L)).thenReturn(new InternalReturnObject<>(0, "", simpleShopRet1));
//        Mockito.when(shopService.getSimpleShopById(0L)).thenReturn(new InternalReturnObject<>(0, "", simpleShopRet2));
//        Mockito.when(shopService.getSimpleShopById(2L)).thenReturn(new InternalReturnObject<>(0, "", simpleShopRet3));
//
//        Mockito.when(refundService.listRefund(null, (byte) 2, null, endTime, 1, 10)).thenReturn(
//                new InternalReturnObject<>(0, "", new SimplePageInfo<>(1, 10, 1, 1, List.of(
//                        simpleRefundRetVo1, simpleRefundRetVo2
//                )))
//        );
//
//        mockMvc.perform(put("/shops/0/liquidation/start")
//                .header("authorization", adminToken)
//                .contentType("application/json;charset=UTF-8")
//                .content("{\"startTime\": null, \"endTime\": \"2021-12-16T00:00:00.000+08:00\"}"))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk());
//    }
//}
