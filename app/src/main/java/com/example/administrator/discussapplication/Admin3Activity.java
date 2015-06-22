package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Admin3Activity extends ActionBarActivity {
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();
    String topicID ,catID,username,roleID;
    JSONArray Data = null;
    String catID2;
    String catTopic;
    String date;
    String owner,num;
    private static final String TAG_cat_id = "cat_id";
    private static final String TAG_cat_topic = "cat_topic";
    private static final String TAG_DATE = "date_time";
    private static final String TAG_DATA = "data";
    private static final String TAG_OWNER = "username";
    private static final String TAG_NUM = "num_reply";
    String passWord, repassWord, oWner;
    JSONParser jParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin3);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.topicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.roleID = intent.getString("role_id");
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);
        EditText editTOPIC = (EditText)this.findViewById(R.id.editTOPIC);
        final EditText editOWNER = (EditText)this.findViewById(R.id.editOWNER);
        final EditText editPASSWORD = (EditText)this.findViewById(R.id.editPASSWORD);
        final EditText editREPASSWORD = (EditText)this.findViewById(R.id.editREPASSWORD);
        ImageButton imageBack = (ImageButton)this.findViewById(R.id.imageBack);
        imageBack.setImageResource(R.drawable.back);
        ImageButton btnHome = (ImageButton) this.findViewById(R.id.imageHome);
        btnHome.setImageResource(R.drawable.home);
        Button btnEdit = (Button)this.findViewById(R.id.btnEdit);
        Button btnCancle = (Button)this.findViewById(R.id.btnCancle);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);

                startActivity(it);
            }
        });


        String url = getURLServer + "jsonShowCat?cat_id="+catID+"";
        // Creating new JSON Parser

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        //String(rs3.getString("owner").getBytes(),"TIS-620")


        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                 catID2 = c.getString(TAG_cat_id);
                 catTopic = c.getString(TAG_cat_topic);
                 date = c.getString(TAG_DATE);
                 owner = c.getString(TAG_OWNER);
                 num = c.getString(TAG_NUM);
            }

            editTOPIC.setText(catTopic);
            editOWNER.setHint(owner);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()
                    , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                oWner = editOWNER.getText().toString();
                passWord = editPASSWORD.getText().toString();
                repassWord = editREPASSWORD.getText().toString();
                String chk_user= null;
                String status = null;
                try {
                    URL url = new URL(getURLServer + "chkUserOnly?username="+oWner+"");
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();

                    while (sc.hasNext()) {
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                         chk_user = jsonObject.getString("chk_user");
                         status = jsonObject.getString("status");


                    }


                    if (oWner.equals("") || passWord.equals("") || repassWord.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show();
                    } else {
                        if("no".equals(chk_user)) {
                            if (!passWord.equals(repassWord)) {
                                Toast.makeText(getApplicationContext(),
                                        "รหัสยืนยันไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                            } else {

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("cat_id", catID));
                                params.add(new BasicNameValuePair("cat_topic", catTopic));
                                params.add(new BasicNameValuePair("username", oWner));
                                params.add(new BasicNameValuePair("password", passWord));
                                jParser.getJSONUrl(getURLServer + "UpdateCategory",params);

                                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catID);
                                it.putExtra("role_id", roleID);

                                startActivity(it);
                                Toast.makeText(getApplicationContext(),
                                        "เพิ่มผู้ดูแลเรียบร้อย", Toast.LENGTH_LONG).show();

                            }
                        }
                        else if("yes".equals(chk_user)){
                            Toast.makeText(getApplicationContext(),
                                    "Username ของคุณซ้ำในระบบ", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);

                startActivity(it);


            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);

                startActivity(it);


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin3, menu);
        return true;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                it.putExtra("topic_id", "");
                it.putExtra("username","");
                it.putExtra("cat_id","");
                it.putExtra("role_id","");
                Toast.makeText(getApplicationContext()
                        ,"ล็อกเอาท์ เรียบร้อย",Toast.LENGTH_LONG).show();
                SaveSharedPreference.clearUserName(Admin3Activity.this);
                System.out.println("");
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
