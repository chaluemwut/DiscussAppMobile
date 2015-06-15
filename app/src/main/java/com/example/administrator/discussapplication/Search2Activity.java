package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Search2Activity extends ActionBarActivity {
    private static String getURLServer = "http://192.168.1.2:8080/DiscussWeb/";
    public String topicID, username, catID,topic;
    Bitmap newBitmap;
    private ImageLoader imageLoader;
    private ListView lisView1;
    private ImageAdapter imageAdap;
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String URLImg = getURLServer + "images/";
    ArrayAdapter<String> arrAd;
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        // Permission StrictMode

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        lisView1 = (ListView) findViewById(R.id.listSearchlist);
        ImageButton imgBtnBack = (ImageButton)findViewById(R.id.imgBtnBack_Edit);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            this.topicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.topic = intent.getString("topic");
            // and get whatever type user account id is
        }
        EditText edtShowCat =(EditText) findViewById(R.id.edtShowCat);
        edtShowCat.setText(topic);
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);
////back
        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);

                startActivity(it);
            }
        });
  ////Listview
         ImageButton btn1 = (ImageButton) findViewById(R.id.btnSearch);
        btn1.setImageResource(R.drawable.searh2);
        // Perform action on click

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // listView is your instance of your ListView

                SearchData();
            }
        });

    }





    public void SearchData() {
        // listView1
        cateList.clear();
        //lisView1 = (ListView) findViewById(R.id.listSearchlist);


        // editSearch
        final EditText inputText = (EditText) findViewById(R.id.btnSearchlist);


        String url = getURLServer + "SearchAPI";

        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("txt", inputText.getText().toString()));
        params.add(new BasicNameValuePair("cat_id",catID));
        try {
            JSONObject json = new JSONObject(getJSONUrl(url, params));


            imageLoader = new ImageLoader(this);
            // GridView and imageAdapter


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
                String urlBitMap = URLImg + img;
                newBitmap = imageLoader.getBitmap(urlBitMap);


                map.put("ImagePathBitmap", newBitmap);
                cateList.add(map);


            }

            if (cateList.equals(null)) {
            Toast.makeText(getApplicationContext(),
                    "ไม่มีข้อมูลที่ค้นหา", Toast.LENGTH_LONG).show();
        }


            lisView1.setClipToPadding(false);

            imageAdap = new ImageAdapter(getApplicationContext());
            lisView1.setAdapter(imageAdap);

            lisView1.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    customLoadMoreDataFromApi(page);
                    // or customLoadMoreDataFromApi(totalItemsCount);
                }

            });

            final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
            // OnClick Item
            lisView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView,
                                        int position, long mylng) {
                    topicID = cateList.get(position).get("topic_id").toString();

                    catID = cateList.get(position).get("cat_id").toString();

                    Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                    it.putExtra("topic_id", topicID);
                    it.putExtra("username", username);
                    it.putExtra("cat_id", catID);
                    startActivity(it);
                    imageLoader.clearCache();

                }

            });


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


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
            private List<ImageAdapter> items = null;
            private Context mContext;
            private int index;

            public ImageAdapter(Context context) {
                mContext = context;
            }
            public void clearlistview()
            {
                 cateList.clear();
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


                ViewHolderItem viewHolder = null;
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


                if (convertView == null) {
                    viewHolder = new ViewHolderItem();
                    convertView = inflater.inflate(R.layout.activity_column_cate, null);
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


                }
                else{
                    viewHolder= (ViewHolderItem) convertView.getTag();

                }

                return convertView;

            }

        }



    public String getJSONUrl(String url, List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Download OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download file..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search2, menu);
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
