package boot.verticle;

import boot.conf.base.VertxConf;
import boot.conf.jdbc.MybatisKit;
import boot.conf.props.SysServer;
import common.exception.HandleThrowable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author valor.
 */
public class SysVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(SysVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(super.vertx);

        /* 关闭服务 */
        router.route("/close")
                .failureHandler(HandleThrowable::handle)
                .handler(this::doClose);

        SysServer sysServer = SysServer.instance();
        final var host = sysServer.getHost();
        final var port = sysServer.getPort();

        var httpServerOptions = VertxConf.newHttpServerOptions(host, port);
        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
        httpServer.requestHandler(router);
        httpServer.listen(res -> {
            if (res.succeeded()) {
                log.info("start '{}' http server bind {}:{}.", SysVerticle.class.getName(), host, port);
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    void doClose(RoutingContext context) {
        MybatisKit.closePool();
        super.getVertx().close(res -> {
            if (res.succeeded()) {
                log.info("Vert.x shutdown successfully.");
            } else {
                if (res.failed()) {
                    context.fail(res.cause());
                }
            }
        });
    }
}
