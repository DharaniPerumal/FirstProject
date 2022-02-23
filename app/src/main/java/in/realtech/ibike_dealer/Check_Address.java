package in.realtech.ibike_dealer;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static in.realtech.ibike_dealer.Sold.CONNECTION_TIMEOUT;

public class Check_Address extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText input_address1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private TextInputLayout input_layout_address1;
    private Button btnSignUp1;
    String nos;
    String amount;
    String sname,smobile,semail,saddress,snos,sprice,mobile;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__address);
        setTitle("Conform Address Details");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
     mobile = sharedpreferences.getString("mobile", "mobile");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
         nos = getIntent().getStringExtra("nos");
         amount = getIntent().getStringExtra("amount");
        String Name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String  address= getIntent().getStringExtra("address");
        final String  mobile= getIntent().getStringExtra("mobile");
        String  pincode= getIntent().getStringExtra("pincode");
        String state = getIntent().getStringExtra("state");
        String  District= getIntent().getStringExtra("District");

        input_layout_address1 = (TextInputLayout) findViewById(R.id.input_layout_address1);
        input_address1 = (EditText) findViewById(R.id.input_address1);
        btnSignUp1 = (Button) findViewById(R.id.btn_signup1);
        TextView nosselect=(TextView)findViewById(R.id.selectednos);
        TextView name=(TextView)findViewById(R.id.name);
        TextView mobile1=(TextView)findViewById(R.id.mobile);
        TextView email1=(TextView)findViewById(R.id.email);

        name.setText(Name);
        mobile1.setText(mobile);
        email1.setText(email);
        nosselect.setText(nos);
        TextView amo=(TextView)findViewById(R.id.amt);
        amo.setText(amount);
        input_address1.setText(address+","
                                + System.getProperty("line.separator")
                                + pincode+","
                                + District+","
                                + System.getProperty("line.separator")
                                + state+" .");

        sname=name.getText().toString();
        smobile=mobile1.getText().toString();
        semail=email1.getText().toString();
        saddress=input_address1.getText().toString();
        snos=nosselect.getText().toString();
        sprice=amo.getText().toString();
        input_address1.addTextChangedListener(new MyTextWatcher(input_address1));
        btnSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                submitForm();
                new AsyncLogin().execute(sname,smobile,semail,saddress);

            }
        });






    }


    private class AsyncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Check_Address.this);
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
                conn.setReadTimeout(Sold.READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("action", "dealer_order")
                        .appendQueryParameter("op", "place_order")
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("mobile", params[1])
                        .appendQueryParameter("mail", params[2])
                        .appendQueryParameter("address", params[3])
                        .appendQueryParameter("qty", nos)
                        .appendQueryParameter("amount", amount)
                        .appendQueryParameter("fitter_id", mobile);

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
            Log.i("result",result);


        }

        }

    /**
     * Validating form
     */
    private void submitForm() {

        if (!validateAddress1()) {
            return;
        }

        Intent intent = new Intent(Check_Address.this, Account_details_purchase.class);
        intent.putExtra("amount", amount);

        intent.putExtra("nos",nos);
//        new AsyncLogin().execute(sname,smobile,semail,saddress,snos,sprice);
        startActivity(intent);
        finish();

//        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();

    }

    private boolean validateAddress1() {
        if (input_address1.getText().toString().trim().isEmpty()) {
            input_layout_address1.setError("Enter your Full address");
            input_address1.requestFocus();
            return false;
        } else {
            input_layout_address1.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
//        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.input_address1:
                    break;

                case R.id.input_address:
                    validateAddress1();
                    break;


            }
        }

        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_name:
//                    validateName();
//                    break;
//                case R.id.input_email:
//                    validateEmail();
//                    break;
//                case R.id.input_mobile:
//                    validateMobile();
//                    break;
//                case R.id.input_address:
//                    validateAddress();
//                    break;
//                case R.id.input_pin_code:
//                    validatePincode();
//                    break;
//                case R.id.input_num_Nos:
//                    validateTotelnoofnos();
//                    break;
//                case R.id.input_num_Nos_text:
//                    validateTotelnoofnostext();
//                    break;
//
//            }
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



