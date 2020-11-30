package api.controller;

import api.response.ResWrapper;
import boot.conf.jdbc.MybatisKit;
import common.exception.BreakException;
import core.mapper.TestMapper;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author valor.
 */
class TestOpsService extends TestOps {

    TestOpsService(Vertx vertx) {
        super(vertx);
    }

    @Override
    void fail(RoutingContext context) {

        SqlSession sqlSession = MybatisKit.openSqlSession(false);
        try {
            TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
            testMapper.insert("name");
            testMapper.insert("12345");

            sqlSession.commit();
        } catch (Throwable e) {
            sqlSession.rollback();
            sqlSession.close();

            throw new BreakException("asd");
        }
        sqlSession.close();

        ResWrapper.asJson(context.response(),  "ok");
    }

    @Override
    void ok(RoutingContext context) {

        SqlSession sqlSession = MybatisKit.openSqlSession(false);
        Connection connection = sqlSession.getConnection();
        Savepoint savepoint;
        try {
            savepoint = connection.setSavepoint();
        } catch (SQLException sqle) {
            throw new BreakException(sqle.getMessage());
        }

        try {
            TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
            testMapper.insert("name");
            testMapper.insert("12345");

            sqlSession.commit();
        } catch (Throwable e) {
            try {
                connection.rollback(savepoint);
                connection.releaseSavepoint(savepoint);
            } catch (SQLException sqle) {
                throw new BreakException(sqle.getMessage());
            }
            sqlSession.close();

            throw new BreakException("asd");
        }
        sqlSession.close();

        ResWrapper.asJson(context.response(),  "ok");
    }

}
