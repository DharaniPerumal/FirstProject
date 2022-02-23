package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.regex.Pattern;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Qrcodegentrater extends AppCompatActivity {
    Bitmap bitmap;
    ImageView qrImage;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("QR-Code");
        setContentView(R.layout.activity_qrcodegentrater);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        String serial = this.getIntent().getExtras().getString("serial");
        String qr = this.getIntent().getExtras().getString("qr");
        TextView serialno= (TextView)findViewById(R.id.serial);
        final TextView qrcode= (TextView)findViewById(R.id.qrcode);
        serialno.setText(serial);
        qrcode.setText(qr);


        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        img = (ImageView) findViewById(R.id.imagev);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = qrcode.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "QR-Code Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });


            qrImage = (ImageView) findViewById(R.id.QR_Image);
            QRGEncoder qrgEncoder = new QRGEncoder(qr, null, QRGContents.Type.TEXT, 500);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                // Setting Bitmap to ImageView
                qrImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v("qr", e.toString());
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

