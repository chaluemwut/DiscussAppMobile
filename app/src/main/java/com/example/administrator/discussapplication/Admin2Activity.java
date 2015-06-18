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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Admin2Activity extends ActionBarActivity {
    private static   String getURLServer = "http://192.168.1.4:8080/DiscussWeB2/";
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
    JSONArray Data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
            ImageButton btnAdd = (ImageButton) this.findViewById(R.id.imageHome);
            ImageButton btnBack = (ImageButton) this.findViewById(R.id.imageBack);
            btnAdd.setImageResource(R.drawable.home);
            btnBack.setImageResource(R.drawable.back);

            // Hashmap for ListView
        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.topicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            this.roleID=intent.getString("role_id");
            // and get whatever type user account id is
        }

        String url = getURLServer+"jsonAllCat";
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
                    String owner = c.getString(TAG_Owner);

                    HashMap<String, String> map = new HashMap<String, String>();
                    // map = new HashMap<String, String>();
                    map.put(TAG_cat_id,catID);
                    map.put(TAG_cat_topic, catTopic);
                    map.put(TAG_DATE, date);
                    map.put(TAG_Owner, owner);
                    topicList.add(map);

                }

                gridV =(GridView)this.findViewById(R.id.gridViewAdmin);
                imageAdap = new ImageAdapter(getApplicationContext());
                gridV.setAdapter(imageAdap);



            } catch (JSONException e) {

                e.printStackTrace();
            }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);
        final EditText edAddTopNm =(EditText)this.findViewById(R.id.editAddtopic);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), AdminActivity.class);
                edAddTopNm.setText("");
                it.putExtra("topic_id", topicID);
                it.putExtra("username",username);
                it.putExtra("cat_id",catID);
                it.putExtra("role_id",roleID);

                startActivity(it);
            }
        });
        ///Add ew Cat
        Button btAddTopNm=(Button)this.findViewById(R.id.btnAddtopic);
        final AlertDialog.Builder adb = new AlertDialog.Builder(Admin2Activity.this);
        btAddTopNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adb.setTitle("ต้องการเพิ่มประเภทกระทู้ ?");
                adb.setMessage("เพิ่มประเภทเรียบร้อย");
                adb.setNegativeButton("ยกเลิก", null);
                adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            String GetTopNm=edAddTopNm.getText().toString();
                            URL url = new URL(getURLServer+"AddCategory?cat_topic="+GetTopNm+"");
                            Scanner sc = new Scanner(url.openStream());


                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /**
                         * Command for Delete
                         * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                         */
                        Intent it = new Intent(getApplicationContext(), Admin2Activity.class);
                        edAddTopNm.setText("");
                        it.putExtra("topic_id", topicID);
                        it.putExtra("username",username);
                        it.putExtra("cat_id",catID);
                        it.putExtra("role_id",roleID);
                        Toast.makeText(getApplicationContext(),
                                "เพิ่มประเภทกระทู้เรียบร้อย", Toast.LENGTH_LONG).show();
                        startActivity(it);
                    }
                });
                adb.show();
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
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {
                viewHolder = new ViewHolderItem();
                convertView = inflater.inflate(R.layout.activity_multibtn_admin, null);

                convertView.setTag(viewHolder);


                viewHolder. txtItemID = (TextView) convertView.findViewById(R.id.ColTopic);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(topicList.get(position).get("cat_topic"));
// ColItemTime
                viewHolder. txtTimeID = (TextView) convertView.findViewById(R.id.Colowner);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(topicList.get(position).get("userName"));

                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.Coltime);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(topicList.get(position).get("date"));


                viewHolder. btnUpdate = (ImageButton) convertView.findViewById(R.id.imgCmdupdate);
                viewHolder. btnUpdate.setImageResource(R.drawable.edit2 );



                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(Admin2Activity.this, "Your Shared (ImageID = " + topicList.get(position).get("cat_id") +"///" + roleID, Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), Admin3Activity.class);
                        String topPicId = topicList.get(position).get("topic_id");
                        String catIDStaff = topicList.get(position).get("cat_id");
                        it.putExtra("topic_id", topPicId);
                        it.putExtra("username",username);
                        it.putExtra("cat_id",catIDStaff);
                        it.putExtra("role_id",roleID);


                        Toast.makeText(getApplicationContext()
                                ,"แก้ไขข้อมูล"+roleID,Toast.LENGTH_LONG).show();
                        System.out.println("");
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
                        adb.setTitle("Delete?");
                        adb.setMessage("Are you sure delete [" + topicList.get(position).get("topic_id") + "]");
                        adb.setNegativeButton("ยกเลิก", null);
                        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Admin2Activity.this, "ต้องการจะลบ (ImageID = " + topicList.get(position).get("cat_topic") + "?", Toast.LENGTH_LONG).show();
                                topicID = topicList.get(position).get("topic_id");
                                catID = topicList.get(position).get("cat_id");
                                username = topicList.get(position).get("userName");
                                cateName = topicList.get(position).get("cat_topic");

                                try {
                                    URL url = new URL(getURLServer + "DeleteCategory?cat_id="+catID+"&cat_topic="+cateName+"&username="+username+"");
                                    Scanner sc = new Scanner(url.openStream());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Admin2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catID);
                                it.putExtra("role_id", roleID);

                                startActivity(it);
                            }
                        });
                        adb.show();
                    }
                });
            }
            else{
                viewHolder= (ViewHolderItem) convertView.getTag();
            }

            return convertView;

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin2, menu);
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
