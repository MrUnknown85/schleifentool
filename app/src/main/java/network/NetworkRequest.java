/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ELschleifentool.network;

/**
 *
 * @author nico
 */
import ELschleifentool.model.BotschutzClickListener;
import ELschleifentool.model.BotschutzInterface;
import ELschleifentool.model.BotschutzParams;
import ELschleifentool.model.BotschutzResponse;
import ELschleifentool.model.ConstructionPage;
import ELschleifentool.model.NavigationPage;
import ELschleifentool.model.PlanetenPage;
import ELschleifentool.model.ProductionPage;
import ELschleifentool.model.IntroPage;
import ELschleifentool.util.Callback;
import ELschleifentool.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRequest implements BotschutzClickListener {

    private ExecutorThread thread;
    private static NetworkRequest instance;
    private final HttpLoggingInterceptor.Level LOG_LEVEL = HttpLoggingInterceptor.Level.NONE;
    private final API api;
    private final OkHttpClient client;
    private final Converter.Factory converter;
    private BotschutzParams latestBotschutzParams = null;

    public static NetworkRequest getNetwork() {
        if (instance == null) {
            instance = new NetworkRequest();
        }
        return instance;
    }

    private NetworkRequest() {
        Map<Type, Converter<ResponseBody, ?>> converters = new HashMap<>();
        converter = new Converter.Factory() {
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return converters.containsKey(type) ? converters.get(type) : super.responseBodyConverter(type, annotations, retrofit);
            }

            public void registerTypeAdapter(Type type, Converter<ResponseBody, ?> converter) {
                converters.put(type, converter);
            }
        };

        converters.put(RechnerResponse.class, new RechnerResponseAdapter());
        converters.put(NavigationPage.class, new NavigationPageAdapter());
        converters.put(IntroPage.class, new IntroPageAdapter());
        converters.put(ProductionPage.class, new ProductionPageAdapter());
        converters.put(PlanetenPage.class, new PlanetenPageAdapter());
        converters.put(ConstructionPage.class, new ConstructionPageAdapter());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(0, TimeUnit.SECONDS);
        builder.writeTimeout(0, TimeUnit.SECONDS);
        builder.readTimeout(0, TimeUnit.SECONDS);
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(LOG_LEVEL));
        builder.addInterceptor(chain -> {
            Request request = chain.request();
            if (latestBotschutzParams != null) {
                Request.Builder requestBuilder = request.newBuilder();
                RequestBody formBody = getBotschutzBody(latestBotschutzParams, request.body());
                request = requestBuilder.post(formBody).build();
                //Log.d("Request " + request.url().toString() + " -> botschutz hinzufügen");
                latestBotschutzParams = null;
            }
            return chain.proceed(request);
        });

        client = builder.build();
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

        Retrofit elRetro = new Retrofit.Builder().baseUrl("http://www.earthlost.de/").client(client)
                .addConverterFactory(converter)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = elRetro.create(API.class);

    }

    public void init(BotschutzInterface botschutzInterface) {
        thread = new ExecutorThread(botschutzInterface, this);
        thread.start();
    }

    @Override
    public void onBotschutzClicked(BotschutzParams params) {
        latestBotschutzParams = params;
    }

    /**
     * This interceptor compresses the HTTP request body. Many webservers can't
     * handle this!
     */
    static class GzipRequestInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    try ( BufferedSink gzipSink = Okio.buffer(new GzipSink(sink))) {
                        body.writeTo(gzipSink);
                    }
                }
            };
        }
    }

    public API getAPI() {
        return api;
    }

    public <T extends BotschutzResponse> void executeRequest(Call<T> obs, Callback<T> action) {
        thread.queueRequest(obs, action);
    }

    private RequestBody getBotschutzBody(BotschutzParams botschutzParams, RequestBody body) throws IOException {
        if (botschutzParams == null) {
            return null;
        }
        String requestParams = "sid=" + botschutzParams.sid;
        requestParams += "&x=" + botschutzParams.x;
        requestParams += "&y=" + botschutzParams.y;
        requestParams += "&captcha_id=" + botschutzParams.captchaId;
        if (body != null && body.contentLength() > 0) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            requestParams += "&" + buffer.readString(charset);
        }
        return RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), requestParams);
    }
}
