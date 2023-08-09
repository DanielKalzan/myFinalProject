package com.example.my_final_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class UpdatePage extends AppCompatActivity {

    TextInputEditText editPassword,editEmail, editRepeatPassword;
    ImageView backHome, editUserImage;
    Dialog imageDialog, loginDialog, findUserDialog;
    Uri imageUri;
    TextView editBtn;

    DBuser dBuser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_page);

        dBuser = new DBuser(this);
        imageDialog = new Dialog(this);
        loginDialog = new Dialog(this);
        findUserDialog = new Dialog(this);


        Intent intent = getIntent();
        String name = intent.getExtras().getString("UserName");
        findUser();
        findView(name);

    }

    private void findUser() {
        ImageView cancel;
        TextInputEditText checkUsername, checkPassword;
        TextView checkBtn;


        findUserDialog.setContentView(R.layout.finduserdialog);
        findUserDialog.setCancelable(false);
        findUserDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));

        cancel = findUserDialog.findViewById(R.id.cancelEdit);
        cancel.setOnClickListener(v -> Exit());

        checkUsername = findUserDialog.findViewById(R.id.tie1_check);
        checkPassword = findUserDialog.findViewById(R.id.tie2_check);
        checkBtn = findUserDialog.findViewById(R.id.check_btn);
        checkBtn.setOnClickListener(v -> {

            String userName = checkUsername.getText().toString();
            String pass = checkPassword.getText().toString();

            if (userName.isEmpty()){
                checkUsername.setError("לא יכול להיות ריק");
            }else if (pass.isEmpty()){
                checkPassword.setError("לא יכול להיות ריק");
            }else {

                boolean Exists = dBuser.ChecksInformation(userName,pass);
                if (Exists){
                    findUserDialog.dismiss();
                }else {
                    Toast.makeText(this, "שם משתמש או סיסמה אינם נכונים", Toast.LENGTH_SHORT).show();
                }
            }

        });

        findUserDialog.show();

    }

    private void findView(String name) {

        editEmail = findViewById(R.id.tie2_edit);
        editEmail.setText(dBuser.getPhoneNum(name));

        editPassword = findViewById(R.id.tie3_edit);
        editPassword.setText(dBuser.getPassword(name));

        editRepeatPassword = findViewById(R.id.tie4_edit);
        editRepeatPassword.setText(dBuser.getPassword(name));

        backHome = findViewById(R.id.back_to_Home);
        backHome.setOnClickListener(v -> Exit());

        editUserImage = findViewById(R.id.editUserImage);
        editUserImage.setImageBitmap(dBuser.getImage(name));
        editUserImage.setOnClickListener(v -> openDialog());

        editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(v ->  CheckDetails(name,editUserImage));
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
                editUserImage.setImageBitmap(b);
            } catch (Exception e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == 0 && resultCode == RESULT_OK) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            editUserImage.setImageBitmap(bitmap);
        }
    }


    private void CheckDetails(String name, ImageView iv) {

        String phoneNum = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String Repassword = editRepeatPassword.getText().toString();

        openLoginDialog();
         if (phoneNum.isEmpty()) {

            editEmail.setError("לא יכול להיות ריק");
            loginDialog.dismiss();


        } else if (password.isEmpty() || password.length() < 6) {

            editPassword.setError("הסיסמה חייבת להיות באורך של לפחות 6 תווים");
            loginDialog.dismiss();


        } else if (!password.equals(Repassword)) {

            editRepeatPassword.setError("הסיסמה לא תואמת");
            loginDialog.dismiss();
        } else{

            new Handler().postDelayed(() -> {

                try {
                    boolean result = dBuser.updateProfile(name,phoneNum,password,imageViewToBy(iv));
                    if (result){
                        Toast.makeText(this, "הפרופיל נערך בהצלחה", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,HomePage.class);
                        intent.putExtra("UserName",name);
                        startActivity(intent);
                    }
                }catch (Exception e){

                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


                loginDialog.dismiss();

            },2500);

        }
    }
    public void openLoginDialog() {

        loginDialog.setContentView(R.layout.loding_dialog);
        loginDialog.setCancelable(false);
        loginDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        loginDialog.show();

    }

    public static byte[] imageViewToBy(ImageView avatar) {

        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onBackPressed() {
        Exit();
    }

    public void Exit(){

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("אַזהָרָה!!!");
        dialogDelete.setMessage("הדברים ששינתה יתבטלו");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> finish());
        dialogDelete.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

        dialogDelete.show();

    }
}