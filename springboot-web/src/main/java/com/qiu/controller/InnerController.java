package com.qiu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.*;
import com.qiu.anotation.MethodInfo;
import com.qiu.anotation.ParameterInfo;
import com.qiu.config.ToolsConfig;
import com.qiu.response.Result;
import com.qiu.util.DateUtil;
import com.qiu.util.HttpApi;
import com.qiu.util.HttpTool;
import com.qiu.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 内部使用的接口，不对用户开放,只允许通过ip访问
 */
@RequestMapping("/inner")
@Controller
@Slf4j
public class InnerController {

    private static final String TOOLS_URI_PRE = "/inner/tools/";
    @Autowired
    ToolsConfig toolsConfig;
    // --------------- 简单验证 ----------------

    /**
     * 页面访问key，用于简单验证
     */

    // --------------- http Digest 验证  ----------------


    // http Digest, 用户列表
    static Map<String, String> DIGEST_AUTH_USER_LIST = new HashMap<>();

    static {
        DIGEST_AUTH_USER_LIST.put("user1", "pswd");
//        if (!YunEnvUtil.isProductEnv()) {
//            String defUser = HttpTool.HttpDigestAuthUtil.DEF_USER;
//            DIGEST_AUTH_USER_LIST.put(defUser, defUser);
//        }
    }


    // --------------- 安全限制 -----------------

    private boolean hostIsIp(HttpServletRequest request) {
        System.out.println(request.getServerName());
        boolean contains = IPUtil.getServerIpListForLinux().contains(request.getServerName());
        boolean b = request.getServerPort() > 8000;
        return contains && b;
    }

    private boolean validAccessKey(HttpServletRequest request) {
        return toolsConfig.getHttpInnerAccessKey().equals(request.getParameter("key"));
    }

