package com.example.newpc.qrcode;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int REQUEST_CODE = 1;

    //Firebase
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonCamera;
    private static final String IMAGE_CAPTURE_FOLDER = "Kidose/Images";

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    public Uri filePath,_imagefileUri;
    private static File file;
   // private static final int CONTENT_REQUEST=1337;
    private File output=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonCamera = (Button) findViewById(R.id.buttonCamera);
        imageView = (ImageView) findViewById(R.id.imageView);

        int permissionCheck = ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.CAMERA);
        if (ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(ImageActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PIC_REQUEST);
            }
        }
        if (ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(ImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is Camera
        else if (view == buttonCamera) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            // _imagefileUri = Uri.fromFile(getFile());
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_PIC_REQUEST) {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            File dir=Environment.getExternalStoragePublicDirectory(IMAGE_CAPTURE_FOLDER);
            Bundle bundle = getIntent().getExtras();
            String pin = bundle.getString("pin");
            String name = bundle.getString("name");
           Intent in=new Intent(Intent.ACTION_VIEW);
            output=new File(dir, pin+"_"+name+".jpg");
            in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            in.setDataAndType(Uri.fromFile(output), "image/jpg");

            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);

           startActivity(in);

           // filePath = Uri.fromFile(getFile());


            Toast.makeText(ImageActivity.this, MediaStore.EXTRA_OUTPUT+" :2", Toast.LENGTH_SHORT).show();
        }
    }

    //this method will upload the file
    private void uploadFile() {
       // Toast.makeText(ImageActivity.this, filePath + "", Toast.LENGTH_LONG).show();
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference storageRef = storage.getReference();
            Bundle bundle = getIntent().getExtras();
            String pin = bundle.getString("pin");
            String name = bundle.getString("name");
            StorageReference storeRef = storageRef.child("IP Pictures/"+pin+"_"+name+".jpg");
            storeRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //if the upload is successful
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(ImageActivity.this, "No image found!", Toast.LENGTH_SHORT).show();
        }
    }

    private File getFile() {
        Bundle bundle = getIntent().getExtras();
        String pin = bundle.getString("pin");
        String name = bundle.getString("name");
        String storage = Environment.getExternalStorageDirectory().getPath();
        file = new File(storage, IMAGE_CAPTURE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file + File.separator + pin + "_" + name + ".jpg");
    }




}
