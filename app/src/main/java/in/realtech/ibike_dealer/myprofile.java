package in.realtech.ibike_dealer;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static in.realtech.ibike_dealer.MainActivity.CONNECTION_TIMEOUT;
import static in.realtech.ibike_dealer.MainActivity.READ_TIMEOUT;
import static in.realtech.ibike_dealer.MainActivity.query;

public class myprofile extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    private static final int REQUEST_SEND_IMAGE = 2;
    private static final int REQUEST_TAKE_PICTURE = 1;
    private static final String POST_FIELD = "op";

    SharedPreferences sharedpreferences;
    String bank;
    String bank_acc;
    String bank_ifsc;
    String bank_verified;
    TextView bankdetail;
    TextView mobile1;
    TextView shop_name1;
    TextView email1;
    TextView addre;
    Uri mLastPhoto;
    String name;
    AsyncTask asyncTask;
    File file23;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static final int GALLERY_REQUEST =1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String pi;
    private static final String TAG = "MyApp";
    ImageView Imageview;
    String mobile;
    SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Account");
        setContentView(R.layout.activity_myprofile);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (asyncTask == null) {
                    asyncTask =    new AsyncLogin().execute(mobile);
                    pullToRefresh.setRefreshing(false);
                } else {
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                        asyncTask =  new AsyncLogin().execute(mobile);
                        pullToRefresh.setRefreshing(false);
                    } else {
                        asyncTask =   new AsyncLogin().execute(mobile);
                        pullToRefresh.setRefreshing(false);
                    }
                }
            }
        });


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncLogin1().execute(mobile);
                        pullToRefresh.setRefreshing(false);
                    }

        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        String id = sharedpreferences.getString("id", "id");
//        String type = sharedpreferences.getString("type", "type");
        name = sharedpreferences.getString("name", "name");
        String mail = sharedpreferences.getString("mail", "mail");
          mobile = sharedpreferences.getString("mobile", "mobile");
        String username = sharedpreferences.getString("username", "username");
        String password = sharedpreferences.getString("password", "password");
        String status = sharedpreferences.getString("status", "status");
        String shop_name = sharedpreferences.getString("shop_name", "shop_name");
        String state = sharedpreferences.getString("state", "state");
        String district = sharedpreferences.getString("district", "district");
        String pincode = sharedpreferences.getString("pincode", "pincode");
        String address = sharedpreferences.getString("address", "address");
//        String latlong = sharedpreferences.getString("latlong", "latlong");
//        String beneid = sharedpreferences.getString("beneid", "beneid");
        bank = sharedpreferences.getString("bank", "bank");
        bank_acc = sharedpreferences.getString("bank_acc", "bank_acc");
        bank_ifsc = sharedpreferences.getString("bank_ifsc", "bank_ifsc");
        bank_verified = sharedpreferences.getString("bank_verified", "bank_verified");
//        String verification_amt = sharedpreferences.getString("verification_amt", "verification_amt");
//        String upi_id = sharedpreferences.getString("upi_id", "upi_id");
        pi = sharedpreferences.getString(mobile + "_pi", mobile + "_pi");
        Log.i(TAG,pi);
        String cli = sharedpreferences.getString("cli", "cli");
        String ci = sharedpreferences.getString("ci", "ci");
//        String added_by = sharedpreferences.getString("added_by", "added_by");
//        String added_at = sharedpreferences.getString("added_at", "added_at");
        TextView uname = (TextView) findViewById(R.id.username);
        uname.setText(name);
        email1 = (TextView) findViewById(R.id.email1);
        email1.setText(mail);
        mobile1 = (TextView) findViewById(R.id.mobile1);
        mobile1.setText(mobile);
        shop_name1 = (TextView) findViewById(R.id.shop_name);
        shop_name1.setText(shop_name);
        bankdetail = (TextView) findViewById(R.id.bankdetails);
        bankdetail.setText("Bank Account Details");
        addre = (TextView) findViewById(R.id.addrs);
        addre.setText("Location : " + address);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Log.i("dateand time",currentDateandTime);

        bankdetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent in = new Intent();
                in.setClass(myprofile.this, Bankaccdetails.class);
                startActivity(in);
            }

        });
