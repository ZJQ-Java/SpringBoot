package com.qiu.response;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum ResultCodeEnum implements IResultCode {
    /**
     * 0：成功
     * 1xxx： 系统类、参数、未登录权限
     */
    SUCCESS(0, "成功"),
    SYSTEM_ERROR(1000, "服务器忙"),
    PARAMS_INVALID(1200, "参数错误"),
    PERMISSION_UNAUTHORIZED(1401, "未登录，需要登录"),
    NO_INTERFACE_PERMISSION(1402, "用户不具该接口权限"),

    /**
     * 数据库 80xx mysql数据库更新失败错误码
     */
    UPDATE_USER_ERROR(8000,"更新用户信息失败"),
    ADD_USER_ERROR(8001,"添加用户信息失败"),

    UN_REALIZE_ERROR(9999, "未实现");

    private final Integer code;
    private final String  message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
