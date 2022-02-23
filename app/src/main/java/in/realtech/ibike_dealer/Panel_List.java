package in.realtech.ibike_dealer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import in.realtech.ibike_dealer.Adapter.Service_Panel_Adapter;
import in.realtech.ibike_dealer.Model_Class.Panel_Model;

public class Panel_List extends AppCompatActivity implements AdapterView.OnItemSelectedListener,PanelList {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences sh_Pref;
    ImageView info;
    String result;
    String str;
    ImageView imageView;
    String status;
    ProgressDialog pdLoading;
    RecyclerView recyclerView;
    List<Panel_Model> panelModelArrayList;
    Service_Panel_Adapter service_panel_adapter;
    Spinner spinner;

    String [] sp = {"ALL","ASSIGN","PENDING","COMPLETED"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_panel_list);
        imageView = (ImageView) findViewById(R.id.info);
        spinner = (Spinner) findViewById(R.id.Sp_Spinner);
        recyclerView = findViewById(R.id.recycler);

        panelModelArrayList = new ArrayList<>();
        service_panel_adapter = new Service_Panel_Adapter(this, panelModelArrayList,this);

        setAdapter();
        //setDataforRecyclerview();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp);
       // ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(ad);

        new AsyncServer("",-1,false).execute();
    }

