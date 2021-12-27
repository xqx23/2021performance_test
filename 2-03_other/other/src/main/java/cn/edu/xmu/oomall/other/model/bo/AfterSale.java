package cn.edu.xmu.oomall.other.model.bo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author 李智樑
 * @create 2021/12/4 19:27
 */
public class AfterSale {
    /**
     * 售后类型
     */
    public enum Type {
        EXCHANGE(0, "换货"),
        MAINTAIN(2, "维修"),
        RETURN(1, "退货");

        private static final Map<Integer, Type> typeMap;

        static {
            typeMap = new HashMap();
            Arrays.stream(Type.values()).forEach(enumitem -> typeMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Type getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    /***
     * 售后单状态
     */
    public enum State {
        NEW(0, "新建态"),
        WAIT_CUSTOMER_DELIVER(1, "待买家发货"),
        CUSTOMER_DELIVERED(2, "买家已发货"),
        WAIT_SHOP_REFUND(3, "待退款"),
        WAIT_SHOP_DELIVER(4, "待店家发货"),
        SHOP_DELIVERED(5, "店家已发货"),
        OVER(6, "已结束"),
        CANCELED(7, "已取消"),
        WAIT_PAY(8, "待支付"),

        ;

        private static final Map<Integer, State> stateMap;

        static {
            stateMap = new HashMap();
            Arrays.stream(State.values()).forEach(enumitem -> stateMap.put(enumitem.code, enumitem));
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static State getTypeByCode(Integer code) {
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
