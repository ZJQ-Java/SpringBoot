package com.qiu.response;

import lombok.Data;

@Data
public class Result<T> {

    int code;

    String message;

    T data;

    String traceMsg;

    public static final <T> Result<T> withOutData(Result result) {
        return new Result<T>(result.getCode(), result.getMessage(),result.getTraceMsg(), null);
    }

    public static final <T> Result<T> success() {
        return new Result<>();
    }

    public static final <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static final <T> Result<T> failure() {
        return failure(ResultCodeEnum.SYSTEM_ERROR);
    }

    /*public static final <T> Result<T> failure(BizException we) {
        if (we != null) {
            return new Result<T>(we.getCode(), we.getMessage(), we.getTraceMsg(), (T) we.getData());
        }
        return failure(ResultCodeEnum.SYSTEM_ERROR);
    }*/

    public static final <T> Result<T> failure(IResultCode we) {
        if (we != null) {
            return new Result<T>(we.getCode(), we.getMessage());
        }
        return failure(ResultCodeEnum.SYSTEM_ERROR);
    }


    public boolean isSuccess() {
        return code == ResultCodeEnum.SUCCESS.getCode();
    }

    public Result() {
        super();
    }

    public Result(T data) {
        super();
        this.data = data;
    }

    public Result(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, String traceMsg, T data) {
        super();
        this.code = code;
        this.message = message;
        this.traceMsg = traceMsg;
        this.data = data;
    }

}
