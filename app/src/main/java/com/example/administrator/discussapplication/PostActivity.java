package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class PostActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        final ImageButton Btn_back = (ImageButton) this.findViewById(R.id.imgBtnBack_Reg);
        ImageButton AddImg = (ImageButton)this.findViewById(R.id.ImgBtnAddImg);
        final EditText etdtoppic = (EditText)this.findViewById(R.id.edtTextTopicPost);
        final EditText etdDetail = (EditText)this.findViewById(R.id.edtTextDescpost);

        Btn_back.setImageResource(R.drawable.back);
        AddImg.setImageResource(R.drawable.addpic3);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AddImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated stub
                String qMessage1 = etdtoppic.getText().toString();
                String qMessage2 = etdDetail.getText().toString();


                //Check Edit Text is Emtry
                if(!"".equals(qMessage1)||!"".equals(qMessage2)) {
                    try {
                        String urlAddUser = ("http://192.168.1.109:8080/DiscussWeb/SendPost?cat_id=42"+"&topic=" + qMessage2
                                + "&desc=" + qMessage2 + "&owner=" + qMessage1);
                        URL url = null;

                        url = new URL(urlAddUser);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        Scanner sc = null;
                        sc = new Scanner(url.openStream());
                        StringBuffer buf = new StringBuffer();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),
                            "เลือกรูปภาพ", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getApplicationContext(), PostImageActivity.class);
                    //it.putExtra("key1", inPutIpAddress);
                    //it.putExtra("key2", inPutSub);
                    // it.putExtra("key3", inPutGp);
                    System.out.println("");
                    startActivity(it);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                }


            }
        });

        /// Start button back
        Btn_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), LandingActivity.class);
                //it.putExtra("key1", inPutIpAddress);
                //it.putExtra("key2", inPutSub);
                // it.putExtra("key3", inPutGp);
                System.out.println("");
                startActivity(it);

            }
        });
        TextView clickpost =(TextView)this.findViewById(R.id.ClickPost);
        clickpost.setOnClickListener(new View.OnClickListener(){

            public  void  onClick(View v){
                String qMessage1 = etdtoppic.getText().toString();
                String qMessage2 = etdDetail.getText().toString();
                if(!"".equals(qMessage1)||!"".equals(qMessage2)) {
                    try {
                        String urlAddUser = ("http://192.168.1.109:8080/DiscussWeb/SendPost?cat_id=42"+"&topic=" + qMessage2
                                + "&desc=" + qMessage2 + "&owner=" + qMessage1);
                        URL url = null;

                        url = new URL(urlAddUser);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        Scanner sc = null;
                        sc = new Scanner(url.openStream());
                        StringBuffer buf = new StringBuffer();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),
                            "เรียร้อย", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post, menu);
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
