package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.core.config.OpenFeignConfig;
import cn.edu.xmu.oomall.other.microservice.vo.RetShareActivityInfoVo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author jxy
 * @create 2021/12/13 6:06 PM
 */
//todo:token验证，不校验的集成已成功
@FeignClient(name = "activity-service", configuration = OpenFeignConfig.class)
public interface ShareActivityService {
    @GetMapping("/shareactivities/{id}")
    InternalReturnObject<RetShareActivityInfoVo> getShareActivityById(@PathVariable(value = "id", required = true) Long id);

}
