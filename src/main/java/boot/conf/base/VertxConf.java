package boot.conf.base;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;

/**
 * @author valor.
 */
public class VertxConf {

    private VertxConf() { }

    public static DeploymentOptions newDeploymentOptions(final int workerPoolSize) {
        var options = new DeploymentOptions();
        options.setWorker(true);
        options.setWorkerPoolSize(workerPoolSize);
        return options;
    }

    public static DeploymentOptions sysDeploymentOptions() {
        var options = new DeploymentOptions();
        options.setWorker(false);
        options.setWorkerPoolSize(1);
        return options;
    }

    public static HttpServerOptions newHttpServerOptions(final String host, final int port) {
        var options = new HttpServerOptions();
        options.setHost(host);
        options.setPort(port);
        options.setSsl(false);
        return options;
    }

    public static HttpServerOptions sslHttpServerOptions(final String host, final int port,
                                                         final String key, final String pem) {
        var options = new HttpServerOptions();
        options.setHost(host);
        options.setPort(port);
        options.setSsl(true);
        options.setUseAlpn(true);
        options.setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath(key).setCertPath(pem));
        return options;
    }
}
