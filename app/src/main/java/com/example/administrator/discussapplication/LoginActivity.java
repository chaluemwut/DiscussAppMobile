package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private static   String getURLServer = "http://192.168.236.1:8070/DiscussAppWeb/";
    SharedPreferences share ;
      EditText EDuser;
      EditText  EDpass;
    CheckBox checkBoxRe;
    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageView imageLogo = (ImageView) this.findViewById(R.id.imageLogo);
        ImageView imageUser = (ImageView) this.findViewById(R.id.imageUser);
        ImageView imagePassword = (ImageView) this.findViewById(R.id.imagePassword);
        imageLogo.setImageResource(R.drawable.logo1_1);
        imageUser.setImageResource(R.drawable.bt_id);
        imagePassword.setImageResource(R.drawable.bt_psw);
         EDuser = (EditText) this.findViewById(R.id.edtUserLog);
         EDpass = (EditText) this.findViewById(R.id.edtPassLog);
         checkBoxRe =(CheckBox)this.findViewById(R.id.checkBoxRe);
        Button BtnLogin = (Button) this.findViewById(R.id.btnLogin);
        TextView signUp = (TextView) this.findViewById(R.id.regis);


        share  = getSharedPreferences("MYPREFERRENCE", Context.MODE_PRIVATE);
        EDuser.setText(share.getString("USERNAME", ""));
        EDpass.setText(share.getString("PASSWORD", ""));
        checkBoxRe.setChecked(share.getBoolean("REMEMBER", false));



        BtnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qMessage1 = EDpass.getText().toString();
                String qMessage2 = EDuser.getText().toString();
                String is_user = null;
                String role_id = null;
                String username = null;
                try {
                    URL url = new URL(getURLServer + "LoginAPI?username=" + qMessage2 + "&password=" + qMessage1);
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();
                    while (sc.hasNext()) {
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                        String status = jsonObject.getString("status");
                        is_user = jsonObject.getString("is_user");
                        role_id = jsonObject.getString("role_id");
                        username = jsonObject.getString("username");
                        //  Text2.setText(is_user);
                    }
                    if (qMessage1.equals("") && qMessage2.equals("")) {
                        Toast.makeText(getApplicationContext()
                                , "กรุณากรอก UsernameและPassword ", Toast.LENGTH_LONG).show();
                    } else if (!qMessage1.equals("") && qMessage2.equals("")) {
                        Toast.makeText(getApplicationContext()
                                , "กรุณากรอก Username ", Toast.LENGTH_LONG).show();
                    } else if (qMessage1.equals("") && !qMessage2.equals("")) {
                        Toast.makeText(getApplicationContext()
                                , "กรุณากรอก  Password", Toast.LENGTH_LONG).show();
                    } else if (!qMessage1.equals("") && (!qMessage2.equals(""))) {
                        if ("yes".equals(is_user)) {

                            if ("yes".equals(is_user) && role_id.equals("3")) {

                                Toast.makeText(getApplicationContext()
                                        , "เข้าระบบสำเร็จ", Toast.LENGTH_LONG).show();
                                Intent it = new Intent(getApplicationContext(), LandingActivity.class);
                                it.putExtra("role_id", role_id);
                                it.putExtra("username", username);
                                // it.putExtra("key3", inPutGp);
                                System.out.println("");
                                startActivity(it);

                            } else if ("yes".equals(is_user) && role_id.equals("2")) {

                                Toast.makeText(getApplicationContext()
                                        , "เข้าระบบผู้ดูแลกระทู้สำเร็จ", Toast.LENGTH_LONG).show();
                                Intent it = new Intent(getApplicationContext(), StaffActivity.class);
                                it.putExtra("role_id", role_id);
                                it.putExtra("username", username);
                                // it.putExtra("key3", inPutGp);
                                System.out.println("");
                                startActivity(it);

                            } else if ("yes".equals(is_user) && role_id.equals("1")) {

                                Toast.makeText(getApplicationContext()
                                        , "เข้าระบบผู้ดูแลระบบสำเร็จ", Toast.LENGTH_LONG).show();
                                Intent it = new Intent(getApplicationContext(), AdminActivity.class);
                                it.putExtra("role_id", role_id);
                                it.putExtra("username", username);
                                // it.putExtra("key3", inPutGp);
                                System.out.println("");
                                startActivity(it);

                            }
                        } else if ("no".equals(is_user)) {
                            Toast.makeText(getApplicationContext()
                                    , "Password หรือ Username ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext()
                            , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext()
                            , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากแอฟพลิเคชัน ?");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("ต้องออกจากแอฟพลิเคชันหรือไม่ ");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                LoginActivity.super.onBackPressed();
            }
        });

        dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        SharedPreferences.Editor editor = share.edit();

        editor.putBoolean("REMEMBER",checkBoxRe.isChecked());


            editor.putString("USERNAME", EDuser.getText().toString());
            editor.putString("PASSWORD", EDpass.getText().toString());
            editor.commit();


    }
}
