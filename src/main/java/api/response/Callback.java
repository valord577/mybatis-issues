package api.response;

/**
 * @author valor.
 */
public class Callback<T> {

    /**
     * 页面转跳参数
     */
    private int code;

    /**
     * 返回的消息
     */
    private String message;

    /**
     * 返回的数据
     */
    private T value;

    private Callback(boolean result) {
        this.message = "";
        this.code = result ? 1 : 0;
    }

    public static <T> Callback<T> success() {
        return new Callback<>(true);
    }

    public static <T> Callback<T> fail() {
        return new Callback<>(false);
    }

    public Callback<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public Callback<T> setMsg(String msg) {
        this.message = msg;
        return this;
    }

    public Callback<T> setData(T data) {
        this.value = data;
        return this;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        if (null == this.message) {
            this.message = "";
        }
        return this.message;
    }

    public T getValue() {
        return this.value;
    }
}
