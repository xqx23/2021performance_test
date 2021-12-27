package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.other.model.vo.liquidation.SimpleShopVo;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    private static String customerToken;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    private static void login() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken = jwtHelper.createToken(1L, "lzl", 0L, 3, 3600);
    }

    @Test
    public void getCustomerStates() throws Exception {
        String responseString = this.mvc.perform(get("/customers/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"data\":[{\"code\":0,\"name\":\"正常\"},{\"code\":1,\"name\":\"被封禁\"}],\"errmsg\":\"成功\"}", responseString, true);
    }

    @Test
    @Transactional
    public void getSelfInfo() throws Exception {
        String responseString = this.mvc.perform(get("/self")
                        .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void logout() throws Exception {
        String responseString = this.mvc.perform(get("/logout")
                        .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void modifyCustomer() throws Exception {
        String requestJson = "{\"name\": \"test\"}";
        String responseString = this.mvc.perform(put("/self")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void resetUserSelfPassword() throws Exception {
        String requestJson = "{\"name\": \"699275\"}";
        String responseString = this.mvc.perform(put("/password/reset")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        //用户不存在
        requestJson = "{\"name\": \"lz\"}";
        responseString = this.mvc.perform(put("/password/reset")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":608,\"errmsg\":\"登录用户id不存在\"}", responseString, false);
    }

    @Test
    @Transactional
    public void modifyPassword() throws Exception {
        String requestJson = "{\n" +
                "  \"captcha\": \"123456\",\n" +
                "  \"newPassword\": \"1Aa@23\"\n" +
                "}";

        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(1L);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(true);

        String responseString = this.mvc.perform(put("/password")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {

        String responseString = this.mvc.perform(get("/shops/0/customers")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        responseString = this.mvc.perform(get("/shops/1/customers")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":505,\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);


        responseString = this.mvc.perform(get("/shops/0/customers?userName=699275")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

    }

    @Test
    @Transactional
    public void getCustomerById() throws Exception {

        String responseString = this.mvc.perform(get("/shops/0/customers/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        responseString = this.mvc.perform(get("/shops/1/customers/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"code\":\"RESOURCE_ID_OUTSCOPE\",\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);

    }

    @Test
    @Transactional
    public void banCustomer() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String responseString = this.mvc.perform(put("/shops/0/customers/1/ban")
                        .contentType("application/json;charset=UTF-8")
                        .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        responseString = this.mvc.perform(put("/shops/1/customers/1/ban")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"code\":\"RESOURCE_ID_OUTSCOPE\",\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);

    }

    @Test
    @Transactional
    public void releaseCustomer() throws Exception {

        String responseString = this.mvc.perform(put("/shops/0/customers/1/release")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        responseString = this.mvc.perform(put("/shops/1/customers/1/release")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"code\":\"RESOURCE_ID_OUTSCOPE\",\"errmsg\":\"操作的资源id不是自己的对象\"}", responseString, false);

    }

    @Test
    @Transactional
    public void signUpUser() throws Exception {
        String requestJson = "{\n" +
                "  \"mobile\": \"22222222222\",\n" +
                "  \"email\": \"123456@qq.com\",\n" +
                "  \"userName\": \"jxy3\",\n" +
                "  \"password\": \"123456\",\n" +
                "  \"name\": \"jxy\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/customers")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);


        responseString = this.mvc.perform(post("/customers")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":612,\"errmsg\":\"邮箱已被注册\"}", responseString, false);

        requestJson = "{\n" +
                "  \"mobile\": \"32222222222\",\n" +
                "  \"email\": \"12356@qq.com\",\n" +
                "  \"userName\": \"699275\",\n" +
                "  \"password\": \"123456\",\n" +
                "  \"name\": \"699275\"\n" +
                "}";
        responseString = this.mvc.perform(post("/customers")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", customerToken)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":613,\"errmsg\":\"用户名已被注册\"}", responseString, false);

    }

    @Test
    @Transactional
    public void loginCustomer() throws Exception {
        String requestJson = "{\"userName\": \"699275\",\n" + "\"password\": \"123456\"}";
        String responseString = this.mvc.perform(post("/login")
                .contentType("application/json;charset=UTF-8")
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        requestJson = "{\"userName\": \"jxy4\",\n" + "\"password\": \"123456\"}";
        responseString = this.mvc.perform(post("/login")
                .contentType("application/json;charset=UTF-8")
                .content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":609,\"errmsg\":\"用户名不存在或者密码错误\"}", responseString, false);

    }

    @Test
    @Transactional
    public void changeCustomerPoint() throws Exception {
        String requestJson = "{\"points\": 1}";
        String responseString = this.mvc.perform(put("/internal/point/1")
                        .contentType("application/json;charset=UTF-8")
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);

        requestJson = "{\"points\": -1000}";
        responseString = this.mvc.perform(put("/internal/point/1")
                        .contentType("application/json;charset=UTF-8")
                        .content(requestJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }

    @Test
    @Transactional
    public void getUserBriefInfo() throws Exception {
        String responseString = this.mvc.perform(get("/internal/customers/1")
                .contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        JSONAssert.assertEquals("{\"errno\":0,\"errmsg\":\"成功\"}", responseString, false);
    }
}

