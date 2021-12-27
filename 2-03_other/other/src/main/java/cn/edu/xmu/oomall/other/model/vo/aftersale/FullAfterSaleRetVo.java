package cn.edu.xmu.oomall.other.model.vo.aftersale;

import cn.edu.xmu.oomall.other.constant.Constants;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.oomall.other.model.vo.SimpleUserRetVo;
import cn.edu.xmu.oomall.other.model.vo.customer.SimpleCustomerRetVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @Author jxy
 * @create 2021/12/11 10:56 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullAfterSaleRetVo {
    private Long id;
    private Long orderId;
    private Long orderItemId;
    private SimpleCustomerRetVo customer;
    private Long shopId;
    private String serviceSn;
    private Byte type;
    private String reason;
    private Long price;
    private Long quantity;
    private SimpleObject region;
    private String detail;
    private String consignee;
    private String mobile;
    private String customerLogSn;
    private String shopLogSn;
    private Byte state;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtCreate;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtModified;

    @ApiModelProperty(value = "创建者")
    private SimpleUserRetVo creator;

    @ApiModelProperty(value = "修改者")
    private SimpleUserRetVo modifier;
}
