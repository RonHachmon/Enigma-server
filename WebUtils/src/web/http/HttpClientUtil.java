package web.http;

import okhttp3.*;

import java.io.IOException;
import java.util.function.Consumer;

public class HttpClientUtil {

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void generateNewSession() {
        HTTP_CLIENT =        new OkHttpClient.Builder()
                .cookieJar(simpleCookieManager)
                .followRedirects(false)
                .build();
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runBlocking(String finalUrl) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        try (Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute()) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void runAsyncWithBody(String finalUrl, Callback callback, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(requestBody)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
