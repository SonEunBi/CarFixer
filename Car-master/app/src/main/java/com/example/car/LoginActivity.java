package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity
{

    private FirebaseAuth mFirebaseAuth;      //파이어 베이스 인증 처리
    private DatabaseReference mDatabaseRef;  //실시간 데이터 베이스
    private EditText mEtEmail, mEtPwd;       // 로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("car2022");

        mEtEmail= findViewById(R.id.editTextEmail);
        mEtPwd= findViewById(R.id.editTextPassword);

        Button buttonLogin =findViewById(R.id.buttonSignup);
        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //로그인 요청
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()){
                            //로그인 성공



                        }else {
                            Toast.makeText(LoginActivity.this,"로그인 실패...",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
        Button buttonSignin =findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //회원가입 화면으로 이동한다.
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}