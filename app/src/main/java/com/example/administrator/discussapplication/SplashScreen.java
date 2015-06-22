package com.example.administrator.discussapplication;

/**
 * Created by Administrator on 2/13/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
    private String topicID,username,catID,roleID;
    //Set waktu lama splashscreen
    private static int splashInterval = 2000;
    SaveSharedPreference share = new SaveSharedPreference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
       share.getSharedPreferences(SplashScreen.this);
     //   share.clearUserName(SplashScreen.this);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub

                if(SaveSharedPreference.getUserName(SplashScreen.this).length() == 0)
                {
                    // call Login Activity
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);



                    startActivity(it);
                }
                else
                {   username = SaveSharedPreference.getUserName(SplashScreen.this);
                    roleID = SaveSharedPreference.getroleID(SplashScreen.this);
                    Boolean check = SaveSharedPreference.getheckBox(SplashScreen.this);
                    Log.i("DDDDD", username);
                    Log.i("DDDDD", roleID);
                    Log.i("DDDDD", String.valueOf(check));
                    if(check) {
                        // Call Next Activity
                        if (roleID.equals("3")) {
                            Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                            it.putExtra("topic_id", topicID);
                            it.putExtra("username", username);
                            it.putExtra("cat_id", catID);
                            it.putExtra("role_id", "3");
                            startActivity(it);
                        } else if (roleID.equals("2")) {
                            Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                            it.putExtra("topic_id", topicID);
                            it.putExtra("username", username);
                            it.putExtra("cat_id", catID);
                            it.putExtra("role_id", "2");
                            startActivity(it);
                        } else if (roleID.equals("1")) {
                            Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                            it.putExtra("topic_id", topicID);
                            it.putExtra("username", username);
                            it.putExtra("cat_id", catID);
                            it.putExtra("role_id", "1");
                            startActivity(it);
                        }
                    }
                }


                //jeda selesai Splashscreen
                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub

            }
        }, splashInterval);

    };



}
