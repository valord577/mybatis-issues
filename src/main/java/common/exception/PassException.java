package common.exception;

/**
 * @author valor
 */
public class PassException extends RuntimeException {

    /**
     * 页面转跳参数
     */
    private final int code;

    public PassException(String message) {
        this(message, 0);
    }

    public PassException(String message, int code) {
        super(message);

        this.code = code;
    }

    public PassException(Throwable cause) {
        this(cause, 0);
    }

    public PassException(Throwable cause, int code) {
        super(cause);

        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
