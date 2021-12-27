package cn.edu.xmu.oomall.other.microservice.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:李智樑
 * @time:2021/12/10 13:29
 **/
public class CouponActivity {
    public enum State {
        /**
         * 草稿
         */
        DRAFT((byte) 0, "草稿"),

        /**
         * 上线
         */
        ONLINE((byte) 1, "上线"),
        /**
         * 下线
         */
        OFFLINE((byte) 2, "下线");

        private static final Map<Byte, State> STATE_MAP;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            STATE_MAP = new HashMap();
            for (State enum1 : values()) {
                STATE_MAP.put(enum1.code, enum1);
            }
        }

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static State getTypeByCode(Integer code) {
            return STATE_MAP.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
