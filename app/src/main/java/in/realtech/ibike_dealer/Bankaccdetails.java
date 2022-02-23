package in.realtech.ibike_dealer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class Bankaccdetails extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    TextView tx1,tx2,tx3,tx4,tx5;
    String bank;
    String bank_ac;
    String bank_ifsc;
    String bank_verified;
    String verification_amt;
    String upi_id;
//    Button copybutt;
    ImageView img;

    private ClipboardManager myClipboard;
    private ClipData myClip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Bank Account Details");
        setContentView(R.layout.activity_bankaccdetails);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        bank = sharedpreferences.getString("bank", "bank");
        bank_ac = sharedpreferences.getString("bank_acc", "bank_acc");
        bank_ifsc = sharedpreferences.getString("bank_ifsc", "bank_ifsc");
        bank_verified = sharedpreferences.getString("bank_verified", "bank_verified");
         verification_amt = sharedpreferences.getString("verification_amt", "verification_amt");
         upi_id = sharedpreferences.getString("upi_id", "upi_id");
        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3= (TextView)findViewById(R.id.textView4);
        tx4= (TextView)findViewById(R.id.textView5);
        tx5= (TextView)findViewById(R.id.textView6);
        tx1.setText(bank);
        tx2.setText( bank_ac);
        tx3.setText( bank_ifsc);
        tx4.setText(bank_verified);
        tx5.setText(upi_id);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        img = (ImageView) findViewById(R.id.imagev);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = tx1.getText().toString();
                String text1 = tx2.getText().toString();
                String text2 = tx3.getText().toString();
                String text3 = tx4.getText().toString();
                String text4 = tx5.getText().toString();
                myClip = ClipData.newPlainText(text, text+","+text1+","+text2+","+text3+","+text4);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Your Bank Details Copied",
                        Toast.LENGTH_SHORT).show();
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
