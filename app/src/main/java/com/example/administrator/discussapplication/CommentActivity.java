package com.example.administrator.discussapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class CommentActivity extends ActionBarActivity {
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();
    Bitmap bitmap;
    ProgressDialog pDialog;
    ImageView ImgPost;


    private static final String TAG_DATA = "data";

    private ListView ListV;
    ArrayList<HashMap<String, Object>> postList = new ArrayList<>();

    //JSON Node Names Gridviwe

    private static final String TAG_TOPIC_ID2 = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA2 = "data";
    private static final String TAG_TIME2 = "dateTime";
    private static final String URLImg = getURLServer+"images/";
    HashMap<String, Object> map2;
    JSONArray Data = null;

    ArrayList cateList = new ArrayList<>();
    String urlImage,img,topicname,topicID2,owner,datetime,id,topicID,catID,username,roleID;

    ArrayList<String> listdata = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);



        ImgPost = (ImageView)this.findViewById(R.id.imageComment);
        ImgPost.setImageResource(R.drawable.add_image2);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Comment);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONParser jParser = new JSONParser();
        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.topicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.roleID=intent.getString("role_id");
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);

        btnBack.setImageResource(R.drawable.back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "3");
                    startActivity(it);
                }
                else  if(roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "2");
                    startActivity(it);
                }
                else  if(roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "1");
                    startActivity(it);
                }

            }
        });
        // Getting JSON from URL
        String url2 = getURLServer+"jsonPost_reply3?topic_id="+topicID+"&cat_id="+catID+"";
        JSONObject json2 = jParser.getJSONFromUrl(url2);

        try {

            Data = json2.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                 topicID2 = c.getString(TAG_TOPIC_ID2);
                 id = c.getString(TAG_CAT_ID);
                 topicname = c.getString(TAG_TOPIC);
                 owner = c.getString(TAG_OWNER);
                 img = c.getString(TAG_IMG);
                 datetime = c.getString(TAG_TIME2);

                this.urlImage = URLImg + img;



            }

            }  catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    ,"เชื่อมต่อระบบล้มเหลว ",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

          new LoadImage().execute(urlImage);
        TextView commetTopic =(TextView)this.findViewById(R.id.DetailComment3);
        TextView ownerTopic =(TextView)this.findViewById(R.id.postComment3);
        TextView timeTopic =(TextView)this.findViewById(R.id.TimeComment);
        commetTopic.setText(topicname);
        ownerTopic.setText(owner);
        timeTopic.setText(datetime);
        //Comment2
        Button btnComment = (Button)this.findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), Comment2Activity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);
                System.out.println("");
                startActivity(it);

            }
        });

    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CommentActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        } protected void onPostExecute(Bitmap image) {

            if(image != null){
                ImgPost.setImageBitmap(image);
                pDialog.dismiss();

            }else{

                pDialog.dismiss();

            }
        }
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
                SaveSharedPreference.clearUserName(CommentActivity.this);
                System.out.println("");
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
