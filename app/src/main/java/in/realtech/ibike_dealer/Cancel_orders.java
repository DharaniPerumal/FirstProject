package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Date;
import java.util.List;

public class Cancel_orders extends AppCompatActivity {
    ListView list;
    String[] product = {"BikeBoss"};

    String[] tnos = {};
    String[] amount11= {};
    String[] orderdate = {};
    String[] orderid = {};
    String[] status = {};
    List<RowItem> rowItems;


    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_orders);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mobile = sharedpreferences.getString("mobile", "mobile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        setTitle("Cancel Orders");
        list = (ListView) findViewById(R.id.list);
        new AsyncLogin().execute();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Cancel_orders.this);
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
                        .appendQueryParameter("action", "dealer_order")
                        .appendQueryParameter("fitter_id", mobile)
                        .appendQueryParameter("op", "get_orders_by_status")
                        .appendQueryParameter("status", "5")
                        ;

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
            Log.i("result", result);

            rowItems = new ArrayList<RowItem>();
            try {
                JSONArray array = new JSONArray(result);

                int count = array.length();
                String myorder = Integer.toString(count);
                Log.i("myorders",myorder);
                setTitle("Cancel Orders "+"(" + myorder + ")");

                for (int i = 0; i < array.length(); i++) {

                    JSONObject d = array.getJSONObject(i);
                    String qty = d.getString("qty");
                    String amount1 = d.getString("amount");
                    String dt = d.getString("dt");
                    String order_id = d.getString("order_id");
                    String sta = d.getString("status");


                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date inputDate = fmt.parse(dt);
                    fmt = new SimpleDateFormat("dd-MM-yyyy");
                    String date = fmt.format(inputDate);
                    Log.i("dt", date);

                    tnos = new String[]{qty + " Nos."};
                    amount11 = new String[]{"Rs " + amount1};
                    orderdate = new String[]{date};
                    orderid = new String[]{order_id};
                    status = new String[]{sta};

                    for (int j = 0; j < tnos.length; j++) {
                        RowItem item = new RowItem(tnos[j], amount11[j], orderdate[j], orderid[j],status[j]);
                        rowItems.add(item);
                    }
                }

                CustomBaseAdapter adapter = new CustomBaseAdapter(Cancel_orders.this, rowItems);
                list.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }



    }




    public class CustomBaseAdapter extends BaseAdapter implements ListAdapter {
        Context context;
        List<RowItem> rowItems;
        String orderidtext;

        public CustomBaseAdapter(Context context, List<RowItem> items) {
            this.context = context;
            this.rowItems = items;
        }



        /*private view holder class*/
        private class ViewHolder {

            TextView txtnos;
            TextView txtamt;
            TextView txtdate;
            TextView txtorderid;
            TextView utr;
            TextView statustext;
            TextView cancelbtn;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CustomBaseAdapter.ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.delivered_list, null);
                holder = new CustomBaseAdapter.ViewHolder();
                holder.txtnos = (TextView) convertView.findViewById(R.id.subtitle1);
                holder.txtamt = (TextView) convertView.findViewById(R.id.amount1);
                holder.txtdate = (TextView) convertView.findViewById(R.id.date1);
                holder.txtorderid = (TextView) convertView.findViewById(R.id.ordid);
                holder.utr = (TextView) convertView.findViewById(R.id.utr);
                holder.cancelbtn = (TextView) convertView.findViewById(R.id.cancel);
                holder.statustext =convertView.findViewById(R.id.status1);





                convertView.setTag(holder);


            }
            else {
                holder = (CustomBaseAdapter.ViewHolder) convertView.getTag();
            }

            final RowItem rowItem = (RowItem) getItem(position);

            holder.txtnos.setText(rowItem.getNos());
            holder.txtamt.setText(rowItem.getAmmount());
            holder.txtdate.setText(rowItem.getDate());
            holder.txtorderid.setText(rowItem.getOrderId());



            return convertView;
        }






        @Override
        public int getCount() {
            return rowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return rowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return rowItems.indexOf(getItem(position));
        }


    }

    public class RowItem {
        private String nos;
        private String amt;
        private String date3;
        private String orid;
        private String stas;

        public RowItem(String nos, String amt, String date3, String orid,String stas) {
            this.nos = nos;
            this.amt = amt;
            this.date3 = date3;
            this.orid = orid;
            this.stas = stas;
        }

        public String getNos() {
            return nos;
        }

        public void setNos(String nos) {
            this.nos = nos;
        }
        public String getStatus() {
            return stas;
        }

        public void setStatus(String stas) {
            this.stas = stas;
        }

        public String getAmmount() {
            return amt;
        }

        public void setAmmount(String amt) {
            this.amt = amt;
        }

        public String getDate() {
            return date3;
        }

        public void setDate(String date3) {
            this.date3 = date3;
        }

        public String getOrderId() {
            return orid;
        }

        public void setOrderid(String orid) {
            this.orid = orid;
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
