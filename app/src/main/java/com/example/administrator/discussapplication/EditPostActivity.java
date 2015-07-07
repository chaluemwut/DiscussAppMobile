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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class EditPostActivity extends ActionBarActivity {
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    //JSON Node Names Gridviwe
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String TAG_CAT_NAME = "cat_topic";
    static Config con = new Config() ;
    private static   String getURLServer = con.getURL();
    private static final String URLImg = getURLServer + "images_re/";
    private static long back_pressed;
    public ImageLoader imageLoader;
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    //ArrayList<HashMap<String, Object>> cateList = new ArrayList<HashMap<String, Object>>();
    JSONParser jParser = new JSONParser();
    JSONArray Data2 = null;
    ArrayList<HashMap<String, Object>> cateList2 = new ArrayList<>();
    private ListView listV;
    private ImageAdapter imageAdap;
    private ImageAdapter imageAdapter;
    private String username, topicID, catID, roleID;
    private String TopicId, CatId, Username;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton btnRefresh = (ImageButton) this.findViewById(R.id.btnRefreshedit);

        //Get Parameter From Login Actvity
        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.topicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.roleID = intent.getString("role_id");
            // and get whatever type user account id is
        }
        // Get Username//
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);




        btnRefresh.setImageResource(R.drawable.refresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), EditPostActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);
                startActivity(it);
            }
        });
        ///// button Post Activity
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Edit);
        btnBack.setImageResource(R.drawable.back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "3");
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), StaffActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "2");
                    startActivity(it);
                } else if (roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "1");
                    startActivity(it);
                }

            }
        });
        new LoadViewTask().execute();
    }

    private void displayListView() {
        listV = (ListView) findViewById(R.id.listViewEdit);
        listV.setClipToPadding(false);

        imageAdap = new ImageAdapter(getApplicationContext());
        listV.setAdapter(imageAdap);


        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        // OnClick Item

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {

                topicID = cateList.get(position).get("topic_id").toString();
                catID = cateList.get(position).get("cat_id").toString();

                Intent it = new Intent(getApplicationContext(), EditCommentActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);
                startActivity(it);
            }

        });
    }

        private void updateJSON() {
            String url = getURLServer + "jsonEditPost?owner=" + username + "";


            //ShowData();
            Bitmap newBitmap;

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            // GridView and imageAdapter

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
                    String urlBitMap = URLImg + img;
                    newBitmap = imageLoader.getBitmapFromURL(urlBitMap);


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
                Toast.makeText(getApplicationContext()
                        , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
            }


        }

    public String GetTopicId() {
        return TopicId;
    }

    public void SetTopicId(String TopicId) {
        this.TopicId = TopicId;
    }

    public String GetCatId() {
        return CatId;
    }

    public void SetCatId(String CatId) {
        this.CatId = CatId;
    }

    public String GetUsername() {
        return Username;
    }

    public void SetUsername(String Username) {
        this.Username = Username;
    }


    public class LoadViewTask extends AsyncTask<Void, Void, Boolean> {


        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(EditPostActivity.this);
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

            ViewHolderItem viewHolder = null;
            viewHolder = new ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.activity_column_cate, null);

            }
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(viewHolder);

                // ColPhoto

                //imageView.getLayoutParams().height = 60;
                // imageView.getLayoutParams().width = 60;
                viewHolder.imageView.setPadding(5, 5, 5, 5);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
                    Log.d("ERROR", cateList.get(position).get("ImagePathBitmap").toString());
                    viewHolder.imageView.setImageBitmap((Bitmap) cateList.get(position).get("ImagePathBitmap"));
                    //  AsyncTask<String, Integer, Bitmap> exec = new Bitmapload(imageView);
                    // exec.execute(cateList.get(position).get("ImagePathBitmap").toString());


                } catch (Exception e) {
                    // When Error
                    viewHolder.imageView.setImageResource(R.drawable.add_image);
                }

                // ColImageID
                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.Topic);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(cateList.get(position).get("topic").toString());

                // ColItemID
                viewHolder.txtItemID = (TextView) convertView.findViewById(R.id.Owner);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(cateList.get(position).get("owner").toString());
// ColItemTime
                viewHolder.txtTimeID = (TextView) convertView.findViewById(R.id.timestamp);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(cateList2.get(position).get("cat_topic").toString());



                viewHolder = (ViewHolderItem) convertView.getTag();


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_post, menu);
        return true;
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
                SaveSharedPreference.clearUserName(EditPostActivity.this);
                System.out.println("");
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


