
package com.example.pengfeisong.videochatdemo;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.Frame.ROTATE;
import com.affectiva.android.affdex.sdk.detector.Face;
import com.affectiva.android.affdex.sdk.detector.FrameDetector;
import com.affectiva.android.affdex.sdk.detector.PhotoDetector;
import com.example.pengfeisong.videochatdemo.model.AsyncFrameDetector;
import com.example.pengfeisong.videochatdemo.model.CameraView;
import com.example.pengfeisong.videochatdemo.model.FacialInfo;
import com.example.pengfeisong.videochatdemo.model.CameraHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener, CameraView.OnCameraViewEventListener, AsyncFrameDetector.OnDetectorEventListener {


    @Override
    public void onCameraFrameAvailable(byte[] frame, int width, int height, ROTATE rotation) {

    }

    @Override
    public void onCameraStarted(boolean success, Throwable error) {

    }

    @Override
    public void onSurfaceViewSizeChanged() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class JoyDataPoint implements Comparable<JoyDataPoint>{
        float score;
        long time;

        JoyDataPoint(float s, long t) {
            this.score = s;
            this.time = t;
        }

        public int compareTo(@NonNull JoyDataPoint p2) {
            return (int)(this.time - p2.time) / 1000;
        }

    }

    private static String API_KEY="46081352";
    private static String SESSION_ID="1_MX40NjA4MTM1Mn5-MTUyNTI5ODQxMDcyM35GcFlrOU1Kc0VlMnh1eXRaNXJxSE16N2h-fg";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NjA4MTM1MiZzaWc9NGVhZjA1Y2NhNTM4ZDMwNzM0OTk2MjVjOGNiZDZkNjE1OTIxNWU0MTpzZXNzaW9uX2lkPTFfTVg0ME5qQTRNVE0xTW41LU1UVXlOVEk1T0RReE1EY3lNMzVHY0Zsck9VMUtjMFZsTW5oMWVYUmFOWEp4U0UxNk4yaC1mZyZjcmVhdGVfdGltZT0xNTI1Mjk4NDI0Jm5vbmNlPTAuMzg5NzA0NjE0MzYzNzU0NiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTI3ODkwNDI0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
    private static String LOG_TAG=MainActivity.class.getSimpleName();
    private static final int RC_SETTINGS = 123;
    // Affectiva
//    SurfaceView cameraDetectorSurfaceView;
//    CameraDetector cameraDetector;
    LinkedList<JoyDataPoint> window1;
    LinkedList<Float> window2;
    TextView joyLevel;
    FacialInfo facialInfo;

    //2.Specify the max processing rate used by the Camera Detector. (This is in FPS, Frames per Second)
    int maxProcessingRate = 10;

    private DatabaseReference mDatabase; //Set up db

    /** Autentication */


    private Session session;

    private FrameLayout SubscriberContainer;
    private FrameLayout PublisherContainer;

    private Publisher publisher;
    private Subscriber subscriber;

    /***
     * Initialize detector
     */
    AsyncFrameDetector asyncDetector; // runs FrameDetector on a background thread
    private final Handler mHandler = new Handler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        SubscriberContainer = (FrameLayout)findViewById(R.id.subscriber_container);
        PublisherContainer = (FrameLayout)findViewById(R.id.publisher_container);
        this.joyLevel = (TextView)findViewById(R.id.joyLevel);

        /**
         * This is for affactiva
         * */



        asyncDetector = new AsyncFrameDetector(this);
        asyncDetector.setOnDetectorEventListener(this);
        asyncDetector.start();
        /*******
         *
         */



        mDatabase = mDatabase = FirebaseDatabase.getInstance().getReference();
    }





    /** ***********************************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @AfterPermissionGranted(RC_SETTINGS)
    private void requestPermission() {
        String[] perm = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if(EasyPermissions.hasPermissions(this, perm)) {
//
            session = new Session.Builder(this, API_KEY, SESSION_ID).build();
            session.setSessionListener(this);
            session.connect(TOKEN);
//
        } else {
            EasyPermissions.requestPermissions(this, "This app need to access your camera and mic", RC_SETTINGS, perm);
        }
    }

    @Override
    public void onConnected(Session session) {

        Log.d(LOG_TAG, "onConnected: Connected to session: "+session.getSessionId());
        System.out.println("yoyoyoyoyo ");
        publisher = new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);

        View pv = publisher.getView();
        PublisherContainer.addView(pv);
//
//        Runnable processFrame = new Runnable() {
//
//            public void run() {
//                View view = (View) PublisherContainer;
//                for(int i = 0; i < 20; i++) {
//                    processFrame(view);
//                }
//
//            }
//        };

//        mHandler.post(processFrame);

        session.publish(publisher);
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view

        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void processFrame(View view) {
        System.out.println("view is height is: " + view.getHeight());
        if(view != null) {

//            Bitmap bitmap = null;
            Bitmap bitmap = getBitmapFromView(view);

            if(bitmap != null) {

                Frame.BitmapFrame frame = new Frame.BitmapFrame(bitmap, Frame.COLOR_FORMAT.UNKNOWN_TYPE);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                float timestamp = (float) SystemClock.elapsedRealtime()/1000f;
                System.out.println("time stamp is : " + timestamp);
                Frame.ROTATE rotation = Frame.ROTATE.NO_ROTATION;
                frame.setTargetRotation(rotation);

                asyncDetector.process(frame,timestamp);
            }

        }

    }


    @Override
    public void onDisconnected(Session session) {
        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: "+session.getSessionId());
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
        Log.e(LOG_TAG, "onError: "+ opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - "+opentokError.getMessage() + " in session: "+ session.getSessionId());


    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: "+opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() +  " - "+opentokError.getMessage());
    }

    /**
     * Call when user want to end the video chat
     */
    public void endChat(View view){
//        sendEmotionToDB(facialInfo.getJoyData());

        Intent intent = new Intent(this, EndingActivity.class);
        startActivity(intent); /** Starts an instance of EndingActivity specified by the Intent */

    }


    //  List<Face> faces, Frame frame, float timeStamp
