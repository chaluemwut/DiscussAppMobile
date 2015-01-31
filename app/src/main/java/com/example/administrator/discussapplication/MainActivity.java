package com.example.administrator.discussapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
  LoginMain loginmain = new LoginMain();
    JSONObject jsonobject;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> worldlist;
    ArrayList<WorldPopulation> world;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView image = (ImageView) this.findViewById ( R.id.image );
        ImageView image2 = (ImageView) this.findViewById ( R.id.image2 );
        ImageView image3 = (ImageView) this.findViewById ( R.id.image3 );
        EditText EDuser = (EditText) this.findViewById(R.id.editText1);
        EditText EDpass = (EditText) this.findViewById(R.id.editText1);
        Button BtnLogin = (Button) this.findViewById(R.id.btn_login);
        image.setImageResource ( R.drawable.logo1_1 );
        image2.setImageResource ( R.drawable.bt_id );
        image3.setImageResource ( R.drawable.bt_psw );
        String User = "test";
        String Psw = "test";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        ///newupdate 21/01/2558
        //send to user,password
        try {
            String url = "http://192.168.1.112:8070/DiscussApp/LoginAPI?username="+User+"&password="+Psw;

            params.add(new BasicNameValuePair("username", User));
            params.add(new BasicNameValuePair("password", Psw));

           String resultServer  = getHttpPost(url,params);
            String ser = new String();
    } catch (Exception e) {
             Log.e("Error", e.getMessage());
            e.printStackTrace();
         }
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 0) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


    //  private class DownloadJSON extends AsyncTask<Void, Void, Void> {

      //  @Override
        //protected Void doInBackground(Void... params) {
            // Locate the WorldPop  ulation Class
        //    world = new ArrayList<WorldPopulation>();
            // Create an array to populate the spinner
          //  worldlist = new ArrayList<String>();

            // JSON file URL address
           // jsonobject = JSONfunction
              //      .getJSONfromURL("http://192.168.1.112:8070/DiscussApp/LoginAPI?username="+Psw+"&password="+User);

            //try {
             //   String  url="http://192.168.1.112:8070/DiscussApp/LoginAPI";

            //    params.add(new BasicNameValuePair("username", User));
              //  params.add(new BasicNameValuePair("password", Psw));

                // Locate the NodeList name
               //jsonarray = jsonobject.getJSONArray("status");
              // for (int i = 0; i < jsonarray.length(); i++) {
                //Log.d(jsonobject.toString())
                //jsonobject.getJSONObject()
                   // WorldPopulation worldpop = new WorldPopulation();

                    //worldpop.setUsername(jsonobject.optString("username"));
                   // worldpop.setPassword(jsonobject.optString("password"));
                 //   worldpop.setPopulation(jsonobject.optString("population"));
                  //  worldpop.setFlag(jsonobject.optString("flag"));
                  //  world.add(worldpop);

                    // Populate spinner with country names
                   // worldlist.add(jsonobject.optString("us"));


             // }
           // } catch (Exception e) {
          //      Log.e("Error", e.getMessage());
          //      e.printStackTrace();
          //  }
          //  return null;
      //  }


   // }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
