package in.realtech.ibike_dealer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Locale;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import static in.realtech.ibike_dealer.TotalStock.CONNECTION_TIMEOUT;
import static in.realtech.ibike_dealer.TotalStock.READ_TIMEOUT;

@SuppressLint("Registered")
public class Pending_activity extends AppCompatActivity {
    private static final int REQUEST_WRITE_STORAGE = 1;
    ListView listView;
    List<String> list = new ArrayList<>();
    ListViewAdapters adapter;
    String[] title;
    String[] description;
    MenuItem searchViewItem;
    SearchView searchView;
    ArrayList<Model> arrayList = new ArrayList<Model>();
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private static final String TAG = "CardListActivity";
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
    String expiry_dt;
    String payment_status;
    String price;
    String coupon;
    String image;
    String added_date;
    String store_date;
    String out_date;
    String fitter_date;
    String user_date;
    AsyncTask asyncTask;
    Context context = Pending_activity.this;
    SwipeRefreshLayout pullToRefresh;
    String mobile;

    //    SwipeRefreshLayout swipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_details);

        String stock = getIntent().getStringExtra("stock");
        this.setTitle("Stock List" + " (" + stock + " nos.)");
        title = new String[]{};
        description = new String[]{};

        listView = findViewById(R.id.listView);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");