//    private void setDataforRecyclerview() {
//
//        panelModelArrayList.add(new Panel_Model("red_xpress_cargo \r\n  _9944911122", "KA   51 AD 0434",
//                "9944911122", "0000000", "perundurai", "2022-01-19 03:16:21", "2001RT00003123"));
//        panelModelArrayList.add(new Panel_Model("test", "2001BB00001111", "000000000000", "000000000000",
//                "000000000", "2022-01-19 04:00:23", "2001BB00001111"));
//        panelModelArrayList.add(new Panel_Model("ss travels", "2109RT00003218",
//                "8760250766", "8564344326", "erode", "2022-01-19 05:15:04", "2109RT00003218"));
//        panelModelArrayList.add(new Panel_Model("11111", "2001BB00001555",
//                "9003246700", "1111111111", "888", "2022-01-19 05:56:22", "2001BB00001555"));
//        panelModelArrayList.add(new Panel_Model("2222", "2109RT00003222",
//                "22222", "222", "333", "2022-01-19 06:12:38", "2109RT00003222"));
//        panelModelArrayList.add(new Panel_Model("madura    itractor", "TN 25 W 9117",
//                "7358967366", "9548445", "tewst", "2022-01-24 10:44:13", "2001RT00003154"));
//        panelModelArrayList.add(new Panel_Model("l.periyasamy_9578999767", "TN 39 AU 8289",
//                "9578999767", "233", "erd", "2022-01-24 12:50:55", "2001RT00003148"));
//        panelModelArrayList.add(new Panel_Model("rtsvehicles", "Passion",
//                "9865212396", "", "erd", "2022-01-2412:54:27", "1908BB00000148"));
//        panelModelArrayList.add(new Panel_Model("Asian", "TN88Y4567(ACTIVA3G)",
//                "9442222855", "", "1", "2022-01-2402:31:00", "1908BB00000132"));
//        panelModelArrayList.add(new Panel_Model("l.periyasamy_9578999767", "TN 39 AU8289",
//                "9578999767", "12326633", "ev", "2022-01-2403:21:21", "2001RT00003148"));
//    }

    private void setAdapter() {
        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        service_panel_adapter = new Service_Panel_Adapter(this, panelModelArrayList,this);
        recyclerView.setAdapter(service_panel_adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItemPosition()==0 && !panelModelArrayList.isEmpty()){
            service_panel_adapter.addData(panelModelArrayList);
        }else if (adapterView.getSelectedItemPosition()==1 && !panelModelArrayList.isEmpty()){
            ArrayList<Panel_Model> arrayList = new ArrayList<>();//temparary storage
            for (int j=0;j<panelModelArrayList.size();j++)
            {
                Panel_Model panel_list = panelModelArrayList.get(j);
               if (panel_list.getReassign_count().equals("0"))
               {
                   arrayList.add(panel_list);
               }
            }
            service_panel_adapter.addData(arrayList);
        }
        else if (adapterView.getSelectedItemPosition()==2 && !panelModelArrayList.isEmpty())
        {
            ArrayList<Panel_Model> arrayList = new ArrayList<>();
            for (int j=0;j<panelModelArrayList.size();j++)
            {
                Panel_Model panel_list = panelModelArrayList.get(j);
                if (Integer.parseInt(panel_list.getReassign_count())>0)
                {
                    arrayList.add(panel_list);
                }
            }
            service_panel_adapter.addData(arrayList);
        }
        else if (adapterView.getSelectedItemPosition()==3 && !panelModelArrayList.isEmpty())
        {
            ArrayList<Panel_Model> arrayList = new ArrayList<>();
            for (int j=0;j<panelModelArrayList.size();j++)
            {
                Panel_Model panel_list = panelModelArrayList.get(j);
                if (panel_list.getReassign_count().equals("-3"))
                {
                    arrayList.add(panel_list);
                }
            }
            service_panel_adapter.addData(arrayList);
        }
//        if (adapterView.getItemAtPosition(i).equals(0)) {
//
//        } else {
//            ArrayList arrayList = new ArrayList();
//            arrayList.add(panelModelArrayList);
//            for (int i=0;i<panelModelArrayList.size();i++){
//                if (adapterView.getItemAtPosition(i).equals("ASSIGNED")) {
//                    if (reassign_count>=0){
//                        arrayList.add(panelModelArrayList);
//                    }
//                }
//            }
//
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void setRefreshData(String id, int position, boolean isBtnViewed) {
        new AsyncServer(id,position,isBtnViewed).execute();
    }

    private class AsyncServer extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Panel_List.this);
        HttpURLConnection conn;
        URL url = null;
        String serialId;
        int positions;
        boolean isBtnVisible;

        public AsyncServer(String serialId, int positions, boolean isBtnVisible) {
            this.isBtnVisible=isBtnVisible;
            this.serialId=serialId;
            this.positions=positions;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://65.0.207.184/service/api/service_entry_status.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                Uri.Builder builder = new Uri.Builder();
                StringBuilder builder1 = new StringBuilder();
                builder1.append("{");
//                for (String key : params.keySet()) {
//                    builder.appendQueryParameter(key, params.get(key));
//                    builder1.append("\"").append(key).append("\"").append(":").append("\"").
//                            append(params.get(key)).append("\"").append(",");
//                }
                builder1.deleteCharAt(builder1.length() - 1);
                builder1.append("}");

                //String query = builder.build().getEncodedQuery();
                //Log.v(TAG, builder1.toString());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
//                    writer.write(builder1.toString());
//                else
                writer.write("{\"data\":\"All\"}");
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ("exception");
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            str = result;
            Log.i("result", result);
            updates(result);

        }

        void updates(String result) {
            panelModelArrayList.clear();
            if (result.equalsIgnoreCase("exception")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Panel_List.this); //Home is name of the activity
                builder.setTitle("Error");
                builder.setMessage("Check your Internet connection, Try agin...");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                try {
                    JSONArray ar = new JSONArray(result);
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject d = ar.getJSONObject(i);
                        String sno = d.getString("sno");
                        String username = d.getString("username");
                        String vehicleno = d.getString("vehicleno");
                        String contact1 = d.getString("contact1");
                        String contact2 = d.getString("contact2");
                        String imei = d.getString("imei");
                        String serialno = d.getString("serialno");
                        String problem_category = d.getString("problem_category");
                        String place = d.getString("place");
                        String product_type = d.getString("product_type");
                        String problem = d.getString("problem");
                        String vehicle_avail = d.getString("vehicle_avail");
                        String dt = d.getString("dt");
                        String reassign_reason = d.getString("reassign_reason");
                        String reassign_date = d.getString("reassign_date");
                        String reassign_count = d.getString("reassign_count");
                        String fitter_name = d.getString("fitter_name");
                        String fitter_accept_time = d.getString("fitter_accept_time");
                        String completion_time = d.getString("completion_time");
                        String compliant = d.getString("compliant");
                        String solution = d.getString("solution");
                        String cash_collected = d.getString("cash_collected");
                        String cancel_reason = d.getString("cancel_reason");
                        Log.i("sno", "ghjjhgjhj");

                       if (isBtnVisible && serialno.equals(serialId))//sno equals
                       {
                           panelModelArrayList.add(new Panel_Model(sno,username,vehicleno
                                   ,contact1,contact2,imei,serialno,problem_category,place,product_type,problem,vehicle_avail,
                                   dt,reassign_reason,reassign_date,reassign_count,fitter_name,fitter_accept_time,completion_time,
                                   compliant,solution,cash_collected,cancel_reason, true));
                       }
                       else{
                           panelModelArrayList.add(new Panel_Model(sno,username,vehicleno
                                   ,contact1,contact2,imei,serialno,problem_category,place,product_type,problem,vehicle_avail,
                                   dt,reassign_reason,reassign_date,reassign_count,fitter_name,fitter_accept_time,completion_time,
                                   compliant,solution,cash_collected,cancel_reason,false));
                       }
                    }
                    service_panel_adapter.addData(panelModelArrayList);

                } catch (JSONException e) {

                    Log.e("TAG", "errorUpdate: " + e.toString());

                    e.printStackTrace();

                }

            }

        }

    }
}