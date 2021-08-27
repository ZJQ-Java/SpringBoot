package com.qiu.util;

import com.github.pagehelper.util.StringUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.qiu.anotation.MethodInfo;
import com.qiu.anotation.ParameterInfo;
import com.qiu.controller.InnerController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpTool {

    private static Log logger = LogFactory.getLog(HttpTool.class);

    static Map<String, HttpApi> HTTP_API_LIST = Collections.emptyMap();

    static {
        try {
            logger.info("init internal http api");
            Map<String, HttpApi> apiList = new TreeMap<>();
            Method[] methodList = InnerController.class.getMethods();
            if (methodList != null) {
                for (Method method : methodList) {
                    try {
                        int modifiers = method.getModifiers();
                        if (method.isAnnotationPresent(MethodInfo.class) && Modifier.isStatic(modifiers)
                                && Modifier.isPublic(modifiers)) {
                            HttpApi httpApi = new HttpApi();
                            httpApi.setName(method.getName());
                            httpApi.setDesc(ObjectUtil.notNull(method.getAnnotation(MethodInfo.class).value(), method.getName()));
                            httpApi.setShowType(method.getAnnotation(MethodInfo.class).showType());
                            httpApi.setMethod(method);
                            Parameter[] params = method.getParameters();
                            if (params != null) {
                                for (Parameter param : params) {
                                    String paramName = param.getName();
                                    String desc = param.getName();
                                    boolean isMust = true;
                                    if (param.isAnnotationPresent(ParameterInfo.class)) {
                                        paramName = param.getAnnotation(ParameterInfo.class).parameter();
                                        desc = param.getAnnotation(ParameterInfo.class).desc();
                                        if (param.getType().isPrimitive()) {
                                            isMust = true;
                                        } else {
                                            isMust = param.getAnnotation(ParameterInfo.class).must();
                                        }
                                    }
                                    httpApi.addParams(new HttpApi.ParamInfo(param.getType(), paramName, desc, isMust));
                                }
                            }
                            apiList.put(httpApi.getDesc(), httpApi);
                        }
                    } catch (Exception e) {
                        logger.warn(method.getName() + ", " + e.getMessage());
                    }
                }
            }
            Map<String, HttpApi> sortedApiList = new LinkedHashMap<>();
            apiList.values().stream().forEach(api -> sortedApiList.put(api.getName(), api));
            HTTP_API_LIST = Collections.unmodifiableMap(sortedApiList);
            logger.info("internal http api list: " + HTTP_API_LIST.keySet());
        } catch (Exception e) {
            logger.error("init internal http api failed: " + e.getMessage(), e);
        }
    }

    public static void main(String... strings) {
        System.out.println(HTTP_API_LIST);
    }

    public static HttpApi find(String functionName) {
        return HTTP_API_LIST.get(functionName);
    }

    public static List<HttpApi> findAll() {
        return new ArrayList<>(HTTP_API_LIST.values());
    }

    public static class HttpDigestAuthUtil {

        static Log logger = LogFactory
                .getLog(HttpDigestAuthUtil.class);

        static final Cache<String, Boolean> OPAQUE_LIST = CacheBuilder.newBuilder().maximumSize(300)
                .expireAfterWrite(1, TimeUnit.DAYS).build();

        // http Digest 登录通过后，写到html代码上的认证信息
        static final Cache<String, String> DIGEST_AUTH_ACCESS_LIST = CacheBuilder.newBuilder().maximumSize(300)
                .expireAfterWrite(1, TimeUnit.DAYS).build();

        public static final String DEF_USER = "guest";

//        static String REALM = "wis_tool_" + YunEnvUtil.getEnv().toString().toLowerCase();
        static String REALM = "wis_tool_" + "sandbox".toLowerCase();

        static {
//            if (!YunEnvUtil.isProductEnv()) {
                REALM += " login: " + DEF_USER + "/" + DEF_USER;
//            }
        }

        public static String getLoginUser(HttpServletRequest request, Map<String, String> userWithPswdList) {
            try {
                String authenticate = request.getHeader("Authenticate");
                Map<String, String> authenticateParams = getAuthenticateInfo(authenticate);
                if (authenticateParams.isEmpty()) {
                    logger.warn("authenticate is empty");
                    return null;
                }
                String opaque = authenticateParams.get("opaque");
                if (opaque == null || OPAQUE_LIST.getIfPresent(opaque) == null) {
                    logger.warn("invalid opaque, " + authenticate);
                    return null;
                }
                String username = authenticateParams.get("username");
                if (StringUtil.isEmpty(username)) {
                    logger.warn("invalid username, " + authenticate);
                    return null;
                }
                String password = userWithPswdList.get(username);
                if (password == null) {
                    logger.warn("invalid username, " + authenticate);
                    return null;
                }
                String realm = authenticateParams.get("realm");
                if (!REALM.equals(realm)) {
                    logger.warn("invalid realm, " + authenticate);
                    return null;
                }
                String digestResponse = authenticateParams.get("response");
                if (StringUtil.isEmpty(digestResponse)) {
                    logger.warn("invalid response, " + authenticate);
                    return null;
                }
                String _digestResponse = buildResponse(authenticateParams, request.getMethod(), password);
                if (_digestResponse == null) {
                    logger.warn("not support qop, " + authenticate);
                    return null;
                }
                if (!digestResponse.equals(_digestResponse)) {
                    logger.warn("digest response is valid, " + _digestResponse + ", " + authenticate);
                    return null;
                }
                logger.info("http digest auth success, username=" + username
                        + ", uri=" + request.getRequestURL().toString()
                        + ", " + authenticate);
                return username;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        public static void responseDigestAuth(HttpServletResponse response) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("Authenticate", buildAuthHeader());
        }

        public static String getAccessKey(String username) {
            String accessKey = UUID.randomUUID().toString();
            DIGEST_AUTH_ACCESS_LIST.put(accessKey, username);
            return accessKey;
        }

        public static String isValidAccessKey(String accessKey) {
            return DIGEST_AUTH_ACCESS_LIST.getIfPresent(accessKey);
        }

        static String buildResponse(Map<String, String> authenticateParams, String method, String password)
                throws Exception {
            String username = authenticateParams.get("username");
            String nonce = authenticateParams.get("nonce");
            String qop = authenticateParams.get("qop");
            String nc = authenticateParams.get("nc");
            String cnonce = authenticateParams.get("cnonce");
            String uri = authenticateParams.get("uri");
            String ha1 = getMD5(username + ":" + REALM + ":" + password);
            String ha2 = getMD5(method.toUpperCase() + ":" + uri);
            if (StringUtil.isEmpty(qop)) {
                return getMD5(ha1 + ":" + nonce + ":" + ha2);
            } else if ("auth".equals(qop)) {
                return getMD5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2);
            } else {
                return null;
            }
        }

        static String buildAuthHeader() {
            return new StringBuilder("Digest")
                    .append(" realm=\"").append(REALM).append("\"")
                    .append(", qop=\"auth\"")
                    .append(", nonce=\"").append(getUUID()).append("\"")
                    .append(", opaque=\"").append(newOpaque()).append("\"")
                    .append(", algorithm=MD5").toString();
        }

        static String newOpaque() {
            String opaque = getUUID();
            OPAQUE_LIST.put(opaque, true);
            return opaque;
        }

        static String getUUID() {
            return UUID.randomUUID().toString().replace("-", "");
        }

        static Map<String, String> getAuthenticateInfo(String authenticate) {
            if (StringUtil.isEmpty(authenticate) || !authenticate.startsWith("Digest")) {
                return Collections.emptyMap();
            }
            authenticate = authenticate.substring("Digest".length()).trim();
            String[] arr = authenticate.split(",");
            Map<String, String> params = new HashMap<>();
            for (String s : arr) {
                int index = s.indexOf('=');
                if (index > 0) {
                    String k = s.substring(0, index).trim();
                    String v = StringUtils.strip(s.substring(index + 1), "\"\' ");
                    params.put(k, v);
                }
            }
            return params;
        }

        public static void main(String[] args) throws Exception {
            REALM = "Hacking Articles";
            Map<String, String> authenticateInfo = getAuthenticateInfo(
                    "Digest realm=\"Hacking Articles\", nonce=\"58bac26865505\"" +
                            ", uri=\"/auth/02-2617.php\", opaque=\"8d8909139750c6bd277cfe1388314f48\"" +
                            ", qop=auth, nc=00000001, cnonce=\"72ae56dde9406045\" " +
                            ", response=\"ac8e3ecd76d33dd482783b8a8b67d8c1\", username=\"guest\"");
            String s = buildResponse(authenticateInfo, "GET", "guest");
            System.out.println(authenticateInfo);
            System.out.println(s);
            System.out.println(s.equals("ac8e3ecd76d33dd482783b8a8b67d8c1"));
        }

        static String getMD5(String str) throws Exception {
            /** 创建加密对象 */
            MessageDigest md = MessageDigest.getInstance("MD5");
            /** 加密 */
            md.update(str.getBytes(StandardCharsets.UTF_8));
            /** 获取加密后的内容 (16位的字符数组) */
            byte[] md5Bytes = md.digest();
            String res = "";
            /** 把加密后字节数组转化成32位字符串 (把每一位转化成16进制的两位) */
            for (int i = 0; i < md5Bytes.length; i++) {
                int temp = md5Bytes[i] & 0xFF;
                /** 把temp值转化成16进制的两位数，如果不够两位前面补零 */
                if (temp <= 0xF) {
                    res += "0";
                }
                res += Integer.toHexString(temp);
            }
            System.out.println(str + " = " + res);
            return res;
        }

    }

}
