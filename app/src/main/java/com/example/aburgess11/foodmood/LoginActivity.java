package com.example.aburgess11.foodmood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by liangelali on 7/24/17.
 */

public class LoginActivity extends AppCompatActivity {

    String TAG ="LoginActivity";
    private FirebaseAuth mAuth;
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    Switch groupToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        groupToggle = (Switch) findViewById(R.id.groupToggle);

        mAuth = FirebaseAuth.getInstance();

        info.setText("To use FoodMood features like GroupSwiping, sign into your Facebook account!");

        loginButton.setToolTipMode(LoginButton.ToolTipMode.NEVER_DISPLAY);

        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("isDismissed", false);
                setResult(RESULT_OK, data);
                handleFacebookAccessToken(loginResult.getAccessToken());
                finish();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
                Log.d("Test", "onSuccess: ");
                Toast.makeText(getApplicationContext(), "Login canceled. You can login anytime from your profile!", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("isDismissed", true);
                setResult(RESULT_OK, data);
                finish();

            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
                Log.d("Test", "onError: ");
                Toast.makeText(getApplicationContext(), "Login failed. You can login anytime from your profile!", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("isDismissed", true);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Authentication error",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }
}
