package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 李智樑
 * @create 2021/12/5 18:17
 */
//经过了初步集成测试
@FeignClient(name = "freight-service")
public interface RegionService {
    // region内部api已完成，若地区不存在或被封禁也会返回该地区信息
    @GetMapping("/internal/region/{id}")
    InternalReturnObject<SimpleObject> getSimpleRegionById(@RequestParam("id") Long id);
}
