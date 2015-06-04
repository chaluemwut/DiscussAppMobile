package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        ImageView imageLogo = (ImageView) this.findViewById(R.id.imageLogo);
        ImageView imageUser = (ImageView) this.findViewById(R.id.imageUser);
        ImageView imagePassword = (ImageView) this.findViewById(R.id.imagePassword);
        imageLogo.setImageResource(R.drawable.logo1_1);
        imageUser.setImageResource(R.drawable.bt_id);
        imagePassword.setImageResource(R.drawable.bt_psw);
        final EditText EDuser = (EditText) this.findViewById(R.id.edtUserLog);
        final EditText EDpass = (EditText) this.findViewById(R.id.edtPassLog);
        Button BtnLogin = (Button) this.findViewById(R.id.btnLogin);
        TextView signUp = (TextView) this.findViewById(R.id.regis);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qMessage1 = EDpass.getText().toString();
                String qMessage2 = EDuser.getText().toString();
                String is_user = null;
                String role_id = null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/Webtest/LoginAPI?username="+qMessage2+"&password="+qMessage1);
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();
                    while(sc.hasNext()){
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                        String status = jsonObject.getString("status");
                        is_user = jsonObject.getString("is_user");
                        role_id = jsonObject.getString("role_id");
                      //  Text2.setText(is_user);
                    }
                    if(qMessage1.equals("")&&qMessage2.equals("")){
                        Toast.makeText(getApplicationContext()
                                ,"กรุณากรอก UsernameและPassword ",Toast.LENGTH_LONG).show();
                    }
                    else if(!qMessage1.equals("")&&qMessage2.equals("")){
                        Toast.makeText(getApplicationContext()
                        ,"กรุณากรอก Username ",Toast.LENGTH_LONG).show();
                    }
                   else if(qMessage1.equals("")&&!qMessage2.equals("")){
                        Toast.makeText(getApplicationContext()
                                ,"กรุณากรอก  Password",Toast.LENGTH_LONG).show();
                    }
                    else if (!qMessage1.equals("")&&(!qMessage2.equals(""))){
                        if("yes".equals(is_user)){
                            Toast.makeText(getApplicationContext()
                                    ,"เข้าระบบสำเร็จ",Toast.LENGTH_LONG).show();
                            Intent it = new Intent(getApplicationContext(), LandingActivity.class);
                            //it.putExtra("key1", inPutIpAddress);
                            //it.putExtra("key2", inPutSub);
                            // it.putExtra("key3", inPutGp);
                            System.out.println("");
                            startActivity(it);
                        }
                        else if("no".equals(is_user)){
                            Toast.makeText(getApplicationContext()
                                    ,"Password หรือ Username ไม่ถูกต้อง",Toast.LENGTH_LONG).show();
                        }

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


///intet Register
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                    Intent it = new Intent(getApplicationContext(), RegisterActivity.class);
                    //it.putExtra("key1", inPutIpAddress);
                    //it.putExtra("key2", inPutSub);
                    // it.putExtra("key3", inPutGp);
                    System.out.println("");
                    startActivity(it);
            }
            });


    }

}
