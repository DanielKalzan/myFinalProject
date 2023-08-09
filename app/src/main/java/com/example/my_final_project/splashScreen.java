package com.example.my_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {

    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sp = getSharedPreferences("userChecked", 0);



        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    getFromSp();
                    finish();

                }
            }, 3000);

        }catch (Exception e){

            startActivity(new Intent(splashScreen.this, MainActivity.class));
        }


    }

    public void getFromSp() {

        String result = sp.getString("Checked", "");
        String name = sp.getString("userName", "");

        if (result.isEmpty()){

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Checked", "not");
            editor.putString("userName", "");
            editor.apply();
        }else{
            if (result.equals("ok")){
                Intent intent = new Intent(this, HomePage.class);
                intent.putExtra("UserName", name);
                startActivity(intent);
            }else {
                startActivity(new Intent(splashScreen.this, MainActivity.class));
            }
        }





    }
}