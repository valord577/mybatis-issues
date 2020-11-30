package common.exception;

import api.response.Callback;
import common.utils.jackson.JsonMarker;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author valor.
 */
public class HandleThrowable {

    private static final Logger log = LoggerFactory.getLogger(HandleThrowable.class);

    protected static final JsonMarker jsonMarker = JsonMarker.instance();

    private HandleThrowable() { }

    public static void handle(RoutingContext context) {
        Throwable e = context.failure();
        HttpServerRequest request = context.request();

        StringBuilder builder = new StringBuilder(64);
        builder.append(request.absoluteURI()).append(" -- > ");

        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement element : trace) {
            builder.append(element.getFileName())
                    .append("[").append(element.getLineNumber()).append("]")
                    .append("&");
        }
        builder.deleteCharAt(builder.length() - 1);

        String message = e.getMessage();
        String value = builder.toString();
        log.error("全局错误处理: \n Cause: {} \n Value: {}", message, request.absoluteURI());

        System.err.println(e.toString());
        for (StackTraceElement element : trace) {
            String traceMsg = builder.delete(0, builder.length())
                    .append("\tat ").append(element).toString();

            System.err.println(traceMsg);
        }

        Callback<String> callback;
        if (e instanceof BreakException) {
            int code = ((BreakException) e).getCode();
            callback = Callback.<String>fail().setMsg(message).setData(value).setCode(code);
        } else if (e instanceof PassException) {
            int code = ((PassException) e).getCode();
            callback = Callback.<String>fail().setMsg(message).setData(value).setCode(code);
        } else {
            callback = Callback.<String>fail().setMsg(message).setData(value);
        }

        String res;
        try {
            res = jsonMarker.write(callback);
        } catch (Exception ex) {
            res = ex.getMessage();
        }

        // from vert.x 3.9.3
        // change the HTTP code of Business Exceptions to 200.
        int httpCode;
        if (e instanceof BreakException) {
            httpCode = 500;
        } else if (e instanceof PassException) {
            httpCode = 200;
        } else {
            httpCode = context.statusCode();
            if (httpCode < 0) {
                httpCode = 200;
            }
        }

        HttpServerResponse response = context.response();
        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatusCode(httpCode);
        response.end(res);
    }
}
