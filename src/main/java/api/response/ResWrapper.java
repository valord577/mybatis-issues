package api.response;

import common.utils.jackson.JsonMarker;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;

/**
 * @author valor.
 */
public class ResWrapper {

    protected static final JsonMarker jsonMarker = JsonMarker.instance();

    private ResWrapper() { }

    public static <T> void asJson(HttpServerResponse response, T data) {
        setJsonType(response);
        response.end(toJson(data));
    }

    public static void asJson(HttpServerResponse response, String msg) {
        setJsonType(response);
        response.end(toJson(msg));
    }

    public static <T> void asJson(HttpServerResponse response, T data, String msg) {
        setJsonType(response);
        response.end(toJson(data, msg));
    }

    private static void setJsonType(HttpServerResponse response) {
        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    private static <T> String toJson(T data) {

        Callback<T> callback = Callback.<T>success().setData(data);

        String res;
        try {
            res = jsonMarker.write(callback);
        } catch (Exception ex) {
            res = ex.getMessage();
        }
        return res;
    }

    private static String toJson(String msg) {

        @SuppressWarnings("rawtypes")
        Callback callback = Callback.success().setMsg(msg);

        String res;
        try {
            res = jsonMarker.write(callback);
        } catch (Exception ex) {
            res = ex.getMessage();
        }
        return res;
    }

    private static <T> String toJson(T data, String msg) {

        Callback<T> callback = Callback.<T>success().setData(data).setMsg(msg);

        String res;
        try {
            res = jsonMarker.write(callback);
        } catch (Exception ex) {
            res = ex.getMessage();
        }
        return res;
    }

}
