package com.example.administrator.discussapplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
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

    Bitmap bitmap;
    ProgressDialog pDialog;
    ImageView ImgPost;

    private static final String TAG_ID = "id";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_DESC = "description";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "date_time";
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
    private static final String URLImg = "http://192.168.1.12:8080/DiscussWeb/images/";
    HashMap<String, Object> map2;
    JSONArray Data = null;
    JSONArray Data2 = null;
    ArrayList cateList = new ArrayList<>();
    String urlImage;
    String  topicname;
    String img ;
    String  topicID2 ;
    String owner ;
    String datetime ;
    String id;


    ArrayList<String> listdata = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        ImgPost = (ImageView)this.findViewById(R.id.imageComment);
        ImgPost.setImageResource(R.drawable.add_image2);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Comment);
        btnBack.setImageResource(R.drawable.back);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        String url2 = "http://192.168.1.12:8080/DiscussWeb/jsonPost_reply3?topic_id=47&cat_id=42";
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

                //HashMap<String, Object> map = new HashMap<String, Object>();
                // map.put(TAG_TOPIC_ID2, topicID);
                //map.put(TAG_ID, ID);
                // map.put(TAG_DESC, desc);
                // map.put(TAG_NAME, name);
                //map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object

                //cateList.add(map);

            }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

          new LoadImage().execute(urlImage);
        TextView commetTopic =(TextView)this.findViewById(R.id.DetailComment);
        TextView ownerTopic =(TextView)this.findViewById(R.id.postComment);
        TextView timeTopic =(TextView)this.findViewById(R.id.TimeComment);
        commetTopic.setText(topicname);
        ownerTopic.setText(owner);
        timeTopic.setText(datetime);


    }
    private class GETSET{
        String TopicID;
        String id;
        String desc ;
        String name ;
        String datetime ;
        public String getTopicId(){
             return TopicID;
        }
        public  void setTopicID(String TopicID){
            TopicID = this.TopicID;
        }
        public String getId(){
            return id;
        }
        public  void setId(String id){
            id = this.id;
        }
        public String getDesc(){
            return desc;
        }
        public  void setDesc(String desc){
            desc = this.desc;
        }
        public String getName(){
            return name;
        }
        public  void setName(String name){
            name = this.name;
        }
        public String getDatetime(){
            return datetime;
        }
        public  void setDatetime(String datetime){
            name = this.datetime;
        }

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
