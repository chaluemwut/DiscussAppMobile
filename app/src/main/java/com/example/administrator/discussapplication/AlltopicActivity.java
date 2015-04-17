package com.example.administrator.discussapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AlltopicActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltopic);

        // listView1
        final ListView lisView1 = (ListView)findViewById(R.id.listView1);

        try {

            JSONArray data = createJSON();

            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("MemberID", c.getString("MemberID"));
                map.put("Name", c.getString("Name"));
                map.put("Tel", c.getString("Tel"));
                MyArrList.add(map);
            }


            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(AlltopicActivity.this, MyArrList, R.layout.activity_column_alltopic,
                    new String[] {"MemberID", "Name", "Tel"}, new int[] {R.id.ColMemberID, R.id.ColName, R.id.ColTel});
            lisView1.setAdapter(sAdap);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*** Create JSON ****/
    private JSONArray createJSON() throws JSONException
    {

        ArrayList<JSONObject> MyArrJson = new ArrayList<JSONObject >();
        JSONObject object;

        /*** Rows 1 ***/
        object = new JSONObject();
        object.put("MemberID","1");
        object.put("Name", "Weerachai");
        object.put("Tel", "0819876107");
        MyArrJson.add(object);

        /*** Rows 2 ***/
        object = new JSONObject();
        object.put("MemberID","2");
        object.put("Name", "Win");
        object.put("Tel", "021978032");
        MyArrJson.add(object);

        /*** Rows 3 ***/
        object = new JSONObject();
        object.put("MemberID","3");
        object.put("Name", "Eak");
        object.put("Tel", "0876543210");
        MyArrJson.add(object);

        JSONArray json = new JSONArray(MyArrJson);
        return json;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alltopic, menu);
        return true;
    }

}
