package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
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
    public GridView gridV1;
    public GridView gridV2;

    public Button btnNext;
    public Button btnPre;

    ArrayList<HashMap<String, String>> myArrList;
    TextView txtView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //ImageButton Btn_logout = (ImageButton) this.findViewById(R.id.imgBtnLogout);
        //ImageButton Btn_profile = (ImageButton) this.findViewById(R.id.imgBtnSet);
       // ImageButton Btn_post = (ImageButton) this.findViewById(R.id.imgBtnPost);
        ImageView Avt = (ImageView) this.findViewById(R.id.Advt);
       // Btn_logout.setImageResource(R.drawable.bt_logout1);
        Button Btn_profile =(Button)this.findViewById(R.id.bt_edit);
        Button Btn_post=(Button)this.findViewById(R.id.bt_post);
        Spinner spin = (Spinner) findViewById(R.id.spinner);


        arrList.add("--SELECTE--");
        arrList.add("IT Computer Notebook");
        arrList.add("Cemera");
        arrList.add("Talephone");
        arrList.add("Clothing");

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
        gridV1=(GridView) findViewById(R.id.gridImg1);


        gridV1.setAdapter(new ImageAdapter(this));


        gridV1.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

                    public void onItemClick(AdapterView<?> arg0, View view,int position,long arg3) {
                        Toast.makeText(getApplicationContext(), GridViewConfig.getResim_list().
                                get(position), Toast.LENGTH_SHORT).show();

                    }
                });
        gridV2=(GridView) findViewById(R.id.gridImg2);


        gridV2.setAdapter(new ImageAdapter(this));


        gridV2.setOnItemClickListener(new AdapterView.OnItemClickListener()  {

            public void onItemClick(AdapterView<?> arg0, View view,int position,long arg3) {
                Toast.makeText(getApplicationContext(), GridViewConfig.getResim_list().
                        get(position), Toast.LENGTH_SHORT).show();

            }
        });

        /// End Grid//
/// Start button set //
        Btn_post.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), PostActivity.class);
                //it.putExtra("key1", inPutIpAddress);
                //it.putExtra("key2", inPutSub);
               // it.putExtra("key3", inPutGp);
                System.out.println("");
                startActivity(it);

            }
        });
        Btn_profile.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), ProfileActivity.class);
                //it.putExtra("key1", inPutIpAddress);
                //it.putExtra("key2", inPutSub);
                // it.putExtra("key3", inPutGp);
                System.out.println("");
                startActivity(it);

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
