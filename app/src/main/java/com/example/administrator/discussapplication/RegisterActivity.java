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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RegisterActivity extends ActionBarActivity {
    private static   String getURLServer = "http://192.168.1.4:8080/DiscussWeB2/";
    private String topicID,username,catID,roleID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Reg);
        Button submit = (Button)this.findViewById(R.id.btnRegister);
        final EditText EDuser = (EditText) this.findViewById(R.id.edtUserName);
        final EditText EDpass = (EditText) this.findViewById(R.id.edtPassWord);
        final EditText EDCpass = (EditText) this.findViewById(R.id.edtCPassword);
        final EditText EDAdd = (EditText) this.findViewById(R.id.edtAddress);
        final EditText EDtel = (EditText) this.findViewById(R.id.edtTel);
        final EditText EDeamil = (EditText) this.findViewById(R.id.edtEmail);
        final EditText EDname = (EditText) this.findViewById(R.id.edtName);
        btnBack.setImageResource(R.drawable.back);

        final JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
               // String url = "http://localhost:8080/Webtest/chkUserAPI?username=123&email=123";
                String qMessage1 = EDuser.getText().toString();
                String qMessage2 = EDpass.getText().toString();

                String qMessage3 = EDCpass.getText().toString();
                String qMessage4 = EDAdd.getText().toString();
                String qMessage5 = EDtel.getText().toString();
                String qMessage6 = EDeamil.getText().toString();
                String qMessage7 = EDname.getText().toString();
                String chk_user = null;
                String status= null;


                try {
                    URL url = new URL(getURLServer+"chkUserAPI?username="+qMessage1+"&email="+qMessage6);
                    Scanner sc = new Scanner(url.openStream());
                    StringBuffer buf = new StringBuffer();

                    while(sc.hasNext()){
                        buf.append(sc.next());
                        JSONObject jsonObject = new JSONObject(buf.toString());
                         chk_user = jsonObject.getString("chk_user");
                         status = jsonObject.getString("status");


                    }
                    //check edittext is emmtry
                    if(qMessage1.equals("")||qMessage2.equals("")||qMessage3.equals("")||qMessage4.equals("")||qMessage5.equals("")||
                            qMessage6.equals("")||qMessage7.equals("")){
                        Toast.makeText(getApplicationContext(),
                                "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_LONG).show();
                       }
                    else {

                        if("no".equals(chk_user)) {
                            if(!qMessage2.equals(qMessage3)) {
                                Toast.makeText(getApplicationContext(),
                                        "รหัสยืนยันไม่ถูกต้อง", Toast.LENGTH_LONG).show();
                            }
                            else{///complete Regisrter
//                                URL urlAddUser = new URL(getURLServer+"RegisterAPI?username=" + qMessage1 + "&password=" + qMessage2
//                                        + "&address=" + qMessage4 + "&tel=" + qMessage5 + "&email=" + qMessage6 + "&name=" + qMessage7);
//                                Scanner scUser = new Scanner(urlAddUser.openStream());


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("username", qMessage1));
                                params.add(new BasicNameValuePair("password",qMessage2));
                                params.add(new BasicNameValuePair("name", qMessage7));
                                params.add(new BasicNameValuePair("tel", qMessage5));
                                params.add(new BasicNameValuePair("email",qMessage6));
                                params.add(new BasicNameValuePair("address", qMessage4));



                                //HttpPost httpPost = new HttpPost(getURLServer+"RegisterAPI");
                                jParser.getJSONUrl(getURLServer+"RegisterAPI",params);


                                Toast.makeText(getApplicationContext(),
                                        "สมัครสมาชิกเรียบร้อย", Toast.LENGTH_LONG).show();
//                                Intent it = new Intent(getApplicationContext(), LandingActivity.class);
//                                System.out.println("");
//                                username= qMessage1;
//                                roleID = "3";
//                                it.putExtra("topic_id", topicID);
//                                it.putExtra("username", username);
//                                it.putExtra("cat_id", catID);
//                                it.putExtra("role_id", roleID);
//                                startActivity(it);

                            }
                        }

                        else if("yes".equals(chk_user)){
                            Toast.makeText(getApplicationContext(),
                                    "Username หรือ Email ของคุณซ้ำในระบบ", Toast.LENGTH_LONG).show();
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
        ////Intent Login
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                System.out.println("");
                startActivity(it);
            }
        });

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
