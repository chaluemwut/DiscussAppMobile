package com.example.administrator.discussapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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
    private static   String getURLServer = "http://192.168.1.2:8080/DiscussWeb/";
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
    String urlImage,img,topicname,topicID2,owner,datetime,id,topicID,catID,username;

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
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);

        btnBack.setImageResource(R.drawable.back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), LandingActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                System.out.println("");
                startActivity(it);

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
                Toast.makeText(CommentActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }


}
