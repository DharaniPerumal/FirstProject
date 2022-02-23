package in.realtech.ibike_dealer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class changeactivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    EditText etext1, etext2, etext3, etext4, etext5,etext6,etext7,etext8;
    TextView tx0,tx1,tx2,tx3,tx4,tx5,tx7,tx8,tx9,msg;
    Button find;
    String name;
    String mail;
    String mobile;
    String shop_name;
    String address;
    String bank;
    String bank_acc;
    String bank_ifsc;
    String bank_verified;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeactivity);
        setTitle("Edit Account Details");

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         name = sharedpreferences.getString("name", "name");
         mail = sharedpreferences.getString("mail", "mail");
         mobile = sharedpreferences.getString("mobile", "mobile");
         shop_name = sharedpreferences.getString("shop_name", "shop_name");
         address = sharedpreferences.getString("address", "address");
        bank = sharedpreferences.getString("bank", "bank");
        bank_acc = sharedpreferences.getString("bank_acc", "bank_acc");
        bank_ifsc = sharedpreferences.getString("bank_ifsc", "bank_ifsc");
        bank_verified = sharedpreferences.getString("bank_verified", "bank_verified");
        etext1 = (EditText) findViewById(R.id.edit1);
        etext2 = (EditText) findViewById(R.id.edit2);
        etext3= (EditText)findViewById(R.id.edit3);
        etext4= (EditText)findViewById(R.id.edit4);
        etext5= (EditText)findViewById(R.id.edit5);
        etext6= (EditText)findViewById(R.id.edit6);
        etext7= (EditText)findViewById(R.id.edit7);
        etext8= (EditText)findViewById(R.id.edit8);

        tx0 = (TextView) findViewById(R.id.txt0);
        tx1 = (TextView) findViewById(R.id.txt1);
        tx2 = (TextView) findViewById(R.id.txt2);
        tx3= (TextView)findViewById(R.id.txt3);
        tx4= (TextView)findViewById(R.id.txt4);
        tx5= (TextView)findViewById(R.id.txt5);
        tx7= (TextView)findViewById(R.id.txt7);
        tx8= (TextView)findViewById(R.id.txt8);
        tx9= (TextView)findViewById(R.id.txt9);

        tx0.setText("Change Your Details");
        tx1.setText("Enter Your Name");
        tx2.setText("Enter Your Email");
        tx3.setText("Enter Your Mobile");
        tx4.setText("Enter Your ShopName");
        tx5.setText("Enter Your Address");
        tx7.setText("Enter Your Branch");
        tx8.setText("Enter Your Account No");
        tx9.setText("Enter Your Ifsc");

        etext1.setText(name);
        etext2.setText(mail);
        etext3.setText(mobile);
        etext4.setText(shop_name);
        etext5.setText(address);
        etext6.setText(bank);
        etext7.setText(bank_acc);
        etext8.setText(bank_ifsc);
        etext3 .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(etext3.getText().toString().length()!= 10)
                {

                    etext3.setError("Invalid Mobile Number");

                }
            }
        });

        etext2 .addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
                if (!etext2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {

                    etext2.setError("Invalid Email Address");

                }
            }
        });
        Button find = (Button) findViewById(R.id.btnchange);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String nam = etext1.getText().toString();
                String mal = etext2.getText().toString();
                String mbl = etext3.getText().toString();
                String shp = etext4.getText().toString();
                String addres = etext5.getText().toString();
                String bname = etext3.getText().toString();
                String acc = etext4.getText().toString();
                String ifsc = etext5.getText().toString();

                if (!etext2.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {

                    etext2.setError("Invalid Email Address");

                }
                if(etext3.getText().toString().length()!= 10)
                {

                    etext3.setError("Invalid Mobile Number");

                }

            }
        });

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