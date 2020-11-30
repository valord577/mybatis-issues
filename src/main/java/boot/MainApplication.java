package boot;

import boot.args.Argument;
import boot.conf.base.VertxConf;
import boot.conf.props.ApiServer;
import boot.verticle.ApiVerticle;
import boot.verticle.SysVerticle;
import common.utils.enums.CharSet;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author valor.
 */
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) throws Exception {

        ckArgs(args);

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(SysVerticle::new, VertxConf.sysDeploymentOptions());

        /* ----- deploy ----- */
        ApiServer api = ApiServer.instance();
        vertx.deployVerticle(ApiVerticle::new, VertxConf.newDeploymentOptions(api.getWorker()));
    }

    /**
     * 处理参数 {@link boot.args.Argument}
     *
     *   --active=starter / --active=tester
     */
    private static void ckArgs(String[] args) throws Exception {
        for (String arg : args) {
            byte[] bytes = arg.getBytes(CharSet.UTF_8.getCharset());
            int length = bytes.length;
            if (length < 4) {
                log.warn("Illegal Arg: '{}'", arg);
                continue;
            }
            if (bytes[0] != '-' || bytes[1] != '-') {
                log.warn("Illegal Arg: '{}'", arg);
                continue;
            }

            int index = 0;
            for (int i = 2; i < length; i++) {
                if (bytes[i] == '=') {
                    index = i;
                    break;
                }
                if (i + 1 == length) {
                    index = length;
                }
            }
            if (index <= 2) {
                log.warn("Empty Key of Arg: '{}'", arg);
                continue;
            }
            if (index >= length - 1) {
                log.warn("Empty Value of Arg: '{}'", arg);
                continue;
            }

            int kLen = index - 2;
            int vLen = length - 1 - index;
            byte[] k = new byte[kLen];
            byte[] v = new byte[vLen];
            System.arraycopy(bytes, 2, k, 0, kLen);
            System.arraycopy(bytes, index + 1, v, 0, vLen);

            String key = new String(k, CharSet.UTF_8.getCharset());
//            String value = new String(v);

            boolean done = Argument.deal(key, v);
            if (!done) {
                log.warn("Illegal Arg: '{}'", arg);
            }
        }

    }
}
