package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ap.agroplus.General;
import com.ap.agroplus.R;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.network.GetLoginFromServer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPass;
    General general;
    AppData data;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        data = new AppData(LoginActivity.this);
        general = new General(LoginActivity.this);
        etUsername = (EditText) findViewById(R.id.edit_username);
        etPass = (EditText) findViewById(R.id.edit_pass);
    }

    public void DontHaveAccount(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public void ForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        finish();
    }

    public void LoginUser(View view) {
        String username = etUsername.getText().toString();
        String password = etPass.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            general.error("Please all fields must be filled");
            return;
        }
        GetLoginFromServer getLoginFromServer = new GetLoginFromServer(LoginActivity.this, username, password);
        getLoginFromServer.LoginUser(LoginActivity.this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (data.getLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, Home2Activity.class));
            finish();
        }
    }
}
