package in.realtech.ibike_dealer;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.os.AsyncTask.Status.RUNNING;

public class stock_available extends AppCompatActivity {
    private static final String TAG = "stockavailable";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;


    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String sno;
    String imei;
    String serial_no;
    String username;
    String fitter_id;
    String vehicle_no;
    String device_type;
    String phase;
    String latlng;
    String dt;
    String live;
    String p1;
    String p2;
    String settings;
    String sim;
    String cimi;
    String qr_code;
    String  expiry_dt;
    String payment_status;
    String price;
    String coupon;
    String image;
    String store_date;
    String out_date;
    String fitter_date;
    String user_date;
    AsyncTask asyncTask;
    SwipeRefreshLayout pullToRefresh;
    private ProgressDialog mprocessingdialog;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String pending = getIntent().getExtras().getString("pending");
        setTitle(" ");
        setContentView(R.layout.activity_stock_available);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mprocessingdialog = new ProgressDialog(this);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh1);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");
        new AsyncLogin().execute(mobile);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              if (asyncTask == null) {
                  asyncTask =  new AsyncLogin().execute(mobile);
                  pullToRefresh.setRefreshing(false);
              } else {
                  if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                      asyncTask.cancel(true);
                      asyncTask =  new AsyncLogin().execute(mobile);
                      pullToRefresh.setRefreshing(false);
                  } else {
                      asyncTask = new AsyncLogin().execute(mobile);
                      pullToRefresh.setRefreshing(false);
                  }
              }
            }
        });
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(stock_available.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected String doInBackground(String... params) {
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
                        .appendQueryParameter("action", "fitter_devices")
                        .appendQueryParameter("type", "fitter0")
                        .appendQueryParameter("fitter_id", mobile);
                String query = builder.build().getEncodedQuery();
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
            Log.i("dinesh",result);



//            int count = jsonArray.length();
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            JSONArray array = null;
            try {
                array = new JSONArray(result);
                int count = array.length();
                String Onms = Integer.toString(count);
                Log.i("total",Onms);
                setTitle("Waiting List "+" ("+ Onms + " nos."+")");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject d = array.getJSONObject(i);

                    sno= d.getString("sno");
                    imei = d.getString("imei");
                    serial_no = d.getString("serial_no");
                    username= d.getString("username");
                    fitter_id= d.getString("fitter_id");
                    vehicle_no = d.getString("vehicle_no");
                    device_type = d.getString("device_type");
                    phase = d.getString("phase");
                    latlng = d.getString("latlng");
                    dt = d.getString("dt");
                    live = d.getString("live");
                    p1 = d.getString("p1");
                    p2 = d.getString("p2");
                    settings = d.getString("settings");
                    sim = d.getString("sim");
                    cimi  = d.getString("cimi");
                    qr_code = d.getString("qr_code");
                    expiry_dt = d.getString("expiry_dt");
                    payment_status = d.getString("payment_status");
                    price= d.getString("price");
                    coupon= d.getString("coupon");
                    image= d.getString("image");
//                    added_date = d.getString("added_date");
                    store_date = d.getString("store_date");
                    out_date  = d.getString("out_date");
                    fitter_date  = d.getString("fitter_date");
                    user_date = d.getString("user_date");
                   String plan = d.getString("plan");
                    // Adding Header data
                    listDataHeader.add(serial_no+ " ("+ plan +")");
                    List<String> lease_offer = new ArrayList<String>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(fitter_date));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date1 = simpleDateFormat.format(date.getTime());

                    lease_offer.add(qr_code);
                    lease_offer.add(date1);
//                    Log.i("result", lease_offer.toString());
                    // Header into Child data
                    listDataChild.put(listDataHeader.get(i), lease_offer);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            listAdapter = new ExpandableListAdapters(getApplicationContext(), listDataHeader, listDataChild);
            // setting list adapter
            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    if(childPosition==0) {
                        final String data = listAdapter.getChild(groupPosition, childPosition).toString();
//                        Toast.makeText(stock_available.this, data, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(stock_available.this);
                        alertDialogBuilder.setMessage("Are you sure,You wanted to accept the device");
                        alertDialogBuilder.setPositiveButton("Accept",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
//                                     Toast.makeText(stock_available.this, data, Toast.LENGTH_LONG).show();
                                        new stock_available.AsyncLogin1().execute(data);
                                    }
                                });


                        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }

                    return false;

                }

                });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pending_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
//        if (itemId == R.id.action_settings) {
//
//        }
        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_settings10) {

            IntentIntegrator integrator = new IntentIntegrator(stock_available.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();

//            new IntentIntegrator(stock_available.this)
//                    .setOrientationLocked(false)
//                    .setCaptureActivity(ScannerActivity.class)
//                    .setBarcodeImageEnabled(true)
//                    .initiateScan();

        }
        else if(itemId == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() != null) {
//                try {
//                    String qr = result.getContents();
//                    Log.i("qr",qr);
//                    new stock_available.AsyncLogin1().execute(qr);
//
//                } catch (Exception e) {
//                    Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_SHORT);
//                }
//            } else {
//                Log.v(TAG, "contents null");
//            }
//        } else {
//            Log.v(TAG, "result null");
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");
                String qr = result.getContents();

                new stock_available.AsyncLogin1().execute(qr);
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class AsyncLogin1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(stock_available.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected String doInBackground(String... params) {
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
                        .appendQueryParameter("action", "fitter_stock_accept")
                        .appendQueryParameter("qr",params[0])
                        .appendQueryParameter("fitter_id", mobile);


                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            try {
                JSONObject res=new JSONObject(result);
                String status=res.getString("status");
                String msg=res.getString("msg");
                if(status != "error") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(stock_available.this);
                    builder.setTitle("Scan Result");
                    builder.setMessage(msg);
                    builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // Your positive action
                          return;
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(stock_available.this);
                    builder.setTitle("Scan Result");
                    builder.setMessage(msg +"  "+ serial_no);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            new stock_available.AsyncLogin().execute();
        }
    }
}

