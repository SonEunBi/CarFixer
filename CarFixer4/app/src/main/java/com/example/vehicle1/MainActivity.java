package com.example.vehicle1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.vehicle1.ui.centerlist.CenterFragment;
import com.example.vehicle1.ui.home.HomeFragment;
import com.example.vehicle1.ui.mycar.MycarFragment;
//import com.example.vehicle1.ui.result.CarResult;
//import com.example.vehicle1.ui.result.ResultFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


// implements View.OnClickListener
public class MainActivity extends AppCompatActivity {

    //카메라
    final String TAG = getClass().getSimpleName();
    ImageView imageView;
    Button cameraBtnFir;
    TextView precaution;

    //   하단 네비게이션
    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;
    private long lastTimeBackPressed;
    FloatingActionButton fab;
    TextView test1;


    private void init() {
//        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() { //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new TabSelectedListener());
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test1 = findViewById(R.id.test1);
        init(); //객체 정의
        SettingListener(); //리스너 등록

        //새로운 activity 생성된 후 fragment에 재연결될 때 fragment 추가된 내용 기억
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.home_ly, new HomeFragment()).commitAllowingStateLoss();
        }
        // 카메라
        imageView = findViewById(R.id.imageview);
        cameraBtnFir = findViewById(R.id.camera_button_first);
        precaution = findViewById(R.id.precautions);

        //fab 관리
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "카메라가 실행됩니다", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //fab 클릭 시 카메라 액티비티 활성화
                Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                startActivity(intent);

            }
        });

        // 카메라 권한 설정, 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
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


    //하단 메뉴바 클릭 시 해당 fragment 띄움
    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.tab_home: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new HomeFragment()).commitAllowingStateLoss();
                    return true;
                }
                case R.id.tab_mycar: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new MycarFragment()).commitAllowingStateLoss();
                    return true;
                }

                case R.id.tab_dashboard: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new CenterFragment()).commitAllowingStateLoss();
                    return true;
                }
//                case R.id.tab_settings: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new SettingsFragment()).commitAllowingStateLoss();
//                    return true;
//                }
//                case R.id.tab_result: {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.home_ly, new ResultFragment()).commitAllowingStateLoss();
//                    return true;
//                }
            }
            return false;
        }
    }
}