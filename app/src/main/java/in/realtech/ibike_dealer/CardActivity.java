package in.realtech.ibike_dealer;

import android.app.ProgressDialog;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CardActivity extends AppCompatActivity implements SearchView.OnCloseListener, SearchView.OnQueryTextListener {
    private static final String TAG = "stockavailable";
    private SearchView search;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> childdata;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String sno;
    List<String> lease_offer = new ArrayList<String>();

    String imei;
    String serial_no;
    String username;
    String fitter_id;
    String vehicle_no;
    String device_type;
    String phase;
    String  latlng;
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

    private ProgressDialog mprocessingdialog;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String mobile;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stock_available);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mprocessingdialog = new ProgressDialog(this);
        String stock = getIntent().getExtras().getString("stock");
        this.setTitle("Stock List "+"( "+ stock +" )");
        searchView = (SearchView) findViewById(R.id.searchView);
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);



        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");
        new AsyncLogin().execute(mobile);
    }
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.expandGroup(i);
        }
    }
    @Override
    public boolean onClose() {
//        listAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CardActivity.this);
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
            String consumptions;
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            JSONArray array = null;
            try {
                array = new JSONArray(result);


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

                    // Adding Header data
                    listDataHeader.add(serial_no);



                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(fitter_date));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date1 = simpleDateFormat.format(date.getTime());
                    String s =  qr_code
                            + System.getProperty ("line.separator")
                            + System.getProperty ("line.separator")
                            +date1;

                    lease_offer.add(s);

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


            listAdapter = new in.realtech.ibike_dealer.ExpandableListAdapters(getApplicationContext(), listDataHeader, listDataChild);
//            adapter = new ArrayAdapter<String>(CardActivity.this, android.R.layout.simple_list_item_1,listDataHeader);
            // setting list adapter
            expListView.setAdapter(listAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }


                @Override
                public boolean onQueryTextChange(String query) {
                    List<String> headerlist = new ArrayList<String>();
                    for (String header : listDataHeader ) {
                        if (header.contains(query)) {


                        }


                    }

                    return false;
                }

            });


            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    String headerTitle = listDataChild.get(
                            listDataHeader.get(groupPosition)).get(childPosition);
                    String header=listDataHeader.get(groupPosition);

//                    Toast.makeText(
//                            getApplicationContext(),
//                            listDataHeader.get(groupPosition)
//                                    + " -> "
//                                    + listDataChild.get(
//                                    listDataHeader.get(groupPosition)).get(
//                                    childPosition), Toast.LENGTH_SHORT
//                    ).show();
                    Intent intent=new Intent(CardActivity.this,Qrcodegentrater.class);
                    intent.putExtra("qrcode",headerTitle);
                    intent.putExtra("serialno",header);
                    startActivity(intent);
                    return false;
                }
            });

        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        final android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchViewItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings1) {

        }
        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_settings1) {

            new IntentIntegrator(CardActivity.this)
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                try {
                    String qr = result.getContents();
//                    Log.i("qr",qr);
//                    new stock_available.AsyncLogin1().execute(qr);

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




