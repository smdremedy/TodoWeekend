package com.soldiersofmobile.todoekspert;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements LoginAsyncTask.LoginCallback {


    public static final int MIN_PASSWORD_LENGTH = 4;
    @BindView(R.id.username_edit_text)
    EditText usernameEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.activity_login)
    LinearLayout activityLogin;
    @BindView(R.id.progress)
    ProgressBar progress;

    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG) {
            usernameEditText.setText("test");
            passwordEditText.setText("test");
        }
        App app = (App) getApplication();
        loginManager = app.getLoginManager();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginManager.setLoginCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginManager.setLoginCallback(null);
    }

    @OnClick({R.id.sign_in_button, R.id.sign_up_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                tryToLogin();
                break;
            case R.id.sign_up_button:
                break;
        }
    }

    private void tryToLogin() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean hasErrors = false;
        if (username.isEmpty()) {
            hasErrors = true;
            usernameEditText.setError(getString(R.string.this_field_is_required));
        }

        int length = password.length();
        if (length < MIN_PASSWORD_LENGTH) {
            hasErrors = true;
            passwordEditText.setError(
                    getString(R.string.password_error, MIN_PASSWORD_LENGTH, length));
        }

        if (!hasErrors) {
            loginManager.login(username, password);
        }

    }


    @Override
    public void showProgress(boolean inProgress) {
        signInButton.setEnabled(!inProgress);
        progress.setVisibility(inProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String error) {
        if (error != null) {
            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, TodoListActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
