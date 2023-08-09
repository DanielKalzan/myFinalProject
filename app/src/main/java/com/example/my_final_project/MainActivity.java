package com.example.my_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);


    }

    public void openSignup(View view) {

        startActivity(new Intent(this,SignupPage.class));

    }

    public void openLogin(View view) {

        startActivity(new Intent(this, LoginPage.class));

    }


}