package com.example.my_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    public static String getNaem;
    TextView welcome, battery, date_tv;
    ImageView userImage;
    CardView cv;
    Dialog dialog;
    DBuser dBuser;
    public static final int REQUEST_CALL = 1;
    FloatingActionButton music;
    MediaPlayer mp;
    boolean isPlay = false;
    SharedPreferences sp;
    public static int notificationCounter = 0;


    public static BottomNavigationView navigationView;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        dialog = new Dialog(this);
        dBuser = new DBuser(this);
        sp = getSharedPreferences("userChecked", 0);
        mp = MediaPlayer.create(this,R.raw.background_music);

        findView();
        navItemSelector();
        showBattery();
        showDate();

        music = findViewById(R.id.music);
        music.setOnClickListener(v -> playMusic());

    }

    static void counter(int c, BottomNavigationView bottomNavigationView, int id) {

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(id);
        badgeDrawable.setVisible(c>0);
        badgeDrawable.setBackgroundColor(Color.BLUE);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setNumber(c);
    }

    private void showDate() {
        date_tv = findViewById(R.id.date);
        date_tv.setText( new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));
    }

    private void showBattery() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int laval = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                battery.setText(laval + "%");

            }
        };
        this.registerReceiver(this.broadcastReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


    }



    private void navItemSelector() {
        navigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.activityHome,new Home_Fragment()).commit();
        navigationView.setSelectedItemId(R.id.nav_home);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment =null;

            switch (item.getItemId()){

                case R.id.nav_home:
                    fragment = new Home_Fragment();
                    break;

                case R.id.nav_shopp:
                    fragment = new ShoppingList_Fragment();
                    break;
                case R.id.nav_list:
                    fragment = new List_Fragment();
                    break;

                case R.id.nav_info:
                    fragment = new Info_Fragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.activityHome,fragment).commit();
            return true;
        });

    }



    private void playMusic() {
        if (!isPlay){
            mp.start();
            isPlay = true;
            music.setImageResource(R.drawable.ic_music_off);
        }else {
            mp.pause();
            isPlay = false;
            music.setImageResource(R.drawable.ic_music_note);
        }
    }

    public void sandEmail(View view){

        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"danielgaz@company.com"});
        startActivity(Intent.createChooser(email, "Send mail..."));
    }

    public void openMaps(View view){

        Uri gmmIntentUri = Uri.parse("geo:32.096478938030415, 34.902340118478385?q="
                +Uri.parse("32.096478938030415, 34.902340118478385"));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void openWhatsapp(View view){

        String url = "https://api.whatsapp.com/send?phone= +972 0546463305 Number&text=היי";

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);


    }

    public void findView() {

        welcome = findViewById(R.id.welcome_tv);

        userImage = findViewById(R.id.iv);

        battery = findViewById(R.id.battery);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("UserName");
        welcome.setText("ברוך הבא"+" "+name);

        getNaem =name;


        try {
            userImage.setImageBitmap(dBuser.getImage(name));
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        cv = findViewById(R.id.user_cv);
        cv.setOnClickListener(v -> openMenuDialog(name));
    }

   public void openMenuDialog(String name){


       ImageView userImage, close;
       RelativeLayout logout, call, editProfile;
       TextView num;
       dialog.setContentView(R.layout.popup_menu);
       userImage = dialog.findViewById(R.id.userImage);
       userImage.setImageBitmap(dBuser.getImage(name));
       num = dialog.findViewById(R.id.userEmail);
       num.setText(dBuser.getPhoneNum(name));
       close = dialog.findViewById(R.id.close);
       close.setOnClickListener(v1 -> dialog.dismiss());
       logout = dialog.findViewById(R.id.logout);
       logout.setOnClickListener(v1 -> {
           showDialogLogout();
           dialog.dismiss();
       });

       call = dialog.findViewById(R.id.EmergencyCall);
       call.setOnClickListener(v -> {
           EmergencyCall();
           dialog.dismiss();
       });

       editProfile = dialog.findViewById(R.id.setProfile);
       editProfile.setOnClickListener(v -> {

           Intent intent = new Intent(this, UpdatePage.class);
           intent.putExtra("UserName",name);
           startActivity(intent);
           dialog.dismiss();

       });


       dialog.show();


    }


    public void EmergencyCall() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);

        }else {


            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:0546463305")));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL ){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                EmergencyCall();
            }

        }

    }


    @Override
    public void onBackPressed() {
        showDialogExit();
    }

    public void showDialogExit(){

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("אַזהָרָה!!!");
        dialogDelete.setMessage("אתה בטוח שאת רוצה לצאת מהאפליקציה??");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> finish());
        dialogDelete.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

        dialogDelete.show();
    }

    public void showDialogLogout(){

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this);
        dialogDelete.setTitle("אַזהָרָה!!!");
        dialogDelete.setMessage("אתה בטוח שאת רוצה להתנתק??");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Checked", "not");
            editor.putString("userName", "");
            editor.apply();
            finish();
            startActivity(new Intent(this,MainActivity.class));
        });
        dialogDelete.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

        dialogDelete.show();
    }

}