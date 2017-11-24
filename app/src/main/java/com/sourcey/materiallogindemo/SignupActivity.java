package com.sourcey.materiallogindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Bind;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final String signup_url = "http://10.0.2.2:8006/BruinsInfo/Signup";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), new ProcessResult() {
                        @Override
                        public void returnString(String result) {
                            System.out.println(result);
                            if (result == null || result.equals("0")) {
                                onSignupFailed();
                            } else {
                                onSignupSuccess();
                            }
                        }
                    });

                    String user_name = _nameText.getText().toString();
                    String user_address = _addressText.getText().toString();
                    String user_email = _emailText.getText().toString();
                    String user_phone = _mobileText.getText().toString();
                    String user_pass = _passwordText.getText().toString();
                    String reEnterPassword = _reEnterPasswordText.getText().toString();
                    //BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                    if (!user_pass.equals(reEnterPassword)) {
                        onSignupFailed();
                        return;
                    }
                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("user_name", user_name);
                        jsonParam.put("user_pass", user_pass);
                        jsonParam.put("user_email", user_email);
                        jsonParam.put("user_phone", user_phone);
                        jsonParam.put("user_address", user_address);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String param = jsonParam.toString();

                    //BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                    backgroundTask.execute(signup_url, param);
                    // On complete call either onLoginSuccess or onLoginFailed
                    progressDialog.dismiss();
                }
            }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(startIntent);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
        Intent startIntent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(startIntent);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}