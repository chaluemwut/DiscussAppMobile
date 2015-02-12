package com.example.administrator.discussapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class LoginActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView image = (ImageView) this.findViewById(R.id.image);
        ImageView image2 = (ImageView) this.findViewById(R.id.image2);
        ImageView image3 = (ImageView) this.findViewById(R.id.image3);
        final EditText EDuser = (EditText) this.findViewById(R.id.editText1);
        final EditText EDpass = (EditText) this.findViewById(R.id.editText1);
        Button BtnLogin = (Button) this.findViewById(R.id.btn_login);
        image.setImageResource(R.drawable.logo1_1);
        image2.setImageResource(R.drawable.bt_id);
        image3.setImageResource(R.drawable.bt_psw);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qMessage1 = EDpass.getText().toString();
                String qMessage2 = EDuser.getText().toString();
                try {
                    URL url = new URL("http://192.168.1.112:8070/DiscussApp/LoginAPI?"+qMessage1+"&"+qMessage2);
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();
                    while(sc.hasNext()){
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                        String status = jsonObject.getString("status");
                        String is_user = jsonObject.getString("is_user");
                      //  Text2.setText(is_user);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
