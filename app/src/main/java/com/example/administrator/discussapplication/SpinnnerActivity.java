package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SpinnnerActivity extends ActionBarActivity {
    private ImageLoader imageLoader;
    private ListView listV;
    private ImageAdapter imageAdap;
    private static   String getURLServer = "http://192.168.236.1:8070/DiscussAppWeb/";
    public String topicID,username,catID,roleID ;
    private String TopicId,CatId,Username,Catname;
    ///value Spinner

    private static final String TAG_cat_id_SPINNER = "cat_id";
    private static final String TAG_cat_topic_SPINNER = "cat_topic";
    private static final String TAG_USER_SPINNER = "userName";
    private static final String TAG_DATA_SPINNER = "data";
    private JSONArray DataSpinner=null;
    final ArrayList<String> spinnerlist =new ArrayList<String>();
    ArrayList<HashMap<String, String>> spinner = new ArrayList<HashMap<String, String>>();
    ////ListView
    Bitmap newBitmap;

    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String URLImg = getURLServer+"images/";
    ArrayAdapter<String> arrAd;
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinnner);

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
        ImageButton btnSearchlist = (ImageButton)this.findViewById(R.id.btnSearchlist);
        btnSearchlist.setImageResource(R.drawable.searh);
        ImageButton imgBtnBack = (ImageButton)this.findViewById(R.id.imgBtnBack);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //SearchList
        btnSearchlist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("topic", GetCatname());
                it.putExtra("role_id", "3");
                startActivity(it);
            }
        });

        //////back//////////
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent it = new Intent(getApplicationContext(), LandingActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    it.putExtra("role_id", "3");
                    startActivity(it);
                }




        });




        //////start spinner
        String urlSpinner = getURLServer + "jsonAllCat";
        JSONParser jParser = new JSONParser();
        JSONObject jsonSpinner = jParser.getJSONFromUrl(urlSpinner);
        try

        {

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


        }

        catch(
                JSONException e
                )

        {
            e.printStackTrace();
            Toast.makeText(this, "ไม่มีการเชื่อมต่อ..",
                    Toast.LENGTH_LONG).show();
        }


        Spinner spin = (Spinner) findViewById(R.id.spinnerView);


        arrAd=new ArrayAdapter<String>(SpinnnerActivity.this,android.R.layout.simple_list_item_1,spinnerlist);

        spin.setAdapter(arrAd);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            public void onItemSelected (AdapterView < ? > adapterView,
                                        View view,int i, long l){
// TODO Auto-generated method stub

                Catname = spinnerlist.get(i);
                SetCatname(Catname);
                catID = spinner.get(i).get("cat_id");

                Toast.makeText(SpinnnerActivity.this,
                        String.valueOf("Your Selected : " + spinnerlist.get(i) + catID),
                        Toast.LENGTH_SHORT).show();


                // listV.setAdapter(imageAdap);

                // arrAd.notifyDataSetChanged();
                final ArrayList<HashMap<String, Object>> backUpList = ListData(catID);
                imageLoader.clearCache();
                listV.setClipToPadding(false);

                imageAdap = new ImageAdapter(getApplicationContext());
                listV.setAdapter(imageAdap);

                listV.setOnScrollListener(new EndlessScrollListener() {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to your AdapterView
                        customLoadMoreDataFromApi(page);
                        // or customLoadMoreDataFromApi(totalItemsCount);
                    }

                });

                final AlertDialog.Builder viewDetail = new AlertDialog.Builder(SpinnnerActivity.this);
                // OnClick Item
                listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> myAdapter, View myView,
                                            int position, long mylng) {

                        topicID = backUpList.get(position).get("topic_id").toString();

                        catID = backUpList.get(position).get("cat_id").toString();

                        Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catID);
                        it.putExtra("role_id", "3");
                        startActivity(it);
                        imageLoader.clearCache();
                    }


                });

            }


            public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
                Toast.makeText(SpinnnerActivity.this,
                        String.valueOf("Your Selected Empty"),
                        Toast.LENGTH_SHORT).show();
            }


        });

        ///////end spinner


        ///////end spinner

        /////////EndListView






    }

    public ArrayList<HashMap<String, Object>> ListData(String catId){

        cateList.clear();
        JSONParser jParser = new JSONParser();

        Log.i("CAT_ID",catID);
        listV = (ListView) findViewById(R.id.listSearchlist);
        String url = getURLServer+"jsonPost_reply2?cat_id="+catId+"";


        //StartListview
        JSONObject json = jParser.getJSONFromUrl(url);
        imageLoader = new ImageLoader(this);
        // GridView and imageAdapter

        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);
                String catID2 = c.getString(TAG_CAT_ID);
                String topic = c.getString(TAG_TOPIC);
                String owner = c.getString(TAG_OWNER);
                String img = c.getString(TAG_IMG);
                String dateTime = c.getString(TAG_TIME);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_CAT_ID, catID2);
                map.put(TAG_TOPIC, topic);
                map.put(TAG_OWNER, owner);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object
                String urlBitMap = URLImg+img;
                newBitmap = imageLoader.getBitmap(urlBitMap);


                map.put("ImagePathBitmap", newBitmap);
                cateList.add(map);


            }
            /// Start Grid//
            // Next

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()
                    ,"เชื่อมต่อระบบล้มเหลว ",Toast.LENGTH_LONG).show();
        }



        return cateList;
    }


    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
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
        public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
        {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
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
            if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
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

        class ViewHolderItem {
            ImageView imageView;
            TextView txtImageID;
            TextView txtItemID;
            TextView txtTimeID;
            int position = -1;
            Handler handler;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolderItem    viewHolder = new ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.activity_column_cate, null);
            }
                viewHolder.imageView= (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(viewHolder);

                // ColPhoto

                //imageView.getLayoutParams().height = 60;
                // imageView.getLayoutParams().width = 60;
                viewHolder.imageView.setPadding(5, 5, 5, 5);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
                    Log.d("ERROR", cateList.get(position).get("ImagePathBitmap").toString());
                    viewHolder.imageView.setImageBitmap((Bitmap)cateList.get(position).get("ImagePathBitmap"));
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
                viewHolder. txtItemID = (TextView) convertView.findViewById(R.id.Owner);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(cateList.get(position).get("owner").toString());
// ColItemTime
                viewHolder. txtTimeID = (TextView) convertView.findViewById(R.id.timestamp);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(cateList.get(position).get("dateTime").toString());



            if(convertView!=null){
                viewHolder= (ViewHolderItem) convertView.getTag();
            }

            return convertView;

        }

    }
    public String GetCatname(){
        return Catname;
    }
    public void SetCatname(String Catname){
        this.Catname=Catname;
    }
    @Override
    public void onDestroy()
    {   Log.i("onDestory","end");
        listV.setAdapter(null);
        imageLoader.clearCache();
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ออกจากแอฟพลิเคชัน ?");
        dialog.setIcon(R.drawable.ic_launcher);
        dialog.setCancelable(true);
        dialog.setMessage("ต้องออกจากแอฟพลิเคชันหรือไม่ ");
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}