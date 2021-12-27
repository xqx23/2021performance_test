package cn.edu.xmu.oomall.other.model.vo.share;

import cn.edu.xmu.oomall.other.constant.Constants;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleObject;
import cn.edu.xmu.oomall.other.microservice.vo.SimpleProductRetVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulShareRetVo {
    private Long id;
    private SimpleProductRetVo product;
    private Long sharerId;
    private Long customerId;
    private Byte state;
    private SimpleObject creator;
    private SimpleObject modifier;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtCreate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT, timezone = "GMT+8")
    private ZonedDateTime gmtModified;
}
