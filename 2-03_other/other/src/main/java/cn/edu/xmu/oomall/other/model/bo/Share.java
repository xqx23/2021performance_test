package cn.edu.xmu.oomall.other.model.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lu Zhang
 * @create 2021/12/6
 */
@Data
public class Share {
    public enum State {

        VALID((byte) 0, "有效"),
        NOTVALID((byte) 1, "失效");

        private static final Map<Byte, Share.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Share.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Share.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
