package com.example.vehicle1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.vehicle1.ui.result.ResultFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    //?????????
    final String TAG = getClass().getSimpleName();
    ImageView imageView;
    Button cameraBtnFir;
    TextView precaution;
    Button uploadBtn;

    private int GALLERY_CODE = 10;
    Uri selectedImageUri;
    private ActivityResultLauncher<Intent> resultLauncher;
    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    //   ?????? ???????????????
    private long lastTimeBackPressed;
    FirebaseAuth mAuth;

    //?????? ????????? ????????? ????????? ????????? ??? ????????? ???
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        //??? ??? ????????? ?????? ??????
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this, "'??????' ????????? ??? ??? ??? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
    }


    //activity ?????? ????????? ??? ?????????
    //super??? ????????? ????????? ?????? ???????????? oncreate ???????????? ?????????
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //  test1 = findViewById(R.id.test1);

        // ??????????????? ?????? ??????
        imageView = findViewById(R.id.imageview);
        cameraBtnFir = findViewById(R.id.camera_button_first);
        precaution = findViewById(R.id.precautions);
        uploadBtn = findViewById(R.id.uploadBtn);

        cameraBtnFir.setOnClickListener(this);
        //??????????????? ?????? ??????
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????? ??????
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                new ActivityResult(GALLERY_CODE, intent);
                //  startActivityForResult(Intent.createChooser(intent, "???????????? ???????????????."), 0);
                DBupload();
            }
        });

        // ?????????????????? ?????? ?????????
        mAuth = FirebaseAuth.getInstance();
        // 6.0 ??????????????? ????????? ???????????? ?????? ?????? ??? ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "?????? ?????? ??????");
            } else {
                Log.d(TAG, "?????? ?????? ??????");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    //??????
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //?????? ?????? ??? ????????????
                    //??????????????? 0??????
                    //result.getResultCode() == GALLERY_CODE &&
                    if ( result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        //uri ????????????
                        selectedImageUri = data.getData();
                        //log??? ????????? ?????? uri??? ??? ??? ??????
                        Log.d(TAG, "uri:" + String.valueOf(selectedImageUri));
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "?????? ??????", "?????? ????????? ?????????????????????.");
                            selectedImageUri = Uri.parse(imageSaveUri);

                            Log.d(TAG, "PhotoActivity - onActivityResult() called" + selectedImageUri);
                        }
                    }
                }
//                    else if (result.getResultCode() == GALLERY_CODE) {
//                        try {
//                            //storage ?????? ??????
//                            FirebaseStorage storage = FirebaseStorage.getInstance();
//
//                            StorageReference storageRef = storage.getReference();
//
//                            Uri file = Uri.fromFile(new File(selectedImageUri));
//                            final StorageReference riversRef = storageRef.child("images/" + file.getLastPathSegment());
//                            UploadTask uploadTask = riversRef.putFile(file);
//                        }
//                    }
            });


    public void openSomeActivityForResult() throws IOException {
//????????? ?????? ??????
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        someActivityResultLauncher.launch(takePictureIntent);
//createImageFile ???????????? ??????,
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //?????? ?????? ???
            }
            //??????????????? ??????????????? ?????? (?????? ???????????? ????????? ???)
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.vehicle1.fileprovider",
                        photoFile);
                //????????? ????????? ?????? ????????? ????????? ?????? ??????
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
        }
        //ACTION_~ ??? ????????? ???????????? ????????? ??????????????? - ?????? ????????? ?????? ??????
