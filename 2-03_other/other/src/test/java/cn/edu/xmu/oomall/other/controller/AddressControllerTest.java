package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.other.microservice.RegionService;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author jxy
 * @create 2021/12/9 10:05 PM
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {
    private static String customerToken_1;
    private static String customerToken_2;
    private static String shopToken;


//    @Autowired
//    private RegionService regionService;


    @MockBean
    private RegionService regionService;

    @Autowired
    private MockMvc mvc;

    @BeforeAll
    private static void login() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken_1 = jwtHelper.createToken(1L, "lzl", -1L, 3, 3600);
        customerToken_2 = jwtHelper.createToken(2L, "abc", -1L, 3, 3600);
        shopToken = jwtHelper.createToken(1L, "shop_admin", 1L, 3, 3600);
    }

    @Test
    @Transactional
    public void addAddress() throws Exception {
        SimpleObject regionSimpleRetVo = new SimpleObject(1L, "中国");
        Mockito.when(regionService.getSimpleRegionById(1L)).thenReturn(new InternalReturnObject(regionSimpleRetVo));

        String requestJson = "{\n" +
                "  \"regionId\": 1,\n" +
                "  \"detail\": \"长沙\",\n" +
                "  \"consignee\": \"jxy2\",\n" +
                "  \"mobile\": \"11211111111\"\n" +
                "}";

        String responseString = this.mvc.perform(post("/addresses")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void getAddresses() throws Exception {
        SimpleObject regionSimpleRetVo = new SimpleObject(2417L, "中国");
        Mockito.when(regionService.getSimpleRegionById(2417L)).thenReturn(new InternalReturnObject(regionSimpleRetVo));

        String responseString = this.mvc.perform(get("/addresses")
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
    public void updateDefaultAddress() throws Exception {

        String responseString = this.mvc.perform(put("/addresses/1/default")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
        responseString = this.mvc.perform(put("/addresses/0/default")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"address: Address Not Found\"}", responseString, false);

        responseString = this.mvc.perform(put("/addresses/8/default")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_2))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(" {\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);

    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        SimpleObject regionSimpleRetVo = new SimpleObject(2L, "湖南");
        Mockito.when(regionService.getSimpleRegionById(2L)).thenReturn(new InternalReturnObject(regionSimpleRetVo));

        String requestJson = "{\n" +
                "  \"regionId\": 2,\n" +
                "  \"detail\": \"hhhh\",\n" +
                "  \"consignee\": null,\n" +
                "  \"mobile\": \"1222223333\"\n" +
                "}";

        String responseString = this.mvc.perform(put("/addresses/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
        responseString = this.mvc.perform(put("/addresses/0")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1)
                .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);

        responseString = this.mvc.perform(put("/addresses/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_2)
                .content(requestJson))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(" {\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);
    }

    @Test
    @Transactional
    public void deleteAddresss() throws Exception {

        String responseString = this.mvc.perform(delete("/addresses/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
        responseString = this.mvc.perform(delete("/addresses/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":504,\"errmsg\":\"操作的资源id不存在\"}", responseString, false);

        responseString = this.mvc.perform(delete("/addresses/8")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken_2))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(" {\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);

    }
}
