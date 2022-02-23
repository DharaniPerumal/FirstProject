package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class purchase_product extends AppCompatActivity implements View.OnClickListener {

    String price;
    ImageButton btnIncrement;
    ImageButton btnDecrement;
    EditText display;
    TextView selectpayment,unitprice,stockavailavle;
    int counter = 1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_product);
        Button mButton =(Button)findViewById(R.id.addbtnn);
        setTitle("Purchase product");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String Name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String  address= getIntent().getStringExtra("address");
        String  mobile= getIntent().getStringExtra("mobile");
        String  pincode= getIntent().getStringExtra("pincode");
        String state = getIntent().getStringExtra("state");
        String  District= getIntent().getStringExtra("District");
        String  postal= getIntent().getStringExtra("postal");

        unitprice=(TextView)findViewById(R.id.unitprice);
        stockavailavle=(TextView)findViewById(R.id.stockavailable);
        selectpayment=(TextView)findViewById(R.id.selectpayment);
        btnIncrement = (ImageButton)findViewById(R.id.plus);
        btnDecrement =(ImageButton) findViewById(R.id.minus);
        display=(EditText)findViewById(R.id.quantity);
        display.setText("1");
        mButton.setOnClickListener(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.imageViewunit);
        ImageAdapter adapterView = new ImageAdapter(this);
        mViewPager.setAdapter(adapterView);

//        ImageView images=(ImageView)findViewById(R.id.imageViewunit);
//
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(purchase_product.this,UnitFullScreen.class);
                startActivity(intent);
            }
        });

        new AsyncLogin().execute();
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(purchase_product.this);
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
                        .appendQueryParameter("op", "data");

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

            try {
                JSONObject array = new JSONObject(result);

                price=array.getString("price");
                String stock=array.getString("stock");
                Log.i("stock&price",stock+price);
                unitprice.setText(price);

                    Integer a=Integer.parseInt(stock);
                if (a < 10){
                    TextView texthurry=findViewById(R.id.stockhurry);
                    texthurry.setText("Hurry! Only "+ stock +" left in Stock");

                }else {

                    stockavailavle.setText("Stock is Available: " + stock + " Nos.");

                }
                selectpayment.setText(price);
                btnIncrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter = counter + 1;


                        if ( counter > 0 )
                        {
                            display.setText(String.valueOf(counter));
                            display.setError(null);
                            int a = Integer.parseInt(display.getText().toString());
                            int unit=Integer.parseInt(price);
                            int result = a * unit;
                            NumberFormat formatter = new DecimalFormat("#,###");
                            double myNumber = result;
                            String formattedNumber = formatter.format(myNumber);
                            selectpayment.setText(formattedNumber);

                        }
                        else
                        {
                            display.setError("Select The Totel Quantity");
                            // do not set text
                        }


//                display.setText(String.valueOf(counter));

                    }
                });
                btnDecrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counter = counter - 1;

                        if ( counter > 0 )
                        {
                            display.setText(String.valueOf(counter));
                            display.setError(null);
                            int a = Integer.parseInt(display.getText().toString());
                            int unit=Integer.parseInt(price);
                            int result = a * unit;

                            NumberFormat formatter = new DecimalFormat("#,###");
                            double myNumber = result;
                            String formattedNumber = formatter.format(myNumber);
                            selectpayment.setText(formattedNumber);
                        }
                        else
                        {
                            display.setError("Select The Totel Quantity");
                            // do not set text
                        }

//                display.setText(String.valueOf(counter));
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



    @Override
    public void onClick(View view)
    {

        Intent intent=new Intent(purchase_product.this,BuyAddress.class);
        intent.putExtra("noss", display.getText().toString());
        intent.putExtra("ammount", selectpayment.getText().toString());
        this.startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




    //on app launch TextView will show zero

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






    public class ImageAdapter extends PagerAdapter {
        Context mContext;

        ImageAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        private int[] sliderImageId = new int[]{
                R.drawable.bike_boss,R.drawable.bike_boss_3,R.drawable.bike_3,
        };

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(sliderImageId[position]);
            ((ViewPager) container).addView(imageView, 0);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(purchase_product.this,UnitFullScreen.class);
                    startActivity(intent);
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return sliderImageId.length;
        }
    }
}







