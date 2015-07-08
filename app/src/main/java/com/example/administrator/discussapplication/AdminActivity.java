package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdminActivity extends ActionBarActivity {
    private static final String TAG_CAT_NAME = "cat_topic";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    ///value Spinner
    private static final String TAG_IMG = "img";
    //JSON Node Names Gridviwe
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    static Config con = new Config();
    private static String getURLServer = con.getURL();
    private static final String URLImg = getURLServer + "images_re/";
    private static long back_pressed;
    public ImageLoader imageLoader;
    JSONArray Data = null;
    JSONArray Data2 = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    ArrayList<HashMap<String, Object>> cateList2 = new ArrayList<>();
    //ArrayList<HashMap<String, Object>> cateList = new ArrayList<HashMap<String, Object>>();
    JSONParser jParser = new JSONParser();
    private GridView gridV;
    private ImageAdapter imageAdap;
    private String catIDAdmin;
    private String username, topicID, catID, roleID;
    private String TopicId, CatId, Username;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton BtnPost = (ImageButton) this.findViewById(R.id.PostIcon);
        ImageButton BtnUpdate = (ImageButton) this.findViewById(R.id.UpdateIcon);
        ImageButton BtnSreach = (ImageButton) this.findViewById(R.id.btnSearh);
        ImageView ImgUser = (ImageView) this.findViewById(R.id.imgUser_Landing);
        ImageButton btnAdmin = (ImageButton) this.findViewById(R.id.btnAdmin);
        ImgUser.setImageResource(R.drawable.bt_id);
        BtnPost.setImageResource(R.drawable.add1);
        BtnUpdate.setImageResource(R.drawable.icon);
        BtnSreach.setImageResource(R.drawable.searh);
        btnAdmin.setImageResource(R.drawable.admin);
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
        TextView TB = (TextView) this.findViewById(R.id.TB);
        TB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);
            }
        });



        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);

                startActivity(it);
            }
        });

        BtnSreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (roleID.equals("3")) {
                    Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "3");
                    startActivity(it);
                } else if (roleID.equals("2")) {
                    Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "2");
                    startActivity(it);
                } else if (roleID.equals("1")) {
                    Intent it = new Intent(getApplicationContext(), SearchAdminActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "1");
                    startActivity(it);
                }
            }
        });


        ///// button Post Activity
        BtnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), PostActivity.class);

                it.putExtra("username", username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "1");
                Toast.makeText(getApplicationContext()
                        , "เพิ่มกระทู้", Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });
        BtnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "1");
                Toast.makeText(getApplicationContext()
                        , "แก้ไขข้อมูลส่วนตัว" + roleID, Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });

        new LoadViewTask().execute();
    }

    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }

    public String GetTopicId() {
        return TopicId;
    }

    public void SetTopicId(String TopicId) {
        this.TopicId = TopicId;
    }

    public String GetUsername() {
        return Username;
    }

    public void SetUsername(String Username) {
        this.Username = Username;
    }

    private void displayListView() {

        /// Start Grid//
        gridV = (GridView) findViewById(R.id.gridViewAdmin);
        // Next
        gridV.setClipToPadding(false);
        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        // OnClick Item
        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {

                topicID = cateList.get(position).get("topic_id").toString();
                SetTopicId(topicID);
                catID = cateList.get(position).get("cat_id").toString();
                imageAdap.SetCatId(catID);
                Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", roleID);

                startActivity(it);
            }

        });

        /////////setAdaptet GridView
        imageAdap = new ImageAdapter(getApplicationContext());
        gridV.setAdapter(imageAdap);

        gridV.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }



    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) {
                    this.loading = true;
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }

//////////////////////////////
/////////////////////////
public class LoadViewTask extends AsyncTask<Void, Void, Boolean> {


    ProgressDialog pd;


    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(AdminActivity.this);
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
    private void updateJSON() {
        Bitmap newBitmap;
        String url = getURLServer + "jsonShowCatID";
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        // GridView and imageAdapter
        cateList.clear();
        cateList2.clear();
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
////get Cat_Name

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
}

    //////////////////////////
    /////ImageAdapter/////
    class ImageAdapter extends BaseAdapter {

        String CatId;
        private Context mContext;
        public ImageAdapter(Context context) {
            mContext = context;
        }

        public String GetCatId() {
            return CatId;
        }

        public void SetCatId(String CatId) {
            this.CatId = CatId;
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            catIDAdmin = GetCatId();

            ViewHolderItem viewHolder = null;
            viewHolder = new ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {


                convertView = inflater.inflate(R.layout.activity_landing_admin, null);
            }
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
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
                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.ColTopic);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(cateList.get(position).get("topic").toString());

                // ColItemID
                viewHolder.txtItemID = (TextView) convertView.findViewById(R.id.Colowner);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(cateList.get(position).get("owner").toString());

