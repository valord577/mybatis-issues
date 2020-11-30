package boot.conf.props;

import boot.conf.PropLoader;

/**
 * @author valor.
 */
public class ApiServer {

    private final boolean ssl;

    private final String host;

    private final int port;

    private final String key;

    private final String pem;

    private final int worker;

    public ApiServer() {
        this.ssl = false;
        this.host = "[::]";
        this.port = 63340;
        this.key = "";
        this.pem = "";
        this.worker = 8;
    }

    public static ApiServer instance() {
        return PropLoader.getProps(ApiServer.class);
    }

    public boolean isSsl() {
        return ssl;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getKey() {
        return key;
    }

    public String getPem() {
        return pem;
    }

    public int getWorker() {
        return worker;
    }
}
