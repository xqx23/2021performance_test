package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.ShoppingCartRetVo;
import cn.edu.xmu.oomall.other.model.vo.shoppingcart.ShoppingCartVo;
import cn.edu.xmu.oomall.other.service.ShoppingCartService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class ShoppingCartController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /***
     * 买家获得购物车列表
     *@author 李智樑
     *@create 2021/12/5 4:24 PM
     */

    @ApiOperation(value = "买家获得购物车列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/carts")
    public Object getCarts(@LoginUser Long userId, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        ReturnObject returnObject = shoppingCartService.getCarts(userId, page, pageSize);
        returnObject = (Common.getPageRetVo(returnObject, ShoppingCartRetVo.class));
        return Common.decorateReturnObject(returnObject);
    }


    /***
     * 买家清空购物车
     *@author jxy
     *@create 2021/12/5 8:06 PM
     */

    @ApiOperation(value = "买家清空购物车", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @DeleteMapping("/carts")
    public Object clearCarts(@LoginUser Long UserId) {
        ReturnObject returnObject = shoppingCartService.clearCart(UserId);
        return Common.decorateReturnObject(returnObject);
    }

    /***
     * 买家删除购物车中商品
     *@author jxy
     *@create 2021/12/5 10:30 PM
     */

    @ApiOperation(value = "买家删除购物车中商品", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "购物车id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @DeleteMapping("/carts/{id}")
    public Object deleteCart(@LoginUser Long UserId, @PathVariable("id") Long id) {
        ReturnObject returnObject = shoppingCartService.deleteCart(UserId, id);
        return Common.decorateReturnObject(returnObject);
    }

    /***
     * 买家将商品加入购物车
     *@author jxy
     *@create 2021/12/8 7:50 PM
     */

    @ApiOperation(value = "买家将商品加入购物车", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PostMapping("/carts")
    public Object addToCart(@LoginUser Long userId, @LoginName String loginName, @Valid @RequestBody ShoppingCartVo vo, BindingResult bindingResult) {
        // 合法性检验
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (res != null) {
            return res;
        }

        ReturnObject ret = shoppingCartService.addToCart(userId, loginName, vo);
        return Common.decorateReturnObject(ret);

    }

    /***
     * 买家修改购物车单个商品的数量或规格
     *@author jxy
     *@create 2021/12/5 9:02 PM
     */

    @ApiOperation(value = "买家修改购物车单个商品的数量或规格", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "购物车id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "carts")
    @PutMapping("/carts/{id}")
    public Object changeCartInfo(@LoginUser Long userId, @LoginName String loginName, @PathVariable("id") Long id, @RequestBody ShoppingCartVo vo) {
        ReturnObject ret = shoppingCartService.modifyCart(id, vo, userId, loginName);
        return Common.decorateReturnObject(ret);
    }
}
