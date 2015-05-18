package com.example.administrator.discussapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ImageAdapter extends BaseAdapter implements ListAdapter {
    //private Context mContext;
    private Context context;

    private List<String> urlList;

    public ImageAdapter(Context context) {
        super();
        this.context = context;

        GridViewConfig.addImageUrls();
    }


    public int getCount() {
        return GridViewConfig.getResim_list().size();

    }

    public Object getItem(int position) {

        return GridViewConfig.getResim_list().get(position);
    }


    public long getItemId(int position) {

        return position;
    }

    public List<String> getUrlList(){
        return urlList;
    }

    public void setUrlList(List<String> urlList){
        this.urlList = urlList;
    }

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
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.activity_column_cate, null);
        }
            imageView = (ImageView) convertView.findViewById(R.id.icon);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        try {
            Bitmap bitMap = getBitmapFromURL(getUrlList().get(position));
            imageView.setImageBitmap(bitMap);
        }catch (Exception e){

           e.printStackTrace();
        }


        return imageView;
    }

    private Drawable LoadImageFromURL(String url)

    {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


}
