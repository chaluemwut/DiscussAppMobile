package com.example.administrator.discussapplication;

import android.content.Intent;
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
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();

    SaveSharedPreference  share;

      EditText EDuser;
      EditText  EDpass;
    CheckBox checkBoxRe;


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
        share = new SaveSharedPreference();
        //getSharedPreferences("MYPREFERRENCE", Context.MODE_PRIVATE);
//        EDuser.setText(share.getString("USERNAME", ""));
//        EDpass.setText(share.getString("PASSWORD", ""));
//        checkBoxRe.setChecked(share.getBoolean("REMEMBER", false));



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
                            SaveSharedPreference.setUserName(LoginActivity.this, username, role_id, checkBoxRe.isChecked());

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



    private static long back_pressed;
    private Toast toast;
    @Override

    public void onBackPressed()
    {


        if (back_pressed + 2000 > System.currentTimeMillis())
        {

            // need to cancel the toast here
            toast.cancel();

            // code for exit
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else
        {
            // ask user to press back button one more time to close app
            toast=  Toast.makeText(getBaseContext(), "คลิกอีกครั้งเพื่อออกจาก Discuss App", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();
    }
  //  protected void onDestroy(){
    //   super.onDestroy();
//        SharedPreferences.Editor editor = share.edit();
//
//        editor.putBoolean("REMEMBER",checkBoxRe.isChecked());
//
//
//            editor.putString("USERNAME", EDuser.getText().toString());
//            editor.putString("PASSWORD", EDpass.getText().toString());
//          editor.commit();


    //}
}
