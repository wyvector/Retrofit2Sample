package pl.gratitude.retrofit2sample.application;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import pl.gratitude.retrofit2sample.api.RestClient;
import pl.gratitude.retrofit2sample.utils.DateTimeDeserializer;
import pl.gratitude.retrofit2sample.utils.DateTimeSerializer;

/**
 * Created on 12.08.2016.
 *
 * @author Slawomir Onyszko
 */
public class RetrofitSampleApplication extends Application {

    private static RetrofitSampleApplication instance;
    private Gson gson;

    public RetrofitSampleApplication() {
        super();
    }

    public static RetrofitSampleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        gson = gsonBuilder();
    }

    public Gson getGson() {
        return gson;
    }

    private Gson gsonBuilder() {
        return new GsonBuilder()
                .registerTypeAdapter(
                        DateTime.class,
                        new DateTimeDeserializer()
                )
                .registerTypeAdapter(
                        DateTime.class,
                        new DateTimeSerializer()
                ).create();
    }
}
