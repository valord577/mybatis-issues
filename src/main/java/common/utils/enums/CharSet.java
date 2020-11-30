package common.utils.enums;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author valor.
 */
public enum CharSet {

    UTF_8("utf-8", StandardCharsets.UTF_8),
    GB18030("gb18030", Charset.forName("GB18030")),
    ;

    private final String name;

    private final Charset charset;

    CharSet(String name, Charset charset) {
        this.name = name;
        this.charset = charset;
    }

    public String getName() {
        return this.name;
    }

    public Charset getCharset() {
        return this.charset;
    }

}
