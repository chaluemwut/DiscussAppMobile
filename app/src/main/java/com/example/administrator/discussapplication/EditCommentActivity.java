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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class EditCommentActivity extends ActionBarActivity {
    private static   String getURLServer = "http://192.168.1.2:8080/DiscussWeb/";
    Bitmap bitmap;
    ProgressDialog pDialog;
    ImageView ImgPost;
    EditText etdCate ;
    EditText edtTextTopicEdit ;
    TextView edtTextDescEdit;

    ArrayList<HashMap<String, Object>> catList = new ArrayList<>();
    private static final String TAG_CATENAME = "cat_topic";
    private static final String TAG_OWNER_CATE = "username";
    private static final String TAG_DATE = "date_time";
    private static final String TAG_NUM_COUNT = "num_reply";
    private static final String TAG_DATA = "data";


    //JSON Node Names Gridviwe

    private static final String TAG_TOPIC_ID2 = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_COMMENT = "description";
    private static final String TAG_IMG = "img";
    private static final String TAG_TIME2 = "dateTime";
    private static final String URLImg = getURLServer+"images/";
    private  String getTopicName,getDesc;
    JSONArray Data = null;
//get Intent
    String topicID,catID,username;
//get JsonjsonPost_reply3
    String urlImage,img,topicname,owner,datetime,id,comment,topicID2;
// jsonShowCat
    String catID2,ownerCat,date,numCount,catName;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        ImgPost =(ImageView)this.findViewById(R.id.ImgBtnAddImg);

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
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Edit);
        btnBack.setImageResource(R.drawable.back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                System.out.println("");
                startActivity(it);

            }
        });
        String urlShowCatName = getURLServer+"jsonShowCat?cat_id="+catID+"";
        JSONObject json = jParser.getJSONFromUrl(urlShowCatName);

        try {

            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                catID2 = c.getString(TAG_CAT_ID);
                catName = c.getString(TAG_CATENAME);

                ownerCat = c.getString(TAG_OWNER_CATE);
                date = c.getString(TAG_DATE);
                numCount = c.getString(TAG_NUM_COUNT);

            }
        }  catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        // Getting JSON from URL
       // String url2 = getURLServer+"jsonPost_reply3?topic_id="+toppicID+"&cat_id="+catID+"";
        String urlShowPost = getURLServer+"jsonPost_reply3?topic_id="+topicID+"&cat_id="+catID+"";
        JSONObject json2 = jParser.getJSONFromUrl(urlShowPost);

        try {

            Data = json2.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                topicID2 = c.getString(TAG_TOPIC_ID2);
                id = c.getString(TAG_CAT_ID);
                comment = c.getString(TAG_COMMENT);
                topicname = c.getString(TAG_TOPIC);
                owner = c.getString(TAG_OWNER);
                img = c.getString(TAG_IMG);
                datetime = c.getString(TAG_TIME2);

                this.urlImage = URLImg + img;



            }
            new LoadImage().execute(urlImage);
             etdCate =(EditText)this.findViewById(R.id.etdCate);
             edtTextTopicEdit =(EditText)this.findViewById(R.id.edtTextTopicEdit);
             edtTextDescEdit =(TextView)this.findViewById(R.id.edtTextDescEdit);

            etdCate.setText(catName);
            edtTextTopicEdit.setText(topicname.toString());
            edtTextDescEdit.setText(comment.toString());


        }  catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Button btnEdit =(Button) this.findViewById(R.id.btnEdit);

        //Update to Dataase
        btnEdit.setOnClickListener(new View.OnClickListener (){
            public void onClick (View v){
                getTopicName=edtTextTopicEdit.getText().toString() ;
                getDesc=edtTextDescEdit.getText().toString();
                URL urlAddUser = null;
                try {
                    if(!getTopicName.equals(topicname)||!getDesc.equals(comment)){
                        // urlAddUser = new URL(getURLServer+"UpDatePost?topic_id="+topicID2+"&cat_id="+catID2+"&topic="+getTopicName+"&desc="+getDesc);
                        urlAddUser = new URL(getURLServer+"UpDatePost?topic_id="+topicID2+"&cat_id="+catID2+"&topic="+getTopicName+"&desc="+getDesc);


                        Scanner scUser = new Scanner(urlAddUser.openStream());

                        Toast.makeText(getApplicationContext(),
                                "แก้ไขเรียบร้อย", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        System.out.println("");
                        startActivity(it);
                    }

                    else {

                        Toast.makeText(getApplicationContext(),
                                "กรุณาแก้ไขกระทู้ของคุณ", Toast.LENGTH_LONG).show();
                    }
                }catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
    }
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditCommentActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

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
                Toast.makeText(EditCommentActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_comment, menu);
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
