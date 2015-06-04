package com.example.administrator.discussapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LandingActivity extends ActionBarActivity {
    List<String> arrList = new ArrayList<String>();


    private GridView gridV;
    private ImageAdapter2 imageAdap;
    ///value Spinner
    private static String urlSpinner = "http://192.168.236.1:8070/DiscussAppWeb/jsonAllCat";
    private static final String TAG_cat_id_SPINNER = "cat_id";
    private static final String TAG_cat_topic_SPINNER = "cat_topic";
    private static final String TAG_USER_SPINNER = "userName";
    private static final String TAG_DATA_SPINNER = "data";
    private JSONArray DataSpinner=null;
    final ArrayList<String> spinnerlist =new ArrayList<String>();
    ArrayList<HashMap<String, String>> spinner = new ArrayList<HashMap<String, String>>();

    //JSON Node Names Gridviwe
    private static String url = "http://192.168.236.1:8070/DiscussAppWeb/jsonShowCatID";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String URLImg = "http://192.168.236.1:8070/DiscussAppWeb/images/";
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONParser jParser = new JSONParser();
        ImageButton BtnPost = (ImageButton) this.findViewById(R.id.PostIcon);
        ImageButton BtnUpdate = (ImageButton) this.findViewById(R.id.UpdateIcon);
        ImageView ImgUser = (ImageView) this.findViewById(R.id.imgUser_Landing);
        ImgUser.setImageResource(R.drawable.bt_id);
        BtnPost.setImageResource(R.drawable.post);
        BtnUpdate.setImageResource(R.drawable.icon);
        ImageView Avt = (ImageView) this.findViewById(R.id.Advt);

        //////start spinner


        JSONObject jsonSpinner = jParser.getJSONFromUrl(urlSpinner);
        try {

// Getting JSON Array
            DataSpinner = jsonSpinner.getJSONArray(TAG_DATA_SPINNER);

            for (int i = 0; i < DataSpinner.length(); i++) {
                JSONObject c = DataSpinner.getJSONObject(i);
                String catID = c.getString(TAG_cat_id_SPINNER);
                String catTopic = c.getString(TAG_cat_topic_SPINNER);
                String catUser = c.getString(TAG_USER_SPINNER);

                HashMap<String, String> map = new HashMap<String, String>();
                // map = new HashMap<String, String>();
                map.put(TAG_cat_id_SPINNER,catID);
                map.put(TAG_cat_topic_SPINNER, catTopic);
                map.put(TAG_USER_SPINNER, catUser);
                spinner.add(map);
                // Populate spinner with CATTPPIC names
                spinnerlist.add(c.getString(TAG_cat_topic_SPINNER));

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }




        Spinner spin = (Spinner) findViewById(R.id.spinner);

            ArrayAdapter<String> arrAd;
        arrAd = new ArrayAdapter<String>(LandingActivity.this, android.R.layout.simple_list_item_1,spinnerlist);

        spin.setAdapter(arrAd);
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView,
                                           View view, int i, long l) {
// TODO Auto-generated method stub

                    Toast.makeText(LandingActivity.this,
                            String.valueOf("Your Selected : " + spinnerlist.get(i)),
                            Toast.LENGTH_SHORT).show();
                }

                public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
                    Toast.makeText(LandingActivity.this,
                            String.valueOf("Your Selected Empty"),
                            Toast.LENGTH_SHORT).show();
                }
            });
            ///////end spinner
            ///// button Post&Update
            BtnPost.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Intent it = new Intent(getApplicationContext(), PostActivity.class);

                    System.out.println("");
                    startActivity(it);

                }
            });
            BtnUpdate.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    Intent it = new Intent(getApplicationContext(), ProfileActivity.class);

                    System.out.println("");
                    startActivity(it);

                }
            });
            /// Start Grid//
            gridV = (GridView) findViewById(R.id.gridView_Landing);

            Bitmap newBitmap;


            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);

            // GridView and imageAdapter

            try {

// Getting JSON Array
                Data = json.getJSONArray(TAG_DATA);

                for (int i = 0; i < Data.length(); i++) {
                    JSONObject c = Data.getJSONObject(i);
                    String topic = c.getString(TAG_TOPIC);
                    String owner = c.getString(TAG_OWNER);
                    String img = c.getString(TAG_IMG);
                    String dateTime = c.getString(TAG_TIME);

                    HashMap<String, Object> map = new HashMap<String, Object>();

                    map.put(TAG_TOPIC, topic);
                    map.put(TAG_OWNER, owner);
                    map.put(TAG_TIME, dateTime);
                    // Thumbnail Get ImageBitmap To Object
                    String BitMap = URLImg + img;
                    newBitmap = loadBitmap(BitMap);
                    map.put("ImagePathBitmap", newBitmap);
                    cateList.add(map);


                }

                gridV.setClipToPadding(false);
                imageAdap = new ImageAdapter2(getApplicationContext());
                gridV.setAdapter(imageAdap);


            } catch (JSONException e) {
                e.printStackTrace();

            }

    }
        ///GridView



    /////ImageAdapter/////
    class ImageAdapter2 extends BaseAdapter {

        private Context mContext;

        public ImageAdapter2(Context context) {
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

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_column_cate, null);
            }

            // ColPhoto
            ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
            //imageView.getLayoutParams().height = 60;
            // imageView.getLayoutParams().width = 60;
            imageView.setPadding(5, 5, 5, 5);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try {
                imageView.setImageBitmap((Bitmap) cateList.get(position).get("ImagePathBitmap"));
            } catch (Exception e) {
                // When Error
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }

            // ColImageID
            TextView txtImageID = (TextView) convertView.findViewById(R.id.Topic);
            txtImageID.setPadding(5, 0, 0, 0);
            txtImageID.setText(cateList.get(position).get("topic").toString());

            // ColItemID
            TextView txtItemID = (TextView) convertView.findViewById(R.id.Owner);
            txtItemID.setPadding(5, 0, 0, 0);
            txtItemID.setText(cateList.get(position).get("owner").toString());
// ColItemTime
            TextView txtTimeID = (TextView) convertView.findViewById(R.id.timestamp);
            txtTimeID.setPadding(5, 0, 0, 0);
            txtTimeID.setText(cateList.get(position).get("dateTime").toString());
            return convertView;

        }

    }


    /**
     * ** Get Image Resource from URL (Start) ****
     */

    private static final String TAG = "Image";
    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    /***** Get Image Resource from URL (End) *****/














    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
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