//        Intent mediaScanIntent = new Intent(Intent.ACTION_PICK);
//        Uri contentUri = Uri.fromFile(new File(mCurrentPhotoPath));
//        mediaScanIntent.setData(contentUri);
//        //????????? ????????? ?????????
//        sendBroadcast(mediaScanIntent);
//        Toast.makeText(this, "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
    }

    // ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // ?????? onClick ????????? ????????????

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_button_first:
                try {
                    openSomeActivityForResult();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //?????? ???????????? ???????????? ???????????? ???
                precaution.setVisibility(View.GONE);
                break;

            case R.id.uploadBtn:
                DBupload();
//                clickBtn(imageView);
//                httpGetConnection(url, data);
                selectedImageUri = null;
                break;
//            case R.id.print_result:
//                imageView.setVisibility(View.GONE);
//                cameraBtnFir.setVisibility(View.GONE);
//                uploadBtn.setVisibility(View.GONE);
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new ResultFragment()).commitAllowingStateLoss();
//                break;
        }
    }

    // ???????????? ????????? ????????? ???????????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK && intent.hasExtra("intent")) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    //???????????? ????????? ????????? ????????? ????????? ????????? ??????
    private File createImageFile() throws IOException {
        //????????? ??????
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        //getExternalStorageDirectory() - ????????? ????????? ????????? ?????? ?????????
        File storageDir = new File(Environment.getExternalStorageDirectory() + "images/");
        if (!storageDir.exists()) {
            Log.v("??????", "storageDir ?????? x " + storageDir.toString());
            storageDir.mkdirs(); //?????? ????????? ?????? ??????
        }
        Log.v("??????", "storageDir ????????? " + storageDir.toString());
        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void DBupload() {

        if (selectedImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("????????????...");
            progressDialog.show();

            //storage ?????? ??????
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //?????? ??????
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd_HHmmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            StorageReference storageRef = storage.getReferenceFromUrl("gs://project01-232ff.appspot.com/").child("images").child(filename);

            //????????? ???????????? ???
            storageRef.putFile(selectedImageUri)//?????????
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //????????? ?????? Dialog ?????? ??????
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                            getUrlFromDB(filename);
                        }
                    })
                    //?????????
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "????????? ??????!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //?????????
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog??? ???????????? ???????????? ????????? ??????
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
        }
    }
//?????? ????????? ???????????? ???????????? ??????
//    private void saveFile(Uri image_uri) {
//
//        ContentValues values = new ContentValues();
//        String fileName =  "woongs"+System.currentTimeMillis()+".png";
//        values.put(MediaStore.Images.Media.DISPLAY_NAME,fileName);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            values.put(MediaStore.Images.Media.IS_PENDING, 1);
//        }
//
//        ContentResolver contentResolver = getContentResolver();
//        Uri item = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//        try {
//            ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
//            if (pdf == null) {
//                Log.d("Woongs", "null");
//            } else {
//                byte[] inputData = getBytes(image_uri);
//                FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
//                fos.write(inputData);
//                fos.close();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    values.clear();
//                    values.put(MediaStore.Images.Media.IS_PENDING, 0);
//                    contentResolver.update(item, values, null, null);
//                }
//
//                // ??????
//                galleryAddPic(fileName);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d("Woongs", "FileNotFoundException  : "+e.getLocalizedMessage());
//        } catch (Exception e) {
//            Log.d("Woongs", "FileOutputStream = : " + e.getMessage());
//        }
//    }



    public void httpGetConnection(String UrlData, String ParamData) {
        BufferedReader br = null;
        StringBuffer sb = null;
        new Thread() {
            String totalUrl = "";
            BufferedReader br = null;
            StringBuffer sb = null;
            HttpURLConnection connection = null;
            String responseData = "";
            String returnData = "";
            public void run() {
                try {
                    if (ParamData != null && ParamData.length() > 0 &&
                            !ParamData.equals("") && !ParamData.contains("null")) { //???????????? ?????? ????????? ????????? ??????
                        totalUrl = UrlData.trim().toString() + "?" + ParamData.trim().toString();
                    } else {
                        totalUrl = UrlData.trim().toString();
                    }

                    URL url = new URL(totalUrl);

                    connection = (HttpURLConnection) url.openConnection();
                    // http ????????? ????????? ?????? ?????? ??????
                    //request header??? ?????? - json ????????? ???????????? ??????
                    connection.setRequestProperty("Accept", "application/json");
                    //???????????? ???????????? ??????
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestMethod("GET");

                    // http ?????? ??????
                    connection.connect();

                    br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    sb = new StringBuffer();

                    while ((responseData = br.readLine()) != null) {
                        sb.append(responseData); //StringBuffer??? ???????????? ????????? ??????????????? ?????? ??????
                    }

                    returnData = sb.toString();
                    Log.d(TAG, returnData);
                    System.out.println(returnData);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // http ?????? ??? ?????? ?????? ??? BufferedReader??? ???????????????
                    try {
                        if (br != null) {
                            br.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public void getUrlFromDB(String filename){
        Log.d(TAG, "uri:" + String.valueOf(filename));
        // ?????????????????? ??????
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://project01-232ff.appspot.com/").child("images/" + filename);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(getApplicationContext(), "URL?????? : " + uri.toString(), Toast.LENGTH_SHORT).show();
                String url = "http://172.19.88.34:8080/assessment";
                String data = "url=" + uri.toString();
                Log.d(TAG, "uri:" + data);
                httpGetConnection(url, data);
            }
        });
    }
}
// ????????? ???????????? ??????
//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }