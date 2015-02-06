package com.example.administrator.discussapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends ActionBarActivity {
    List<String> arrList = new ArrayList<String>();

    TextView txtView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ImageButton bt_logout = (ImageButton)this.findViewById(R.id.imgBtn);
        ImageView Avt = (ImageView)this.findViewById(R.id.Advt);
        bt_logout.setImageResource(R.drawable.bt_logout);
        Spinner spin = (Spinner) findViewById(R.id.spinner);


        arrList.add("--SELECTE--");
        arrList.add("Mercury");
        arrList.add("Venus");
        arrList.add("Earth");
        arrList.add("Mars");

        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(LandingActivity.this,
                android.R.layout.simple_spinner_item,
                arrList);
        spin.setAdapter(arrAd);
        spin.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
// TODO Auto-generated method stub

                Toast.makeText(LandingActivity.this,
                        String.valueOf("Your Selected : " + arrList.get(i)),
                        Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
// TODO Auto-generated method stub
                Toast.makeText(LandingActivity.this,
                        String.valueOf("Your Selected Empty"),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }



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
