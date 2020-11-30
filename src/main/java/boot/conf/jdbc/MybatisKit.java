package boot.conf.jdbc;

import common.utils.IoKit;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * @author valor.
 */
public class MybatisKit {

    private static volatile boolean built = false;

    private static volatile SqlSessionFactory sqlSessionFactory;

    private static final String _mybatis_xml = "mybatis.xml";

    private MybatisKit() { }

    public static void build() {
        if (!built) {
            synchronized (MybatisKit.class) {
                if (!built) {
                    InputStream in = ClassLoader.getSystemResourceAsStream(_mybatis_xml);
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
                    IoKit.close(in);

                    built = true;
                }
            }
        }
    }

    private static void isBuilt() {
        if (!built) {
            throw new UnsupportedOperationException("Please invoke 'MybatisKit#build' first.");
        }
    }

    /* SqlSession */

    public static SqlSession openSqlSession(boolean autoCommit) {
        isBuilt();
        return sqlSessionFactory.openSession(autoCommit);
    }

    public static void closePool() {
        if (built) {
            MysqlDataSource.dataSource().close();
        }
    }
}
