package boot.args;

import boot.conf.PropLoader;
import common.utils.IoKit;
import common.utils.enums.CharSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author valor.
 */
public class Argument {

    private Argument() { }

    private static final Logger log = LoggerFactory.getLogger(Argument.class);

    private enum Args {
        active("加载配置文件") {
            @Override
            public void handle(byte[] e) throws Exception {
                Argument.active(e);
            }
        },

        ;

        Args(String explain) { }

        public abstract void handle(byte[] e) throws Exception;

    }

    public static boolean deal(String key, byte[] value) throws Exception {
        Args args;
        try {
            args = Args.valueOf(key);
        } catch (Throwable e) {
            return false;
        }

        args.handle(value);
        return true;
    }

    /* ----------------------------------------------------- */

    /**
     * 加载配置文件
     */
    private static void active(byte[] value) throws Exception {
        byte[] ext = {'.', 'p', 'r', 'o', 'p'};
        int extLength = ext.length;

        boolean splicer = false;

        int length = value.length;
        if (length < extLength + 1) {
            // ".prop".length()
            splicer = true;
        }

        int i = extLength - 1;
        int j = length - 1;
        while (i >= 0) {
            if (ext[i] != value[j]) {
                splicer = true;
                break;
            }

            i--;
            j--;
        }

        String filename;
        if (splicer) {
            byte[] tmp = new byte[length + extLength];
            System.arraycopy(value, 0, tmp, 0, length);
            System.arraycopy(ext, 0, tmp, length, extLength);
            filename = new String(tmp, CharSet.UTF_8.getCharset());
        } else {
            filename = new String(value, CharSet.UTF_8.getCharset());
        }

        log.info("The following profile(s) will be active: '{}'", filename);

        InputStream in = ClassLoader.getSystemResourceAsStream(filename);
        PropLoader.build(in);
        IoKit.close(in);
    }

}
