package boot.verticle;

import api.controller.TestOps;
import boot.conf.base.VertxConf;
import boot.conf.props.ApiServer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author valor.
 */
public class ApiVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(ApiVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        Router testRouter = TestOps.create(super.vertx);

        Router main = Router.router(super.vertx);
        main.mountSubRouter("/test", testRouter);

        ApiServer apiServer = ApiServer.instance();
        final var host = apiServer.getHost();
        final var port = apiServer.getPort();

        HttpServerOptions httpServerOptions;
        if (apiServer.isSsl()) {
            httpServerOptions = VertxConf.sslHttpServerOptions(host, port, apiServer.getKey(), apiServer.getPem());
        } else {
            httpServerOptions = VertxConf.newHttpServerOptions(host, port);
        }

        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);
        httpServer.requestHandler(main);
        httpServer.listen(res -> {
            if (res.succeeded()) {
                log.info("start '{}' http server bind {}:{}.", ApiVerticle.class.getName(), host, port);
            } else {
                res.cause().printStackTrace();
            }
        });
    }

}
