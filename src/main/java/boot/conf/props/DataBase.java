package boot.conf.props;

import boot.conf.PropLoader;

/**
 * @author valor.
 */
public class DataBase {
    // ;[database]
    private final String host;
    private final String port;
    private final String schema;
    private final String driver;
    private final String username;
    private final String password;
    private final int minIdle;
    private final int maxPool;

    public DataBase() {
        // ;[database]
        this.host = "";
        this.port = "";
        this.schema = "";
        this.driver = "";
        this.username = "";
        this.password = "";
        this.minIdle = 1;
        this.maxPool = 1;
    }

    public static DataBase instance() {
        return PropLoader.getProps(DataBase.class);
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getSchema() {
        return schema;
    }

    public String getDriver() {
        return driver;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxPool() {
        return maxPool;
    }
}
