package com.qg.bank.pojo;

/**
 * 操作结果类，用于封装操作结果和错误信息
 */
public class Result {
    private boolean success;      // 操作是否成功
    private String message;       // 操作消息
    private Object data;          // 操作数据

    // 构造方法
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 静态工厂方法
    public static Result success(String message) {
        return new Result(true, message);
    }

    public static Result success(String message, Object data) {
        return new Result(true, message, data);
    }

    public static Result fail(String message, String eMessage) {
        return new Result(false, message, eMessage);
    }
    public static Result fail(String message) {
        return new Result(false, message);
    }

    // getter方法
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
