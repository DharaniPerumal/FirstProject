package in.realtech.ibike_dealer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Webview_online extends Activity {
    private WebView wv1;

    private ProgressBar spinner;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_online);
        setTitle(" hi");
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        final String MyPREFERENCES = "MyPrefs" ;
        SharedPreferences sharedpreferences;
        ImageView backbtn =(ImageView)findViewById(R.id.backbtn1);
        backbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String name = sharedpreferences.getString("name", "name");
        String mail = sharedpreferences.getString("mail", "mail");
        String  mobile = sharedpreferences.getString("mobile", "mobile");
        String shop_name = sharedpreferences.getString("shop_name", "shop_name");
        String state = sharedpreferences.getString("state", "state");
        String district = sharedpreferences.getString("district", "district");
        String pincode = sharedpreferences.getString("pincode", "pincode");
        String address = sharedpreferences.getString("address", "address");
        String  pi = sharedpreferences.getString(mobile + "_pi", mobile + "_pi");

             wv1=findViewById(R.id.webView);
             wv1.setWebViewClient(new MyBrowser());
             spinner.setVisibility(View.VISIBLE);
                  String url = "http://dealer.igps.io/mobile_session.php?mobile="+ mobile +"&name="+ name +"&mail="+ mail +"&shop_name="+ shop_name +"&address="+ address +"&district="+ district +"&state="+ state +"&pincode="+ pincode +"&pi="+ pi;
                  Log.i("URl",url);
                  wv1.getSettings().setLoadsImagesAutomatically(true);
                  WebSettings settings = wv1.getSettings();
                  settings.setDomStorageEnabled(true);
                  wv1.getSettings().setAllowFileAccess(true);
                  wv1.getSettings().setJavaScriptEnabled(true);
                  wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                  wv1.getSettings().setPluginState(WebSettings.PluginState.ON);
                  wv1.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                  wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                  wv1.loadUrl(url);
                  wv1.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(
                    WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                spinner.setVisibility(View.VISIBLE);

                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            public void onPageFinished(WebView view, String url) {
                        spinner.setVisibility(View.INVISIBLE);
               }
        });
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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