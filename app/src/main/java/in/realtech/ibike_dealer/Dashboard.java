package in.realtech.ibike_dealer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.content.SharedPreferences;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.navigation.NavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.timessquare.CalendarPickerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static in.realtech.ibike_dealer.MainActivity.CONNECTION_TIMEOUT;
import static in.realtech.ibike_dealer.MainActivity.READ_TIMEOUT;
import static in.realtech.ibike_dealer.MainActivity.query;
public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ImageView image;
    Toolbar toolbar;
    private Animation animationUp;
    private Animation animationDown;
    String expiry;
    String mobile;
     String stock;
    String[] country = {};
    String datetime1;
    List<String> ent = new ArrayList<>();
    String datetime;
    String pi;
    String[] earning = new String[]{};
    String strr;
    String str1;
    String newdate1;
    TextView textView;
//     LineChart chart;
    CalendarPickerView calendar;
    Date first;
    Date last;
    AsyncTask asyncTask;
    private SimpleDateFormat dateFormatter;
    Menu menu;
//    EditText mEditInit;
    SwipeRefreshLayout pullToRefresh;
    String return_stock;
    String sales;
    String name,mail,district,state,shop_name,address,pincode;
    float f;
    String mDate;
    CardView Dashboard;

    private static final String TAG = in.realtech.ibike_dealer.MainActivity.class.getSimpleName();
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"REFGF");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView txtTitle = (TextView) findViewById(R.id.txtd4);
//        circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
        CardView card_view7 = findViewById(R.id.card_view9); // creating a CardView and assigning a value.

        card_view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashboard.this, Panel_List.class);
                startActivity(intent);
            }
        });


        pullToRefresh = findViewById(R.id.pullToRefresh);
        Date m = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(m);
        cal.add(Calendar.DATE, -7); // 10 is the days you want to add or subtract
        m = cal.getTime();

//        chart = findViewById(R.id.chart);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateandTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String older_date = sdf1.format(m);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        datetime = older_date;
        datetime1 = currentDateandTime;
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String id = sharedpreferences.getString("id", "id");
        String type = sharedpreferences.getString("type", "type");
        String name = sharedpreferences.getString("name", "name");
        String mail = sharedpreferences.getString("mail", "mail");
         mobile = sharedpreferences.getString("mobile", "mobile");

        String shop_name = sharedpreferences.getString("shop_name", "shop_name");
        String state = sharedpreferences.getString("state", "state");
        String district = sharedpreferences.getString("district", "district");
        String pincode = sharedpreferences.getString("pincode", "pincode");
        String address = sharedpreferences.getString("address", "address");
        String  pi = sharedpreferences.getString(mobile + "_pi", mobile + "_pi");
        setTitle(name);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final View hView =  navigationView.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.user);
        nav_user.setText(name);
         menu = navigationView.getMenu();
        TextView nav = hView.findViewById(R.id.textView);
        nav.setText(mail);
        image = hView.findViewById(R.id.imageView);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        new AsyncLogin().execute(mobile,datetime,datetime1);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (asyncTask == null) {
                    asyncTask =    new AsyncLogin().execute(mobile,datetime,datetime1);
                    pullToRefresh.setRefreshing(false);
                } else {
                    if (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        asyncTask.cancel(true);
                        asyncTask =  new AsyncLogin().execute(mobile,datetime,datetime1);
                        pullToRefresh.setRefreshing(false);
                    } else {
                        asyncTask =   new AsyncLogin().execute(mobile,datetime,datetime1);
                        pullToRefresh.setRefreshing(false);
                    }
                }
            }
        });
        pullToRefresh.setRefreshing(true);
    }

    private ArrayList<Date> getHolidays(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = datetime;

        Date date = null;
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Date> holidays = new ArrayList<>();
        holidays.add(date);

        return holidays;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dash_calender, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        View view = MenuItemCompat.getActionView(searchMenuItem);
        ImageButton  closeSearchViewImageButton = (ImageButton) view
                .findViewById(R.id.closeImageButton);
        closeSearchViewImageButton
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                    {
                        MenuItemCompat.collapseActionView(searchMenuItem);
                    }
                });

        final EditText searchEditText = (EditText) view
                .findViewById(R.id.searchEditText);
        searchEditText.setText(datetime +" To " +datetime1);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Dashboard.this);
                LayoutInflater inflater = Dashboard.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.activity_dateselect, null);
                dialogBuilder.setView(dialogView);
                final CalendarPickerView calendar = dialogView.findViewById(R.id.calendar_view);
                Calendar pastYear = Calendar.getInstance();
                pastYear.add(Calendar.YEAR, -1);
                Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.DATE,1);
                calendar.init(pastYear.getTime(), nextYear.getTime()) //
                        .inMode(CalendarPickerView.SelectionMode.RANGE)
                        .withSelectedDate(new Date());
                calendar.getSelectedDates();
                calendar.highlightDates(getHolidays());
                Button btnn = dialogView.findViewById(R.id.btt);
                final AlertDialog alertDialog = dialogBuilder.create();
                btnn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Date first = calendar.getSelectedDates().get(0);
                        Date last = calendar.getSelectedDates().get(calendar.getSelectedDates().size() - 1);
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String strDate = dateFormat.format(first);
                        System.out.println("array"+calendar.getSelectedDates());
                        System.out.println("first"+first);
                        System.out.println("last"+last);
                        DateFormat datefor = new SimpleDateFormat("dd-MM-yyyy");
                        String strDa = datefor.format(last);
                        searchEditText.setText(strDate +" To " +strDa);


                        if(calendar.getSelectedDates().size()<8){
                            Toast.makeText(Dashboard.this,"Select From and To, (ex)This to this. ",Toast.LENGTH_SHORT).show();
                        }else {
                            new AsyncLogin().execute(mobile,strDate,strDa);
                            alertDialog.dismiss();
                        }

                    }
                });
