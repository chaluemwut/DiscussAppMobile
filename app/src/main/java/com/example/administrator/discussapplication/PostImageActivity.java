package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PostImageActivity extends ActionBarActivity {

    byte[] image1 = null;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    public static final int CAMERA_PIC_REQUEST = 1337;
    public static final int DIALOG_UPLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    String backupAddImg;
    String topic_id;
    String cat_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_image);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final EditText txtSDCard = (EditText)findViewById(R.id.edtTextUpImage);
        ImageView imgView = (ImageView) findViewById(R.id.ImgAddImg);
        imgView.setImageResource(R.drawable.add_image);
        Bundle extras = getIntent().getExtras();

//Button Upload on Bataase and server
        Button btnUpload = (Button)findViewById(R.id.btnPost);
        btnUpload.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                // TODO Auto-generated stub


                //  try {
                //     URL urlAddUser = new URL("http://192.168.1.118:8080/testWeb/SendPost?catid=" + qMessage1 + "&topic=" + qMessage2
                //            + "&desc=" + qMessage3 + "&owner=" + qMessage4);


                // } catch (MalformedURLException e) {
                //   e.printStackTrace();
                // }


                final String strUrlServer = "http://192.168.1.49:8080/testWeb/SengImage";





                txtSDCard.setText(backupAddImg);
                final String strSDPath = txtSDCard.getText().toString();


                //final String strUrlimg = "http://192.168.1.23:8080/testWeb/NewPostBoard?topic_id="+topic_id+"&cat_id="+cat_id;
                new UploadFileAsync().execute(strSDPath,strUrlServer);
                // URL url = new URL(strUrlimg);
                //  HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // txtSDCard.setText(backupAddImg);




            }
        });
//Button Upload Img on Mobile
        Button bt = (Button) findViewById(R.id.btnPicked);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                loadImagefromGallery();

            }

        });

        // txtSDCard
        // btnUpload




    }
    //Picked Img And Show
    public String GetImgAddress(){
        return imgDecodableString;
    }
    public void SetImgAddress(String imgDecodableString){
        this.imgDecodableString =imgDecodableString;

    }
    //show Image and picked from Gallery
    public void loadImagefromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                backupAddImg = this.imgDecodableString;

                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.ImgAddImg);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));



            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // Create intent to Open Image applications like Gallery, Google Photos

    //Upload to Server and Database
    public void showSuscess(String resServer)
    {

        /** Get result from Server (Return the JSON Code)
         * StatusID = ? [0=Failed,1=Complete]
         * Error	= ?	[On case error return custom error message]
         *
         * Eg Upload Failed = {"StatusID":"0","Error":"Cannot Upload file!"}
         * Eg Upload Complete = {"StatusID":"1","Error":""}
         */

        /*** Default Value ***/
        String strStatusID = "0";
        String strError = "Upload file Successfully";

        try {

            JSONObject c = new JSONObject(resServer);
            strStatusID = c.getString("StatusID");
            strError = c.getString("Error");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Prepare Status
        if(strStatusID.equals("0"))
        {
            AlertDialog.Builder ad  = new AlertDialog.Builder(this);
            ad.setTitle("Status!");
            ad.setIcon(android.R.drawable.btn_star_big_on);
            ad.setMessage(strError);
            ad.setPositiveButton("OK", null);
            ad.show();
        }
        else
        {
            Toast.makeText(PostImageActivity.this, "Upload file Successfully", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_UPLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Uploading file.....");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }


    public class UploadFileAsync extends AsyncTask<String, Void, Void> {

        String resServer;
        protected void onPreExecute() {

            super.onPreExecute();
            showDialog(DIALOG_UPLOAD_PROGRESS);
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub

            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            int resCode = 0;
            String resMessage = "";

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";

            String strSDPath = params[0];
            String strUrlServer = params[1];

            try {
                /** Check file on SD Card ***/
                File file = new File(strSDPath);
                if(!file.exists())
                {
                    resServer = "{\"StatusID\":\"0\",\"Error\":\"Please check path on SD Card\"}";
                    return null;
                }

                FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));

                URL url = new URL(strUrlServer);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(conn
                        .getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream
                        .writeBytes("Content-Disposition: form-data; name=\"filUpload\";filename=\""
                                + strSDPath + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Response Code and  Message
                resCode = conn.getResponseCode();
                if(resCode == HttpURLConnection.HTTP_OK)
                {
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int read = 0;
                    while ((read = is.read()) != -1) {
                        bos.write(read);
                    }
                    byte[] result = bos.toByteArray();
                    bos.close();

                    resMessage = new String(result);

                }

                Log.d("resCode=", Integer.toString(resCode));
                Log.d("resMessage=",resMessage.toString());

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

                resServer = resMessage.toString();


            } catch (Exception ex) {
                // Exception handling
                return null;
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            showSuscess(resServer);
            dismissDialog(DIALOG_UPLOAD_PROGRESS);
            removeDialog(DIALOG_UPLOAD_PROGRESS);
        }



    }
}