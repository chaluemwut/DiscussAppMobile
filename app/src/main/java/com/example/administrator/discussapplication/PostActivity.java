package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostActivity extends ActionBarActivity {
    private static String getURLServer = "http://192.168.1.4:8080/DiscussWeB2/";
    private String topicID, username, catID, roleID;

    ///value Spinner

    private static String urlSpinner = getURLServer + "jsonAllCat";
    private static final String TAG_cat_id_SPINNER = "cat_id";
    private static final String TAG_cat_topic_SPINNER = "cat_topic";
    private static final String TAG_USER_SPINNER = "userName";
    private static final String TAG_DATA_SPINNER = "data";
    private JSONArray DataSpinner = null;
    final ArrayList<String> spinnerlist = new ArrayList<String>();
    ArrayList<HashMap<String, String>> spinner = new ArrayList<HashMap<String, String>>();
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);
        final ImageButton Btn_back = (ImageButton) this.findViewById(R.id.imgBtnBack_Reg);

        final EditText etdtoppic = (EditText) this.findViewById(R.id.edtTextTopicPost);
        final EditText etdDetail = (EditText) this.findViewById(R.id.edtTextDescpost);

        Btn_back.setImageResource(R.drawable.back);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            this.catID = intent.getString("cat_id");
            this.topicID = intent.getString("topic_id");
            this.username = intent.getString("username");
            this.roleID = intent.getString("role_id");
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);
        JSONObject jsonSpinner = jParser.getJSONFromUrl(urlSpinner);
        try {

// Getting JSON Array
            DataSpinner = jsonSpinner.getJSONArray(TAG_DATA_SPINNER);

            for (int i = 0; i < DataSpinner.length(); i++) {
                JSONObject c = DataSpinner.getJSONObject(i);
                String catID = c.getString(TAG_cat_id_SPINNER);
                String catTopic = c.getString(TAG_cat_topic_SPINNER);
                String catUser = c.getString(TAG_USER_SPINNER);

                HashMap<String, String> map = new HashMap<String, String>();
                // map = new HashMap<String, String>();
                map.put(TAG_cat_id_SPINNER, catID);
                map.put(TAG_cat_topic_SPINNER, catTopic);
                map.put(TAG_USER_SPINNER, catUser);
                spinner.add(map);
                // Populate spinner with CATTPPIC names
                spinnerlist.add(c.getString(TAG_cat_topic_SPINNER));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "ไม่มีการเชื่อมต่อ..",
                    Toast.LENGTH_LONG).show();
        }


        Spinner spinPost = (Spinner) findViewById(R.id.spinPost);

        ArrayAdapter<String> arrAd;
        arrAd = new ArrayAdapter<String>(PostActivity.this, android.R.layout.simple_list_item_1, spinnerlist);

        spinPost.setAdapter(arrAd);
        spinPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int position, long mylng) {
// TODO Auto-generated method stub
                catID = spinner.get(position).get("cat_id");
                Toast.makeText(PostActivity.this,
                        String.valueOf("Your Selected : " + spinnerlist.get(position) + catID),
                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
                Toast.makeText(PostActivity.this,
                        String.valueOf("Your Selected Empty"),
                        Toast.LENGTH_SHORT).show();
            }
        });
        ///////end spinner

        ImageButton AddImg = (ImageButton) this.findViewById(R.id.ImgBtnAddImg);
        AddImg.setImageResource(R.drawable.addpic3);
        AddImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated stub
                String qMessage1 = etdtoppic.getText().toString();
                String qMessage2 = etdDetail.getText().toString();


                //Check Edit Text is Emtry
                if (!"".equals(qMessage1) || !"".equals(qMessage2)) {

//POST url//
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("cat_id", catID));
                    params.add(new BasicNameValuePair("topic", qMessage1));
                    params.add(new BasicNameValuePair("desc", qMessage2));
                    params.add(new BasicNameValuePair("owner", username));
                    jParser.getJSONUrl(getURLServer + "SendPost", params);


                    Toast.makeText(getApplicationContext(),
                            "เลือกรูปภาพ" + qMessage1, Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getApplicationContext(), PostImageActivity.class);
                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show();
                }


            }
        });

        /// Start button back
        Btn_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                } else if (roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                }

            }
        });
        ImageButton home = (ImageButton) findViewById(R.id.btnHome);
        home.setImageResource(R.drawable.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                } else if (roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", roleID);
                    startActivity(it);
                }
            }
        });
        TextView clickpost = (TextView) this.findViewById(R.id.ClickPost);
        clickpost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String qMessage1 = etdtoppic.getText().toString();
                String qMessage2 = etdDetail.getText().toString();
                if (!"".equals(qMessage1) || !"".equals(qMessage2)) {


                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("cat_id", catID));
                    params.add(new BasicNameValuePair("topic", qMessage1));
                    params.add(new BasicNameValuePair("desc", qMessage2));
                    params.add(new BasicNameValuePair("owner", username));


                    jParser.getJSONUrl(getURLServer + "SendPost", params);
                    Toast.makeText(getApplicationContext(),
                            "โพสเรียร้อย", Toast.LENGTH_LONG).show();
                    if (roleID.equals("3")) {
                        Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", roleID);
                        startActivity(it);
                    } else if (roleID.equals("2")) {
                        Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", roleID);
                        startActivity(it);
                    } else if (roleID.equals("1")) {
                        Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", roleID);
                        startActivity(it);
                    }
                } else {
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
