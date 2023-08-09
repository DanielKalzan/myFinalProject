package com.example.my_final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignupPage extends AppCompatActivity {

    ImageView userImage;
    Dialog imageDialog, loginDialog, smsDialog;
    Uri imageUri;

    TextInputEditText SignupUsername, SignupPhoneNum, SignupPassword, SignupRepeatPassword;
    TextView signupBtn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DBuser dBuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        findView();

        imageDialog = new Dialog(this);
        loginDialog = new Dialog(this);
        smsDialog = new Dialog(this);
        dBuser = new DBuser(this);

        userImage.setOnClickListener(v -> openDialog());

        signupBtn.setOnClickListener(v -> CheckDetails());

    }

    private void CheckDetails() {

        String num = SignupPhoneNum.getText().toString();
        String password = SignupPassword.getText().toString();
        String Repassword = SignupRepeatPassword.getText().toString();
        String userName = SignupUsername.getText().toString();

        if (userName.isEmpty()) {

            SignupUsername.setError("לא יכול להיות ריק");


        } else if (num.isEmpty()) {

            SignupPhoneNum.setError("לא יכול להיות ריק");


        } else if (num.length() < 10) {

            SignupPhoneNum.setError("מספר הטלפון קצר מדי");

        } else if (password.isEmpty() || password.length() < 6) {

            SignupPassword.setError("הסיסמה חייבת להיות באורך של לפחות 6 תווים");

        } else if (!password.equals(Repassword)) {

            SignupRepeatPassword.setError("הסיסמאות אינם דומות");

        } else {
                try {
                    boolean insert = dBuser.insertData(userName, num, password, imageViewToBy(userImage));

                    if (insert) {

                        sendSms(num, userName, String.valueOf(sendRandomCode(num)));
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public int sendRandomCode(String num){

        Random random = new Random();
        int code = random.nextInt(10000 - 1000) + 1000;
        String message = "קוד אימות המספר הוא: " + code;

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(num, null, message, null, null);

        return code;
    }


    public void openLoginDialog() {

        loginDialog.setContentView(R.layout.loding_dialog);
        loginDialog.setCancelable(false);
        loginDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        loginDialog.show();

    }


    private void findView() {
        userImage = findViewById(R.id.userImage);
        SignupUsername = findViewById(R.id.tie1_signup);
        SignupPhoneNum = findViewById(R.id.tie2_signup);
        SignupPassword = findViewById(R.id.tie3_signup);
        SignupRepeatPassword = findViewById(R.id.tie4_signup);
        signupBtn = findViewById(R.id.signup_btn);
    }

    private void openDialog() {

        ImageView gallery, Camera;
        imageDialog.setContentView(R.layout.image_dielog);
        imageDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        gallery = imageDialog.findViewById(R.id.gallery);
        Camera = imageDialog.findViewById(R.id.camera);
        Camera.setOnClickListener(v -> openCamera());
        gallery.setOnClickListener(v -> openGallery());


        imageDialog.show();

    }

    private void openCamera() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);

        imageDialog.dismiss();


    }

    private void openGallery() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
        imageDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {


            imageUri = data.getData();
            try {
                Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                userImage.setImageBitmap(b);
            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == 0 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            userImage.setImageBitmap(bitmap);
        }
    }

    public void backToMain(View view) {

        finish();

    }

    public static byte[] imageViewToBy(ImageView avatar) {

        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public void openLogin(View view) {
        startActivity(new Intent(this, LoginPage.class));
        finish();
    }

    private void sendSms(String num, String name, String cod) {

        TextView btn;
        EditText et;

        smsDialog.setContentView(R.layout.sms_dialog);
        smsDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        et = smsDialog.findViewById(R.id.det);
        btn = smsDialog.findViewById(R.id.ok_btn);
        btn.setOnClickListener(v -> {

            if (et.getText().toString().equals(cod)) {
                String message = "שלום " + name + " החשבון נוצר בהצלחה";

                SmsManager mySms = SmsManager.getDefault();
                mySms.sendTextMessage(num, "daniel", message, null, null);

                Intent intent = new Intent(this, HomePage.class);
                intent.putExtra("UserName", name);
                startActivity(intent);

                smsDialog.dismiss();
            } else {
                Toast.makeText(this, "not good", Toast.LENGTH_SHORT).show();
                et.setText("");
            }

        });


        smsDialog.setCancelable(false);
        smsDialog.show();

    }
}