package in.realtech.ibike_dealer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import static com.android.volley.VolleyLog.TAG;


public class Full_Srce_Myprofile extends Activity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Bitmap bmp;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String mobile = this.getIntent().getExtras().getString("mobile");
        String images = this.getIntent().getExtras().getString("images");
        String imgNameLocal = sharedpreferences.getString(mobile + "_jpg1", "");
        Log.i("images", images);

        int i = 0;
        photoView = (PhotoView) findViewById(R.id.photo_view);

        if (images.equalsIgnoreCase("null")) {

            photoView.setImageResource(R.drawable.ac_images);

        } else {
            setImage(photoView, i, mobile, images);

        }

        ImageButton backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();

            }
        });



//        photoView.setImageResource(R.drawable.acc_images);
    }

    private void setImage(ImageView imageView, int i, String mobile, String imagess) {
        String imgNameServer = imagess;
        if (!imgNameServer.equals("null")) {
            String imgNameLocal = sharedpreferences.getString(mobile + "_jpg3", "");
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
//                            .apply(RequestOptions.circleCropTransform())
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
                            .into(photoView);
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
        String url = "https://igps.io/user_images/" + img;
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
                            toEdit.putString(mobile + "_jpg3", img);
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
}