        new AsyncLogin().execute();
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (asyncTask == null) {
                    asyncTask = new AsyncLogin().execute();
                    pullToRefresh.setRefreshing(false);
                } else {
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                        asyncTask = new AsyncLogin().execute();
                        pullToRefresh.setRefreshing(false);
                    } else {
                        asyncTask = new AsyncLogin().execute();
                        pullToRefresh.setRefreshing(false);
                    }
                }

            }

        });
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Pending_activity.this);
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String result) {
            Log.i("result",result);
            arrayList.clear();

            pdLoading.dismiss();
//            Log.i("result", result);

            JSONArray array = null;
            try {
                array = new JSONArray(result);


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
                    added_date = d.getString("added_date");
                    store_date = d.getString("store_date");
                    out_date = d.getString("out_date");
                    fitter_date = d.getString("fitter_date");
                   String plan = d.getString("plan");
                    user_date = d.getString("user_date");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(fitter_date));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date1 = simpleDateFormat.format(date.getTime());
                    list.add(serial_no + "!" + qr_code + "!" + date1);
                    ListData ld = new ListData();
                    ld.setTitle(serial_no);
                    ld.setDescription(qr_code);
                    ld.setText(date1);
                    title = new String[]{serial_no};
                    String[]  paln = new String[]{plan};
                    description = new String[]{qr_code};
                    for (int j = 0; j < title.length; j++) {
                        Model model = new Model(title[j], description[j],paln[j]);
                        arrayList.add(model);
                    }
                    adapter = new ListViewAdapters(Pending_activity.this, arrayList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String header = arrayList.get(position).getTitle();
                            String qr = arrayList.get(position).getDesc();
                            Intent intent = new Intent(Pending_activity.this, Qrcodegentrater.class);
                            intent.putExtra("serial", header);
                            intent.putExtra("qr", qr);
                            startActivity(intent);
                            Log.v("clicked possion", header);
                            //display value here
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrmenu, menu);
        searchViewItem = menu.findItem(R.id.app_bar_search);
        searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchViewItem);
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
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (itemId == R.id.action_settings1) {

            IntentIntegrator integrator = new IntentIntegrator(Pending_activity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();


//            new IntentIntegrator(Pending_activity.this)
//                    .setOrientationLocked(false)
//                    .setCaptureActivity(ScannerActivity.class)
//                    .setBarcodeImageEnabled(true)
//                    .initiateScan();
        } else if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.download) {
            if(isReadStorageAllowed()){
                //If permission is already having then showing the toast
                //Toast.makeText(SplashActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                //Existing the method with return
                writeExcel();
                File file = new File(Environment.getExternalStorageDirectory()+ "/ExcelDownload/" +"testfile" + ".xls" );
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                startActivity(intent);
                return true;
            } else {
                requestStoragePermission();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");
                try {
                    String qr = result.getContents();
                    searchViewItem.expandActionView();
                    searchView.setQuery(qr, false);
                } catch (Exception e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                }

            }

        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            if (result.getContents() != null) {
//                try {
//                    String qr = result.getContents();
//                    searchViewItem.expandActionView();
//                    searchView.setQuery(qr, false);
//                } catch (Exception e) {
//                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
//                }
//            } else {
//                Log.v(TAG, "contents null");
//            }
//        } else {
//            Log.v(TAG, "result null");
//        }
//    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void writeExcel() {
        String Fnamexls = "testfile" + ".xls";
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/ExcelDownload");
        directory.mkdirs();
        File file = new File(directory, Fnamexls);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        if (list.size() > 0) {
            try {
                workbook = Workbook.createWorkbook(file, wbSettings);
                WritableSheet sheet = workbook.createSheet("Available Stocks", 0);
                try {
                    sheet.addCell(new Label(0, 0, "#"));
                    sheet.addCell(new Label(1, 0, "Serial No"));
                    sheet.addCell(new Label(2, 0, "Qr-Code"));
                    sheet.addCell(new Label(3, 0, "Date"));
                    for (int i = 0; i < list.size(); i++) {
                        String str = list.get(i);
                        String[] arr = str.split("!");
                        String number = arr[0];
                        String qrcode = arr[1];
                        String date = arr[2];
                        int j = i + 1;
                        sheet.addCell(new Label(0, j, String.valueOf(i + 1)));
                        sheet.addCell(new Label(1, j, number));
                        sheet.addCell(new Label(2, j, qrcode));
                        sheet.addCell(new Label(3, j, date));
                    }
                } catch (WriteException e) {
                    e.printStackTrace();
                    Log.v("Available Stocks", e.getLocalizedMessage());
                }
                try {
                    workbook.write();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    workbook.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }
    //Requesting permission
    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_STORAGE);
    }
    class ListViewAdapters extends BaseAdapter {
        //variabls
        Context mContext;
        LayoutInflater inflater;
        List<Model> modellist;
        ArrayList<Model> arrayList;
        //constructor
        public ListViewAdapters(Context context, List<Model> modellist) {
            mContext = context;
            this.modellist = modellist;
            inflater = LayoutInflater.from(mContext);
            this.arrayList = new ArrayList<Model>();
            this.arrayList.addAll(modellist);
        }
        public ListViewAdapters getFilter() {
            return null;
        }
        public class ViewHolder {
            TextView mTitleTv, mDescTv,mversion;
            ImageView btn;
        }
        @Override
        public int getCount() {
            return modellist.size();
        }
        @Override
        public Object getItem(int i) {
            return modellist.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(final int postition, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.row, null);
                //locate the views in row.xml
                holder.mTitleTv = view.findViewById(R.id.mainTitle);
                holder.mDescTv = view.findViewById(R.id.mainDesc);
                holder.mversion = view.findViewById(R.id.mainversion);
                holder.btn = view.findViewById(R.id.btnaccept);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //set the results into textviews
            holder.mTitleTv.setText(modellist.get(postition).getTitle());
            holder.mDescTv.setText(modellist.get(postition).getDesc());
            holder.mversion.setText(modellist.get(postition).getPlan());
            holder.mTitleTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Pending_activity.this, Qrcodegentrater.class);
                    intent.putExtra("serial", modellist.get(postition).getTitle());
                    intent.putExtra("qr", modellist.get(postition).getDesc());
                    startActivity(intent);
                }
            });
            holder.mDescTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Pending_activity.this, Qrcodegentrater.class);
                    intent.putExtra("serial", modellist.get(postition).getTitle());
                    intent.putExtra("qr", modellist.get(postition).getDesc());
                    startActivity(intent);
                }
            });

            final String qrcode=modellist.get(postition).getDesc();

            holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Pending_activity.this);
                builder.setMessage("Do you want to Return This "+"("+modellist.get(postition).getTitle()+")"+" ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new AsyncLogin1().execute(qrcode,mobile);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually

                alert.show();


            }
        });


            return view;
        }



        private class AsyncLogin1 extends AsyncTask<String, String, String> {
            ProgressDialog pdLoading = new ProgressDialog(Pending_activity.this);
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
                            .appendQueryParameter("action", "fitter_return_stock")
                            .appendQueryParameter("qr", params[0])
                            .appendQueryParameter("fitter_id", params[1]);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Pending_activity.this);

                try {
                    JSONObject obj=new JSONObject(result);
                   String status=obj.getString("status");
                    String msg=obj.getString("msg");
                    Log.i("result", result);
                    builder.setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    new AsyncLogin().execute();
                                    dialog.dismiss();
                                }
                            });

                    //Setting the title manually

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            modellist.clear();
            if (charText.length() == 0) {
                modellist.addAll(arrayList);
            } else {
                for (Model model : arrayList) {
                    if (model.getTitle().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        modellist.add(model);
                    }
                    if (model.getDesc().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        modellist.add(model);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}


