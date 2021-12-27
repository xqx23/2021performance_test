package cn.edu.xmu.oomall.other.model.bo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:李智樑
 * @time:2021/12/8 17:54
 **/
public class Coupon {
    public enum State {
        RECEIVED(1, "已领取"),
        USED(2, "已使用"),
        OUT_OF_DATE(3, "已失效");

        private static final Map<Integer, Coupon.State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(Coupon.State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Coupon.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
