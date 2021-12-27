package cn.edu.xmu.oomall.other.aop;

import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.customer.ResetPwdRetVo;
import cn.edu.xmu.privilegegateway.annotation.aop.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
* @author jxy
* @create 2021/12/25 12:10 PM
*/

@RestController
@RequestMapping(value = "", produces = "application/json;charset=UTF-8")
public class TryController {
    @Autowired
    private HttpServletResponse httpServletResponse;

    @GetMapping("/try")
    @Mask
    public Object test1() {
        AfterSaleRetVo afterSaleRetVo = new AfterSaleRetVo();
        afterSaleRetVo.setAddress("312213321123213");
        afterSaleRetVo.setDetail("21331232132");
        afterSaleRetVo.setMobile("1233212312");
        return new ReturnObject<>(afterSaleRetVo);
    }

}
