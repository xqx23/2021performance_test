package cn.edu.xmu.oomall.other.aop;

import cn.edu.xmu.privilegegateway.annotation.util.JacksonUtil;
import cn.edu.xmu.privilegegateway.annotation.util.JwtHelper;
import cn.edu.xmu.privilegegateway.annotation.util.ResponseUtil;
import cn.edu.xmu.privilegegateway.annotation.util.ReturnNo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.catalina.LifecycleState;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author:李智樑
 * @time:2021/12/23 11:16
 **/
@Aspect
@Component
public class MaskAspect {

    final private static Integer USER_LEVEL_0 = 0;

    final private static Integer USER_LEVEL_1 = 1;

    final private static Integer USER_LEVEL_2 = 2;

    final private static String PHONE_NUMBER_PATTERN = "^1[0-9]{10}$";

    @Pointcut("@annotation(cn.edu.xmu.oomall.other.aop.Mask)")
    public void maskAspect() {
    }

    @Before("maskAspect()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @Around("maskAspect()")
    public Object around(JoinPoint joinPoint) throws JsonProcessingException {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseUtil.fail(ReturnNo.AUTH_NEED_LOGIN);
        }

        // 获取结果
        Object obj = null;
        try {
            obj = ((ProceedingJoinPoint) joinPoint).proceed(joinPoint.getArgs());
        } catch (Throwable e) {
        }

        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        Integer userLevel = null;

        if (null != userAndDepart) {
            userLevel = userAndDepart.getUserLevel();
            if (userLevel != null) {
                return masking(obj, userLevel);
            }
        }

        return obj;
    }

    private static Object masking(Object object, Integer level) throws JsonProcessingException {
        String[] args = new String[]{"mobile", "address", "detail"};
        ObjectMapper mapper = new ObjectMapper();
        String json = JacksonUtil.toJson(object);
        JsonNode root = mapper.readTree(json);

        for (String arg : args) {
            List<JsonNode> nodes = root.findParents(arg);
            for (JsonNode node : nodes) {
                String text = node.get(arg).asText();
                String maskedText = mask(arg, text, level);
                ((ObjectNode) node).put(arg, maskedText);
            }
        }

        return mapper.convertValue(root, object.getClass());
    }

    private static String mask(String arg, String input, Integer level) {
        String output;
        switch (arg) {
            case "mobile":
                output = maskMobile(input, level);
                break;
            case "address":
                output = maskAddress(input, level);
                break;
            case "detail":
                output = maskDetail(input, level);
                break;
            default:
                output = "";
        }
        return output;
    }

    private static String maskAddress(String address, Integer level) {
        String output;
        switch (level) {
            case 2:
                output = address;
                break;
            case 1:
                StringBuilder stringBuilder = new StringBuilder(address);
                int beginIndex = Math.min(3, stringBuilder.length() - 1);
                for (int i = beginIndex; i < stringBuilder.length(); i++) {
                    stringBuilder.setCharAt(i, '*');
                }
                output = stringBuilder.toString();
                break;
            case 0:
            default:
                output = null;
        }
        return output;
    }

    private static String maskMobile(String mobile, Integer level) {
        String output;
        switch (level) {
            case 2:
                output = mobile;
                break;
            case 1:
                StringBuilder stringBuilder = new StringBuilder(mobile);
                stringBuilder.replace(3, 7, "****");
                output = stringBuilder.toString();
                break;
            case 0:
            default:
                output = null;
        }
        return output;
    }

    private static String maskDetail(String detail, Integer level) {
        String output;
        switch (level) {
            case 2:
                output = detail;
                break;
            case 1:
                StringBuilder stringBuilder = new StringBuilder(detail);
                int beginIndex = Math.min(3, stringBuilder.length() - 1);
                for (int i = beginIndex; i < stringBuilder.length(); i++) {
                    stringBuilder.setCharAt(i, '*');
                }
                output = stringBuilder.toString();
                break;
            case 0:
            default:
                output = null;
        }
        return output;
    }
}
