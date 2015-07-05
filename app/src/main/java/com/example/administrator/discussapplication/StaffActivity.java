package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class StaffActivity extends ActionBarActivity {


    private static final String TAG_CAT_ID_STAFF = "cat_id";
    private static final String TAG_CAT_TOPPIC= "cat_topic";
    private static final String TAG_USERNAME_STAFF = "username";
    private static final String TAG_TIME_CATE = "date";
    private static final String TAG_Count = "num_reply";
    ///value Spinner
    private static final String TAG_CAT_NAME = "cat_topic";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    static Config con = new Config() ;

    //JSON Node Names Gridviwe
    private static   String getURLServer = con.getURL();
    private static final String URLImg = getURLServer+"images/";
    private static long back_pressed;
    public ImageLoader imageLoader;
    /////get Staff
    String nameCatStaff,catIDStaff,usernameStaff,countComment,timeStaffCat;
    JSONArray Data2 = null;
    ArrayList<HashMap<String, Object>> cateList2 = new ArrayList<>();
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    //ArrayList<HashMap<String, Object>> cateList = new ArrayList<HashMap<String, Object>>();
    JSONParser jParser = new JSONParser();
    private GridView gridV;
    private ImageAdapter imageAdap;
    private String username ,topicID,catID,roleID;
    private String TopicId,CatId,Username;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staff);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton BtnPost = (ImageButton) this.findViewById(R.id.PostIcon);
        ImageButton BtnUpdate = (ImageButton) this.findViewById(R.id.UpdateIcon);
        ImageButton BtnSreach = (ImageButton) this.findViewById(R.id.btnSearh);
        ImageButton btnStaff = (ImageButton) this.findViewById(R.id.btnStaff);
        ImageView ImgUser = (ImageView) this.findViewById(R.id.imgUser_Landing);
        ImgUser.setImageResource(R.drawable.bt_id);
        BtnPost.setImageResource(R.drawable.add1);
        BtnUpdate.setImageResource(R.drawable.icon);
        BtnSreach.setImageResource(R.drawable.searh);
        btnStaff.setImageResource(R.drawable.staff);
        //Get Parameter From Login Actvity
        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.username = intent.getString("username");
            this.roleID = intent.getString("role_id");

            // and get whatever type user account id is
        }
        // Get Username//
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);
        String urlStaff = getURLServer + "jsonCatStaff?username="+username+"";
        JSONObject jsonCate = jParser.getJSONFromUrl(urlStaff);
        try {

// Getting JSON
            Data = jsonCate.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                catIDStaff = c.getString(TAG_CAT_ID_STAFF);
                nameCatStaff = c.getString(TAG_CAT_TOPPIC);
                usernameStaff = c.getString(TAG_USERNAME_STAFF);
                timeStaffCat = c.getString(TAG_TIME_CATE);
                countComment = c.getString(TAG_Count);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView cateName = (TextView)this.findViewById(R.id.cateName);
        cateName.setText(nameCatStaff);
        TextView countCate= (TextView)this.findViewById(R.id.countCate);
        countCate.setText(countComment);

        TextView TB = (TextView)this.findViewById(R.id.TB);
        TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "2");
            }
        });



        new LoadViewTask2().execute();


        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Staff2Activity.class);

                it.putExtra("username",username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id",catID);
                it.putExtra("role_id","2");

                System.out.println("");
                startActivity(it);
            }
        });
        BtnSreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                it.putExtra("username",username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id",catID);
                it.putExtra("role_id","2");
                Toast.makeText(getApplicationContext()
                        ,"ค้นหาข้อมูล",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);
            }
        });


        ///// button Post Activity
        BtnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), PostActivity.class);

                it.putExtra("username",username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id",catID);
                it.putExtra("role_id","2");
                Toast.makeText(getApplicationContext()
                        ,"เพิ่มกระทู้",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });
        BtnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username",username);
                it.putExtra("cat_id",catID);
                it.putExtra("role_id","2");
                Toast.makeText(getApplicationContext()
                        ,"แก้ไขข้อมูล",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });
    }

    private void    updateJSON (){

        cateList2 = new ArrayList<>();
        cateList = new ArrayList<>();
        String url = getURLServer + "jsonShowCatID";
        // Creating new JSON Parser
        Bitmap newBitmap;
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        //String(rs3.getString("owner").getBytes(),"TIS-620")
        // Log.i("JSONWOW", json.toString());

        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);
                String catID = c.getString(TAG_CAT_ID);
                String topic = c.getString(TAG_TOPIC);
                String owner = c.getString(TAG_OWNER);
                String img = c.getString(TAG_IMG);
                String dateTime = c.getString(TAG_TIME);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_CAT_ID, catID);
                map.put(TAG_TOPIC, topic);
                map.put(TAG_OWNER, owner);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object
                String urlBitMap =(URLImg+img);

                Log.i("JSONWOW", urlBitMap.toString());

                newBitmap =  imageLoader.getBitmapFromURL(urlBitMap);

                map.put("ImagePathBitmap", newBitmap);
                cateList.add(map);


                String url2 = getURLServer + "jsonShowCat?cat_id=" + catID + "";
                // Getting JSON from URL
                JSONObject json2 = jParser.getJSONFromUrl(url2);

                Data2 = json2.getJSONArray(TAG_DATA);
                for (int i2 = 0; i2 < Data2.length(); i2++) {
                    JSONObject c2 = Data2.getJSONObject(i2);
                    String CAT_NAME = c2.getString(TAG_CAT_NAME);
                    HashMap<String, Object> map2 = new HashMap<String, Object>();
                    map2.put(TAG_CAT_NAME, CAT_NAME);
                    // Thumbnail Get ImageBitmap To Object
                    cateList2.add(map2);

                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    private   void displayListView() {
        /// Start Grid//
        gridV = (GridView) findViewById(R.id.gridViewStaff);
        // Next
        gridV.setClipToPadding(false);
        imageAdap = new ImageAdapter(getApplicationContext());
        gridV.setAdapter(imageAdap);
        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        // OnClick Item
        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {
                topicID = cateList.get(position).get("topic_id").toString();
                SetTopicId(topicID);
                catID = cateList.get(position).get("cat_id").toString();
                SetCatId(catID);
                Intent it = new Intent(getApplicationContext(), CommentActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "2");
                startActivity(it);
            }
        });
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

    public String GetUsername(){
        return Username;
    }

    public void SetUsername(String  Username){
        this. Username=Username;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_staff, menu);
        return true;
    }
    @Override
    public void onDestroy()
    {   Log.i("onDestory","end");
        gridV.setAdapter(null);
        imageLoader.clearCache();
        super.onDestroy();
    }

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
                SaveSharedPreference.clearUserName(StaffActivity.this);
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////////////////////
    public class LoadViewTask2 extends AsyncTask<Void, Void, Boolean> {


        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(StaffActivity.this);
            pd.setMessage("กรุณารอสักครู่...");
            pd.setIndeterminate(false);
            pd.setCancelable(true);
            pd.show();


        }


        @Override
        protected Boolean doInBackground(Void... params) {
            updateJSON();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    displayListView();
                }
            });
        }
    }

    /////ImageAdapter/////
    class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return cateList.size();
        }

        public Object getItem(int position) {
            return cateList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolderItem   viewHolder = new ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.activity_column_cate, null);
            }
                viewHolder.imageView= (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(viewHolder);

                // ColPhoto
                viewHolder.imageView.setPadding(5, 5, 5, 5);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
                    Log.d("ERROR",cateList.get(position).get("ImagePathBitmap").toString());
                    viewHolder.imageView.setImageBitmap((Bitmap)cateList.get(position).get("ImagePathBitmap"));
                } catch (Exception e) {
                    // When Error
                    viewHolder.imageView.setImageResource(R.drawable.add_image);
                }

                // ColImageID
                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.Topic);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(cateList.get(position).get("topic").toString());

                // ColItemID
                viewHolder. txtItemID = (TextView) convertView.findViewById(R.id.Owner);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(cateList.get(position).get("owner").toString());
                // ColItemTime
                viewHolder. txtTimeID = (TextView) convertView.findViewById(R.id.timestamp);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(cateList2.get(position).get("cat_topic").toString());



            if(convertView!=null){
                viewHolder= (ViewHolderItem) convertView.getTag();
            }

            return convertView;

        }

        class ViewHolderItem {
            ImageView imageView;
            TextView txtImageID;
            TextView txtItemID;
            TextView txtTimeID;
            int position = -1;
            Handler handler;
        }

    }
}
