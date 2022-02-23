package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import in.realtech.ibike_dealer.Adapter.Service_Panel_Adapter;
import in.realtech.ibike_dealer.Model_Class.Panel_Model;

public class Popup_completed extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_completed);
        btn1 = (Button) findViewById(R.id.btn_ok);
        btn2 = (Button) findViewById(R.id.btn_cancel);

        btn2.setOnClickListener(this);

        new Asyncaccept().execute();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_cancel:
                intent = new Intent(this,Otpview.class);
                startActivity(intent);
                break;
        }
    }

    private class Asyncaccept extends AsyncTask<String, String, String> {
        private Panel_Model mPanel;
        URL url = null;
        ProgressDialog pdLoading;
        HttpURLConnection conn;
        String  staus;
        String query;
        Service_Panel_Adapter.MyViewHolder holders;
        public Asyncaccept() {
            //Panel_Model root_response;
//            this.mPanel=root_response;
//            this.holders=holder;
//            this.staus=s;
        }
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                // Enter URL address where your php file resides
                //url = new URL("htpp://igps.io/sales");

                url = new URL("http://65.0.207.184/service/api/dealer_app_api.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                // "action":"accept","sno":"1","imei":"","vehicle_no":"","fitter_name":"","fitter_accept_time":""
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("action","accept")
                        .appendQueryParameter("sno", mPanel.getSno())
                        .appendQueryParameter("imei", mPanel.getImei())
                        .appendQueryParameter("vehicle_no", mPanel.getVehicleno())
                        .appendQueryParameter("complaint", mPanel.getCompliant())
                        .appendQueryParameter("solution", mPanel.getSolution())
                        .appendQueryParameter("completion_time", mPanel.getCompletion_time())
                        .appendQueryParameter("cash_collected", mPanel.getCash_collected());

                query = builder.build().getEncodedQuery();
                Log.d("TAG", "queryData: "+query);
                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                pdLoading.dismiss();
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
                    return(result.toString());

                }else{
                    pdLoading.dismiss();
                    return("unSuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                pdLoading.dismiss();
                return "exception";
            } finally {
                pdLoading.dismiss();
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("TAG", "onPostExecute: servicePanel"+s);
//            if (staus.equals("Accept"))
//            {
//                pdLoading.dismiss();
//                Intent intent = new Intent(mcontext, Completed_Cancel.class);
//                mcontext.startActivity(intent);
//            }
        }
    }

}