                viewHolder. txtTimeID = (TextView) convertView.findViewById(R.id.ColCatName);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(cateList2.get(position).get("cat_topic").toString());

                //Update
                viewHolder.btnUpdate = (ImageButton) convertView.findViewById(R.id.imgCmdupdate);
                viewHolder.btnUpdate.setImageResource(R.drawable.edit2);


                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(AdminActivity.this, "แก้ไขข้อมูล" + cateList.get(position).get("topic") + "", Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), EditCommentActivity.class);
                        String topPicId = cateList.get(position).get("topic_id").toString();
                        catIDAdmin = cateList.get(position).get("cat_id").toString();
                        it.putExtra("topic_id", topPicId);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catIDAdmin);
                        it.putExtra("role_id", "1");


                        Toast.makeText(getApplicationContext()
                                , "แก้ไขข้อมูล" , Toast.LENGTH_LONG).show();
                        System.out.println("");
                        startActivity(it);
                    }
                });
                //Delete
                viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.imgCmdelete);
                viewHolder.btnDelete.setImageResource(R.drawable.delete);
                // imgCmdDelete

                //cmdDelete.setBackgroundColor(Color.TRANSPARENT);
                final AlertDialog.Builder adb = new AlertDialog.Builder(AdminActivity.this);
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        adb.setTitle("ต้องการลบกระทู้?");
                        adb.setMessage("คุณแน่ใจว่าจะลบ [" + cateList.get(position).get("topic") + "]");
                        adb.setNegativeButton("ยกเลิก", null);
                        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminActivity.this, "ลบกระทู้ "+ cateList.get(position).get("topic") + "เรียบร้อย", Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();
                                catIDAdmin = cateList.get(position).get("cat_id").toString();



                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("topic_id", topicID));
                                params.add(new BasicNameValuePair("cat_id", catIDAdmin));

                                jParser.getJSONUrl(getURLServer + "DeleteTopic",params);
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDAdmin);
                                it.putExtra("role_id", "1");

                                startActivity(it);
                            }
                        });
                        adb.show();
                    }
                });
                ///Top
                viewHolder.btnPoint = (ImageButton) convertView.findViewById(R.id.imgCmdpoint);
                viewHolder.btnPoint.setImageResource(R.drawable.point);

                final AlertDialog.Builder dialogP = new AlertDialog.Builder(AdminActivity.this);
                viewHolder.btnPoint.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogP.setTitle("ต้องการปักหมุดกระทู้ ?");
                        dialogP.setMessage("กระทู้ที่เลือก " + cateList.get(position).get("topic") );


                        dialogP.setNegativeButton("ยกเลิกการปักหมุด", new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(AdminActivity.this, "ยกเลิกการปักหมุดของ " + cateList.get(position).get("topic") , Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();
                                catIDAdmin = cateList.get(position).get("cat_id").toString();


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("topic_id", topicID));
                                params.add(new BasicNameValuePair("status", "0"));

                                jParser.getJSONUrl(getURLServer + "UpdateTopComment",params);
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDAdmin);
                                it.putExtra("role_id", "1");

                                startActivity(it);

                            }
                        });


/////Delete Top Comment
                        dialogP.setPositiveButton("ปักหมุดกระทู้", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AdminActivity.this, "ปักหมุกกระทู้ " + cateList.get(position).get("topic"), Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();
                                catIDAdmin = cateList.get(position).get("cat_id").toString();


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("topic_id", topicID));
                                params.add(new BasicNameValuePair("status", "1"));

                                jParser.getJSONUrl(getURLServer + "UpdateTopComment",params);
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDAdmin);
                                it.putExtra("role_id", "1");

                                startActivity(it);
                            }
                        });
                        dialogP.show();
                    }
                });
                viewHolder.btnGO = (ImageButton) convertView.findViewById(R.id.toComment);
                viewHolder.btnGO.setImageResource(R.drawable.go);


                viewHolder.btnGO.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        /**
                         * Command for Delete
                         * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                         */
                        Toast.makeText(AdminActivity.this, ""+cateList.get(position).get("topic")+"", Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), CommentActivity.class);
                        topicID = cateList.get(position).get("topic_id").toString();
                        catIDAdmin = cateList.get(position).get("cat_id").toString();
                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catIDAdmin);
                        it.putExtra("role_id", "1");

                        startActivity(it);
                    }


                });

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
            ImageButton btnUpdate, btnDelete, btnPoint, btnGO;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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
            case R.id.action_settings:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                it.putExtra("topic_id", "");
                it.putExtra("username","");
                it.putExtra("cat_id","");
                it.putExtra("role_id","");
                Toast.makeText(getApplicationContext()
                        ,"ล็อกเอาท์เรียบร้อย",Toast.LENGTH_LONG).show();
                System.out.println("");
                SaveSharedPreference.clearUserName(AdminActivity.this);
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
