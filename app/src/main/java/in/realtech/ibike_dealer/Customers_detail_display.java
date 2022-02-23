package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class Customers_detail_display extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Result_details";
    String date1;
     String image;
    String mobiles;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_detail_display);
        setTitle("Customers Details");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String name = this.getIntent().getExtras().getString("name");
        String mail = this.getIntent().getExtras().getString("mail");
         image = this.getIntent().getExtras().getString("images");
         mobiles = this.getIntent().getExtras().getString("mobile");
        String username = this.getIntent().getExtras().getString("username");
        String diss = this.getIntent().getExtras().getString("dis");
        String states = this.getIntent().getExtras().getString("states");
        String postals = this.getIntent().getExtras().getString("postals");
        String added_at = this.getIntent().getExtras().getString("added_at");
        String address = this.getIntent().getExtras().getString("address");
        int icons = this.getIntent().getExtras().getInt("icons");
//        String username = this.getIntent().getExtras().getString("username");
//        String mobile = this.getIntent().getExtras().getString("mobile");
//        String alt_mobile = this.getIntent().getExtras().getString("alt_mobile");
//        String address = this.getIntent().getExtras().getString("address");
//        String district = this.getIntent().getExtras().getString("district");
//        String state = this.getIntent().getExtras().getString("state");
//        String postal = this.getIntent().getExtras().getString("postal");
        String headerTitle = this.getIntent().getExtras().getString("headerTitle");
        TextView user = (TextView) findViewById(R.id.username);
        TextView email = (TextView) findViewById(R.id.email1);
        TextView mobil = (TextView) findViewById(R.id.mobile1);
        TextView dis = (TextView) findViewById(R.id.district);
        TextView stat = (TextView) findViewById(R.id.state);
        TextView pos = (TextView) findViewById(R.id.postal);
        TextView da = (TextView) findViewById(R.id.date);

        TextView add = (TextView) findViewById(R.id.address);
        ImageView images = (ImageView) findViewById(R.id.profile1);


        images.setOnClickListener(this);


        String[] part = headerTitle.split(":");
//          System.out.println(part[0]);
        final String username1 = part[0];

        final String altmbl = part[2];
        final String address1 = part[3];
        final String district = part[4];
        final String state = part[5];
        final String postal = part[6];
        final String added_at1 = part[7];
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd" );
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        String inputDateStr=added_at;
        Date date = null;
        try {
            date = inputFormat.parse(inputDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);

        user.setText(username);
            email.setText(mail);
            mobil.setText(mobiles);
            dis.setText(diss);
            stat.setText(states);
            pos.setText(postals);
            da.setText(outputDateStr);
            add.setText(address);
//            images.setImageResource(icons);


            int i = 0;

        if (image.equalsIgnoreCase("null")){


            images.setImageResource(R.drawable.ac_images);

        }else {
            setImage(images, i, mobiles, image);

        }

    }



    private void setImage(ImageView imageView, int i, String mobile,String imagess) {
        String imgNameServer = imagess;
        if (!imgNameServer.equals("null")) {
            String imgNameLocal = sharedpreferences.getString(mobile + "_jpg1", "");
            if (imgNameLocal.equals(imgNameServer)) {
                File imgDir = new File(getFilesDir() + File.separator + "photos");
                if (!imgDir.exists()) {
                    imgDir.mkdirs();
                }
                File imgFile = new File(imgDir.getPath() + File.separator + imgNameLocal);
                if (imgFile.exists()) {
                    Glide.with(this)
                            .asBitmap()
                            .load(imgFile)
                            .apply(RequestOptions.circleCropTransform())
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    Log.v(TAG, "onLoadFailed: " + model);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                                        placeholders.set(i, new BitmapDrawable(getResources(), resource));
                                    return false;
                                }
                            })
                            .into(imageView);
                } else {
                    deleteLocalFile(imgNameLocal);
                    downloadVehicleImage(i, mobile, imgNameServer);
                }
            } else {
                Log.v(TAG, imgNameLocal + ": " + imgNameServer);
                deleteLocalFile(imgNameLocal);
                downloadVehicleImage(i, mobile, imgNameServer);
            }
        }
    }

    private void downloadVehicleImage(final int i, final String mobile, final String img) {
        Log.v(TAG, "Downloading");
        String url = "https://igps.io/user_images/user_images/" + img;
        String filePath = getFilesDir() + File.separator + "photos";
        File imgDir = new File(filePath);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }
        final File imgFile = new File(imgDir.getPath() + File.separator + img);
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            FileOutputStream outputStream = new FileOutputStream(imgFile, true);
                            String[] iPathArr = img.split("\\.");
                            String extension = iPathArr[iPathArr.length - 1];
                            if (extension.equalsIgnoreCase("png")) {
                                resource.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            } else {
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            }
                            outputStream.close();
                            SharedPreferences.Editor toEdit = sharedpreferences.edit();
                            toEdit.putString(mobile + "_jpg1", img);
                            toEdit.commit();
                            toEdit.clear();

                        } catch (Exception e) {
                            Log.v(TAG, "File download error: " + e.getLocalizedMessage());
                        }
                    }
                });
    }


    private void deleteLocalFile(String imgName) {
        try {
            File imgDir = new File(getFilesDir() + File.separator + "photos");
            if (!imgDir.exists()) {
                imgDir.mkdirs();
            }
            File imgFile = new File(imgDir.getPath() + File.separator + imgName);
            if (imgFile.exists()) {
                if (imgFile.delete()) {
                    Log.v(TAG, imgName + " deleted");
                } else {
                    Log.v(TAG, imgName + " not deleted");
                }
            } else {
                Log.v(TAG, imgName + " not exists");
            }
        } catch (Exception e) {
            Log.v(TAG, "File delete error: " + e.getLocalizedMessage());
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


    @Override
    public void onClick(View view) {

        Intent intent=new Intent(this,FullscreenActivity.class);
        intent.putExtra("mobile", mobiles);
        intent.putExtra("images", image);

        startActivity(intent);

    }
}
