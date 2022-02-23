package in.realtech.ibike_dealer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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



public class MainActivity extends AppCompatActivity {
    SharedPreferences sh_Pref;
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private EditText etEmail;
    private EditText etPassword;
    private CheckBox saveLoginCheckBox;
    public static String uname, pass;
    public static String query;
    private Boolean saveLogin;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        etEmail = (EditText) findViewById(R.id.mnumber);

       String sUsername = etEmail.getText().toString();
        if (sUsername.matches("")) {
            Toast.makeText(this, "You did not Enter a Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // Triggers when LOGIN Button clicked
    public void checkLogin(View arg0) {
        uname = etEmail.getText().toString();

        etEmail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etEmail.getText().toString().length()!= 10)
                {

                    etEmail.setError("Invalid Mobile Number");

                }
            }
        });



//        SharedPreferences.Editor toEdit = sh_Pref.edit();
//        if (saveLoginCheckBox.isChecked()) {
//            toEdit.putBoolean("saveLogin", true);
//            toEdit.putString("username", uname);
//
//            toEdit.commit();
//        } else {
//            toEdit.clear();
//            toEdit.commit();
//        }

        // Get text from email and passord field


        // Initialize  AsyncLogin() class with email and password
        new AsyncLogin().execute(uname);

    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
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
               // url = new URL("htpp://igps.io/sales");

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
                        .appendQueryParameter("mobile", params[0])
                        .appendQueryParameter("action", "fitter_login")
                        .appendQueryParameter("op", "login");

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
            Log.i("result",result);

            //this method will be running on UI thread
//
            pdLoading.dismiss();
//            Log.i("result",result);
//            String hig = sh_Pref.getString("uname", uname);
//            Log.i("shared",query);

               if (result.equalsIgnoreCase("none")) {
//                   SharedPreferences.Editor toEdit = sh_Pref.edit();
//                   toEdit.putString("mobile", uname);
//                   Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                   startActivity(intent);
//                   Toast.makeText(MainActivity.this, "Mobile Number is Not registered", Toast.LENGTH_LONG).show();
//                   toEdit.apply();
//                   toEdit.clear();

                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                   final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
                   builder.setTitle("Error");
                   builder.setMessage("Your Mobile Number is Not Register, Try agin...");
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int id) {

                           dialog.cancel();
                       }
                   });

                   AlertDialog alert = builder.create();
                   alert.show();
//

               } else if (result.equalsIgnoreCase("exception")) {

                   // If username and password does not match display a error message
//                   Toast.makeText(MainActivity.this,  "error", Toast.LENGTH_LONG).show();

                   final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
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
                   Intent intent = new Intent(MainActivity.this, in.realtech.ibike_dealer.Otpview.class);
                   intent.putExtra("mobilestr",result);
                   intent.putExtra("mobile",uname);

                   startActivity(intent);
                   (MainActivity.this).finish();

               }


        }

    }

        }

