package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
import cn.edu.xmu.oomall.other.microservice.GoodsService;
import cn.edu.xmu.oomall.other.microservice.vo.ProductRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleOnSaleRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.ValidCouponActivityRetVo;
import cn.edu.xmu.oomall.other.model.vo.SimplePageInfo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author jxy
 * @create 2021/12/5 8:49 PM
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {
    private static String customerToken;
    private static String customerToken2;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GoodsService goodsService;

    @MockBean
    private CouponActivityService couponActivityService;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    private static void init() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken = jwtHelper.createToken(1L, "lzl", 0L, 3, 3600);
        customerToken2 = jwtHelper.createToken(16666L, "lzl", 0L, 3, 3600);

    }

    @Test
    @Transactional
    public void getCart() throws Exception {
        ProductRetVo simpleProductRetVo_1 = new ProductRetVo();
        simpleProductRetVo_1.setId(1L);
        simpleProductRetVo_1.setName("111");

        ProductRetVo simpleProductRetVo_2 = new ProductRetVo();
        simpleProductRetVo_2.setId(2L);
        simpleProductRetVo_2.setName("222");

        List<ValidCouponActivityRetVo> simpleCouponActivityRetVos_1 = new ArrayList<>();
        simpleCouponActivityRetVos_1.add(new ValidCouponActivityRetVo(2L, "aa", null, null, null, 1, "hh"));
        List<ValidCouponActivityRetVo> simpleCouponActivityRetVos_2 = new ArrayList<>();
        simpleCouponActivityRetVos_2.add(new ValidCouponActivityRetVo(1L, "bb", null, null, null, 1, "hh"));

        Mockito.when(goodsService.getProductDetails(1L)).thenReturn(new InternalReturnObject(simpleProductRetVo_1));
        Mockito.when(goodsService.getProductDetails(2L)).thenReturn(new InternalReturnObject(simpleProductRetVo_2));
        Mockito.when(couponActivityService.listCouponActivitiesByProductId(1L,1,10)).thenReturn(new InternalReturnObject(new SimplePageInfo<>(1,1,1,1,simpleCouponActivityRetVos_1)));
        Mockito.when(couponActivityService.listCouponActivitiesByProductId(2L,1,10)).thenReturn(new InternalReturnObject(new SimplePageInfo<>(1,1,1,1,simpleCouponActivityRetVos_2)));

        String responseString = this.mvc.perform(get("/carts")
                .header("authorization", customerToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":10,\"page\":1},\"errmsg\":\"成功\"}", responseString, false);
    }


    @Test
    @Transactional
    public void addCart() throws Exception {
        String requestJson = "{\n" +
                "  \"productId\": 1,\n" +
                "  \"quantity\": 2\n" +
                "}";
        SimpleOnSaleRetVo simpleOnSaleRetVo=new SimpleOnSaleRetVo();
        simpleOnSaleRetVo.setPrice(10L);
        Mockito.when(goodsService.getValidNowOnsaleByProductId(1L)).thenReturn(new InternalReturnObject(simpleOnSaleRetVo));
        String responseString = this.mvc.perform(post("/carts").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);


        responseString = this.mvc.perform(post("/carts").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken2).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }



    @Test
    @Transactional
    public void clearCarts() throws Exception {
        String responseString = this.mvc.perform(delete("/carts")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void changeCartInfo() throws Exception {
        String requestJson = "{\n" +
                "  \"productId\": 2,\n" +
                "  \"quantity\": 3\n" +
                "}";
        SimpleOnSaleRetVo simpleOnSaleRetVo=new SimpleOnSaleRetVo();
        simpleOnSaleRetVo.setPrice(10L);
        Mockito.when(goodsService.getValidNowOnsaleByProductId(2L)).thenReturn(new InternalReturnObject(simpleOnSaleRetVo));

        String responseString = this.mvc.perform(put("/carts/1").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken2).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        responseString = this.mvc.perform(put("/carts/3").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken).content(requestJson))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals(" {\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);


    }

    @Test
    @Transactional
    public void deleteCart() throws Exception {

        String responseString = this.mvc.perform(delete("/carts/1").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken2))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
        responseString = this.mvc.perform(delete("/carts/3").contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals(" {\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);


    }

}
