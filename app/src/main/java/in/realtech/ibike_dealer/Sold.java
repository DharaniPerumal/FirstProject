package in.realtech.ibike_dealer;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.TextView.BufferType.SPANNABLE;
import static in.realtech.ibike_dealer.Sold.CONNECTION_TIMEOUT;
import static in.realtech.ibike_dealer.Sold.READ_TIMEOUT;

public class Sold extends AppCompatActivity {
    private static final String TAG = "test";

    ListView lview3;
    ListViewCustomAdapter adapter;
    private static String title[];
    private static String desc[];
    ArrayList<Model> arrayList = new ArrayList<Model>();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    private Animation animationUp;
    private Animation animationDown;
    MenuItem searchViewItem;
    SearchView searchView;
    String sno;
    String s;
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
    String expiry_dt;
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
    String mobiles_cal;

    Context context = Sold.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Sold");
        setContentView(R.layout.activity_sold);
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        title = new String[]{};
        desc = new String[]{};
        lview3 = (ListView) findViewById(R.id.listView3);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mprocessingdialog = new ProgressDialog(this);
        String Sold = getIntent().getExtras().getString("sold");
        this.setTitle("Sold " + " (" + Sold + " nos.)");
        // get the listview
//        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mobile = sharedpreferences.getString("mobile", "mobile");
        new AsyncLogin().execute(mobile);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (asyncTask == null) {

                    asyncTask = new AsyncLogin().execute(mobile);

                    pullToRefresh.setRefreshing(false);
                } else {
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {

                        asyncTask.cancel(true);

                        asyncTask = new AsyncLogin().execute(mobile);
                        adapter.notifyDataSetChanged();
                        pullToRefresh.setRefreshing(false);
                    } else {
                        asyncTask = new AsyncLogin().execute(mobile);
                        adapter.notifyDataSetChanged();
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
        ProgressDialog pdLoading = new ProgressDialog(Sold.this);
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
                        .appendQueryParameter("type", "user")
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
            Log.i(TAG, result + "sold");

            arrayList.clear();
//            int count = jsonArray.length();
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            JSONArray array = null;
            try {
                array = new JSONArray(result);

                int count = array.length();
                String str2 = Integer.toString(count);
                Log.i("total", str2);

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
                  String  plan = d.getString("plan");
                    settings = d.getString("settings");
                    sim = d.getString("sim");

                    Log.i("vehicle", vehicle_no);

                    cimi = d.getString("cimi");
                    qr_code = d.getString("qr_code");
                    expiry_dt = d.getString("expiry_dt");
                    Log.i("expiry_dt", expiry_dt);
                    payment_status = d.getString("payment_status");
                    price = d.getString("price");
                    coupon = d.getString("coupon");
                    image = d.getString("image");
//                    added_date = d.getString("added_date");
                    store_date = d.getString("store_date");
                    out_date = d.getString("out_date");
                    fitter_date = d.getString("fitter_date");
                    user_date = d.getString("user_date");
//                    String header ="    "+ vehicle_no
//                            + System.getProperty ("line.separator")
//                            + System.getProperty ("line.separator")
//                            +"    "+ username;
                    // Adding Header data
                    listDataHeader.add("  " + serial_no +" _ " +"(" + username + ")");

                    List<String> lease_offer = new ArrayList<String>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Calendar date = Calendar.getInstance();
                    date.setTime(dateFormat.parse(user_date));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                    String date1 = simpleDateFormat.format(date.getTime());

                    s = "Username : " + username
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + "Payment Status : " + payment_status
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + "Paid : Rs ," + price
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + "Plan : " + plan
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + "Sold Date : " + date1
                            + System.getProperty("line.separator")
                            + System.getProperty("line.separator")
                            + "vehicle No : " + vehicle_no;

                    desc = new String[]{s};
                    lease_offer.add(s);


//                    lease_offer.add("Price Rs : "+price);
//                    lease_offer.add("Sold-Date : "+date1);
//                    Log.i("result", lease_offer.toString());

                    // Header into Child data
                    listDataChild.put(listDataHeader.get(i), lease_offer);
                    title = new String[]{serial_no +" - " +"(" + username + ")"};
                    desc = new String[]{s};
                    String[] text = new String[]{vehicle_no + ":" + price};
                    for (int j = 0; j < title.length; j++) {
                        Model model = new Model(title[j], desc[j], text[j]);
                        arrayList.add(model);
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            adapter = new ListViewCustomAdapter(Sold.this, arrayList);
            lview3.setAdapter(adapter);


//            listAdapter = new ExpandableListAdapte(getApplicationContext(), listDataHeader, listDataChild);

            // setting list adapter
//            expListView.setAdapter(listAdapter);

//            expListView.setAdapter(listAdapter);
//            for(int i=0; i < listAdapter.getGroupCount(); i++) {
//                expListView.expandGroup(i);
//            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_menu, menu);
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
                    lview3.clearTextFilter();
                } else {
                    adapter.filter(s);
                }
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon action bar is clicked; go to parent activity
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class ListViewCustomAdapter extends BaseAdapter {
        public String title[];
        public String description[];
        public Activity context;
        public LayoutInflater inflater;
        ArrayList<Model> arrayList;
        List<Model> modellist;
        private Animation animationUp;
        private Animation animationDown;

        public ListViewCustomAdapter(Activity context, List<Model> modellist) {
            super();
            this.modellist = modellist;
            this.context = context;
            this.arrayList = new ArrayList<Model>();
            this.arrayList.addAll(modellist);
//            this.title = title;
//            this.description = description;

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return modellist.size();
        }

        @Override
        public Object getItem(int position) {
            return modellist.get(position);
        }


        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class ViewHolder {
            TextView txtViewTitle;
            TextView txtViewDescription;
            ImageView icon;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_sold, null);
                holder.txtViewTitle = (TextView) convertView.findViewById(R.id.txtViewTitle);
                holder.txtViewDescription = (TextView) convertView.findViewById(R.id.txtViewDescription);
                holder.icon = (ImageView) convertView.findViewById(R.id.icons);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.txtViewTitle.setText(modellist.get(position).getTitle());
            holder.txtViewDescription.setVisibility(View.GONE);
            animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
            animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);


            holder.icon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    new AsyncLogin1().execute(modellist.get(position).getTitle());

                }
            });


