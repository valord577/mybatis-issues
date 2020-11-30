package api.controller;

import common.exception.HandleThrowable;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author valor.
 */
public abstract class TestOps {

    private final Router router;

    TestOps(Vertx vertx) {
        this.router = Router.router(vertx);
    }

    private static volatile TestOps ops;

    public static Router create(Vertx vertx) {
        if (null == ops) {
            synchronized (TestOps.class) {
                if (null == ops) {
                    ops = new TestOpsService(vertx);
                    ops.init();
                }
            }
        }
        return ops.getRouter();
    }

    private void init() {

        /* fail to rollback */
        ops.getRouter().route(HttpMethod.GET, "/rollback/fail")
                .failureHandler(HandleThrowable::handle)
                .blockingHandler(this::fail);

        /* success to rollback */
        ops.getRouter().route(HttpMethod.GET, "/rollback/ok")
            .failureHandler(HandleThrowable::handle)
            .blockingHandler(this::ok);

    }

    abstract void fail(RoutingContext context);

    abstract void ok(RoutingContext context);

    Router getRouter() {
        return this.router;
    }
}
