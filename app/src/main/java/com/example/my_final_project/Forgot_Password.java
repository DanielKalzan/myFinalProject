package com.example.my_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;

public class Forgot_Password extends AppCompatActivity {

    static String userName;
    Dialog smsDialog;
    DBuser dBuser;
    TextView change_btn;
    TextInputEditText changePassword, changeRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        smsDialog = new Dialog(this);
        dBuser = new DBuser(this);
        change_btn = findViewById(R.id.change_btn);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("UserName");

        changePassword = findViewById(R.id.tie1_Change);
        changeRepeatPassword = findViewById(R.id.tie2_Change);


        change_btn.setOnClickListener(v -> changePassword());

        findUser(sendSms(dBuser.getPhoneNum(name)));
    }

    private void changePassword() {

        String pass = changePassword.getText().toString();
        String rPass = changeRepeatPassword.getText().toString();


        if (pass.isEmpty() || pass.length() <6){
            changePassword.setError("הסיסמה חייבת להיות באורך של לפחות 6 תווים");
        } else if (rPass.isEmpty()) {
            changeRepeatPassword.setError("לא יכול להיות ריק");
        }else if (!rPass.equals(pass)){
            changeRepeatPassword.setText("");
            changeRepeatPassword.setError("הסיסמאות אינן תואמות");
        }else {

            try {
                boolean result = dBuser.updatePassword(userName,pass);

                if (result){
                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private int sendSms(String num){

        Random random = new Random();

        int cod = random.nextInt(10000 - 1000) + 1000;

        String b = "קוד אימות המספר הוא: " + cod;

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(num, null, b, null, null);

        return cod;

    }


    private void findUser(int cod) {
        /*
        ImageView cancel;
        TextInputEditText checkUsername, checkPassword;
        TextView checkBtn;


        findbyemailDialog.setContentView(R.layout.findbyemaildialog);
        findbyemailDialog.setCancelable(false);
        findbyemailDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));

        cancel = findbyemailDialog.findViewById(R.id.cancelChange);
        cancel.setOnClickListener(v -> Exit());

        checkUsername = findbyemailDialog.findViewById(R.id.tie1_dialogChange);
        checkUsername.setText(name);
        checkPassword = findbyemailDialog.findViewById(R.id.tie2_dialogChange);
        checkBtn = findbyemailDialog.findViewById(R.id.Change_btn);
        checkBtn.setOnClickListener(v -> {

            String Name = checkUsername.getText().toString();
            String email = checkPassword.getText().toString();

            if (Name.isEmpty()){
                checkUsername.setError("לא יכול להיות ריק");
            }else if (email.isEmpty()){
                checkPassword.setError("לא יכול להיות ריק");
            }else {

                boolean Exists = dBuser.userExit(Name,email);
                if (Exists){
                    userName = checkUsername.getText().toString();
                    findbyemailDialog.dismiss();
                }else {
                    Toast.makeText(this, "שם משתמש או הסיסמה אינם נכונים", Toast.LENGTH_SHORT).show();
                }
            }

        });

        findbyemailDialog.show();

         */

        TextView btn;
        EditText et;

        smsDialog.setContentView(R.layout.sms_dialog);
        smsDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        et = smsDialog.findViewById(R.id.det);
        btn = smsDialog.findViewById(R.id.ok_btn);
        btn.setOnClickListener(v -> {

            if (et.getText().toString().equals(String.valueOf(cod))) {
                smsDialog.dismiss();
            } else {
                Toast.makeText(this, "not good", Toast.LENGTH_SHORT).show();
                et.setText("");
            }

        });


        smsDialog.setCancelable(false);
        smsDialog.show();



    }


    public void backToLogin(View view) {
        Exit();
    }

    @Override
    public void onBackPressed() {
        Exit();
    }

    public void Exit(){

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("אַזהָרָה!!!");
        dialogDelete.setMessage("הדברים ששינת יבוטלו");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> finish());

        dialogDelete.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

        dialogDelete.show();

    }
}