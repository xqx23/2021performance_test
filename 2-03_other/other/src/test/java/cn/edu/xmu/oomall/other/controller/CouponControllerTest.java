package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.dao.CouponDao;
import cn.edu.xmu.oomall.other.microservice.CouponActivityService;
import cn.edu.xmu.oomall.other.microservice.vo.CouponActivityDetailRetVo;
import cn.edu.xmu.oomall.other.model.vo.coupon.CouponActivityDetailVo;
import cn.edu.xmu.oomall.other.service.RocketMQService;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import cn.edu.xmu.privilegegateway.annotation.util.RedisUtil;
import cn.edu.xmu.privilegegateway.annotation.util.bloom.BloomFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import util.CouponsFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author:李智樑
 * @time:2021/12/9 21:33
 **/
@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {
    private static String customerToken_1;
    private static String customerToken_2;
    private static String ACTIVITY_KEY = "micro_service_coupon_activity_%d";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CouponDao couponDao;

    @MockBean
    private RocketMQService rocketMQService;

    @MockBean
    private CouponActivityService couponActivityService;

    @MockBean
    private BloomFilter<Long> longBloomFilter;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    private static void login() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken_1 = jwtHelper.createToken(1001L, "lzl", -1L, 3, 3600);
        customerToken_2 = jwtHelper.createToken(2L, "abc", -1L, 3, 3600);
    }
    @Test
    public void getStaten() throws Exception {

        String responseString = this.mvc.perform(get("/coupons/states")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void receiveCoupon() throws Exception {
        CouponActivityDetailRetVo couponActivityDetailVo=new CouponActivityDetailRetVo();
        couponActivityDetailVo.setQuantity(-1);
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject<CouponActivityDetailRetVo>(couponActivityDetailVo));
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Transactional
    public void getCoupons() throws Exception {
        CouponActivityDetailRetVo couponActivityRetVo = new CouponActivityDetailRetVo();

        couponActivityRetVo.setId(1L);
        couponActivityRetVo.setName("测试");
        couponActivityRetVo.setImageUrl("aaa");
        couponActivityRetVo.setQuantity(20);
        couponActivityRetVo.setBeginTime(ZonedDateTime.of(2021, 8, 13, 10, 20, 10, 10, ZoneId.systemDefault()));
        couponActivityRetVo.setEndTime(ZonedDateTime.of(2021, 9, 20, 10, 50, 10, 10, ZoneId.systemDefault()));
        couponActivityRetVo.setCouponTime(ZonedDateTime.of(2021, 7, 30, 11, 2, 10, 10, ZoneId.systemDefault()));

        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject(couponActivityRetVo));

        String responseString = this.mvc.perform(get("/coupons?pageSize=2&page=2")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"data\":{\"pageSize\":2,\"page\":2},\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void useCoupon() throws Exception {
        String responseString = this.mvc.perform(put("/internal/coupons/1/use?userId=1001&userName=李存维"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void banCoupon() throws Exception {
        String responseString = this.mvc.perform(put("/internal/coupons/1/ban?userId=1001&userName=李存维"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void refundCoupon() throws Exception {
        String responseString = this.mvc.perform(put("/internal/coupons/1/refund?userId=1001&userName=李存维"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void couponExists() throws Exception {
        String responseString = this.mvc.perform(get("/internal/coupons/2/exists?userId=1001"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":507,\"errmsg\":\"当前状态禁止此操作\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_1() throws Exception {
        /* quantityType == 0 的情况，领到优惠券 */
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject(
                CouponsFactory.getInstance().getCouponActivity_type_0()
        ));
        Mockito.when(longBloomFilter.checkFilter(Mockito.anyString())).thenReturn(true);
        Mockito.when(longBloomFilter.checkValue(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_2() throws Exception {
        /* quantity == -1 的情况 */

        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(
                new InternalReturnObject(CouponsFactory.getInstance().getNoCouponActivity()));
        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_3() throws Exception {
        /* 不存在优惠活动 */
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(
                new InternalReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST.getCode(), "操作的资源id不存在"));
        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_1))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_4() throws Exception {
        /* quantityType == 0 的情况，不能重复领优惠券 */
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject(
                CouponsFactory.getInstance().getCouponActivity_type_0()
        ));
        Mockito.when(longBloomFilter.checkFilter(Mockito.anyString())).thenReturn(true);
        Mockito.when(longBloomFilter.checkValue(Mockito.anyString(), Mockito.anyLong())).thenReturn(true);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":633,\"errmsg\":\"已领取该优惠券\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_5() throws Exception {
        /* quantityType == 1 的情况，领优惠券 */
        Mockito.when(couponActivityService.getCouponActivityById(Mockito.anyLong())).thenReturn(new InternalReturnObject(
                CouponsFactory.getInstance().getCouponActivity_type_1()
        ));
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.executeScript(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_6() throws Exception {
        String key = String.format(ACTIVITY_KEY, 1);
        /* quantityType == 1 的情况，领罄 */
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject(
                CouponsFactory.getInstance().getCouponActivity_type_1()
        ));
        Mockito.when(redisUtil.hasKey(key)).thenReturn(false);

        Mockito.when(redisUtil.executeScript(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(-1L);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":631,\"errmsg\":\"优惠卷领罄\"}", responseString, false);
    }

    @Test
    @Transactional
    public void receiveCoupon_7() throws Exception {
        /* quantityType == 1，需要loadQuantity */
        Mockito.when(couponActivityService.getCouponActivityById(1L)).thenReturn(new InternalReturnObject(
                CouponsFactory.getInstance().getCouponActivity_type_1()
        ));
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.executeScript(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1L);

        String responseString = this.mvc.perform(post("/couponactivities/1/coupons")
                        .header("authorization", customerToken_2))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    public void loadQuantity() throws Exception {
        couponDao.loadQuantity("key", 1L, 10, 100);
    }

}
