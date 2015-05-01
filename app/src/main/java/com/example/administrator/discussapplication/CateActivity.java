package com.example.administrator.discussapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class CateActivity extends Activity {
    private static String url = "http://192.168.237.170:8070/DiscussAppWeb/jsonShowCatID";
    private GridView gridV;
    private ImageAdapter imageAdap;
    private CustomAdapter urgentTodosAdapter;



    //JSON Node Names
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_ImagePathBitmap ="ImagePathBitmap";
    private static final String URLImg="http://192.168.237.170:8070/DiscussAppWeb/images/";

    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    JSONArray Data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate);

        ImageButton btnAdd = (ImageButton) this.findViewById(R.id.imgBtnAdd_cate);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_cate);
        btnAdd.setImageResource(R.drawable.add1);
        btnBack.setImageResource(R.drawable.back);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //final GridView gridV = (GridView) findViewById(R.id.gridView_Cate);
        Bitmap newBitmap;
        JSONParser jParser = new JSONParser();
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);

        // GridView and imageAdapter
        gridV = (GridView) findViewById(R.id.gridView_Cate);
        gridV.setClipToPadding(false);
        imageAdap = new ImageAdapter(getApplicationContext());
        gridV.setAdapter(imageAdap);


        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);
            //JSONObject c = Data.getJSONObject(0);
            // Storing  JSON item in a Variable
            //String catID = c.getString(TAG_cat_id);
            //String catTopic = c.getString(TAG_cat_topic);
            //  String date = c.getString(TAG_DATE);
            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topic =c.getString(TAG_TOPIC);
                String owner = c.getString(TAG_OWNER);
                String img = c.getString(TAG_IMG);
               // String ImageParhBitmap = c.getString("http://192.168.237.170:8070/DiscussAppWeb/images"+img);
                HashMap<String, Object> map = new HashMap<String, Object>();
                // map = new HashMap<String, String>();
                map.put(TAG_TOPIC,topic);
                map.put(TAG_OWNER, owner);
                map.put(TAG_IMG, URLImg+img);
                ///////////////////////// Thumbnail Get ImageBitmap To Object
               // map.put("ImagePath", (String)c.getString("img"));
                //newBitmap = loadBitmap(c.getString("img"));
                //map.put("ImagePathBitmap", newBitmap);



                cateList.add(map);

            }


           /// SimpleAdapter sAdap;
           // sAdap = new SimpleAdapter(CateActivity.this, cateList, R.layout.activity_column_cate,
          //          new String[]{ "topic", "owner"}, new int[]{ R.id.ColTopic, R.id.ColOwner});
         //   gridV.setAdapter(sAdap);



        } catch (JSONException e) {
            e.printStackTrace();

        }
        ///////////test/////////////////
        // Initialize the subclass of ParseQueryAdapter
        urgentTodosAdapter = new CustomAdapter(this);

        // Initialize ListView and set initial view to mainAdapter

        gridV.setAdapter(urgentTodosAdapter);
        urgentTodosAdapter.loadObjects();

        Toast.makeText(getApplication(), "แสดงรายการ",
                Toast.LENGTH_SHORT).show();


        ///////////////////////////////

    }
/*
//////////////////////////////Bitmap
    private static final String TAG = "Image";
    private static final int IO_BUFFER_SIZE = 4 * 1024;
    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
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
    //////////////////////////////////////
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }
    //////////////////////////////////////
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
    ///////////////////////////////////////
////////////////////////imagAdapter
    class imageAdap extends BaseAdapter {

        private Context mContext;

        public imageAdap(Context context) {
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
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPhoto);
            imageView.getLayoutParams().height = 60;
            imageView.getLayoutParams().width = 60;
            imageView.setPadding(5, 5, 5, 5);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            try
            {
            } catch (Exception e) {
// When Error
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }

// ColImageID
            TextView txtImageID = (TextView) convertView.findViewById(R.id.ColTopic);
            txtImageID.setPadding(5, 0, 0, 0);
            txtImageID.setText("ID : " + cateList.get(position).get("topic").toString());

// ColItemID
            TextView txtItemID = (TextView) convertView.findViewById(R.id.ColOwner);
            txtItemID.setPadding(5, 0, 0, 0);
            txtItemID.setText("Item : " + cateList.get(position).get("owner").toString());

            return convertView;

        }

    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cate, menu);
        return true;
    }

}