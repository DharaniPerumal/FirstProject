package in.realtech.ibike_dealer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
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
/**
 * Created by kamal_bunkar on 19-12-2017.
 */
@SuppressLint("Registered")
public class SplashScreen  extends Activity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences sh_Pref;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String vercode;
    String versionname;
    int versioncode;
    String str;
    String PACKAGE_NAME;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splas);
        setTitle("             ");

        TextView version = findViewById(R.id.version);

        PACKAGE_NAME = getApplicationContext().getPackageName();

       Log.i("packagename",PACKAGE_NAME);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionname = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versioncode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version.setText( versionname + ".0");

        chkStatus(String.valueOf(versioncode));
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        sh_Pref = getSharedPreferences("perf", MODE_PRIVATE);


    }

    void chkStatus(String vercode) {


        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        if (wifi.isConnectedOrConnecting()) {

            new AsyncLogin().execute(String.valueOf(vercode));


        } else if (mobile.isConnectedOrConnecting()) {

            new AsyncLogin().execute(String.valueOf(vercode));

        } else {

            TextView version = findViewById(R.id.version);

            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                versionname = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                versioncode = pInfo.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            version.setText( versionname + ".0");
            vercode = Integer.toString(versioncode);
            Log.i("vercode", vercode);
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("App info");
            builder1.setMessage("No Internet Connection");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Retry",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            chkStatus(String.valueOf(versioncode));
                        }
                    })
                    .setNegativeButton(
                            "Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    finish();

                                }
                            });


            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(SplashScreen.this);
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


                   // conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("action", "app_version_release")
                        .appendQueryParameter("plateform", "android")
                        .appendQueryParameter("package", PACKAGE_NAME)
                        .appendQueryParameter("version", params[0]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                   new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);//({"{\"data\":All}");//"{d""""""""""""""""""""""""""""""""""""""+
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
                    return ("none");
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
            str = result;
            Log.i("result", result);
            updates(result);

        }
    }

    void updates(String result) {

        if (result.equalsIgnoreCase("none")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sharedpreferences.contains("name")) {
                        // not registted user  so show login screen
                        Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }, 3000);

        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("App Update");
            builder1.setMessage("iBike Dealer App Update required");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Update",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Uri uri = Uri.parse("http://igps.io/apk/ibike_dealer.apk");

                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (sharedpreferences.contains("name")) {
                                        // not registted user  so show login screen
                                        Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();

                                }

                            }, 50000);

                        }

                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }
}
