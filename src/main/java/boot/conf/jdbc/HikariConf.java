package boot.conf.jdbc;

import boot.conf.props.DataBase;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author valor.
 */
public class HikariConf {

    public static HikariDataSource load(DataBase props) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(props.getDriver());
        config.setJdbcUrl("jdbc:mysql://" + props.getHost() + ":" + props.getPort() + "/" + props.getSchema());
//        config.setSchema(props.getSchema());
        config.setUsername(props.getUsername());
        config.setPassword(props.getPassword());
        config.setPoolName("hikariCp");
        config.setMaximumPoolSize(props.getMaxPool());
        config.setMinimumIdle(props.getMinIdle());
        // 一个连接idle状态的最大时长(毫秒) 超时则被释放(retired)
        config.setIdleTimeout(180_000L);
        // 一个连接的生命时长(毫秒) 超时而且没被使用则被释放(retired)
        config.setMaxLifetime(600_000L);
        config.setReadOnly(false);
        config.setConnectionTimeout(3_000L);
        config.addDataSourceProperty("useSSL", "false");
        config.addDataSourceProperty("useAffectedRows", "true");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.addDataSourceProperty("connectTimeout", "3000");
        config.addDataSourceProperty("socketTimeout", "2000");

        // 是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("cachePrepStmts", "true");
        // 连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSize", "512");
        // 单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        // 新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useServerPrepStmts", "true");

        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        return new HikariDataSource(config);
    }

}
