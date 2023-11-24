package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;         // 파이어베이스 인증
    private DatabaseReference mDatabaseRef;     // 실시간 데이터베이스
    private EditText emtEmail, emtPwd, emtNickname, emtPwdmatch;          // 회원가입 입력필드
    private TextView checktext;
    private Button btnRegister;                 // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        emtEmail = findViewById(R.id.et_email);
        emtPwd = findViewById(R.id.et_pwd);
        emtPwdmatch = findViewById(R.id.et_pwdmatch);
        emtNickname = findViewById(R.id.et_nickname);
        checktext = findViewById(R.id.text_check);
        btnRegister = findViewById(R.id.btn_register);

        emtPwdmatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //회원가입 처리시작
            String strEmail = emtEmail.getText().toString();
            String strPwd = emtPwd.getText().toString();
            String strNickname = emtNickname.getText().toString();


                    // Firebase auth
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)  //가입이 되었을 때
                    {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseuser = mFirebaseAuth.getCurrentUser();

                            UserAcc acc = new UserAcc();
                            acc.setIdToken(firebaseuser.getUid());
                            acc.setEmailId(firebaseuser.getEmail());
                            acc.setNickname(strNickname);
                            acc.setPassword(strPwd);

                            //setValue는 database에 삽입
                            mDatabaseRef.child("UserAccount").child(firebaseuser.getUid()).setValue(acc);

                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    }
}