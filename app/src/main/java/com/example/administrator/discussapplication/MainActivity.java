package com.example.administrator.discussapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    public  String urlServer ="http://192.168.1.49:8080/DiscussWeb/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        // setContentView(R.layout.activity_main);
        ImageView image = (ImageView) this.findViewById(R.id.Logo_Main);
        image.setImageResource(R.drawable.logo1_1);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

}