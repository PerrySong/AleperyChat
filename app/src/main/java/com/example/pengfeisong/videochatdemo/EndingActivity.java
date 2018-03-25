package com.example.pengfeisong.videochatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EndingActivity extends AppCompatActivity {

    /** Autentication */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);


    }


    /**
     * Call when user click the connect button
     */
    public void connect(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); /** Starts an instance of MainActivity specified by the Intent */
    }

    public void login(View view) {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent); /** Starts an instance of MainActivity specified by the Intent */
    }

    public void signup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent); /** Starts an instance of MainActivity specified by the Intent */
    }


}
