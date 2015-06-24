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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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


public class Comment2Activity extends ActionBarActivity {
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();
    private static final String TAG_ID = "id";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_DESC = "description";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "date_time";
    private static final String TAG_DATA = "data";
    private ListView ListV2;
    JSONArray Data = null;
    private ListView ListV;
    ArrayList<HashMap<String, Object>> postList = new ArrayList<>();
    ImageView ImgPost;
    String toppicID ,catID,username,roleID;
    //getset
    JSONParser jParser = new JSONParser();
    private String CatId,TopicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Comment);
        btnBack.setImageResource(R.drawable.back);
        ImageButton btnPost = (ImageButton) this.findViewById(R.id.comment2);
        btnPost.setImageResource(R.drawable.postcomment);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.toppicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.roleID=intent.getString("role_id");
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);

        SetTopicId(toppicID);
        SetCatId(catID);

        //refresh

        //backTo CommentActivity

        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                    it.putExtra("topic_id", toppicID);
                    it.putExtra("cat_id", catID);
                    it.putExtra("username", username);
                    it.putExtra("role_id", "3");

                    System.out.println("");
                    startActivity(it);
                }
                else if(roleID.equals("2")) {
                    Toast.makeText(getApplicationContext(),
                            "แก้ไขเรียบร้อย", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);
                    it.putExtra("topic_id", toppicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "2");
                    System.out.println("");
                    startActivity(it);
                }
                else  if(roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", toppicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "1");
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

                        it.putExtra("topic_id", toppicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", "3");
                        startActivity(it);
                    } else if (roleID.equals("2")) {
                        Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                        it.putExtra("topic_id", toppicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", "2");
                        startActivity(it);
                    } else if (roleID.equals("1")) {
                        Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                        it.putExtra("topic_id", toppicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", "1");
                        startActivity(it);
                    }
                }

        });
        ListV2 = (ListView) findViewById(R.id.listViewComment2);

        String url = getURLServer+"jsonPost_reply?&topic_id="+toppicID+"";
        JSONObject json = jParser.getJSONFromUrl(url);
        try {
// Comment Text

            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);

                String ID = c.getString(TAG_ID);

                String desc = c.getString(TAG_DESC);

                String name = c.getString(TAG_NAME);

                String dateTime = c.getString(TAG_TIME);


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_ID, ID);
                map.put(TAG_DESC, desc);
                map.put(TAG_NAME, name);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object

                postList.add(map);

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(Comment2Activity.this, postList, R.layout.activity_comment_text,
                    new String[]{"description", "name", "date_time"}, new int[]{R.id.Detail2Comment, R.id.post2Comment, R.id.Time2Comment});
            ListV2.setAdapter(sAdap);


////Post
            final EditText etdcomment =(EditText) this.findViewById(R.id.edtComment2);
            btnPost.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    String desc=etdcomment.getText().toString();
                    if(desc.equals("")){
                        Toast.makeText(Comment2Activity.this,String.valueOf("ไม่มีข้อความ"),Toast.LENGTH_SHORT).show();
                    }
                    else {
///Post servlet
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("name", username));
                            params.add(new BasicNameValuePair("desc",desc));
                            params.add(new BasicNameValuePair("top_id", toppicID));


                            //HttpPost httpPost = new HttpPost(getURLServer+"RegisterAPI");
                            jParser.getJSONUrl(getURLServer+"PostReplyAPI",params);


                            Toast.makeText(getApplicationContext(),
                                    "แสดงความคิดเห็นเรียบร้อย", Toast.LENGTH_LONG).show();
                            //refresh
                            Intent it = new Intent(getApplicationContext(), Comment2Activity.class);
                            it.putExtra("topic_id", toppicID);
                            it.putExtra("cat_id", catID);
                            it.putExtra("username", username);
                            it.putExtra("role_id", roleID);
                            System.out.println("");
                            startActivity(it);

                    }

                }
            });
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    ,"เชื่อมต่อระบบล้มเหลว ",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }



    public String GetTopicId(){
        return TopicId;
    }
    public void SetTopicId(String TopicId){
        this.TopicId=TopicId;
    }
    public String GetCatId(){
        return CatId;
    }
    public void SetCatId(String CatId){
        this.CatId=CatId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment2, menu);
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
            case R.id.action_settings:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                it.putExtra("topic_id", "");
                it.putExtra("username","");
                it.putExtra("cat_id","");
                it.putExtra("role_id","");
                Toast.makeText(getApplicationContext()
                        ,"ล็อกเอาท์ เรียบร้อย",Toast.LENGTH_LONG).show();
                System.out.println("");
                SaveSharedPreference.clearUserName(Comment2Activity.this);
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
