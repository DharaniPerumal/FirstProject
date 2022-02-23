package in.realtech.ibike_dealer;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class checkpermisions extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener  {
    private ImageView imageView;
    private Button cameraButton;

    //you can change as your need
        private static int CAMERA_PERMISSION = 1001;
        private static int CAMERA_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpermisions);
        imageView = (ImageView) findViewById(R.id.image_view);
        cameraButton = (Button) findViewById(R.id.btn_shoot);
        cameraButton.setOnClickListener((View.OnClickListener) checkpermisions.this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == CAMERA_PERMISSION) {
            callCameraIntent();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        if (requestCode == CAMERA_PERMISSION) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Toast.makeText(this, "Sorry we can't proceed", Toast.LENGTH_SHORT).show();
        }
    }

    //call camera intent
    private void callCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }


    //button click
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_shoot) {
            EasyPermissions.requestPermissions(this, "Need Camera Permission", CAMERA_PERMISSION,
                    Manifest.permission.CAMERA);
        }
    }

    //Receive camera result
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);

        }

    }
}