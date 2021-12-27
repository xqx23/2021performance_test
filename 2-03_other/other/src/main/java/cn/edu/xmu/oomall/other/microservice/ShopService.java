package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.model.vo.liquidation.SimpleShopVo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Lu Zhang
 * @create 2021/12/13
 */
@FeignClient(name = "shop-service")
public interface ShopService {

    //已完成集成
    @GetMapping("/shops/{id}")
    InternalReturnObject<SimpleShopVo> getSimpleShopById(@PathVariable Long id);
}
