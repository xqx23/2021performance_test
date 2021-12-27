package cn.edu.xmu.oomall.other.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lu Zhang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulShare {

    public enum State {
        VALID((byte) 0, "有效"),
        LIQUIDATION((byte) 1, "已清算"),
        NOTVALID((byte) 2, "失效");

        private static final Map<Byte, SuccessfulShare.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (SuccessfulShare.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static SuccessfulShare.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    private Long id;

    private Long shareId;

    private Long sharerId;

    private Long productId;

    private Long onsaleId;

    private Long customerId;

    private Byte state;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
    
}
