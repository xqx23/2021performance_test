package cn.edu.xmu.oomall.other.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Gao Yanfeng
 * @date 2021/12/10
 */
@Getter
@AllArgsConstructor
public enum LiquidationState {

    NOT_REMITTED(0, "未汇出"),
    REMITTED(1, "已汇出"),
    ;

    private final Integer code;
    private final String name;
}