    /**
     * 列表引导
     */
    @RequestMapping("/tools")
    public String tools(HttpServletRequest request, HttpServletResponse response, Model model) {
        if (/*!hostIsIp(request) ||*/ !validAccessKey(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        String accessKey = null;
        if (toolsConfig.getEnableToolPageHttpDigestAuth()) {
            String loginUser = HttpTool.HttpDigestAuthUtil.getLoginUser(request, DIGEST_AUTH_USER_LIST);
            if (loginUser == null) {
                HttpTool.HttpDigestAuthUtil.responseDigestAuth(response);
                log.info("return status:" + response.getStatus());
                return null;
            }
            accessKey = HttpTool.HttpDigestAuthUtil.getAccessKey(loginUser);
        } else {
            accessKey = toolsConfig.getHttpInnerAccessKey();
        }
        model.addAttribute("functionList", HttpTool.findAll());
        model.addAttribute("accessKey", accessKey);
        return "tools";
    }

    /**
     * 执行方法
     */
    @RequestMapping({"/tools/*"})
    @ResponseBody
    public <T> Result<T> mIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
        if (/*!hostIsIp(request) ||*/ !validAccessKey(request)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        // 获取方法名
        String uri = request.getRequestURI();
        String functionName = TOOLS_URI_PRE.equals(uri) ? ""
                : request.getRequestURI().substring(TOOLS_URI_PRE.length());

        // 返回方法列表
//        if ("list".equals(functionName) || StringUtils.isEmpty(functionName)) {
//            return (Object) tools(request, response, model);
//        }
//        else {// 执行方法
            String user = "";
            if (toolsConfig.getEnableToolPageHttpDigestAuth()) {
                String accessKey = request.getParameter("key");
                user = HttpTool.HttpDigestAuthUtil.isValidAccessKey(accessKey);
                if (user == null) {
                    return responseJSON(401, "请刷新页面重新登录", null);
                }
            }
            log.info("function: " + functionName + ", user: " + user);
            // 获得要执行的方法的信息
            HttpApi httpApi = HttpTool.find(functionName);
            if (httpApi == null) {
                return responseJSON(404, "方法不存在", null);
            }
            // 生产环境不能执行修改操作
//            if (YunEnvUtil.isProductEnv() && (functionName.startsWith("add") || functionName.startsWith("del")
//                    || functionName.startsWith("save") || functionName.startsWith("update"))) {
//                return responseJSON(401, "不能在生产环境操作此方法", null);
//            }
            // 执行
            Object ret = null;
            try {
                if (httpApi.getParams() == null || httpApi.getParams().isEmpty()) {
                    ret = httpApi.getMethod().invoke(InnerController.class);
                } else {
                    Object[] params = new Object[httpApi.getParams().size()];
                    for (int i = 0; i < httpApi.getParams().size(); i++) {
                        HttpApi.ParamInfo paramInfo = httpApi.getParams().get(i);
                        try {
                            params[i] = JSON
                                    .parseObject(request.getParameter(paramInfo.getName()), paramInfo.getClazz());
                            log.info(
                                    "param: index=" + i + ", name=" + paramInfo.getName() + ", value=" + params[i]);
                            if (paramInfo.isMust() && params[i] == null) {
                                String warnInfo = paramInfo.getName() + " is null";
                                log.error(warnInfo);
                                return responseJSON(0, warnInfo, null);
                            }
                        } catch (Exception e) {
                            String warnInfo = paramInfo.getName() + " is invalid, error: " + e.getMessage();
                            log.error(warnInfo);
                            return responseJSON(0, warnInfo, null);
                        }
                    }
                    ret = httpApi.getMethod().invoke(InnerController.class, params);
                    log.info("debug ret: " + JSON.toJSONString(ret));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return responseJSON(0, "invalid params", null);
            }
            return (Result<T>) responseJSON(0, "", ret);

    }

    private static <T> Result<T> responseJSON(int code, String msg, T data) {
        if (data != null && data instanceof String) {
            try {
                String json = (String) data;
                if (!json.isEmpty() && (json.charAt(0) == '{' || json.charAt(0) == '[')) {
                    data = (T)JSON.parse(json);
                }
            } catch (Exception e) {
            }
        }
//        JSONActionResult jsonResult = new JSONActionResult(code, msg, data);
//        return jsonResult;
        return Result.success(data);
    }


    /*private static String responseHTML(Model beatContext, String view, ViewEntry... viewEntries) {
        if (viewEntries != null) {
            for (ViewEntry viewEntry : viewEntries) {
                beatContext.addAttribute(viewEntry.key, viewEntry.val);
            }
        }
        return view;
    }

    private class ViewEntry {
        public String key;
        public Object val;

        public ViewEntry(String key, Object val) {
            super();
            this.key = key;
            this.val = val;
        }
    }*/

    // ----- 以下为被调用方法列表,返回类型为string时会尝试转为json -----
    @MethodInfo("001) 获取IM token")
    public static String getImToken(
            @ParameterInfo(desc = "用户id", parameter = "userId", must = true) String userId,
            @ParameterInfo(desc = "用户source", parameter = "userSource", must = true) int userSource) throws Exception {
        return "aaa";
    }

    @MethodInfo("002) 获取IM token")
    public static String getImToken2(
            @ParameterInfo(desc = "用户id", parameter = "userId", must = true) String userId,
            @ParameterInfo(desc = "用户source", parameter = "userSource", must = true) int userSource) throws Exception {
        return "aaa";
    }
   /* 
    

    @MethodInfo("002) 列出所有业务线信息")
    public static List<BizLineInfo> getBizList() throws Exception {
        return ServiceFactory.getProductService().getBizLineList(new Page(1, 50));
    }

    @MethodInfo("003) 获取业务线AI配置")
    public static BizLineAiConfig getBizAiConfig(
            @ParameterInfo(desc = "业务线ID", parameter = "bizLineId", must = true) int bizLineId)
            throws Exception {
        return ServiceFactory.getProductService().getBizLineInfo(bizLineId).getConfigInfo().getAiConfig();
    }

    @MethodInfo("004) 设置业务线AI配置")
    public static JSONObject updateBizAiConfig(
            @ParameterInfo(desc = "业务线ID", parameter = "bizLineId", must = true) int bizLineId,
            @ParameterInfo(desc = "是否开启：ENABLE / DISABLE", parameter = "bizLineAiType", must = true) String bizLineAiType,
            @ParameterInfo(desc = "开始时间：hh:mm", parameter = "startTime", must = false) String startTime,
            @ParameterInfo(desc = "结束时间：hh:mm", parameter = "endTime", must = false) String endTime)
            throws Exception {
        JSONObject result = new JSONObject();
        BizLineAiConfig oldConfig = ServiceFactory.getProductService().getBizLineInfo(bizLineId).getConfigInfo()
                .getAiConfig();
        result.put("oldConfig", oldConfig);
        AiTimeSetting aiTimeSetting = new AiTimeSetting();
        aiTimeSetting.setEndTime(ObjectUtil.notNull(endTime, oldConfig.getTimeSetting().getEndTime()));
        aiTimeSetting.setStartTime(ObjectUtil.notNull(startTime, oldConfig.getTimeSetting().getStartTime()));
        if (!aiTimeSetting.isTimeValid()) {
            result.put("warn", "invalid time param");
            return result;
        }
        BizLineAiConfig updateInfo = new BizLineAiConfig();
        updateInfo.setType(BizLineAiType.valueOf(bizLineAiType));
        updateInfo.setTimeSetting(aiTimeSetting);
        result.put("booleanResult", ServiceFactory.getProductService().updateBizLineAiConfig(bizLineId, updateInfo));
        result.put("newAIConfig", ServiceFactory.getProductService().getBizLineInfo(bizLineId).getConfigInfo()
                .getAiConfig());
        return result;
    }*/

    static SerializeConfig DATE_FORMAT_CONFIG = new SerializeConfig();

    static {
        try {
            DATE_FORMAT_CONFIG.put(Long.class, new ObjectSerializer() {
                @Override
                public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                                  int features) throws IOException {
                    SerializeWriter out = serializer.out;
                    if (object == null) {
                        out.writeNull(SerializerFeature.WriteNullNumberAsZero);
                        return;
                    }
                    if (object != null && fieldName.toString().toLowerCase().contains("time")
                            && (fieldType == Long.class || fieldType == Long.TYPE)) {
                        try {
                            out.writeString(DateUtil.formatTimeStamp((Long) object, DateUtil.NORM_DATETIME_MS_FORMAT));
                            return;
                        } catch (Exception e) {
                        }
                    }
                    LongCodec.instance.write(serializer, object, fieldName, fieldType, features);
                }
            });
        } catch (Exception e) {
        }
    }

}