            holder.txtViewTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Intent intent = new Intent(Sold.this, InvoiceSold.class);
                    intent.putExtra("serial", modellist.get(position).getTitle());
                    intent.putExtra("text", modellist.get(position).getText());
                    startActivity(intent);
                    // TODO Auto-generated method stub
                    return true;
                }
            });

            holder.txtViewTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.txtViewDescription.isShown()) {
                        holder.txtViewDescription.setVisibility(View.GONE);
                        holder.txtViewDescription.startAnimation(animationUp);
                    } else {
                        holder.txtViewDescription.setVisibility(View.VISIBLE);
                        holder.txtViewDescription.startAnimation(animationDown);
                    }
                }
            });

            holder.txtViewDescription.setText(modellist.get(position).getDesc());
            return convertView;
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

    public class Model {
        String title;
        String desc;
        String text;

        //constructor
        public Model(String title, String desc, String text) {
            this.title = title;
            this.desc = desc;
            this.text = text;

        }

        public String getTitle() {
            return this.title;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getText() {
            return this.text;
        }

    }


    private class AsyncLogin1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getApplicationContext());
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
                        .appendQueryParameter("action", "fitter_users_serial")
                        .appendQueryParameter("serial", params[0]);


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

            try {
                JSONObject aray = new JSONObject(result);
                JSONArray data = aray.getJSONArray("data");
                for (int j = 0; j < data.length(); j++) {
                    JSONObject d = data.getJSONObject(j);
                    String name = d.getString("name");
                    String username = d.getString("username");
                     mobiles_cal = d.getString("mobile");
                     Log.i(TAG,mobiles_cal);
                }


                Permissions.check(context, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                    private int REQUEST_PHONE_CALL;

                    @Override
                    public void onGranted() {
//                Toast.makeText(mContext, "Phone granted.", Toast.LENGTH_SHORT).show();
                        String phone = mobiles_cal;
                        Log.i(TAG+ "phone",phone);
                        String d = "tel:" + phone;
                        Log.i("Make call", "helllo");
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse(d));
                        try {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions((Activity) context, Manifest.permission.CALL_PHONE, REQUEST_PHONE_CALL);

                            } else {
                                context.startActivity(phoneIntent);
                            }
                            context.startActivity(phoneIntent);

                            Log.i("Finished making a call", "");

                            Log.i("Finished making a call", "");
                        } catch (Exception e) {
                        }

                    }

                    private void requestPermissions(Activity mContext, String callPhone, int request_phone_call) {

                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                Toast.makeText(mContext, "permison denied.", Toast.LENGTH_SHORT).show();
                    }
                });




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}







//
//    public class ExpandableListAdapte extends BaseExpandableListAdapter {
//
//        private Context _context;
//        private List<String> _listDataHeader; // header titles
//        // child data in format of header title, child title
//        private HashMap<String, List<String>> _listDataChild;
//
//        public ExpandableListAdapte(Context context, List<String> listDataHeader,
//                                      HashMap<String, List<String>> listChildData) {
//            this._context = context;
//            this._listDataHeader = listDataHeader;
//            this._listDataChild = listChildData;
//        }
//
//        @Override
//        public Object getChild(int groupPosition, int childPosititon) {
//            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                    .get(childPosititon);
//        }
//
//        @Override
//        public long getChildId(int groupPosition, int childPosition) {
//            return childPosition;
//        }
//
//        @Override
//        public View getChildView(int groupPosition, final int childPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//
//            final String childText = (String) getChild(groupPosition, childPosition);
//
//            if (convertView == null) {
//                LayoutInflater infalInflater = (LayoutInflater) this._context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = infalInflater.inflate(R.layout.list_item, null);
//            }
//
////            ImageView childImage = (ImageView)convertView.findViewById(R.id.payment);
////            childImage.setImageResource(R.drawable.ic_payment_black_24dp);
//
//            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
//            txtListChild.setText(childText);
//            return convertView;
//
//        }
//
//        @Override
//        public int getChildrenCount(int groupPosition) {
//            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                    .size();
//        }
//
//        @Override
//        public Object getGroup(int groupPosition) {
//            return this._listDataHeader.get(groupPosition);
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this._listDataHeader.size();
//        }
//
//        @Override
//        public long getGroupId(int groupPosition) {
//            return groupPosition;
//        }
//
//        @Override
//        public View getGroupView(int groupPosition, boolean isExpanded,
//                                 View convertView, ViewGroup parent) {
//            String headerTitle = (String) getGroup(groupPosition);
//            if (convertView == null) {
//                LayoutInflater infalInflater = (LayoutInflater) this._context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = infalInflater.inflate(R.layout.list_group, null);
//            }
//
//
//            TextView lblListHeader = (TextView) convertView
//                    .findViewById(R.id.lblListHeader);
////            ImageView images = (ImageView) convertView
////                    .findViewById(R.id.imagesheader);
////            images.setImageResource(R.drawable.ic_payment_black_24dp);
//            lblListHeader.setTypeface(null, Typeface.BOLD);
//            lblListHeader.setText(headerTitle);
//            return convertView;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public boolean isChildSelectable(int groupPosition, int childPosition) {
//            return true;
//        }
//    }
//
//
//}




