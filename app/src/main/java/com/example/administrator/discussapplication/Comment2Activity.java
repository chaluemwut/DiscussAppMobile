package com.example.administrator.discussapplication;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Comment2Activity extends ActionBarActivity {
    private static final String TAG_ID = "id";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_DESC = "description";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "date_time";
    private static final String TAG_DATA = "data";
    private ListView ListV2;
    JSONArray Data = null;
    private ListView ListV;
    ArrayList<HashMap<String, Object>> postList = new ArrayList<>();
    ImageView ImgPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Comment);
        btnBack.setImageResource(R.drawable.back);
        ImageButton btnPost = (ImageButton) this.findViewById(R.id.comment2);
        btnPost.setImageResource(R.drawable.postcomment);

        ListV2 = (ListView) findViewById(R.id.listViewComment2);
        JSONParser jParser = new JSONParser();
        String url = "http://192.168.1.12:8080/DiscussWeb/jsonPost_reply?&topic_id=46";
        JSONObject json = jParser.getJSONFromUrl(url);
        try {
// Comment Text

            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);

                String ID = c.getString(TAG_ID);

                String desc = c.getString(TAG_DESC);

                String name = c.getString(TAG_NAME);

                String dateTime = c.getString(TAG_TIME);


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_ID, ID);
                map.put(TAG_DESC, desc);
                map.put(TAG_NAME, name);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object

                postList.add(map);

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(Comment2Activity.this, postList, R.layout.activity_comment_text,
                    new String[]{"description", "name", "date_time"}, new int[]{R.id.Detail2Comment, R.id.post2Comment, R.id.Time2Comment});
            ListV2.setAdapter(sAdap);


        } catch (JSONException e) {
            Toast.makeText(this, "ไม่มีการเชื่อมต่อ..",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment2, menu);
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
