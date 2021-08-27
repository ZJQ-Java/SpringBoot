package com.qiu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "my.web.tools")
@Component
@Data
public class ToolsConfig {
    private String  httpInnerAccessKey;
    // 是否开启 http Digest 登录
    private Boolean enableToolPageHttpDigestAuth;


}
