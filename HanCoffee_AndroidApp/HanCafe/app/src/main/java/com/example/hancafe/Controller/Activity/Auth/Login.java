package com.example.hancafe.Controller.Activity.Auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hancafe.Controller.Activity.Admin.MainAdminActivity;
import com.example.hancafe.Controller.Activity.User.MainActivity;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CartData;
import com.example.hancafe.Model.User;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.example.hancafe.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText edtSDT, edtMK;
    CheckBox cbNhoMK;
    TextView tvSignup, tvQuenMK;
    private FirebaseAuth mAuth;
    private static final String TAG = Login.class.getName();
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode(Locale.getDefault().getLanguage());

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            edtSDT.setText(sharedPreferences.getString(KEY_USERNAME, ""));
            edtMK.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            cbNhoMK.setChecked(true);
        }

        tvQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edtSDT.getText().toString().trim();
                String strMK = edtMK.getText().toString().trim();
                if (strEmail.isEmpty() || strMK.isEmpty()) {
                    Toast.makeText(Login.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                onClickLogin();
            }
        });
    }
    private void onClickLogin() {
        String strId = "";
        String strEmail = edtSDT.getText().toString().trim();
        String strPassword = edtMK.getText().toString().trim();
        String strName = "";
        String strPhone = "";
        String strAdress = "";
        String avatar = "";
        int role = 0;
        List<CartData> cartData = null;

        User user = new User(strEmail, strPassword);

        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<User>> call = apiService.login(user);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful()) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse != null) {
                        User user = apiResponse.getUser();
                        if (user != null) {
                            if (cbNhoMK.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_USERNAME, strEmail);
                                editor.putString(KEY_PASSWORD, strPassword);
                                editor.putBoolean(KEY_REMEMBER, true);
                                editor.apply();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove(KEY_USERNAME);
                                editor.remove(KEY_PASSWORD);
                                editor.remove(KEY_REMEMBER);
                                editor.apply();
                            }
                            String userId = user.getId();
                            System.out.println(userId);
                            // Lưu ID người dùng vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", userId);
                            editor.apply();
                            // Lấy thông tin người dùng từ phản hồi API
                            int role = user.getRole();
                            // Thực hiện hành động phù hợp dựa trên vai trò của người dùng
                            if (role == 0 || role == 1) {
                                // Chuyển hướng đến màn hình phù hợp
                                Intent intent = new Intent(Login.this, MainAdminActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if (role == 2) {
                                // Chuyển hướng đến màn hình phù hợp
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else {
                        // Xử lý trường hợp API không thành công
                        Toast.makeText(Login.this, "Đăng nhập không thành công: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Đã xảy ra lỗi khi đăng nhập: " + response.message(), Toast.LENGTH_SHORT).show();
                }
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable throwable) {
                Toast.makeText(Login.this, "Lỗi khi gọi API: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        mAuth.signInWithEmailAndPassword(strEmail, strPassword)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
////                             Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            if (user != null && user.isEmailVerified()) {
//                                if (cbNhoMK.isChecked()) {
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString(KEY_USERNAME, strEmail);
//                                    editor.putString(KEY_PASSWORD, strPassword);
//                                    editor.putBoolean(KEY_REMEMBER, true);
//                                    editor.apply();
//                                } else {
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.remove(KEY_USERNAME);
//                                    editor.remove(KEY_PASSWORD);
//                                    editor.remove(KEY_REMEMBER);
//                                    editor.apply();
//                                }
//
//                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
//                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if(snapshot.exists()) {
//                                            int role = snapshot.child("role").getValue(Integer.class);
//                                            if (role == 0) {
//                                                Intent intent = new Intent(Login.this, MainAdminActivity.class);
//                                                startActivity(intent);
//                                            } else if (role == 1) {
//                                                Intent intent = new Intent(Login.this, MainAdminActivity.class);
//                                                startActivity(intent);
//                                            } else if (role == 2) {
//                                                Intent intent = new Intent(Login.this, MainActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                                // check phân quyền
//                                // Email đã được xác minh, cho phép đăng nhập
////                                Intent intent = new Intent(Login.this, MainAdminActivity.class);
////                                startActivity(intent);
////                                finishAffinity();
//                            } else {
//                                // Hiển thị thông báo hoặc yêu cầu xác minh email
//                                Toast.makeText(Login.this, "Vui lòng xác minh email trước khi đăng nhập", Toast.LENGTH_SHORT).show();
//                            }
////                            finishAffinity();
////                            sendOTP(strEmail)
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(Login.this, "Sai thông tin đăng nhập.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    private void sendOTP(String strEmail) {
        Intent intent = new Intent(Login.this, OTPLogin.class);
        intent.putExtra("email", strEmail);
        startActivity(intent);
        sendOTPToEmail(strEmail);

    }

    private void sendOTPToEmail(String strEmail) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Xử lý khi gửi email thành công
                                Toast.makeText(Login.this, "Mã OTP đã được gửi qua email", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý khi gửi email thất bại
                                Toast.makeText(Login.this, "Không thể gửi mã OTP qua email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private void onClickVerifyPhoneNumber(String strSDT) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strSDT)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(Login.this, "Fail",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                                goToLoginOTP(strSDT, verificationID);
                            }
                        })         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void setControl() {
        tvSignup = findViewById(R.id.tvSignup);
        btnLogin = findViewById(R.id.btnLogin);
        edtSDT = findViewById(R.id.edtSDT);
        edtMK = findViewById(R.id.edtMK);
        tvQuenMK = findViewById(R.id.tvQUenMK);
        cbNhoMK = findViewById(R.id.cbNhoMK);
    }

    private void setEvent() {
        btnLogin.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            toToMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Login.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void toToMainActivity(String phoneNumber) {
        Intent intent = new Intent(this, MainAdminActivity.class);
        intent.putExtra("phone number", phoneNumber);
        startActivity(intent);
    }
    private void goToLoginOTP(String strSDT, String verificationID) {
        Intent intent = new Intent(this, OTPLogin.class);
        intent.putExtra("phone number", strSDT);
        intent.putExtra("verificationid", verificationID);
        startActivity(intent);

    }
}