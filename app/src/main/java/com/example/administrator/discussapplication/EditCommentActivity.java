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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EditCommentActivity extends ActionBarActivity {
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();

    Bitmap bitmap;

    ProgressDialog pDialog;
    ImageView ImgPost,viewImage;
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
    String topicID,catID,username,roleID;
//get JsonjsonPost_reply3
    String urlImage,img,topicname,owner,datetime,id,comment,topicID2;
// jsonShowCat
    String catID2,ownerCat,date,numCount,catName;

    JSONParser jParser = new JSONParser();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        ImgPost =(ImageView)this.findViewById(R.id.ImgBtnAddImg);
        viewImage=(ImageView)this.findViewById(R.id.viewImage);
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
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Edit);

        btnBack.setImageResource(R.drawable.back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if (roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "3");
                    System.out.println("");
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), Staff2Activity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "2");
                    startActivity(it);
                }
                else  if(roleID.equals("")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "1");
                    startActivity(it);
                }

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
            etdCate =(EditText)this.findViewById(R.id.etdCate);
            edtTextTopicEdit =(EditText)this.findViewById(R.id.edtTextTopicEdit);
            edtTextDescEdit =(TextView)this.findViewById(R.id.edtTextDescEdit);

            etdCate.setText(catName);


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
            edtTextTopicEdit.setText(topicname);
            edtTextDescEdit.setText(comment);


        }  catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Button btnEdit =(Button) this.findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener (){
            public void onClick (View v){
                getTopicName=edtTextTopicEdit.getText().toString() ;
                getDesc=edtTextDescEdit.getText().toString();
                URL urlAddUser = null;

                    if (!getTopicName.equals(topicname) || !getDesc.equals(comment)) {


                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("topic_id", topicID2));
                        params.add(new BasicNameValuePair("cat_id", catID2));
                        params.add(new BasicNameValuePair("topic", getTopicName));
                        params.add(new BasicNameValuePair("desc", getDesc));
                        jParser.getJSONUrl(getURLServer + "UpDatePost", params);


                    } else {

                        Toast.makeText(getApplicationContext(),
                                "กรุณาแก้ไขกระทู้ของคุณ", Toast.LENGTH_LONG).show();
                    }


                if (roleID.equals("3")) {
                    Toast.makeText(getApplicationContext(),
                            "แก้ไขเรียบร้อย", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("cat_id", "3");
                    System.out.println("");
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Toast.makeText(getApplicationContext(),
                            "แก้ไขเรียบร้อย", Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);
                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("cat_id", "2");
                    System.out.println("");
                    startActivity(it);
                }
            else if (roleID.equals("1")) {
                Toast.makeText(getApplicationContext(),
                        "แก้ไขเรียบร้อย", Toast.LENGTH_LONG).show();
                Intent it = new Intent(getApplicationContext(), AdminActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("cat_id", "1");
                System.out.println("");
                startActivity(it);





            }
            }

        });
        ImageButton home = (ImageButton) findViewById(R.id.btnHome);
        home.setImageResource(R.drawable.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
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
        } protected void onPostExecute(final Bitmap image) {

            if(image != null){
                ImgPost.setImageBitmap(image);

                ImgPost.setOnClickListener(new View.OnClickListener() {
                    ImageView imageView;

                    boolean isImageFitToScreen;
                    @Override
                    public void onClick(View v) {
                        ImgPost.setImageBitmap(null);
                        viewImage.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        viewImage.setImageBitmap(image);
                        ImgPost.setImageBitmap(null);
                    }
                });
                viewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewImage.setImageBitmap(null);
                        ImgPost.setVisibility(View.VISIBLE);
                        v.setVisibility(View.GONE);
                        ImgPost.setImageBitmap(image);

                    }
                });
                pDialog.dismiss();
            }else{

                pDialog.dismiss();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_comment, menu);
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
                SaveSharedPreference.clearUserName(EditCommentActivity.this);
                System.out.println("");
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
