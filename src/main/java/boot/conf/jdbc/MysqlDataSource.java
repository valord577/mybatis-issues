package boot.conf.jdbc;

import boot.conf.props.DataBase;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author valor.
 */
public class MysqlDataSource extends UnpooledDataSourceFactory {

    private static final HikariDataSource hikariDataSource = HikariConf.load(DataBase.instance());

    public MysqlDataSource() {
        this.dataSource = hikariDataSource;
    }

    public static HikariDataSource dataSource() {
        return hikariDataSource;
    }
}
