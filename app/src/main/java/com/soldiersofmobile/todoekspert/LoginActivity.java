package com.soldiersofmobile.todoekspert;

import android.content.Context;
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

public class LoginActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (BuildConfig.DEBUG) {
            usernameEditText.setText("test");
            passwordEditText.setText("test");
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(
                CalligraphyContextWrapper.wrap(newBase));
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
            login(username, password);
        }

    }

    private void login(String username, String password) {


        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                signInButton.setEnabled(false);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(50);
                        publishProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return validateCredentials(params[0], params[1]);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progress.setProgress(values[0]);
            }

            @Override
            protected void onPostExecute(String error) {
                super.onPostExecute(error);
                signInButton.setEnabled(true);
                progress.setVisibility(View.GONE);
                if (error != null) {
                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        };

        asyncTask.execute(username, password);

    }

    private String validateCredentials(String username, String password) {
        return "test".equals(username) && "test".equals(password) ?
                null : "Invalid password";
    }
}
