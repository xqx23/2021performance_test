package cn.edu.xmu.oomall.other.aop;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* @author jxy
* @create 2021/12/25 12:16 PM
*/

@SpringBootTest
@AutoConfigureMockMvc
public class MaskAspectTest {
    private static String adminToken;
    private static JwtHelper jwtHelper = new JwtHelper();

    @Autowired
    private MockMvc mvc;

    @Test
    public void  Test1() throws Exception{
        adminToken =jwtHelper.createToken(1L,"admin",1L, 0,3600);
        String responseString = this.mvc.perform(get(
                        "/try").header("authorization", adminToken).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
        String expectedResponse="{\"code\":\"OK\",\"errmsg\":\"成功\",\"data\":{\"address\":null,\"mobile\":null,\"detail\":null}}";
        assert  expectedResponse.equals(responseString);
    }
    @Test
    public void  Test2() throws Exception{
        adminToken =jwtHelper.createToken(1L,"admin",1L, 1,3600);
        String responseString = this.mvc.perform(get(
                        "/try").header("authorization", adminToken).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
        String expectedResponse="{\"code\":\"OK\",\"errmsg\":\"成功\",\"data\":{\"address\":\"312************\",\"mobile\":\"123****312\",\"detail\":\"213********\"}}";
        assert  expectedResponse.equals(responseString);
    }
    @Test
    public void  Test3() throws Exception{
        adminToken =jwtHelper.createToken(1L,"admin",1L, 2,3600);
        String responseString = this.mvc.perform(get(
                        "/try").header("authorization", adminToken).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
        String expectedResponse="{\"code\":\"OK\",\"errmsg\":\"成功\",\"data\":{\"address\":\"312213321123213\",\"mobile\":\"1233212312\",\"detail\":\"21331232132\"}}";
        assert  expectedResponse.equals(responseString);
    }
}
