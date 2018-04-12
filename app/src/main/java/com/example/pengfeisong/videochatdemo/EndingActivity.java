package com.example.pengfeisong.videochatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EndingActivity extends BaseActivity {

    /** Autentication */
    private FirebaseAuth mAuth;
    private Button loginBtn;
    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        loginBtn = (Button)findViewById(R.id.login);
        connectBtn = (Button)findViewById(R.id.connect);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();


//        Toast.makeText(EndingActivity.this, mAuth.getUid(),
//                Toast.LENGTH_SHORT).show();
//        ((TextView)findViewById(R.id.userId)).setText(mAuth.getUid());

        // [END initialize_auth]
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    private void updateUI(FirebaseUser user) {

        if(user != null) {
            //Set the login button logout
            loginBtn.setText("Logout");
            //Show the connect button
            connectBtn.setVisibility(View.VISIBLE);
        } else {
            //Hide the connect button
            connectBtn.setVisibility(View.GONE);
            //Set the text of loginBtn
            loginBtn.setText("Login / Register");
        }
    }

}
