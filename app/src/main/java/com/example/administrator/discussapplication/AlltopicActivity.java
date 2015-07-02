package com.example.administrator.discussapplication;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class AlltopicActivity extends Activity {
    private static   String getURLServer = "http://192.168.42.46:8080/DiscussWeb/";

    private static String url = getURLServer+"jsonAllCat";
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
            toast=  Toast.makeText(getBaseContext(), "??????????????????????? Discuss App", Toast.LENGTH_SHORT);
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
                        ,"????????? ?????????",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
