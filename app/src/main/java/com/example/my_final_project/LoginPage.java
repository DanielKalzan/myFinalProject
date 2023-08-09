package com.example.my_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;

public class LoginPage extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    TextInputEditText loginUsername, loginPassword;
    TextView loginBtn, forgotPass;
    DBuser dBuser;
    Dialog dialog;
    CheckBox remember_me;
    SharedPreferences sp;
    boolean ch = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        dBuser = new DBuser(this);
        dialog = new Dialog(this);
        sp = getSharedPreferences("userChecked", 0);
        findView();

        loginBtn.setOnClickListener(v -> checker());
        forgotPass.setOnClickListener(v -> {

            if (!loginUsername.getText().toString().isEmpty()) {

                if (dBuser.IfUsernameExit(loginUsername.getText().toString())) {
                    BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("נא לאמת")
                            .setDescription("נדרש אימות משתמש כדי להמשיך")
                            .setNegativeButtonText("ביטול")
                            .build();
                    getPrompt().authenticate(promptInfo);
                } else
                    notifyUser("שם משתמש אינו קיים");
            } else
                notifyUser("בבקשה הכנס שם משתמש");
        });


    }

    private void checker() {

        String username = loginUsername.getText().toString();
        String userPassword = loginPassword.getText().toString();
        openLoginDialog();


        if (username.isEmpty() || userPassword.isEmpty()) {

            Toast.makeText(this, "בבקשה הכנס פרטים", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        } else {
            new Handler().postDelayed(() -> {

                try {
                    boolean UserExist = dBuser.ChecksInformation(username, userPassword);

                    if (UserExist) {

                        setInSp(ch, username);

                        Intent intent = new Intent(this, HomePage.class);
                        intent.putExtra("UserName", username);
                        startActivity(intent);


                    }

                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();

            }, 2200);

        }


    }

    public void openLoginDialog() {

        dialog.setContentView(R.layout.loding_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.show();

    }

    private void findView() {

        loginUsername = findViewById(R.id.tieLogin1);
        loginPassword = findViewById(R.id.tieLogin2);
        loginBtn = findViewById(R.id.login_btn);
        forgotPass = findViewById(R.id.forgot_password);
        remember_me = findViewById(R.id.remember_me);
        remember_me.setOnCheckedChangeListener(this);


    }


    public void backToMain(View view) {

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    public void openSignup(View view) {

        startActivity(new Intent(this, SignupPage.class));
        finish();

    }

    private BiometricPrompt getPrompt() {

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("האימות הצליח");

                Intent intent = new Intent(LoginPage.this, Forgot_Password.class);
                intent.putExtra("UserName", loginUsername.getText().toString());
                startActivity(intent);            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("האימות נכשל");

            }
        };

        return new BiometricPrompt(this, executor, callback);

    }

    private void notifyUser(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        ch = remember_me.isChecked();

    }

    public void setInSp(boolean ch, String name) {

        SharedPreferences.Editor editor = sp.edit();
        if (ch) {

            editor.putString("Checked", "ok");

        } else {
            editor.putString("Checked", "not");
        }
        editor.putString("userName", name);
        editor.apply();


    }
}