//                                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
    private class AsyncLogin extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(Dashboard.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            Log.v(TAG,"fhgfjgh");
            super.onPreExecute();
            pullToRefresh.setRefreshing(true);
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


//            return_stock = sharedpreferences.getString("Onms", "Onms");
//            menu.findItem(R.id.nav_Return).setTitle("Returned to Store"+" ("+ return_stock +" nos.)");
//            Log.i("return_stock",return_stock);

            String dateStr = params[1];
            SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy");
            Date dateObj = null;
            {
                try {
                    dateObj = curFormater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
            String from = postFormater.format(dateObj);

            String dateStr1 = params[2];
            SimpleDateFormat curFormater1 = new SimpleDateFormat("dd-MM-yyyy");
            Date dateObj1 = null;
            {
                try {
                    dateObj1 = curFormater1.parse(dateStr1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            SimpleDateFormat postFormater1 = new SimpleDateFormat("yyyy-MM-dd");

            String to = postFormater1.format(dateObj1);


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
                        .appendQueryParameter("from", from)
                        .appendQueryParameter("to", to);


                query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(query);//("{"\data\":\All\"}")
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

//            circularProgressBar.postDelayed(new Runnable() {
//                public void run() {
//                    circularProgressBar.setVisibility(View.INVISIBLE);
//                }
//            }, 6000);

            pullToRefresh.setRefreshing(false);
            pdLoading.dismiss();
            ArrayList<Entry> entries = new ArrayList<>();
            JSONObject j = null;
            return_stock = sharedpreferences.getString("Onms", "Onms");
            String myorders = sharedpreferences.getString("myorders", "myorders");
            menu.findItem(R.id.nav_Return).setTitle("Returned to Store" + " (" + return_stock + " nos.)");
//            menu.findItem(R.id.nav_myorders).setTitle("My Orders "+" ("+ myorders +")");
//

            menu.findItem(R.id.nav_otp).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
//                    showDialog(Dashboard.this,"First Custom Dialog");
                    new AsyncLogin1().execute();

//                    AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
//                    LayoutInflater inflater = getLayoutInflater();
//                    View dialogLayout = inflater.inflate(R.layout.activity_resend_otp, null);
//                    final TextView ratingBar = dialogLayout.findViewById(R.id.textviewotp);
//                    builder.setView(dialogLayout);
//
//                    builder.show();
//
//                    Intent intent = new Intent(Dashboard.this, ResendOtp.class);
//                    startActivity(intent);
                    return false;
                }
            });


            menu.findItem(R.id.nav_salse).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
//                    Intent intent = new Intent(Dashboard.this, Webview_online.class);
//                    startActivity(intent);
                    String url = "http://dealer.igps.io/mobile_session.php?mobile=" + mobile + "&name=" + name + "&mail=" + mail + "&shop_name=" + shop_name + "&address=" + address + "&district=" + district + "&state=" + state + "&pincode=" + pincode + "&pi=" + pi;
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                    return false;
                }
            });
            Log.i("ibike",return_stock);

            try {
                j = new JSONObject(result);
                String status = j.getString("status");
                String name = j.getString("name");
                String image = j.getString("image");
                stock = j.getString("stock");
                final String sold = j.getString("sold");
                final String pending = j.getString("pending");
                menu.findItem(R.id.nav_slideshow).setTitle("Waiting List" + " (" + pending + " nos.)");
                final String customers = j.getString("Completed");
                JSONArray chart1 = j.getJSONArray("chart");
                JSONArray fitter_data = j.getJSONArray("fitter_data");
                final TextView stockname = findViewById(R.id.txtd1);
                stockname.setText("Available Stocks");
                final TextView stock1 = findViewById(R.id.txtd2);
                stock1.setText(stock);
                menu.findItem(R.id.nav_stocklist).setTitle("My Stock List" + " (" + stock + " nos.)");
                final TextView soldname = findViewById(R.id.txtd3);
                soldname.setText("Sold");
                final TextView sold1 = findViewById(R.id.txtd4);
                sold1.setText(sold);
                final TextView earningsname = findViewById(R.id.txtd5);
                earningsname.setText("Waiting List");
                final TextView earnings1 = findViewById(R.id.txtd6);
                earnings1.setText(pending);
                final TextView customersname = findViewById(R.id.txtd7);
                customersname.setText("Completed");
                final TextView Newname = findViewById(R.id.txtd8);
                customersname.setText("Service");
                final TextView Installationname = findViewById(R.id.txtd7);
                customersname.setText("New Installation");
                final TextView customers1 = findViewById(R.id.txtd8);
                customers1.setText(customers);
                ent.clear();
                for (int i = 0; i < chart1.length(); i++) {
                    JSONObject d = chart1.getJSONObject(i);
                    String date = d.getString("date");
                    sales = d.getString("sales");
                    country = new String[]{sales};
                    String ear = d.getString("earning");
                    earning = new String[]{date};
                    float k = Float.parseFloat(sales);
                    entries.add(new Entry(i, k));
                    Log.i("sales", sales);
                    ent.add(date);
                }

                for (int i = 0; i < fitter_data.length(); i++) {
                    JSONObject d = fitter_data.getJSONObject(i);
                    pi = d.getString("pi");
                }
                // ********Dashboard******//
                CardView card_view1 = findViewById(R.id.card_view3); // creating a CardView and assigning a value.

                card_view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Pending_activity.class);
                        intent.putExtra("stock", stock);

                        startActivity(intent);
                    }
                });
                // ********Sold******//
                CardView card_view2 = findViewById(R.id.card_view2); // creating a CardView and assigning a value.

                card_view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Sold.class);
                        intent.putExtra("sold", sold);
                        startActivity(intent);
                    }
                });
                // ********Earnings******//
                CardView card_view3 = findViewById(R.id.card_view4); // creating a CardView and assigning a value.

                card_view3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, stock_available.class);
                        intent.putExtra("pending", pending);
                        startActivity(intent);
                    }
                });
                // ********Customers******//
                CardView card_view4 = findViewById(R.id.card_view5); // creating a CardView and assigning a value.

                card_view4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Customers.class);
                        intent.putExtra("customers", customers);
                        startActivity(intent);
                    }
                });

                CardView card_view5 = findViewById(R.id.card_view6); // creating a CardView and assigning a value.

                card_view5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Webview_online.class);
                        startActivity(intent);
                    }
                });

                CardView card_view6 = findViewById(R.id.card_view8); // creating a CardView and assigning a value.

                card_view6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Sales_activation_code.class);
                        startActivity(intent);
                    }
                });

                CardView card_view7 = findViewById(R.id.card_view9); // creating a CardView and assigning a value.

                card_view7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Dashboard.this, Panel_List.class);
                        startActivity(intent);
                    }
                });
