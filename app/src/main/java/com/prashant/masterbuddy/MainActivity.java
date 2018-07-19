package com.prashant.masterbuddy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prashant.masterbuddy.R;
import com.prashant.masterbuddy.utils.Utils;
import com.prashant.masterbuddy.ws.JsonServiceOKHTTP;
import com.prashant.masterbuddy.ws.JsonServices;
import com.prashant.masterbuddy.ws.VolleyCallback;
import com.prashant.masterbuddy.ws.model.RegisterResult;
import com.prashant.masterbuddy.ws.model.User;
import com.prashant.masterbuddy.ws.model.UserResult;

public class MainActivity extends AppCompatActivity {

    private EditText edtName, edtUsername, edtPassword, edtEmailId, edtRegPassword;
    private LinearLayout llLogin, llRegister;
    private Button btnLogin, btnSignUp, btnSubmit;
    private Application application;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (Application)getApplication();
        edtName = findViewById(R.id.edtName);
        edtEmailId = findViewById(R.id.edtEmailId);
        edtPassword = findViewById(R.id.edtPassword);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtUsername = findViewById(R.id.edtUserName);

        llLogin = findViewById(R.id.llLogin);
        llRegister = findViewById(R.id.llRegister);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSubmit = findViewById(R.id.btnSubmit);

        progressBar = findViewById(R.id.pbLogin);

        application.permissionUtils.requestPermission(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llLogin.setVisibility(View.GONE);
                llRegister.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.GONE);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For Login
                JsonServices jsonServices = new JsonServices(MainActivity.this, new VolleyCallback() {
                    @Override
                    public void starting() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(Object objectFromJson) {
                        if (isDestroyed()) return;
                        progressBar.setVisibility(View.GONE);
                        UserResult userResult = (UserResult)objectFromJson;
                        if (userResult != null && userResult.result != null){
                            if (userResult.result.messageCode == 1){
                                User user = userResult.result.user;
                                if (user != null) {
                                    application.sharedPreferences.edit().putInt(Constants.USER_ID, user.getUserID()).apply();
                                    application.sharedPreferences.edit().putInt(Constants.USER_TYPE, user.getUserType()).apply();
                                    application.sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply();
                                    Toast.makeText(application, "Login successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(application, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            } else if (userResult.result.messageCode == 0) {
                                Toast.makeText(application, "Either mail id or password is wrong.", Toast.LENGTH_SHORT).show();
                            } else if (userResult.result.messageCode == -1) {
                                Toast.makeText(application, "User blocked by admin", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(application, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onSuccess(boolean isSuccess) {
                        if (isDestroyed()) return;
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure() {
                        if (isDestroyed()) return;
                        progressBar.setVisibility(View.GONE);
                        getLoginFailed();
                    }

                });
                jsonServices.doLogin(edtUsername.getText().toString(), edtPassword.getText().toString());
            }
        });

        btnSubmit.setOnClickListener(view -> {
            if (Utils.isEmpty(edtName) || Utils.isEmpty(edtEmailId) || Utils.isEmpty(edtRegPassword)) {
                Toast.makeText(MainActivity.this, "Please verify entered values.", Toast.LENGTH_SHORT).show();
                return;
            }
            // For SignUp
            JsonServiceOKHTTP jsonServices = new JsonServiceOKHTTP(MainActivity.this, new VolleyCallback() {
                @Override
                public void starting() {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onSuccess(Object objectFromJson) {
                    if (isDestroyed()) return;
                    progressBar.setVisibility(View.GONE);
                    RegisterResult registerResult = (RegisterResult) objectFromJson;
                    if (registerResult != null && registerResult.response != null){
                        RegisterResult.Response response = registerResult.response;
                        if (response.isSuccessfullyRegistred()){
                            application.sharedPreferences.edit().putInt(Constants.USER_ID,response.isUserID()).apply();
                            application.sharedPreferences.edit().putInt(Constants.USER_TYPE, 2).apply();
                            application.sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply();
                            Toast.makeText(application, response.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(application, response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(application, "Not able to register", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSuccess(boolean isSuccess) {
                    if (isDestroyed()) return;
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure() {
                    if (isDestroyed()) return;
                    progressBar.setVisibility(View.GONE);
                    getLoginFailed();
                }
            });
            jsonServices.registerUserAsync(edtName.getText().toString(), edtEmailId.getText().toString(),
                    edtRegPassword.getText().toString(), Utils.getCurrentDate());
        });
    }

    private void getLoginFailed() {
        Toast.makeText(application, "UnFortunately Login Is Unsuccessful! .... Please Try Again Later ", Toast.LENGTH_SHORT).show();
        //onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                application.permissionUtils.checkResults(grantResults,this);
                break;
        }
    }
}