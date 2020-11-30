package common.exception;

/**
 * @author valor
 */
public class BreakException extends RuntimeException {

    /**
     * 页面转跳参数
     */
    private final int code;

    public BreakException(String message) {
        this(message, 0);
    }

    public BreakException(String message, int code) {
        super(message);

        this.code = code;
    }

    public BreakException(Throwable cause) {
        this(cause, 0);
    }

    public BreakException(Throwable cause, int code) {
        super(cause);

        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
