package pl.gratitude.retrofit2sample.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import pl.gratitude.retrofit2sample.R;
import pl.gratitude.retrofit2sample.models.User;
import pl.gratitude.retrofit2sample.api.GitHubService;
import pl.gratitude.retrofit2sample.api.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitSampleActivity extends AppCompatActivity {

    private static final String TAG = RetrofitSampleActivity.class.getSimpleName();

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.user_avatar)
    ImageView userAvatar;

    @BindView(R.id.user_login)
    TextView userLogin;

    @BindView(R.id.input_user_name)
    EditText inputUserName;

    private String userName;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_sample);
        unbinder = ButterKnife.bind(RetrofitSampleActivity.this);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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

    private void getData(String userName) {
        RestClient.getClient(GitHubService.class).getUser(userName).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
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
