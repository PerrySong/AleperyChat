package com.example.pengfeisong.videochatdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EndingActivity extends AppCompatActivity {

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

}
