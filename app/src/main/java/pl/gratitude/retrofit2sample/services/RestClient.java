package pl.gratitude.retrofit2sample.services;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest client for consuming rest service.
 * <p/>
 * Created on 06.02.2016
 *
 * @author Sławomir Onyszko
 */
public class RestClient<T> {

    private T service;
    private String baseUrl = "https://api.github.com/";

    public T getClient(Class<? extends T> type) {
        if (service == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(chain.request());
                }
            }).build();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = client.create(type);

        }
        return service;
    }

}
