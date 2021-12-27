package cn.edu.xmu.oomall.other;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.oomall", "cn.edu.xmu.oomall.other", "cn.edu.xmu.privilegegateway.annotation"})
@MapperScan("cn.edu.xmu.oomall.other.mapper")
@EnableSwagger2
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.edu.xmu.oomall.other.microservice")
public class OtherServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtherServiceApplication.class, args);
    }
}
