package pl.gratitude.retrofit2sample.api;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.gratitude.retrofit2sample.R;
import pl.gratitude.retrofit2sample.application.RetrofitSampleApplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest client for consuming rest service.
 * <p/>
 * Created on 06.02.2016
 *
 * @author Sławomir Onyszko
 */
public class RestClient {

    private static final String TAG = RestClient.class.getSimpleName();

    private static Retrofit retrofit = buildClient(RetrofitSampleApplication.getInstance().getApplicationContext());

    public RestClient() {
    }

    private static Retrofit buildClient(Context context) {
        String baseUrl = context.getResources().getString(R.string.api_url);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new BasicInterceptor()).build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(RetrofitSampleApplication.getInstance().getGson()))
                .build();
    }

    public static <T> T getClient(Class<? extends T> type) {
        return retrofit.create(type);
    }

    private static class BasicInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }

}
