package com.example.administrator.discussapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LandingActivity extends ActionBarActivity {
    List<String> arrList = new ArrayList<String>();
    public int currentPage = 1;
    public int displayPerPage = 6;  // Display Perpage
    public int indexRowStart = 0;
    public int indexRowEnd = 0;
    public int TotalRows = 0;
    public GridView gridV;

    public Button btnNext;
    public Button btnPre;

    ArrayList<HashMap<String, String>> myArrList;
    TextView txtView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        ImageButton bt_logout = (ImageButton) this.findViewById(R.id.imgBtn3);
        ImageButton bt_setup = (ImageButton) this.findViewById(R.id.imgBtn2);
        ImageButton bt_create = (ImageButton) this.findViewById(R.id.imgBtn1);
        ImageView Avt = (ImageView) this.findViewById(R.id.Advt);
        bt_logout.setImageResource(R.drawable.bt_logout1);
        bt_setup.setImageResource(R.drawable.bt_setup);
        bt_create.setImageResource(R.drawable.bt_add);
        Spinner spin = (Spinner) findViewById(R.id.spinner);


        arrList.add("--SELECTE--");
        arrList.add("Mercury");
        arrList.add("Venus");
        arrList.add("Earth");
        arrList.add("Mars");

        ArrayAdapter<String> arrAd = new ArrayAdapter<String>(LandingActivity.this,
                android.R.layout.simple_spinner_item,
                arrList);
        //////start spinner
        spin.setAdapter(arrAd);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        ///////end spinner




           /// Start Grid//
        gridV=(GridView) findViewById(R.id.gridImg);


        gridV.setAdapter(new ImageAdapter(this));


        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

                    public void onItemClick(AdapterView<?> arg0, View view,int position,long arg3) {
                        Toast.makeText(getApplicationContext(), GridViewConfig.getResim_list().
                                get(position), Toast.LENGTH_SHORT).show();

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
