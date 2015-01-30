package com.example.administrator.discussapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


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
        image.setImageResource ( R.drawable.logo1_1 );
        image2.setImageResource ( R.drawable.bt_id );
        image3.setImageResource ( R.drawable.bt_psw );

        new DownloadJSON().execute();
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            world = new ArrayList<WorldPopulation>();
            // Create an array to populate the spinner
            worldlist = new ArrayList<String>();
            // JSON file URL address
            jsonobject = JSONfunction
                    .getJSONfromURL("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("worldpopulation");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    WorldPopulation worldpop = new WorldPopulation();

                    worldpop.setUsername(jsonobject.optString("User"));
                    worldpop.setPassword(jsonobject.optString("Password"));
                 //   worldpop.setPopulation(jsonobject.optString("population"));
                  //  worldpop.setFlag(jsonobject.optString("flag"));
                    world.add(worldpop);

                    // Populate spinner with country names
                    worldlist.add(jsonobject.optString("country"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }
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
