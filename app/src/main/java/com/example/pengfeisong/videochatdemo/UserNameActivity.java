package com.example.pengfeisong.videochatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserNameActivity extends BaseActivity {

    TextView username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        username = findViewById(R.id.username);
    }

    public void clickSetUsername(View view) {
        mDatabase.child("usersInfo").child(mAuth.getUid()).child("username").setValue(username);
        Intent intent = new Intent(this, EndingActivity.class);
        startActivity(intent);
    }
}
