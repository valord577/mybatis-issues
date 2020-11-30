package boot.conf.props;

import boot.conf.PropLoader;

/**
 * @author valor.
 */
public class SysServer {

    private final String host;

    private final int port;

    public SysServer() {
        this.host = "[::1]";
        this.port = 63340;
    }

    public static SysServer instance() {
        return PropLoader.getProps(SysServer.class);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
