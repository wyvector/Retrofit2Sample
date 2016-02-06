package pl.gratitude.retrofit2sample.services;

import pl.gratitude.retrofit2sample.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created on 06.02.2016
 *
 * @author Sławomir Onyszko
 */
public interface GitHubService {

    @GET("users/{user}")
    Call<User> getUser(@Path("user") String user);

}
