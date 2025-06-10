package tpp.easy.learning.data.model.api;

public class ResponseWrapper<T> {
    private boolean result;
    private T data;
    private String message;
    private String code;
    private String desc;

    public boolean isResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