//                Dashboard = (CardView) findViewById(R.id.card_view9);
//                Dashboard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i;
//                        switch (view.getId()) {
//                            case R.id.card_view9:
//                                i = new Intent(Dashboard.this, Panel_List.class);
//                                startActivity(i);
//                                break;
//                        }
//                    }

//                card_view7.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent i = new Intent(Dashboard.this,Panel_List.class);
//                        startActivity(i);
//                    }
//                });

//                LineDataSet dataSet = new LineDataSet(entries, "Sold");
//
//                dataSet.setColor(ContextCompat.getColor(Dashboard.this, R.color.colorPrimary));
//                dataSet.setValueTextColor(ContextCompat.getColor(Dashboard.this, R.color.colorPrimaryDark));
//
//                XAxis xAxis = chart.getXAxis();
//                // Set the xAxis position to bottom. Default is top
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                //Customizing x axis value
////                final String months = time;
//
//                xAxis.setValueFormatter(new ValueFormatter() {
//
//
//                    @Override
//                    public String getFormattedValue(float value) {
//
//                        Integer index= (int)value;
//                        try {
//
//                            strr= ent.get(index);
//                            String[] part = strr.split(" ");
//                            String val = part[0];
////                            Log.i("vval",val);
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                            Calendar date = Calendar.getInstance();
//                            date.setTime(dateFormat.parse(val));
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
//                            mDate = simpleDateFormat.format(date.getTime());
////                            Log.i("mdate",mDate);
////                            mDate=val;
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        return mDate;
//                    }
//                });
//            IAxisValueFormatter formatter = new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    return mDate;
//                }
//            };
//                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
////            xAxis.setValueFormatter(formatter);
//                //***
//                // Controlling right side of y axis
//                YAxis yAxisRight = chart.getAxisRight();
//                yAxisRight.setEnabled(false);
//                //***
//                // Controlling left side of y axis
//                YAxis yAxisLeft = chart.getAxisLeft();
//                yAxisLeft.setGranularity(1f);
//                dataSet.setColor(Color.GREEN);
//                dataSet.setCircleColor(Color.GRAY);
//                dataSet.setDrawFilled(true);
//                dataSet.setDrawValues(false);
//                dataSet.setCircleHoleRadius(0.1f);
//                dataSet.setFillColor(Color.DKGRAY);
//                //            dataSet.setFillColor(Color.RED);
//                // Setting Data
//                LineData data = new LineData(dataSet);
////            chart.setBackgroundColor(Color.WHITE);
//                chart.getDescription().setEnabled(false);
//
//                // enable touch gestures
//                chart.setTouchEnabled(true);
//                chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){
//                    @SuppressLint("ShowToast")
//                    @Override
//                    public void onValueSelected(Entry e, Highlight h)
//                    {
//                        float x=e.getX();
//                        Integer index= (int)x;
//                        String strr1= ent.get(index);
////                        Log.i("x",strr1);
//                        String[] part = strr1.split(" ");
//                        String val = part[0];
//                        String time = val;
//
//                        try {
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            Date dateObj = sdf.parse(time);
//                            System.out.println(dateObj);
////                            Log.i("x",dateObj.toString());
//                            newdate1= new SimpleDateFormat("dd-MM-yy").format(dateObj);
//
//                        } catch (final ParseException e1) {
//                            e1.printStackTrace();
//                        }
//
//                        float y=e.getY();
//                        String yvalue = Float.toString(y);
//                        int val2 = Math.round(y);
//                        str1 = Integer.toString(val2);
////                        TextView text =(TextView)findViewById(R.id.edittex);
////                        text.setText(str1+" Nos. "+"@ "  +  newdate1);
////                        Toast.makeText(Dashboard.this,str1+" Nos. "+"@ "  +  newdate1,Toast.LENGTH_SHORT).show();
////                        textView.setText( str1+"% "+"@ "  +  newdate1);
//                        Log.i("y",str1);
//                    }
//                    @Override
//                    public void onNothingSelected()
//                    {
//
//                    }
//                });
//                // set listeners
//                if (Utils.getSDKInt() >= 0) {
//                    // drawables only supported on api level 18 and above
//                    Drawable drawable = ContextCompat.getDrawable(Dashboard.this, R.drawable.fade_red);
//                    dataSet.setFillDrawable(drawable);
//                } else {
//                    dataSet.setFillColor(Color.BLACK);
//                }
//
//                chart.setDrawGridBackground(false);
//                chart.setDragEnabled(true);
//                chart.setScaleEnabled(true);
//                chart.setPinchZoom(true);
//                chart.setData(data);
////                Log.i("data",dataSet.toString());
//                chart.animateX(3500);
//                //refresh
//                chart.invalidate();
//                });
            } catch (JSONException f) {
                f.printStackTrace();

            }

            image.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Dashboard.this, Full_Srce_Myprofile.class);
                    intent.putExtra("mobile", mobile);
                    intent.putExtra("images", pi);
                    startActivity(intent);

                }
            });
            int i = 0;
            setImage(image, i, mobile);
        }

        private String setImage(ImageView imageView, int i, String mobile) {
            String imgNameServer = pi;
            try {
                if (!imgNameServer.equals("null")) {
                    String imgNameLocal = sharedpreferences.getString(mobile + "_pi", "");
                    if (imgNameLocal.equals(imgNameServer)) {
                        File imgDir = new File(getFilesDir() + File.separator + "photos");
                        if (!imgDir.exists()) {
                            imgDir.mkdirs();
                        }
                        File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
                        if (imgFile.exists()) {
                            Glide.with(Dashboard.this)
                                    .asBitmap()
                                    .load(imgFile)
                                    .apply(RequestOptions.circleCropTransform())
                                    .listener(new RequestListener<Bitmap>() {
                                        @Override

                                        public boolean onLoadFailed(GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
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
            } catch (Exception ex) {
                return ("exception");
            }
            return imgNameServer;
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
            Glide.with(Dashboard.this)
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
                                }
                                else {

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private class AsyncLogin1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Dashboard.this);
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
                        .appendQueryParameter("action", "fitter_login_web")
                        .appendQueryParameter("op", "get_otp")
                        .appendQueryParameter("mobile", mobile);

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
            Log.i(TAG, result);

            JSONObject aray= null;
            try {
                aray = new JSONObject(result);
                String status =aray.getString("status");
                String otp =aray.getString("otp");

                if ( status.equals("error")) {

                } else {
                     expiry = aray.getString("expiry");
                }

                String pipeDelimited = otp;
                Log.i("otp",pipeDelimited);
                String[] companies = pipeDelimited.split("|");
                String f1= companies[0];
                String f2= companies[1];
                String f3= companies[2];
                String f4= companies[3];
                String f5= companies[4];
                String f6= companies[5];
                String f7= companies[6];

                showDialog(Dashboard.this,"First Custom Dialog",f7,f2,f3,f4,f5,f6,status,otp,expiry);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

       public void showDialog(Activity activity, String msg,String f7,String f2,String f3,String f4,String f5,String f6,String status,String otp,String expiry){

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.round_corner);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView text1 = (TextView) dialog.findViewById(R.id.text_otp1);
        TextView text2 = (TextView) dialog.findViewById(R.id.text_otp2);
        TextView text3 = (TextView) dialog.findViewById(R.id.text_otp3);
        TextView text4 = (TextView) dialog.findViewById(R.id.text_otp4);
        TextView text5 = (TextView) dialog.findViewById(R.id.text_otp5);
        TextView text6 = (TextView) dialog.findViewById(R.id.text_otp6);
        TextView otpexprired = (TextView) dialog.findViewById(R.id.otpexprired);
        TextView otpvallitity = (TextView) dialog.findViewById(R.id.otpvallitity);


        if ( status.equals("error")){
            otpexprired.setText(otp);
            text1.setVisibility(View.INVISIBLE);
            text2.setVisibility(View.INVISIBLE);
            text3.setVisibility(View.INVISIBLE);
            text4.setVisibility(View.INVISIBLE);
            text5.setVisibility(View.INVISIBLE);
            text6.setVisibility(View.INVISIBLE);
            otpvallitity.setVisibility(View.INVISIBLE);

        } else  {
         otpexprired.setVisibility(View.INVISIBLE);
            String dateStr =expiry;
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateObj = null;
            {
                try {
                    dateObj = curFormater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
            String from = postFormater.format(dateObj);

         otpvallitity.setText("Validity Till "+from);
        text1.setText(f2);
        text2.setText(f3);
        text3.setText(f4);
        text4.setText(f5);
        text5.setText(f6);
        text6.setText(f7);

    }
        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

           ImageView img1 = (ImageView) dialog.findViewById(R.id.refresh);
           img1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   dialog.dismiss();
                   new AsyncLogin1().execute();
               }
           });
        dialog.show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.myprofile.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_stocklist) {
            Intent intent = new Intent(Dashboard.this, Pending_activity.class);
            intent.putExtra("stock", stock);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.stock_available.class);
            startActivity(intent);
        } else if (id == R.id.nav_Return) {
            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.Return_to_Store.class);
            startActivity(intent);

//        }  else if (id == R.id.nav_purchase) {
//        Intent intent = new Intent(Dashboard.this, purchase_product.class);
//        startActivity(intent);

//    } else if (id == R.id.nav_myorders) {
//            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.My_orders.class);
//            startActivity(intent);
//        }

//    } else if (id == R.id.nav_payments) {
//            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.Payment_Activity.class);
//            startActivity(intent);
//
//        }

//        }else if (id == R.id.nav_salse) {
////
//            Intent intent = new Intent(Dashboard.this, in.realtech.ibike_dealer.Onlinepurchase.class);
//            startActivity(intent);
//        }
        }
        else if (id == R.id.nav_tools) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this); //Home is name of the activity
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor toEdit = sharedpreferences.edit();
                    toEdit.clear();
                    toEdit.commit();
                    finish();
                    Intent i = new Intent(Dashboard.this, in.realtech.ibike_dealer.MainActivity.class);
                    i.putExtra("finish", true);
//                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                    startActivity(i);
                    finish();

                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
