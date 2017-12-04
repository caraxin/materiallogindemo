package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final String login_url = "http://131.179.4.157:8080/BruinsInfo/Login";


    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        boolean b = new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), new ProcessResult() {
                            @Override
                            public void returnArrString(ArrayList<String> arrresult) {
                                String result = arrresult.get(0);
                                System.out.println(result);
                                if (result == null || !result.equals("1")) {
                                    onLoginFailed();
                                    Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(startIntent);
                                } else {
                                    onLoginSuccess();
                                    //new intermediate Menu Activity
                                    //SecondActivity has now been renamed to 'LearnAboutLocationActivity'
                                    Intent startIntent = new Intent(getApplicationContext(), MenuActivity.class);
                                    startIntent.putExtra("org.materiallogindemo.EMAIL", _emailText.getText().toString());
                                    startActivity(startIntent);
                                }
                            }
                        });

                        String user_email = _emailText.getText().toString();
                        String user_password = _passwordText.getText().toString();
                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("user_email", user_email);
                            jsonParam.put("user_password", user_password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String param = jsonParam.toString();

                        //BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                        backgroundTask.execute(login_url, param);
                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
