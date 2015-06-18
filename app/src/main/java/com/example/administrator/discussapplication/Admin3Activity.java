package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Admin3Activity extends ActionBarActivity {
    private static   String getURLServer = "http://192.168.1.4:8080/DiscussWeB2/";
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
        ImageButton btnAdd = (ImageButton) this.findViewById(R.id.imageHome);
        btnAdd.setImageResource(R.drawable.home);


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
        JSONParser jParser = new JSONParser();
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
        Button UpdateStaf = (Button)this.findViewById(R.id.button2)  ;
        UpdateStaf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String OWNER = editOWNER.getText().toString();
                String PASSWORD = editPASSWORD.getText().toString();
                String REPASSWORD = editREPASSWORD.getText().toString();
                String chk_user= null;
                String status = null;
                try {
                    URL url = new URL(getURLServer + "chkUserAPI?username=" + OWNER + "");
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();

                    while (sc.hasNext()) {
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                         chk_user = jsonObject.getString("chk_user");
                         status = jsonObject.getString("status");


                    }


                    if (OWNER.equals("") || PASSWORD.equals("") || REPASSWORD.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show();
                    } else {
                        if("no".equals(chk_user)) {
                            if (!PASSWORD.equals(REPASSWORD)) {
                                Toast.makeText(getApplicationContext(),
                                        "รหัสยืนยันไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Yes Complete", Toast.LENGTH_LONG).show();
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin3, menu);
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
