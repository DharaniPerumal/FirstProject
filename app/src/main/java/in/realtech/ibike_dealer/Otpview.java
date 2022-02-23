package in.realtech.ibike_dealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Otpview extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    EditText txt1, txt2, txt3, txt4, txt5, txt6;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView tx1;
    Button find;
    String totalotp;
    String edit1;
    String edit2;
    String edit3;
    String edit4;
    String edit5;
    String edit6;
    String query;
    String status;
    String code;
    private String TAG="codes";
    private SmsVerifyCatcher smsVerifyCatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpview);

//        public static final String MyPREFERENCES = "MyPrefs" ;
//        SharedPreferences sharedpreferences;
//        EditText txt1, txt2, txt3, txt4, txt5, txt6;
//        TextView tx1;
//        Button find;
//        String totalotp;
//        String edit1;
//        String edit2;s
//        String edit3;
//        String edit4;
//        String edit5;
//        String edit6;
//        String query;
//        String status;
//        String code;
//        private String TAG="codes";
//        private SmsVerifyCatcher smsVerifyCatcher;
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_otpview);
        setTitle("OTP");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        txt1 = (EditText) findViewById(R.id.etDigit1);
        txt2 = (EditText) findViewById(R.id.etDigit2);
        txt3 = (EditText) findViewById(R.id.etDigit3);
        txt4 = (EditText) findViewById(R.id.etDigit4);
        txt5 = (EditText) findViewById(R.id.etDigit5);
        txt6 = (EditText) findViewById(R.id.etDigit6);
        tx1 = (TextView) findViewById(R.id.title1);
        final String mbl = getIntent().getExtras().getString("mobile");

        tx1.setText("We have send you on SMS on " + " +91 " + mbl + " with 6 digit verification Code");
        Button find = (Button) findViewById(R.id.button);
        txt1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt1.getText().toString().length() == 1) {
                    txt2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        txt2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt2.getText().toString().length() == 1) {
                    txt3.requestFocus();
                } else if (txt2.getText().toString().length() == 0) {
                    txt1.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        txt3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt3.getText().toString().length() == 1) {
                    txt4.requestFocus();
                } else if (txt3.getText().toString().length() == 0) {
                    txt2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        txt4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt4.getText().toString().length() == 1) {
                    txt5.requestFocus();
                } else if (txt4.getText().toString().length() == 0) {
                    txt3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        txt5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt5.getText().toString().length() == 1) {
                    txt6.requestFocus();
                } else if (txt5.getText().toString().length() == 0) {
                    txt4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        txt6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txt6.getText().toString().length() == 0) {
                    txt5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

            final String mobile = getIntent().getExtras().getString("mobilestr");
            Log.i("mpbile", mobile);
            smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
                @Override
                public void onSmsCatch(String message) {
                    code = parseCode(message);
                    String[] companies = code.split("|");
                    String f1 = companies[0];
                    String f2 = companies[1];
                    String f3 = companies[2];
                    String f4 = companies[3];
                    String f5 = companies[4];
                    String f6 = companies[5];
                    String f7 = companies[6];
                    txt1.setText(f2);
                    txt2.setText(f3);
                    txt3.setText(f4);
                    txt4.setText(f5);
                    txt5.setText(f6);
                    txt6.setText(f7);

                    edit1 = txt1.getText().toString();
                    edit2 = txt2.getText().toString();
                    edit3 = txt3.getText().toString();
                    edit4 = txt4.getText().toString();
                    edit5 = txt5.getText().toString();
                    edit6 = txt6.getText().toString();
                    Log.i("message", message);
                    Log.v(TAG, code);

                    totalotp = edit1 + edit2 + edit3 + edit4 + edit5 + edit6;
                    new AsyncLogin().execute(totalotp, mobile);
                    //then you can send verification code to server
                }
            });

            find.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    /* This code get the values from edittexts  */
                    edit1 = txt1.getText().toString();
                    edit2 = txt2.getText().toString();
                    edit3 = txt3.getText().toString();
                    edit4 = txt4.getText().toString();
                    edit5 = txt5.getText().toString();
                    edit6 = txt6.getText().toString();
                    totalotp = edit1 + edit2 + edit3 + edit4 + edit5 + edit6;

                    new AsyncLogin().execute(totalotp, mobile);
                }
            });


            String getMinutes = "10";//Get minutes from edittexf
            //Check validation over edittext


            final TextView _tv = (TextView) findViewById( R.id.textvalidity );
            final TextView _tv1 = (TextView) findViewById( R.id.textvalidity1 );
            new CountDownTimer(600000, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    _tv.setText(""+String.format("0%d : %d sec",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    _tv1.setVisibility(View.INVISIBLE);

                }

                public void onFinish() {
                    _tv.setVisibility(View.INVISIBLE);
                    _tv1.setVisibility(View.VISIBLE);
                    _tv1.setText("OTP Expired.please try Agin");
                }
            }.start();
        }

        private String parseCode(String message) {
            Pattern p = Pattern.compile("\\b\\d{6}\\b");
            Matcher m = p.matcher(message);
            String code = "";
            while (m.find()) {
                code = m.group(0);
            }

            return code;
        }

        @Override
        protected void onStart() {
            super.onStart();
            smsVerifyCatcher.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
            smsVerifyCatcher.onStop();
        }

        /**
         * need for Android 6 real time permissions
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        private class AsyncLogin extends AsyncTask<String, String, String>
        {
            ProgressDialog pdLoading = new ProgressDialog(Otpview.this);
            HttpURLConnection conn;
            URL url = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();
    }

            @Override
            protected String doInBackground(String... params) {
                try {
                    // Enter URL address where your php file resides
                    //url = new URL("htpp://igps.io/sales");

                    url = new URL("https://igps.io/http.php");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setReadTimeout(READ_TIMEOUT);
                    conn.setConnectTimeout(CONNECTION_TIMEOUT);
                    conn.setRequestMethod("POST");
                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("otp", params[0])
                            .appendQueryParameter("action", "fitter_login")
                            .appendQueryParameter("op", "otp")
                            .appendQueryParameter("hash", params[1]);

                    query = builder.build().getEncodedQuery();

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
                        return(result.toString());

                    }else{

                        return("unSuccessful");
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

                pdLoading.dismiss();
                Log.i("result",result);
                Log.i("quert",query);

                try {
                    JSONObject j = new JSONObject(result);
                    status = j.getString("status");
                    Log.i("status", status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(status.equalsIgnoreCase("error")) {
                    try {
                        JSONObject json = new JSONObject(result);
                        String error = json.getString("message");
                        String error1 = json.getString("status");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Otpview.this);
                        alertDialogBuilder.setTitle(error1);
                        alertDialogBuilder.setMessage(error);

                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {

                        JSONObject json = new JSONObject(result);
                        String success = json.getString("status");
                        String data = json.getString("data");

                        try {
                            JSONArray ar = new JSONArray(data);
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject d = ar.getJSONObject(i);

                                String id = d.getString("id");
                                String type = d.getString("type");
                                String name = d.getString("name");
                                String  mail= d.getString("mail");
                                String  mobile= d.getString("mobile");
                                String  username= d.getString("username");
                                String  password= d.getString("password");
                                String  status= d.getString("status");
                                String  shop_name= d.getString("shop_name");
                                String  state= d.getString("state");
                                String  district= d.getString("district");
                                String  pincode= d.getString("pincode");
                                String  address= d.getString("address");
                                String  latlong= d.getString("latlong");
                                String  beneid= d.getString("beneid");
                                String  bank= d.getString("bank");
                                String  bank_acc= d.getString("bank_acc");
                                String  bank_ifsc= d.getString("bank_ifsc");
                                String  bank_verified= d.getString("bank_verified");
                                String  verification_amt= d.getString("verification_amt");
                                String  upi_id= d.getString("upi_id");
                                String  pi= d.getString("pi");
                                String  cli= d.getString("cli");
                                String  ci= d.getString("ci");
                                String  added_by= d.getString("added_by");
                                String  added_at= d.getString("added_at");
                                Log.i("id",id);
                                SharedPreferences.Editor toEdit = sharedpreferences.edit();
                                toEdit.putString("query",query);
                                toEdit.putString("id",id);
                                toEdit.putString("type",type);
                                toEdit.putString("name",name);
                                toEdit.putString("mail",mail);
                                toEdit.putString("mobile",mobile);
                                toEdit.putString("username",username);
                                toEdit.putString("password",password);
                                toEdit.putString("status",status);
                                toEdit.putString("shop_name",shop_name);
                                toEdit.putString("state",state);
                                toEdit.putString("district",district);
                                toEdit.putString("pincode",pincode);
                                toEdit.putString("address",address);
                                toEdit.putString("latlong",latlong);
                                toEdit.putString("beneid",beneid);
                                toEdit.putString("bank",bank);
                                toEdit.putString("bank_acc",bank_acc);
                                toEdit.putString("bank_ifsc",bank_ifsc);
                                toEdit.putString("bank_verified",bank_verified);
                                toEdit.putString("verification_amt",verification_amt);
                                toEdit.putString("upi_id",upi_id);
                                toEdit.putString(  "pi",pi);
                                toEdit.putString("cli",cli);
                                toEdit.putString("ci",ci);
                                toEdit.putString("added_by",added_by);
                                toEdit.putString("added_at",added_at);
                                toEdit.commit();
                                toEdit.clear();

                                Intent intent = new Intent(Otpview.this, Dashboard.class);
                                startActivity(intent);
                                (Otpview.this).finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

