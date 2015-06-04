package com.example.administrator.discussapplication;


import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class AlltopicActivity extends Activity {
    private static String url = "http://192.168.1.109:8070/DiscussAppWeb/jsonAllCat";
    //JSON Node Names
    private static final String TAG_cat_id = "cat_id";
    private static final String TAG_cat_topic = "cat_topic";
    private static final String TAG_DATE = "date";
    private static final String TAG_DATA = "data";

    JSONArray Data = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltopic);
        ImageButton btnAdd = (ImageButton) this.findViewById(R.id.imgBtnAdd_alltopic);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_alltopic);
        btnAdd.setImageResource(R.drawable.add1);
        btnBack.setImageResource(R.drawable.back);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Hashmap for ListView
        ArrayList<HashMap<String, String>> topicList = new ArrayList<HashMap<String, String>>();
        // listView1
        final ListView lisView1 = (ListView) findViewById(R.id.listView1);
        // Creating new JSON Parser
        JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        //String(rs3.getString("owner").getBytes(),"TIS-620")


        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                 String catID =c.getString(TAG_cat_id);
                String catTopic = c.getString(TAG_cat_topic);
                String date = c.getString(TAG_DATE);
                HashMap<String, String> map = new HashMap<String, String>();
               // map = new HashMap<String, String>();
                map.put(TAG_cat_id,catID);
                map.put(TAG_cat_topic, catTopic);
                map.put(TAG_DATE, date);
                topicList.add(map);

            }


            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(AlltopicActivity.this, topicList, R.layout.activity_column_alltopic,
                    new String[]{"cat_id", "cat_topic", "date"}, new int[]{R.id.ColMemberID, R.id.ColName, R.id.ColTel});
            lisView1.setAdapter(sAdap);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alltopic, menu);
        return true;
    }

}
