package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;

public class InvoiceSold extends AppCompatActivity {


    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String serial;
    String text;
    ImageView img;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_sold);
        setTitle("Invoice Generate");
         serial = this.getIntent().getExtras().getString("serial");
         text = this.getIntent().getExtras().getString("text");

        String[] words=serial.split("\\s");
        String first = words[0];
        Log.i("TAGCHECK",first);



        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        new AsyncLogin().execute(first);
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(InvoiceSold.this);
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
                JSONObject aray= new JSONObject(result);
                JSONArray data=aray.getJSONArray("data");
            for (int j = 0; j < data.length(); j++) {
                        JSONObject d = data.getJSONObject(j);
                        String name = d.getString("name");
                        String username = d.getString("username");
                        String mobile = d.getString("mobile");
                        String mail = d.getString("mail");
                        String date = d.getString("added_at");
                        String address = d.getString("address");
                        String postel = d.getString("postal");
                        String district = d.getString("district");
                        String city = d.getString("city");

                        final TextView tname = findViewById(R.id.name1);
                        final TextView tusername = findViewById(R.id.username1);
                        final TextView tmobile = findViewById(R.id.mobile1);
                        final TextView tmail = findViewById(R.id.mail1);
                        final TextView taddress = findViewById(R.id.address1);
                        final TextView tdate = findViewById(R.id.date1);
                        final TextView tpostel = findViewById(R.id.postal1);
                        final TextView tdistrict = findViewById(R.id.district1);
                        final TextView tserial = findViewById(R.id.serial1);
                        final TextView tcity = findViewById(R.id.city1);
                        final TextView tvehicle = findViewById(R.id.vehicle1);
                        final TextView tprice = findViewById(R.id.price1);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar date1 = Calendar.getInstance();
                date1.setTime(dateFormat.parse(date));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                String addate = simpleDateFormat.format(date1.getTime());


                        tname.setText(": "+name);
                        tusername.setText(": "+ username);
                        tmobile.setText(": "+ mobile);
                        tmail.setText(": "+ mail);
                        taddress.setText(": "+ address);
                        tdate.setText(": "+ addate);
                        tpostel.setText(": "+ postel);
                        tdistrict.setText(": "+district);
                        tserial.setText(": "+ serial);
                        tcity.setText(": "+ city);
                        String[] part = text.split(":");
                        final String vehicleno = part[0];
                        final String price = part[1];
                        tvehicle.setText(": "+ vehicleno);
                        tprice.setText(": Rs "+ price);
                myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                img = (ImageView) findViewById(R.id.imagev);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String text = tname.getText().toString();
                        String text1 = tusername.getText().toString();
                        String text2 = tmobile.getText().toString();
                        String text3 = tmail.getText().toString();
                        String text4 = taddress.getText().toString();
                        String text5 = tdate.getText().toString();
                        String text6 = tpostel.getText().toString();
                        String text7 = tdistrict.getText().toString();
                        String text8 = tserial.getText().toString();
                        String text9 = tcity.getText().toString();
                        String text10 = tvehicle.getText().toString();
                        String text11= tprice.getText().toString();
                        myClip = ClipData.newPlainText("details", "Name : " +text+","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Username : " + text1 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Mobile Number : " + text2 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Email : " + text3 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Address : " + text4 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Date : " + text5 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Postel : " + text6 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "District : " + text7 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Serial No : " + text8 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "City : " + text9 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Vehicle No : " + text10 +","  + System.getProperty("line.separator")
                                + System.getProperty("line.separator")+ "Price : " + text11);
                        myClipboard.setPrimaryClip(myClip);
                        Toast.makeText(getApplicationContext(), "Your Details Copied",
                                Toast.LENGTH_SHORT).show();
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

}

