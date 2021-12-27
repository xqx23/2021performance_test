package cn.edu.xmu.oomall.other.microservice.vo;

import cn.edu.xmu.oomall.core.model.VoObject;
import cn.edu.xmu.oomall.other.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author xiuchen lang 22920192204222
 * @date 2021/11/12 18:28
 */
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "分享活动详情")
public class RetShareActivityInfoVo implements VoObject, Serializable {
    private Long id;
    private ShopVo shop;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime beginTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime endTime;
    private List<StrategyVo> strategy;

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