//  This method gives the main information for Affectiva’s
//   emotion detection. It gives a list of face objects which
// wrap all sorts of information about a face (Level of emotions,
// expressions, characteristics, etc.), the frame object which is a
// wrapper for the image captured, and a timestamp of when the frame was captured.

    //    Check if the frame was processed
//    Check if there are any faces currently recognized.
//    Grab the first face
//    Grab any emotion data from the processed frame.
//    This is where the actual implementation of the facial expressions will occur.
//    The Face object provides all the different information that can be used.


    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        //1. Set the Surface View’s Layout Params with the new sizing

        this.joyLevel.setText("On the image result");
        if (faces == null) {
            joyLevel.setText("Your joy level is: " + -1);
            Log.e(LOG_TAG, "onError: null face");
            return; //frame was not processed
        }


        //2. Check if there are any faces currently recognized.
        if (faces.size() == 0) {
            joyLevel.setText("Your joy level is: " + -2);
            Log.e(LOG_TAG, "onError: null face");
            return; //no face found
        }


        System.out.println("get in here ");
        //3. Check if there are any faces currently recognized.
        Face face = faces.get(0);

        //4. Grab any emotion data from the processed frame. This is where the actual implementation
        // of the facial expressions will occur. The Face object provides all the different information that can be used.
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float surprise = face.emotions.getSurprise();
        //5. Print the levels of emotion. (The scale goes from 0-100)
        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Surprise: " + surprise);

        float meanJoy1 = this.last100MeanJoy(face);
        float meanJoy2 = this.last100MeanJoyWMA(face);
        float meanJoy3 = this.lastTenSecMeanJoy(face);
        facialInfo.addJoy(meanJoy1);
        joyLevel.setText("Your joy level is: " + joy);


        //Show the text associate with the

//        if(meanJoy1 > 40) {
//            joy.setText("You have reach the threshold using WMA");
//        } else {
//            textView1.setText("You had better laugh!");
//        }
//        if(meanJoy2 > 40) {
//            textView2.setText("You have reach the threshold using SMA");
//        } else {
//            textView2.setText("You had better laugh!");
//        }
//        if(meanJoy3 > 40) {
//            textView3.setText("You have reach the threshold using SMA for timestamps");
//            imageView.setImageResource(R.drawable.smile);
//        } else {
//            imageView.setImageResource(R.drawable.unhappy);
//            textView3.setText("You had better laugh!");
//        }

    }

    @Override
    public void onDetectorStarted() {

    }


    private float lastTenSecMeanJoy(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        JoyDataPoint joyDP = new JoyDataPoint(joy, System.currentTimeMillis());
        if(window1.peekFirst() == null || joyDP.compareTo(window1.peekFirst()) < 10) {//Compare to timestamp of the first node of the list and the current node
            window1.add(joyDP);
        } else {
            window1.add(joyDP);
            window1.removeFirst();
        }

        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window1.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window1.get(i).score * (i + 1);
        }
        meanJoy /= (window1.size() * (window1.size() + 1)) / 2;
        return meanJoy;
    }

    private float last100MeanJoy(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(joy);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window2.get(i);
        }
        meanJoy /= window2.size();
        return meanJoy;
    }

    private float last100MeanJoyWMA(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(joy);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window2.get(i) * (i + 1);
        }
        meanJoy /= (window2.size() * (window2.size() + 1)) / 2;
        return meanJoy;
    }

    private void sendEmotionToDB(float[] joy) {
        mDatabase.child("2_MX40NjA4MTM1Mn5-MTUyNDc5MDI5NjAwOH5XT2ZHd2RDUUxFdG1LRHpPS3JYUmpOdjd-fg").child("joy").setValue(joy);
    }
}