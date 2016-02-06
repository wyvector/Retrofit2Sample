package pl.gratitude.retrofit2sample;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.gratitude.retrofit2sample.models.User;
import pl.gratitude.retrofit2sample.services.GitHubService;
import pl.gratitude.retrofit2sample.services.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitSampleActivity extends AppCompatActivity {

    private static final String TAG = RetrofitSampleActivity.class.getSimpleName();

    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.user_avatar)
    ImageView userAvatar;

    @Bind(R.id.user_login)
    TextView userLogin;

    @Bind(R.id.input_user_name)
    EditText inputUserName;

    private RestClient<GitHubService> gitHubServiceRestClient;
    private GitHubService gitHubService;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_sample);
        ButterKnife.bind(RetrofitSampleActivity.this);
        setSupportActionBar(toolbar);
        setup();

    }

    @OnClick(R.id.fab)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (validate()) {
                    getData(userName);
                } else {
                    Snackbar.make(view, getResources().getString(R.string.type_github_user_name), Snackbar.LENGTH_LONG);
                }
                break;
        }
    }

    private void setup() {
        gitHubServiceRestClient = new RestClient<>();
        gitHubService = gitHubServiceRestClient.getClient(GitHubService.class);
    }

    private void getData(String user) {
        Call<User> call = gitHubService.getUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccess()) {
                    User model = response.body();
                    setupView(model);
                } else {
                    Snackbar.make(coordinatorLayout, getResources().getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG);
            }
        });
    }

    private void setupView(User model) {
        try {
            Picasso.with(RetrofitSampleActivity.this).load(model.getAvatarUrl()).into(userAvatar);
            userLogin.setText(model.getLogin());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private boolean validate() {
        boolean result = true;
        if(inputUserName.getText() != null && !inputUserName.getText().toString().isEmpty()) {
            userName = inputUserName.getText().toString().trim();
        } else {
            result = false;
        }
        return result;
    }

}
