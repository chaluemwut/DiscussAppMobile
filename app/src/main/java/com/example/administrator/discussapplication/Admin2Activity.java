package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class Admin2Activity extends ActionBarActivity {
    static Config con = new Config() ;
      private static   String getURLServer = con.getURL();

    String topicID ,catID,username,roleID,cateName;
    public ImageLoader imageLoader;
    private GridView gridV;
    private ImageAdapter imageAdap;
    //JSON Node Names
    private static final String TAG_cat_id = "cat_id";
    private static final String TAG_cat_topic = "cat_topic";
    private static final String TAG_DATE = "date";
    private static final String TAG_DATA = "data";
    private static final String TAG_Owner = "userName";
    ArrayList<HashMap<String, String>> topicList = new ArrayList<HashMap<String, String>>();
    // listView1
    JSONParser jParser = new JSONParser();
    JSONArray Data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageButton btnSearch = (ImageButton)this.findViewById(R.id.btnSearchA2);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imageBack);
        btnSearch.setImageResource(R.drawable.searh);
        btnBack.setImageResource(R.drawable.back);

        // Hashmap for ListView
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

        String url = getURLServer + "jsonAllCat";
        // Creating new JSON Parser

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        //String(rs3.getString("owner").getBytes(),"TIS-620")


        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String catID = c.getString(TAG_cat_id);
                String catTopic = c.getString(TAG_cat_topic);
                String date = c.getString(TAG_DATE);
                String owner = c.getString(TAG_Owner);

                HashMap<String, String> map = new HashMap<String, String>();
                // map = new HashMap<String, String>();
                map.put(TAG_cat_id, catID);
                map.put(TAG_cat_topic, catTopic);
                map.put(TAG_DATE, date);
                map.put(TAG_Owner, owner);
                topicList.add(map);

            }

            gridV = (GridView) this.findViewById(R.id.gridViewAdmin);
            imageAdap = new ImageAdapter(getApplicationContext());
            gridV.setAdapter(imageAdap);


        } catch (JSONException e) {

            e.printStackTrace();
      }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), SearchAdminActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "1");
                startActivity(it);
            }
       });
        ///Add ew Cat

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AdminActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username", username);
                it.putExtra("cat_id", catID);
                it.putExtra("role_id", "1");

                startActivity(it);
            }
        });

        Button btAddTopNm = (Button) this.findViewById(R.id.btnAddtopic);



            btAddTopNm.setOnClickListener(new View.OnClickListener() {
                 AlertDialog.Builder adb = new AlertDialog.Builder(Admin2Activity.this);
                 EditText edAddTopNm = (EditText)findViewById(R.id.editAddtopic);

                @Override
                public void onClick(View v) {
                    String GetTopNm = edAddTopNm.getText().toString();
                    if (!GetTopNm.isEmpty()) {
                        adb.setTitle("ต้องการเพิ่มประเภทกระทู้ ?");
                        adb.setMessage("ต้องเพิ่มกระทู้ " + GetTopNm);
                        adb.setNegativeButton("ยกเลิก", null);
                        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                String GetTopNm = edAddTopNm.getText().toString();
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("cat_topic", GetTopNm));
                                jParser.getJSONUrl(getURLServer + "AddCategory", params);


                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);
                                edAddTopNm.setText("");
                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catID);
                                it.putExtra("role_id", "1");
                                Toast.makeText(getApplicationContext(),
                                        "เพิ่มประเภทกระทู้เรียบร้อย", Toast.LENGTH_LONG).show();
                                startActivity(it);
                            }
                        });
                        adb.show();


                    } else {
                        Toast.makeText(getApplicationContext(),
                                "กรอกประเภทกระทู้", Toast.LENGTH_LONG).show();

                    }
                }

            });

    }

    class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return topicList.size();
        }

        public Object getItem(int position) {
            return topicList.get(position);
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
            ImageButton btnUpdate,btnDelete;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolderItem viewHolder = null;
            viewHolder = new ViewHolderItem();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {

                convertView = inflater.inflate(R.layout.activity_multibtn_admin, null);
            }
                convertView.setTag(viewHolder);


                viewHolder. txtItemID = (TextView) convertView.findViewById(R.id.ColTopic);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(topicList.get(position).get("cat_topic"));
// ColItemTime
                viewHolder. txtTimeID = (TextView) convertView.findViewById(R.id.Colowner);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(topicList.get(position).get("userName"));

                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.ColTime);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(topicList.get(position).get("date"));


                viewHolder. btnUpdate = (ImageButton) convertView.findViewById(R.id.imgCmdupdate);
                viewHolder. btnUpdate.setImageResource(R.drawable.edit2 );



                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(Admin2Activity.this, "แก้ไข " + topicList.get(position).get("cat_topic") +"" , Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), Admin3Activity.class);
                        String topPicId = topicList.get(position).get("topic_id");
                        String catIDStaff = topicList.get(position).get("cat_id");
                        it.putExtra("topic_id", topPicId);
                        it.putExtra("username",username);
                        it.putExtra("cat_id",catIDStaff);
                        it.putExtra("role_id","1");


                                            startActivity(it);
                    }
                });
                //Delete
                viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.imgCmdelete);
                viewHolder.btnDelete.setImageResource(R.drawable.delete);
                // imgCmdDelete

                //cmdDelete.setBackgroundColor(Color.TRANSPARENT);
                final AlertDialog.Builder adb = new AlertDialog.Builder(Admin2Activity.this);
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        adb.setTitle("ต้องการลบประเภทกระทู้?");
                        adb.setMessage("คุณแน่ใจว่าจะลบ " + topicList.get(position).get("cat_topic") + "");
                        adb.setNegativeButton("ยกเลิก", null);
                        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Admin2Activity.this, "ลบประเภท " + topicList.get(position).get("cat_topic") + "เรียบร้อย", Toast.LENGTH_LONG).show();
                                topicID = topicList.get(position).get("topic_id");
                                catID = topicList.get(position).get("cat_id");
                               String username2 = topicList.get(position).get("userName");
                                cateName = topicList.get(position).get("cat_topic");



                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("cat_id", catID));
                                params.add(new BasicNameValuePair("cat_topic", cateName));
                                params.add(new BasicNameValuePair("username", username2));

                                jParser.getJSONUrl(getURLServer + "DeleteCategory", params);
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catID);
                                it.putExtra("role_id", "1");

                                startActivity(it);
                            }
                        });
                        adb.show();
                    }
                });

                viewHolder= (ViewHolderItem) convertView.getTag();


            return convertView;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
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
                SaveSharedPreference.clearUserName(Admin2Activity.this);
                startActivity(it);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
