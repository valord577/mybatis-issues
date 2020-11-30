package common.utils.enums;

/**
 * @author valor.
 */
public enum NewLine {

    CR("\r"),
    LF("\n"),
    CRLF("\r\n");

    private final String value;

    NewLine(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
