package com.example.administrator.discussapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CateActivity extends Activity {
    private static String url = "http://192.168.236.1:8070/DiscussAppWeb/jsonShowCatID";
    private GridView gridV;
    private ImageAdapter2 imageAdap;
    private CustomAdapter urgentTodosAdapter;



    //JSON Node Names
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String URLImg="http://192.168.236.1:8070/DiscussAppWeb/images/";

    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    JSONArray Data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate);
        gridV = (GridView) findViewById(R.id.gridView_Cate);

        Bitmap newBitmap;
        ImageButton btnAdd = (ImageButton) this.findViewById(R.id.imgBtnAdd_cate);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_cate);
        btnAdd.setImageResource(R.drawable.add1);
        btnBack.setImageResource(R.drawable.back);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONParser jParser = new JSONParser();
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
                String BitMap = URLImg+img;
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
            try
            {
                imageView.setImageBitmap((Bitmap)cateList.get(position).get("ImagePathBitmap"));
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

    /***** Get Image Resource from URL (Start) *****/
   // private static final String TAG = "Image";
   // private static final int IO_BUFFER_SIZE = 4 * 1024;
    /*
    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }*/


    /***** Get Image Resource from URL (Start) *****/

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

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
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
        getMenuInflater().inflate(R.menu.menu_cate, menu);
        return true;
    }

}