//        ImageView editonclick = (ImageView) findViewById(R.id.btnn);
//        editonclick.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                Intent in = new Intent();
//                in.setClass(myprofile.this, changeactivity.class);
//                startActivity(in);
//            }
//        });
        Imageview = (ImageView) findViewById(R.id.profile);

        Imageview.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub


                selectImage();
                return true;
            }
        });



        Imageview.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(myprofile.this,Full_Srce_Myprofile.class);
                intent.putExtra("mobile", mobile);
                intent.putExtra("images", pi);
                startActivity(intent);

            }
        });


        int i = 0;
        setImage(Imageview, i, mobile);
    }

    private void setImage(ImageView imageView, int i, String mobile) {
        String imgNameServer = pi;
        if (!imgNameServer.equals("null")) {
            String imgNameLocal = sharedpreferences.getString(mobile + "_pi", "");
            if (imgNameLocal.equals(imgNameServer)) {
                File imgDir = new File(getFilesDir() + File.separator + "photos");
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
                if (imgFile.exists()) {
                    Glide.with(myprofile.this)
                            .asBitmap()
                            .load(imgFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    Log.v(TAG, "onLoadFailed: " + model);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                        placeholders.set(i, new BitmapDrawable(getResources(), resource));
                                    return false;
                                }
                            })
                            .into(imageView);
                } else {
                    deleteLocalFile(imgNameLocal);
                    downloadVehicleImage(i, mobile, imgNameServer);
                }
            } else {
                Log.v(TAG, imgNameLocal + ": " + imgNameServer);
                deleteLocalFile(imgNameLocal);
                downloadVehicleImage(i, mobile, imgNameServer);
            }
        }
    }

    private void downloadVehicleImage(final int i, final String mobile, final String img) {
        Log.v(TAG, "Downloading");
        String url = "https://igps.io/user_images/" + img;
        String filePath = getFilesDir() + File.separator + "photos";
        File imgDir = new File(filePath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        final File imgFile = new File(imgDir.getPath() + File.separator + img);
        Glide.with(myprofile.this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream outputStream = new FileOutputStream(imgFile, true);
                            String[] iPathArr = img.split("\\.");
                            String extension = iPathArr[iPathArr.length - 1];
                            if (extension.equalsIgnoreCase("png")) {
                                resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            } else {
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            }
                            outputStream.close();
                            SharedPreferences.Editor toEdit = sharedpreferences.edit();
                            toEdit.putString(mobile + "_pi", img);
                            toEdit.commit();
                            toEdit.clear();

                        } catch (Exception e) {
                            Log.v(TAG, "File download error: " + e.getLocalizedMessage());
                        }
                    }
                });
    }


    private void deleteLocalFile(String imgName) {
        try {
            File imgDir = new File(getFilesDir() + File.separator + "photos");
            if (!imgDir.exists()) {
                imgDir.mkdirs();
            }
            File imgFile = new File(imgDir.getPath() + File.separator + imgName);
            if (imgFile.exists()) {
                if (imgFile.delete()) {
                    Log.v(TAG, imgName + " deleted");
                } else {
                    Log.v(TAG, imgName + " not deleted");
                }
            } else {
                Log.v(TAG, imgName + " not exists");
            }
        } catch (Exception e) {
            Log.v(TAG, "File delete error: " + e.getLocalizedMessage());
        }
    }

