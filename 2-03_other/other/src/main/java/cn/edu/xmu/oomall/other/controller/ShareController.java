package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.core.util.Common;
import cn.edu.xmu.oomall.core.util.ReturnNo;
import cn.edu.xmu.oomall.core.util.ReturnObject;
import cn.edu.xmu.oomall.other.model.vo.share.ShareRetVo;
import cn.edu.xmu.oomall.other.model.vo.share.SuccessfulShareRetVo;
import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.privilegegateway.annotation.aop.Audit;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginName;
import cn.edu.xmu.privilegegateway.annotation.aop.LoginUser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;

/**
 * @author Lu Zhang
 * @create 2021/12/6
 */
@Api(value = "分享", tags = "share")
@RestController
@RefreshScope
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class ShareController {

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShareService shareService;

    /***
     * 顾客查询自己的分享记录
     */
    @ApiOperation(value = "买家查询所有分享记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "productId", value = "货品Id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/shares")
    public Object getOwnShares(@LoginUser Long userId,
                               @RequestParam(required = false) Long productId,
                               @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
                               @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        if (beginTime != null && endTime != null) {
            if (beginTime.isAfter(endTime)) {
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.LATE_BEGINTIME));
            }
        }

        ReturnObject ret = shareService.listShares(userId, null, productId, beginTime, endTime, page, pageSize);
        ret = Common.getPageRetVo(ret, ShareRetVo.class);

        return Common.decorateReturnObject(ret);
    }

    /***
     * 查看商品的详细信息
     */
    @ApiOperation(value = "查看商品的详细信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "sid", value = "分享Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "id", value = "货品Id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/shares/{sid}/products/{id}")
    public Object getGoodsDetails(@LoginUser Long userId, @LoginName String userName,
                                  @PathVariable(value = "sid", required = true) Long sid,
                                  @PathVariable(value = "id", required = true) Long productId) {
        return Common.decorateReturnObject(shareService.getProductById(userId, userName, sid, productId));
    }

    /***
     * 商铺管理员查询分享记录
     */
    @ApiOperation(value = "商铺管理员查询分享记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店铺Id"),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "货品Id"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping("/shops/{did}/products/{id}/shares")
    public Object getSharesByShopId(@PathVariable("did") Long shopId,
                                    @PathVariable("id") Long productId,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        ReturnObject ret = shareService.listShares(null, shopId, productId, null, null, page, pageSize);
        ret = Common.getPageRetVo(ret, ShareRetVo.class);
        return Common.decorateReturnObject(ret);
    }

    /***
     * 分享者查询所有分享成功记录
     */
    @ApiOperation(value = "分享者查询所有分享成功记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "productId", value = "货品Id"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @GetMapping("/beshared")
    public Object getBeShared(@LoginUser Long userId,
                              @RequestParam(required = false) Long productId,
                              @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
                              @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize) {
        if (beginTime != null && endTime != null) {
            if (beginTime.isAfter(endTime)) {
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.LATE_BEGINTIME));
            }
        }
        ReturnObject ret = shareService.listBeShared(userId, null, productId, beginTime, endTime, page, pageSize);
        ret = Common.getPageRetVo(ret, SuccessfulShareRetVo.class);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员查询店铺分享成功记录
     *
     * @param shopId
     * @param productId
     * @param beginTime
     * @param endTime
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "管理员查询店铺分享成功记录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "did", value = "店铺id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "货品Id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "endTime", value = "结束时间"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "page", value = "页码"),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "每页数目")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit(departName = "shops")
    @GetMapping("/shops/{did}/products/{id}/beshared")
    public Object getBeSharedByShopId(@PathVariable(value = "did", required = true) Long shopId,
                                      @PathVariable(value = "id", required = true) Long productId,
                                      @RequestParam(value = "beginTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime beginTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endTime,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        if (beginTime != null && endTime != null) {
            if (beginTime.isAfter(endTime)) {
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.LATE_BEGINTIME));
            }
        }

        ReturnObject ret = shareService.listBeShared(null, shopId, productId, beginTime, endTime, page, pageSize);
        ret = Common.getPageRetVo(ret, SuccessfulShareRetVo.class);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 分享者生成分享链接
     *
     * @param userId
     * @param userName
     * @param onsaleId
     * @return
     */
    @ApiOperation(value = "分享者生成分享链接", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "id", value = "货品销售Id", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功")
    })
    @Audit
    @PostMapping("/onsales/{id}/shares")
    public Object createShareLink(@LoginUser Long userId,
                                  @LoginName String userName,
                                  @PathVariable(value = "id", required = true) Long onsaleId) {

        ReturnObject returnObject = shareService.createShareLink(userId, userName, onsaleId);
        return Common.decorateReturnObject(returnObject);
    }
}
