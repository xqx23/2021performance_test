//package cn.edu.xmu.oomall.other.controller;
//
//import cn.edu.xmu.oomall.other.microservice.OrderService;
//import cn.edu.xmu.oomall.other.microservice.PaymentService;
//import cn.edu.xmu.oomall.other.microservice.RegionService;
//import cn.edu.xmu.oomall.other.microservice.vo.*;
//import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
//import cn.edu.xmu.oomall.other.model.vo.aftersale.AfterSaleRetVo;
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
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * @author:李智樑
// * @time:2021/12/4 19:42
// **/
//@SpringBootTest
//@AutoConfigureMockMvc
//public class AfterSaleControllerTest {
//    private static String customerToken_1;
//    private static String customerToken_2;
//    private static String shopToken;
//
//    @MockBean
//    private RegionService regionService;
//    @MockBean
////    @Autowired
//    private OrderService orderService;
////    @Autowired
//    @MockBean
//    private PaymentService paymentService;
//    @Autowired
//    private MockMvc mvc;
//
//    @BeforeAll
//    private static void login() {
//        JwtHelper jwtHelper = new JwtHelper();
//        customerToken_1 = jwtHelper.createToken(1L, "lzl", 1L, 3, 3600);
//        customerToken_2 = jwtHelper.createToken(2L, "abc", -1L, 3, 3600);
//        shopToken = jwtHelper.createToken(1L, "shop_admin", 1L, 3, 3600);
//    }
//
//    @Test
//    public void getStates() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales/states"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSales() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithType() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales?type=1")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithTypeAndState() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales?type=1&state=2")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"total\":0,\"pages\":0,\"pageSize\":10,\"page\":1,\"list\":[]},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithPageInfo() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales?page=2&pageSize=1")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":1,\"page\":2},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithDate() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales?beginTime=2021-12-05T10:31:25.000Z&endTime=2021-12-06T10:31:25.000Z")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithTypeStateError() throws Exception {
//        String responseString;
//        responseString = this.mvc.perform(get("/aftersales?type=9")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":503,\"errmsg\":\"字段不合法\"}", responseString, false);
//
//        responseString = this.mvc.perform(get("/aftersales?state=9")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":503,\"errmsg\":\"字段不合法\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesByShop() throws Exception {
//        String responseString = this.mvc.perform(get("/shops/1/aftersales")
//                        .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithTypeByShop() throws Exception {
//        String responseString = this.mvc.perform(get("/shops/1/aftersales?type=1")
//                        .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithTypeAndStateByShop() throws Exception {
//        String responseString = this.mvc.perform(get("/shops/1/aftersales?type=1&state=2")
//                        .header("authorization", shopToken))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"total\":0,\"pages\":0,\"pageSize\":10,\"page\":1,\"list\":[]},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithPageInfoByShop() throws Exception {
//        String responseString = this.mvc.perform(get("/shops/1/aftersales?page=2&pageSize=1")
//                        .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":1,\"page\":2},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAllAfterSalesWithDateByShop() throws Exception {
//        String responseString = this.mvc.perform(get("/shops/1/aftersales?beginTime=2021-12-05T10:31:25.000Z&endTime=2021-12-06T10:31:25.000Z")
//                        .header("authorization", shopToken))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAfterSaleById() throws Exception {
//        SimpleObject regionSimpleRetVo = new SimpleObject(1L, "中国");
//
//        Mockito.when(regionService.getSimpleRegionById(1L)).thenReturn(new InternalReturnObject(regionSimpleRetVo));
//
//        String responseString = this.mvc.perform(get("/aftersales/1")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"id\":1,\"orderId\":1,\"orderItemId\":1,\"customer\":{\"id\":1,\"name\":\"李智樑\"},\"shopId\":1,\"serviceSn\":\"131a21aa\",\"type\":1,\"reason\":\"测试\",\"price\":11,\"quantity\":1,\"region\":{\"id\":1,\"name\":\"中国\"},\"detail\":null,\"consignee\":\"测试\",\"mobile\":\"13456781234\",\"customerLogSn\":\"123456\",\"shopLogSn\":null,\"state\":1},\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAfterSaleByNotExistId() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales/300")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void getAfterSaleByErrorId() throws Exception {
//        String responseString = this.mvc.perform(get("/aftersales/1")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void updateAfterSaleById() throws Exception {
//        String requestJson = "{\n" +
//                "  \"quantity\": 2,\n" +
//                "  \"reason\": \"\",\n" +
//                "  \"regionId\": null,\n" +
//                "  \"detail\": \"just for test\",\n" +
//                "  \"consignee\": \"\",\n" +
//                "  \"mobile\": \"\"\n" +
//                "}";
//
//        String responseString = this.mvc.perform(put("/aftersales/3")
//                        .contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void createAftersale() throws Exception {
//        String requestJson = "{\n" +
//                "  \"type\": 0,\n" +
//                "  \"quantity\": 2,\n" +
//                "  \"reason\": \"hhh\",\n" +
//                "  \"regionId\": 1,\n" +
//                "  \"detail\": \"hhhh\",\n" +
//                "  \"consignee\": \"hhh\",\n" +
//                "  \"mobile\": \"11111111111\"\n" +
//                "}";
//        OrderItemRetVo orderItemRetVo=new OrderItemRetVo();
//        orderItemRetVo.setId(1L);
//        orderItemRetVo.setCustomerId(1L);
//        orderItemRetVo.setShopId(1L);
//        Mockito.when(orderService.getOrderItemById(1L,1L)).thenReturn(new InternalReturnObject(orderItemRetVo));
//        Mockito.when(regionService.getSimpleRegionById(1L)).thenReturn(new InternalReturnObject());
//
//
//        String responseString = this.mvc.perform(post("/orderItems/1/aftersales")
//                        .contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void deleteAftersaleById() throws Exception {
//        String responseString = this.mvc.perform(delete("/aftersales/1")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//        responseString = this.mvc.perform(delete("/aftersales/1")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//        responseString = this.mvc.perform(delete("/aftersales/0")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void addWayBillNumber() throws Exception {
//        String requestJson ="{\n" +
//                "  \"logSn\": \"123456\"\n" +
//                "}";
//        String responseString = this.mvc.perform(put("/aftersales/1/sendback").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_2).content(requestJson))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//    responseString = this.mvc.perform(put("/aftersales/1/sendback").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/aftersales/0/sendback").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_2).content(requestJson))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void confirmAftersaleEnd() throws Exception {
//        String responseString = this.mvc.perform(put("/aftersales/5/confirm")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/aftersales/5/confirm")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/aftersales/0/confirm")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//        responseString = this.mvc.perform(put("/aftersales/1/confirm")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":507,\"errmsg\":\"当前状态禁止此操作\"}", responseString, false);
//
//    }
//    @Test
//    @Transactional
//    public void adminGetAftersaleById() throws Exception {
//        SimpleObject regionSimpleRetVo = new SimpleObject(1L, "中国");
//
//        Mockito.when(regionService.getSimpleRegionById(1L)).thenReturn(new InternalReturnObject(regionSimpleRetVo));
//
//        String responseString = this.mvc.perform(get("/shops/1/aftersales/1")
//                        .header("authorization", customerToken_1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//         responseString = this.mvc.perform(get("/shops/-1/aftersales/1")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//          responseString = this.mvc.perform(get("/shops/-1/aftersales/0")
//                        .header("authorization", customerToken_2))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//
//    }
//
//    @Test
//    @Transactional
//    public void adminConfirmAftersale() throws Exception {
//        String requestJson = "{\n" +
//                "  \"confirm\": true,\n" +
//                "  \"price\": 10,\n" +
//                "  \"conclusion\": \"hhh\",\n" +
//                "  \"type\": 1\n" +
//                "}";
////        OrderItemRetVo orderItemRetVo=new OrderItemRetVo();
////        orderItemRetVo.setId(1L);
////        orderItemRetVo.setCustomerId(1L);
////        orderItemRetVo.setShopId(1L);
////        orderItemRetVo.setPrice(100L);
////        orderItemRetVo.setDiscountPrice(5L);
////        orderItemRetVo.setPoint(5L);
////        Mockito.when(orderItemService.getOrderItemByOrderItemId(1L,1L)).thenReturn(new InternalReturnObject(orderItemRetVo));
//
//        String responseString = this.mvc.perform(put("/shops/-1/aftersales/7/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_2).content(requestJson))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/shops/1/aftersales/7/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/shops/-1/aftersales/0/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_2).content(requestJson))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//        requestJson = "{\n" +
//                "  \"confirm\": true,\n" +
//                "  \"price\": 10,\n" +
//                "  \"conclusion\": \"hhh\",\n" +
//                "  \"type\": 0\n" +
//                "}";
//        responseString = this.mvc.perform(put("/shops/1/aftersales/8/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void adminConfirmAftersale2() throws Exception {
//            String  requestJson ="{\n" +
//                "  \"confirm\": true,\n" +
//                "  \"price\": 10,\n" +
//                "  \"conclusion\": \"hhh\",\n" +
//                "  \"type\": 0\n" +
//                "}";
//       String responseString = this.mvc.perform(put("/shops/1/aftersales/7/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//        requestJson ="{\n" +
//                "  \"confirm\": false,\n" +
//                "  \"price\": 10,\n" +
//                "  \"conclusion\": \"hhh\",\n" +
//                "  \"type\": 0\n" +
//                "}";
//        responseString = this.mvc.perform(put("/shops/1/aftersales/8/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void adminReceive() throws Exception {
//        SimplePaymentRetVo paymentRetVo = new SimplePaymentRetVo(1L, "12", 1L, "1", (byte) 2, (String) null, 10L, "1", (byte) 0, null, null, null);
//        Mockito.when(orderService.getPaymentByOrderItemId(1L)).thenReturn(new InternalReturnObject(paymentRetVo));
//        OrderItemRetVo orderItemRetVo = new OrderItemRetVo();
//        orderItemRetVo.setId(1L);
//        orderItemRetVo.setCustomerId(1L);
//        orderItemRetVo.setShopId(1L);
//        orderItemRetVo.setPrice(100L);
//        orderItemRetVo.setDiscountPrice(5L);
//        orderItemRetVo.setPoint(5L);
//        Mockito.when(orderService.getOrderItemById(1L, 1L)).thenReturn(new InternalReturnObject(orderItemRetVo));
//        AfterSaleRetVo afterSaleRetVo = new AfterSaleRetVo();
//        afterSaleRetVo.setOrderId(1L);
//        AftersaleRecVo aftersaleRecVo = new AftersaleRecVo();
//        Mockito.when(orderService.createAftersaleOrder(1L, aftersaleRecVo)).thenReturn(new InternalReturnObject(afterSaleRetVo));
//
//        String requestJson = "{\n" +
//                "  \"confirm\": true,\n" +
//                "  \"conclusion\": \"hh\"\n" +
//                "}";
//        String responseString = this.mvc.perform(put("/shops/-1/aftersales/2/receive").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_2).content(requestJson))
//                .andExpect(status().isForbidden())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/shops/1/aftersales/0/receive").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
//
//        responseString = this.mvc.perform(put("/shops/1/aftersales/2/receive").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//
//
//    }
//    @Test
//    @Transactional
//    public void adminReceive2() throws Exception {
//      String requestJson ="{\n" +
//                "  \"confirm\": false,\n" +
//                "  \"conclusion\": \"hh\"\n" +
//                "}";
//       String  responseString = this.mvc.perform(put("/shops/1/aftersales/8/confirm").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    @Transactional
//    public void adminDeliver() throws Exception {
//        String requestJson ="{\n" +
//                "  \"shopLogSn\": \"1234\"\n" +
//                "}";
//        String  responseString = this.mvc.perform(put("/shops/1/aftersales/9/deliver").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1).content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//
//    @Test
//    @Transactional
//    public void GetPaymentByAftersaleId() throws Exception {
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
//        Mockito.when(paymentService.listPaymentInternal("20211210231106043", null, null, null, 1, 10)).thenReturn(
//                new InternalReturnObject<>(0, "", new SimplePageInfo<>(1, 10, 1, 1, List.of(
//                        simplePaymentRetVo1, simplePaymentRetVo2, simplePaymentRetVo3
//                )))
//        );
//        String  responseString = this.mvc.perform(get("/aftersales/5/payments").contentType("application/json;charset=UTF-8")
//                        .header("authorization", customerToken_1))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andReturn().getResponse().getContentAsString();
//        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
//    }
//    @Test
//    public void test1()
//    {
////        InternalReturnObject internalReturnObject= orderService.getPaymentByOrderItemId(3L);
////        System.out.println(internalReturnObject.getData());
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
////        internalReturnObject= orderService.createAftersaleOrder(1L,aftersaleRecVo);
////        System.out.println(internalReturnObject.getData());
////        InternalReturnObject internalReturnObject= paymentService.listPaymentInternal(null,null,null,null,1,10);
////        System.out.println(internalReturnObject.getData());
//    }
//
//
//}
