package cn.edu.xmu.oomall.other.microservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author jxy
 * @create 2021/12/5 7:30 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCouponActivityRetVo {
    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
