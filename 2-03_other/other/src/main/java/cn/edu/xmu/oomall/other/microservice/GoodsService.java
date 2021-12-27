package cn.edu.xmu.oomall.other.microservice;

import cn.edu.xmu.oomall.other.microservice.vo.OnSaleDetailsRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.ProductRetVo;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleOnSaleRetVo;
import cn.edu.xmu.privilegegateway.annotation.util.InternalReturnObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @Author jxy
 * @create 2021/12/5 5:11 PM
 */
@FeignClient(name = "goods-service")
public interface GoodsService {

    //已完成并初步通过集成
    @GetMapping("/products/{id}")
    InternalReturnObject<ProductRetVo> getProductDetails(@PathVariable("id") Long id);

    //已完成初步通过集成测试
    @GetMapping("/internal/onsales/{id}")
    InternalReturnObject<OnSaleDetailsRetVo> selectFullOnsale(@PathVariable("id") Long id);

    //获取当前时间有效的onsale,已完成并初步通过集成测试
    @GetMapping("/internal/products/{id}/onsale")
    InternalReturnObject<SimpleOnSaleRetVo> getValidNowOnsaleByProductId(@PathVariable Long id);

    // 已完成初步通过集成测试
    @GetMapping("/internal/products/{id}/commissonrate")
    InternalReturnObject<Integer> getCommissionRate(@PathVariable Long id);
}
