package in.realtech.ibike_dealer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class imgupload extends AppCompatActivity {

    private static final int CAMERA_PICTURE = 1;
    private static final int GALLERY_PICTURE = 2;
    String filePath1 = null;
    String filePath2 = null;
    String filePath3 = null;
    Bitmap chosenImage;
    File destination = null;
    ImageView ivImg1, ivImg2, ivImg3;

    String img1Selected1 = null, img2Selected1 = null, img3Selected1 = null;
    String img1Selected2 = null, img2Selected2 = null, img3Selected2 = null;

    LinearLayout llImgHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgupload);
        Button btnImg1 = (Button) findViewById(R.id.btn_multi_img_1);
        Button btnImg2 = (Button) findViewById(R.id.btn_multi_img_2);
        Button btnImg3 = (Button) findViewById(R.id.btn_multi_img_3);
        Button btnUpload = (Button) findViewById(R.id.btn_multi_upload);

        llImgHolder = (LinearLayout) findViewById(R.id.ll_multi_img_holder);

        ivImg1 = (ImageView) findViewById(R.id.iv_multi_img1);
        ivImg2 = (ImageView) findViewById(R.id.iv_multi_img2);
        ivImg3 = (ImageView) findViewById(R.id.iv_multi_img3);

        btnImg1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                img1Selected1 = "fulfilled";
                choosePictureAction();
            }
        });

        btnImg2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (img1Selected2 != null) {
                    img2Selected1 = "fulfilled";
                    choosePictureAction();
                } else {
                    Log.e("Please ", "Please select first image");
                }
            }
        });

        btnImg3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                if (img2Selected2 != null) {
                    img3Selected1 = "fulfilled";
                    choosePictureAction();
                } else {
                    Log.e("Please ", "Please select second image");
                }
            }
        });

        btnUpload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "https://igps.io/http.php";

                new uploadAsynTask().execute(url);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PICTURE && resultCode == RESULT_OK) {
            chosenImage = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            chosenImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            //filePath1 = destination.toString();

            if (img1Selected1 != null) {

                filePath1 = destination.toString();
            } else if (img2Selected1 != null) {

                filePath2 = destination.toString();
            } else if (img3Selected1 != null) {

                filePath3 = destination.toString();
            }

            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (chosenImage != null) {
                if (img1Selected1 != null) {
                    llImgHolder.setVisibility(View.VISIBLE);
                    ivImg1.setVisibility(View.VISIBLE);

                    img1Selected1 = null;
                    img1Selected2 = "fulfilled";
                    ivImg1.setImageBitmap(chosenImage);
                } else if (img2Selected1 != null) {
                    ivImg2.setVisibility(View.VISIBLE);

                    img2Selected1 = null;
                    img2Selected2 = "fulfilled";
                    ivImg2.setImageBitmap(chosenImage);
                } else if (img3Selected1 != null) {
                    ivImg3.setVisibility(View.VISIBLE);

                    img3Selected1 = null;
                    img3Selected2 = "fulfilled";
                    ivImg3.setImageBitmap(chosenImage);
                }

            }


        } else if (requestCode == CAMERA_PICTURE
                && resultCode == RESULT_CANCELED) {

        } else if (requestCode == GALLERY_PICTURE
                && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            destination = new File(selectedImagePath);

            if (img1Selected1 != null) {
                filePath1 = selectedImagePath;
            } else if (img2Selected1 != null) {
                filePath2 = selectedImagePath;
            } else if (img3Selected1 != null) {
                filePath3 = selectedImagePath;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            chosenImage = BitmapFactory.decodeFile(selectedImagePath, options);

            if (chosenImage != null) {

                if (img1Selected1 != null) {
                    llImgHolder.setVisibility(View.VISIBLE);
                    ivImg1.setVisibility(View.VISIBLE);

                    img1Selected1 = null;
                    img1Selected2 = "fulfilled";
                    ivImg1.setImageBitmap(chosenImage);
                } else if (img2Selected1 != null) {
                    ivImg2.setVisibility(View.VISIBLE);

                    img2Selected1 = null;
                    img2Selected2 = "fulfilled";
                    ivImg2.setImageBitmap(chosenImage);
                } else if (img3Selected1 != null) {
                    ivImg3.setVisibility(View.VISIBLE);

                    img3Selected1 = null;
                    img3Selected2 = "fulfilled";
                    ivImg3.setImageBitmap(chosenImage);
                }

            }

        } else if (requestCode == GALLERY_PICTURE
                && resultCode == RESULT_CANCELED) {

        }
    }

    private void choosePictureAction() {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(imgupload.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PICTURE);
                } else if (items[which].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_PICTURE);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });

        builder.show();

    }


    private class uploadAsynTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(imgupload.this, null, null);
            ProgressBar spinner = new android.widget.ProgressBar(imgupload.this, null, android.R.attr.progressBarStyle);
            spinner.getIndeterminateDrawable().setColorFilter(Color.parseColor("#009689"), android.graphics.PorterDuff.Mode.SRC_IN);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(spinner);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("FilePathBin1", filePath1);

            File file1 = new File(filePath1);
            File file2 = null;
            File file3 = null;



            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMHHmmss");
            String strDate = sdf.format(c.getTime());
            if (img2Selected2 != null) {
                Log.e("FilePathBin2", filePath2);
                file2 = new File(filePath2);
            }
            if (img3Selected2 != null) {
                Log.e("FilePathBin3", filePath3);
                file3 = new File(filePath3);
            }

            MultipartEntity reqEntity;
            HttpEntity resEntity;
            try {
                HttpClient client = new DefaultHttpClient();
                String postURL = params[0];
                HttpPost post = new HttpPost(postURL);

                FileBody bin1 = new FileBody(file1);
                FileBody bin2 = null;
                FileBody bin3 = null;

                if (img2Selected2 != null) {

                    bin2 = new FileBody(file2);
                }
                if (img3Selected2 != null) {

                    bin3 = new FileBody(file3);
                }

                reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


                reqEntity.addPart("parking_title", new StringBody("Test13:26"));
                reqEntity.addPart("latitude", new StringBody("10.12313213"));
                reqEntity.addPart("longtitude", new StringBody("12.123213213"));
                reqEntity.addPart("note", new StringBody("12"));

                reqEntity.addPart("filename[0]", bin1);

                if (img2Selected2 != null) {
                    img2Selected2 = null;
                    reqEntity.addPart("filename[1]", bin2);
                }
                if (img3Selected2 != null) {
                    img3Selected2 = null;
                    reqEntity.addPart("filename[2]", bin3);
                }
                reqEntity.addPart("action", new StringBody("fitter_image"));
                reqEntity.addPart("op", new StringBody("pi"));
                reqEntity.addPart("no", new StringBody("9787106666"));
                reqEntity.addPart("time", new StringBody(strDate));
                reqEntity.addPart("name", new StringBody("pi"));
                reqEntity.addPart("filename", new StringBody("1"));

                post.setEntity(reqEntity);
                HttpResponse response = client.execute(post);
                resEntity = response.getEntity();
                String entityContentAsString = EntityUtils.toString(resEntity);
                Log.d("stream:", entityContentAsString);


                return entityContentAsString;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (result != null) {
                dialog.dismiss();
                Log.e("addPhotoResult", result);

                filePath1 = null;
                filePath2 = null;
                filePath3 = null;

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String error = jsonObject.getString("Error");
                    String message = jsonObject.getString("message");

                    Log.e("error", error);
                    Log.e("message", message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

    }
}