//    protected void onActivityResult(int requestCode,
//                                    int resultCode,
//                                    Intent data) {
//
//        // Match the request 'pic id with requestCode
//        if (requestCode == pic_id) {
//
//            // BitMap is data structure of image file
//            // which stor the image in memory
//
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//
//            // Set the image in imageview for display
//            Imageview.setImageBitmap(photo);
//
//        }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "cs_" + new Date().getTime() + ".jpg");
                           mLastPhoto = Uri.fromFile(photo);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    mLastPhoto);

                            // start the image capture Intent
                            startActivityForResult(intent, REQUEST_TAKE_PICTURE);
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else {
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
//
//            }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                if (requestCode==MY_CAMERA_REQUEST_CODE) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;

        if (resultCode == RESULT_OK) {
            Log.i(TAG, String.valueOf(requestCode));
            if (requestCode == REQUEST_SEND_IMAGE) {
                Uri uri = data.getData();
                if (uri == null) {
                    return;
                }
                Uri selectedImageURI = data.getData();
                File file = new File(getRealPathFromURI(selectedImageURI));
//                File file = new File(getRealPathFromURI(uri));
                final Handler handler = new Handler();
                MediaScannerConnection.scanFile(
                        this, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(final String path, final Uri uri) {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Log.i("path",path);
                                        Log.i("uri",uri.toString());
                                        new AsyncLogin().execute(path);
//                                        uploadFile(uri);
                                    }
                                });
                            }
                        });
            } else if (requestCode == REQUEST_TAKE_PICTURE) {


                File file = new File(getRealPathFromURI(mLastPhoto));
                final Handler handler = new Handler();
                MediaScannerConnection.scanFile(
                        this, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(final String path, final Uri uri) {

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        new AsyncLogin().execute(path);
                                    }
                                });
                            }
                        });

            }
        }
    }






    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(myprofile.this);
        HttpURLConnection conn;
        URL url = null;
        void writeToDos(DataOutputStream dos, String key, String val) {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + val.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(val + lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);
            } catch (Exception e) {
                Log.v(TAG, e.getLocalizedMessage());
            }
        }

        @Override
        protected String doInBackground(String... params) {


            File file = new File(params[0]);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMHHmmss");
                String strDate = sdf.format(c.getTime());
                String response;
                try {
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    DataOutputStream dos;
                    int bytesAvailable, bufferSize, bytesRead;
                    int maxBufferSize = 1024 * 1024;
                    byte[] buffer;

                    FileInputStream fileInputStream = new FileInputStream(file);
                    Log.v(TAG, "Size: " + file.length() / 1024 + " kb");

                    URL url = new URL("https://igps.io/http.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("pi", params[0]);

                    dos = new DataOutputStream(conn.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    writeToDos(dos, "action", "fitter_image");
                    writeToDos(dos, "op", "pi");
                    writeToDos(dos, "no", mobile);
                    writeToDos(dos, "time", strDate);

                    dos.writeBytes("Content-Disposition: form-data; name=\"" + "pi" + "\";filename=\"" + params[0] + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        response = sb.toString();
                    } else {
                        response = "Error: " + conn.getResponseCode();
                    }
                } catch (Exception e) {
                    response = "Error: " + e.getLocalizedMessage();
                }
                return response;
            }


        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String datee = sdf.format(new Date());
            Log.i("dateand time",datee);

            if( result.equals("success")){


            }
            else
                {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(myprofile.this);


                //Setting message manually and performing action on button click
                builder.setMessage(result)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                             dialog.cancel();
                            }


                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();
            }






            new AsyncLogin1().execute(mobile);
        }
    }



    private class AsyncLogin1 extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(myprofile.this);
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//
//            circularProgressBar.setColor(ContextCompat.getColor(Dashboard.this, R.color.colorPrimaryDark));
//            circularProgressBar.setBackgroundColor(ContextCompat.getColor(Dashboard.this, R.color.primaryTextColor));
//            circularProgressBar.setProgressBarWidth(10);
//            circularProgressBar.setBackgroundProgressBarWidth(20);
//            int animationDuration = 5000; // 2500ms = 2,5s
//            circularProgressBar.setProgressWithAnimation(100, animationDuration);
//            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String datee = sdf.format(new Date());

            try {

                // Enter URL address where your php file resides
                url = new URL("https://igps.io/http.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("fitter_id", params[0])
                        .appendQueryParameter("action", "fitter_dashboard")
                        .appendQueryParameter("from", datee)
                        .appendQueryParameter("to", datee);


                query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {

                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("ibike", result);

            JSONObject j = null;
            try {
                j = new JSONObject(result);


                JSONArray fitter_data = j.getJSONArray("fitter_data");


                for (int i = 0; i < fitter_data.length(); i++) {
                    JSONObject d = fitter_data.getJSONObject(i);
                    String pi1 = d.getString("pi");
                    Log.i(TAG, pi1);
                    pi=pi1;


                    setImage1(Imageview,i,mobile,pi1);
                }
            } catch (JSONException f) {
                f.printStackTrace();
            }


        }
    }




    private void setImage1(ImageView imageView, int i, String mobile,String pi1) {
        String imgNameServer = pi1;
        if (!imgNameServer.equals("null")) {
            String imgNameLocal = sharedpreferences.getString(mobile + "_pi", "");
            if (imgNameLocal.equals(imgNameServer)) {
                File imgDir = new File(getFilesDir() + File.separator + "photos");
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
                if (imgFile.exists()) {
                    Glide.with(myprofile.this)
                            .asBitmap()
                            .load(imgFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    Log.v(TAG, "onLoadFailed: " + model);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                        placeholders.set(i, new BitmapDrawable(getResources(), resource));
                                    return false;
                                }
                            })
                            .into(imageView);
                } else {
                    deleteLocalFile1(imgNameLocal);
                    downloadVehicleImage1(i, mobile, imgNameServer);
                }
            } else {
                Log.v(TAG, imgNameLocal + ": " + imgNameServer);
                deleteLocalFile1(imgNameLocal);
                downloadVehicleImage1(i, mobile, imgNameServer);
            }
        }
    }

    private void downloadVehicleImage1(final int i, final String mobile, final String img) {
        Log.v(TAG, "Downloading");
        String url = "https://igps.io/user_images/" + img;
        String filePath = getFilesDir() + File.separator + "photos";
        File imgDir = new File(filePath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        final File imgFile = new File(imgDir.getPath() + File.separator + img);
        Glide.with(myprofile.this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream outputStream = new FileOutputStream(imgFile, true);
                            String[] iPathArr = img.split("\\.");
                            String extension = iPathArr[iPathArr.length - 1];
                            if (extension.equalsIgnoreCase("png")) {
                                resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            } else {
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            }
                            outputStream.close();
                            SharedPreferences.Editor toEdit = sharedpreferences.edit();
                            toEdit.putString(mobile + "_pi", img);
                            toEdit.commit();
                            toEdit.clear();

                        } catch (Exception e) {
                            Log.v(TAG, "File download error: " + e.getLocalizedMessage());
                        }
                    }
                });
    }


    private void deleteLocalFile1(String imgName) {
        try {
            File imgDir = new File(getFilesDir() + File.separator + "photos");
            if (!imgDir.exists()) {
                imgDir.mkdirs();
            }
            File imgFile = new File(imgDir.getPath() + File.separator + imgName);
            if (imgFile.exists()) {
                if (imgFile.delete()) {
                    Log.v(TAG, imgName + " deleted");
                } else {
                    Log.v(TAG, imgName + " not deleted");
                }
            } else {
                Log.v(TAG, imgName + " not exists");
            }
        } catch (Exception e) {
            Log.v(TAG, "File delete error: " + e.getLocalizedMessage());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_settings) {
            Intent in = new Intent();
            in.setClass(myprofile.this, changeactivity.class);
            startActivity(in);
            return true;
        }else if(itemId==android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    }

