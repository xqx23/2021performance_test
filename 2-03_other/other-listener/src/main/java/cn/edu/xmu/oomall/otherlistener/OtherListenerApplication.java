package cn.edu.xmu.oomall.otherlistener;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.oomall.core.util", "cn.edu.xmu.oomall.core.model", "cn.edu.xmu.oomall.otherlistener", "cn.edu.xmu.privilegegateway"})
@MapperScan("cn.edu.xmu.oomall.otherlistener.mapper")
@EnableFeignClients(basePackages = "cn.edu.xmu.oomall.otherlistener.microservice")
public class OtherListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtherListenerApplication.class, args);
    }

}
