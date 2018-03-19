package com.example.pengfeisong.videochatdemo;

import android.Manifest;
import android.content.Intent;
import android.media.MediaCas;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener{

    private static String API_KEY="46081352";
    private static String SESSION_ID="1_MX40NjA4MTM1Mn5-MTUyMTI1MDgzMzE1Mn5ONkhxZVEyakc3bDQxTzN2YmRXMENLVUl-fg";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NjA4MTM1MiZzaWc9ZDdmYjJjZDJiYjFmYjZmZWEyZWI5MDg5NTVhZWM2ODRmODk0YTVhNTpzZXNzaW9uX2lkPTFfTVg0ME5qQTRNVE0xTW41LU1UVXlNVEkxTURnek16RTFNbjVPTmtoeFpWRXlha2MzYkRReFR6TjJZbVJYTUVOTFZVbC1mZyZjcmVhdGVfdGltZT0xNTIxMjUwODk2Jm5vbmNlPTAuNDU5Njg1MjI1NzEyNTA2MzYmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTUyMzg0Mjg5NSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG=MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS = 123;

    private Session session;

    private FrameLayout PublisherContainer;
    private FrameLayout SubscriberContainer;

    private Publisher publisher;

    private Subscriber subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        PublisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        SubscriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermission() {
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perm)) {

            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);

        } else {
            EasyPermissions.requestPermissions(this, "This app need to access your camera and mic", RC_SETTINGS, perm);
        }
    }

    @Override
    public void onConnected(Session session) {

        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        PublisherContainer.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {

        if(subscriber == null) {
            subscriber = new Subscriber.Builder(this, stream).build();
            session.subscribe(subscriber);
            SubscriberContainer.addView(subscriber.getView());
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if(subscriber != null){
            subscriber = null;
            SubscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }

    /**
     * Call when user want to end the video chat
     */
    public void endChat(View view){
        Intent intent = new Intent(this, EndingActivity.class);
        startActivity(intent); /** Starts an instance of EndingActivity specified by the Intent */

    }
}
