package com.example.nol.login_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.UserProfile;
import com.example.nol.login_demo.util.CredentialsManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button Logout;
    private Button GetToken;
    private Auth0 mAuth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logout = (Button) findViewById(R.id.logout);
        Logout.setOnClickListener(LogoutOnClick);
        GetToken = (Button) findViewById(R.id.gettoken);
        GetToken.setOnClickListener(GetTokenOnClick);

    }
    private Button.OnClickListener GetTokenOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gettoken();
        }
    };

    private void gettoken(){
        mAuth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        AuthenticationAPIClient aClient = new AuthenticationAPIClient(mAuth0);
        aClient.tokenInfo(CredentialsManager.getCredentials(this).getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, payload.getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Get IdToken Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private Button.OnClickListener LogoutOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logout();
        }
    };

    private void logout() {
        CredentialsManager.deleteCredentials(this);
        startActivity(new Intent(this, LoginPage.class));
        finish();
    }
}
