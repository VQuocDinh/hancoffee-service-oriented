package com.example.hancafe.Controller.Activity.Auth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hancafe.Controller.Activity.Admin.MainAdminActivity;
import com.example.hancafe.Controller.Api.ApiResponse;
import com.example.hancafe.Controller.Api.ApiService;
import com.example.hancafe.Model.CartData;
import com.example.hancafe.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.hancafe.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    Button btnDK;
    private ProgressDialog progressDialog;
    EditText edtSDT, edtMK, edtRMK;
    TextView tvLogin, tvQuenMK;
    private FirebaseAuth mAuth;
    private static final String TAG = Signup.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        setControl();
        setEvent();

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        FirebaseApp.initializeApp(this);
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());

        tvQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onClickVerifyPhoneNumber(strSDT);
//                onClickSignup();
                handleSignUp();
            }
        });
    }

    private void handleSignUp() {
        String strId = "";
        String strEmail = edtSDT.getText().toString().trim();
        String strPassword = edtMK.getText().toString().trim();
        String strConfirmPassword = edtRMK.getText().toString().trim();
        String strName = "";
        String strPhone = "";
        String strAdress = "";
        String avatar = "";
        int role = 0;
        List<CartData> cartData = null;

        if (!validateInput(strEmail, strPassword, strConfirmPassword)) {
            return;
        }

        User user = new User(strEmail, strPassword);
        // Call API
        ApiService apiService = ApiService.apiService;
        Call<ApiResponse<User>> call = apiService.register(user);

        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));
                } else {
                    Toast.makeText(Signup.this, "Đăng ký thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Signup.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateInput(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(Signup.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(Signup.this, "Mật khẩu và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onClickSignup() {
        String strEmail = edtSDT.getText().toString().trim();
        String strPassword = edtMK.getText().toString().trim();
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid(); // Lấy UID của người dùng
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                                userRef.child("id").setValue(userId);
                                userRef.child("email").setValue(strEmail); // Lưu email vào Realtime Database
                                userRef.child("role").setValue(2); // Mặc định quyền là 2 (khách hàng)
                                userRef.child("name").setValue("");
                                userRef.child("phone").setValue("");
                                userRef.child("address").setValue("");
                                userRef.child("imgAvt").setValue("");
                                userRef.child("date").setValue("");
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Signup.this, "Một email xác nhận đã được gửi đến địa chỉ email của bạn.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Signup.this, Login.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                                    Toast.makeText(Signup.this, "Gửi email xác nhận thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
//                            finishAffinity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                                Toast.makeText(Signup.this, "Fail",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                                togoOTPSignup(strSDT, verificationID);
                            }
                        })         // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void togoOTPSignup(String strSDT, String verificationID) {
        Intent intent = new Intent(this, OTPSignup.class);
        intent.putExtra("phone number", strSDT);
        intent.putExtra("verificationid", verificationID);
        startActivity(intent);
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
                                Toast.makeText(Signup.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
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


    private void setControl() {
        btnDK = findViewById(R.id.btnDK);
        edtSDT = findViewById(R.id.edtSDT);
        edtMK = findViewById(R.id.edtMK);
        edtRMK = findViewById(R.id.edtRMK);
        tvLogin = findViewById(R.id.tvLogin);
        tvQuenMK = findViewById(R.id.tvQuenMK);
    }

    private void setEvent() {
        btnDK.setBackgroundColor(getResources().getColor(R.color.white));
    }
}