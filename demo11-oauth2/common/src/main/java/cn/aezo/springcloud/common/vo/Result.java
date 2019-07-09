package cn.aezo.springcloud.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class Result<T> implements Serializable {
    private static final String Meta_Status_Success = "success";
    private static final String Meta_Status_Error = "error";

    private String metaStatus;
    private String metaMessage;
    private Map<String, Object> context;
    private T data;
    private Integer metaCode;
    private List metaErrors;

    public static Result success() {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Success;
        return result;
    }

    public static Result success(String metaMessage) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Success;
        result.metaMessage = metaMessage;
        return result;
    }

    public static Result success(Map<String, Object> context) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Success;
        result.context = context;
        return result;
    }

    public static <T> Result success(T data) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Success;
        result.data = data;
        return result;
    }

    public static Result failure() {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        return result;
    }

    public static Result failure(String metaMessage) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        result.metaMessage = metaMessage;
        return result;
    }

    public static Result failure(String metaMessage, Map<String, Object> context) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        result.metaMessage = metaMessage;
        result.context = context;
        return result;
    }

    public static <T> Result failure(String metaMessage, T data) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        result.metaMessage = metaMessage;
        result.data = data;
        return result;
    }

    public static Result failure(String metaMessage, List metaErrors, Map<String, Object> context) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        result.metaMessage = metaMessage;
        result.metaErrors = metaErrors;
        result.context = context;
        return result;
    }

    public static Result failure(Integer metaCode, String metaMessage, List metaErrors, Map<String, Object> context) {
        Result result = new Result();
        result.metaStatus = Result.Meta_Status_Error;
        result.metaCode = metaCode;
        result.metaMessage = metaMessage;
        result.metaErrors = metaErrors;
        result.context = context;
        return result;
    }

    public static boolean isSuccess(Result result) {
        return (result != null && Result.Meta_Status_Success.equals(result.getMetaStatus()));
    }

    public static boolean isError(Result result) {
        return !isSuccess(result);
    }
}