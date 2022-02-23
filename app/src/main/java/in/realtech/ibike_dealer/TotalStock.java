package in.realtech.ibike_dealer;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.List;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TotalStock extends AppCompatActivity {
    private static final String TAG = "stockavailable";
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String sno;
    String str2;
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
    EditText edit;
    private ProgressDialog mprocessingdialog;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stock = getIntent().getExtras().getString("stock");
        this.setTitle("Stock List "+"( "+ stock +" )");
        setContentView(R.layout.activity_total_stock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mprocessingdialog = new ProgressDialog(this);
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);
        edit = (EditText) findViewById(R.id.edit);
        list = new ArrayList<>();
        // get the listview


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");
        new AsyncLogin().execute(mobile);
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TotalStock.this);
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
                        .appendQueryParameter("type", "fitter")
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
            Log.i("dinesh", result);
            String consumptions;
            JSONArray array = null;
            try {
                array = new JSONArray(result);

                int count = array.length();
                String str2 = Integer.toString(count);


                for (int i = 0; i < array.length(); i++) {
                    JSONObject d = array.getJSONObject(i);

                    sno = d.getString("sno");
                    imei = d.getString("imei");
                    serial_no = d.getString("serial_no");
                    username = d.getString("username");
                    fitter_id = d.getString("fitter_id");
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
                    cimi = d.getString("cimi");
                    qr_code = d.getString("qr_code");
                    expiry_dt = d.getString("expiry_dt");
                    payment_status = d.getString("payment_status");
                    price = d.getString("price");
                    coupon = d.getString("coupon");
                    image = d.getString("image");
//                    added_date = d.getString("added_date");
                    store_date = d.getString("store_date");
                    out_date = d.getString("out_date");
                    fitter_date = d.getString("fitter_date");
                    user_date = d.getString("user_date");

                    // Adding Header data


                    List<String> lease_offer = new ArrayList<String>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(fitter_date));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date1 = simpleDateFormat.format(date.getTime());

                    lease_offer.add(qr_code);
                    lease_offer.add(date1);
//                    Log.i("result", lease_offer.toString());


                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date2 = Calendar.getInstance();
                    date.setTime(dateFormat1.parse(fitter_date));
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date3 = simpleDateFormat1.format(date2.getTime());
                    String total = serial_no
                            +"                                   "

                            + qr_code
                            + System.getProperty("line.separator");
                    list.add(total);
                    String s = list.get(i);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            adapter = new ArrayAdapter<String>(TotalStock.this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //  ItemClicked item = (ItemClicked) adapterView.getItemAtPosition(i);
                    String header = list.get(i);
                    Intent intent = new Intent(TotalStock.this, Qrcodegentrater.class);
                    intent.putExtra("header", header);
                    startActivity(intent);
                }


            });


            edit.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

                }

                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {

                }

                public void afterTextChanged(Editable arg0) {

                    adapter.getFilter().filter(arg0);

                }
            });
        }

        private class ItemClicked {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrmenu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
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
        if (itemId == R.id.action_settings1) {

            new IntentIntegrator(TotalStock.this)
                    .setOrientationLocked(false)
                    .setCaptureActivity(ScannerActivity.class)
                    .setBarcodeImageEnabled(true)
                    .initiateScan();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                try {
                    final String  newText = result.getContents();
                    Log.v(TAG,newText);
                    Log.i("qr",newText);
                    edit.setText(newText);

                } catch (Exception e) {
                    Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_SHORT);
                }
            } else {
                Log.v(TAG, "contents null");
            }
        } else {
            Log.v(TAG, "result null");
        }


    }


}


