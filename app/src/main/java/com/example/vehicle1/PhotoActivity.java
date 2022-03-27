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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vehicle1.ui.result.ResultFragment;
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
    //카메라
    final String TAG = getClass().getSimpleName();
    ImageView imageView;
    Button cameraBtnFir;
    TextView precaution;
    Button uploadBtn;

    Uri selectedImageUri;
    private ActivityResultLauncher<Intent> resultLauncher;
    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    //   하단 네비게이션
    private long lastTimeBackPressed;
    FirebaseAuth mAuth;

    //뒤로 가기를 누르면 어플을 종료할 수 있도록 함
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        //두 번 클릭시 어플 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }


    //activity 최초 생성할 때 호출함
    //super을 붙이는 이유가 상위 클래스의 oncreate 호출하기 위해서
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

      //  test1 = findViewById(R.id.test1);

        // 레이아웃과 변수 연결
        imageView = findViewById(R.id.imageview);
        cameraBtnFir = findViewById(R.id.camera_button_first);
        precaution = findViewById(R.id.precautions);
        uploadBtn = findViewById(R.id.uploadBtn);

        cameraBtnFir.setOnClickListener(this);
        //갤러리에서 파일 선택
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
                DBupload();

            }
        });

        // 파이어베이스 권한 초기화
        mAuth = FirebaseAuth.getInstance();
        // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }



    //사진
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //사진 촬영 후 가져오기
                    //리퀘스트가 0이면
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        //uri 불러오기
                        selectedImageUri = data.getData();
                        //log를 통해서 바로 uri를 볼 수 있음
                        Log.d(TAG, "uri:" + String.valueOf(selectedImageUri));
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                            String imageSaveUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "사진 저장", "찍은 사진이 저장되었습니다.");
                            selectedImageUri = Uri.parse(imageSaveUri);


                            Log.d(TAG, "PhotoActivity - onActivityResult() called" + selectedImageUri);
                        }
                    }
                }
            });

    public void openSomeActivityForResult() throws IOException {
//인텐트 객체 생성
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        someActivityResultLauncher.launch(takePictureIntent);
//createImageFile 만들어서 넣고,
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //에러 발생 시
            }
            //성공적으로 만들어졌을 경우 (내부 저장소에 저장할 때)
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.vehicle1.fileprovider",
                        photoFile);
                //카메라 앱에서 찍은 사진을 저장할 위치 설정
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
//        //ACTION_~ 을 통해서 갤러리에 사진을 업데이트함 - 여기 때문에 오류 생김
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri contentUri = Uri.fromFile(new File(mCurrentPhotoPath));
//        mediaScanIntent.setData(contentUri);
//        //이걸로 인텐트 전송함
//        sendBroadcast(mediaScanIntent);
//        Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // 버튼 onClick 리스너 처리부분
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_button_first:
                try {
                    openSomeActivityForResult();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //사진 촬영하면 주의사항 사라지게 함
                precaution.setVisibility(View.GONE);
                break;

            case R.id.uploadBtn:
                DBupload();
//                clickBtn(imageView);
//                httpGetConnection(url, data);
                selectedImageUri = null;
                break;
            case R.id.print_result:
                imageView.setVisibility(View.GONE);
                cameraBtnFir.setVisibility(View.GONE);
                uploadBtn.setVisibility(View.GONE);
                //getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new ResultFragment()).commitAllowingStateLoss();
                break;
        }
    }

        // 카메라로 촬영한 영상을 가져오는 부분
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

    //카메라로 촬영한 사진이 저장될 파일을 만드는 함수
    private File createImageFile() throws IOException {
        //파일명 설정
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        //getExternalStorageDirectory() - 최상위 경로를 가지고 오는 메서드
        File storageDir = new File(Environment.getExternalStorageDirectory() + "images/");
        if (!storageDir.exists()) {
            Log.v("알림", "storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs(); //파일 없으면 새로 생성
        }
        Log.v("알림", "storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void DBupload() {

        if (selectedImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage 객체 생성
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //이름 설정
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd_HHmmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            StorageReference storageRef = storage.getReferenceFromUrl("gs://graduation-project-74d71.appspot.com/").child("images/" + filename);


            //여기를 수정해야 함
            storageRef.putFile(selectedImageUri)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    String url = "http://172.19.98.71:8080/assessment";
    String data = "url=https://modo-phinf.pstatic.net/20190826_221/1566810773449mMq4j_JPEG/mosauv6ibr.jpeg";


    public static void httpGetConnection(String UrlData, String ParamData) {
        String totalUrl = "";
        if (ParamData != null && ParamData.length() > 0 &&
                !ParamData.equals("") && !ParamData.contains("null")) { //파라미터 값이 널값이 아닌지 확인
            totalUrl = UrlData.trim().toString() + "?" + ParamData.trim().toString();
        } else {
            totalUrl = UrlData.trim().toString();
        }
        HttpURLConnection connection = null;
        String responseData = "";
        String returnData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        try {
            URL url = new URL(totalUrl);
            connection = (HttpURLConnection) url.openConnection();
            // http 요청에 필요한 타입 정의 실시
            //request header값 세팅 - json 형식의 타입으로 요청
            connection.setRequestProperty("Accept", "application/json");
            //타입설정 형식으로 전송
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("GET");

            // http 요청 실시
            connection.connect();

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            sb = new StringBuffer();

            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            returnData = sb.toString();

            System.out.println(returnData);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
// 이미지 돌려주는 함수
//    public static Bitmap rotateImage(Bitmap source, float angle) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
//                matrix, true);
//    }