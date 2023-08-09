package com.example.my_final_project;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Home_Fragment extends Fragment {

    Dialog dialog;
    OrdersDB ordersDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        findView0(view);
        findView1(view);
        findView2(view);
        findView3(view);

        dialog = new Dialog(getContext());
        ordersDB = new OrdersDB(getContext(),HomePage.getNaem+"order.db");

        return view;
    }

    private void findView0(View view) {

        ImageView iv00,iv01,iv02;
        ImageView sop00,sop01,sop02;
        TextView name00,name01,name02;
        TextView price00,price01,price02;


        iv00 = view.findViewById(R.id.iv00);
        name00 = view.findViewById(R.id.itemName00);
        price00 = view.findViewById(R.id.itemPrice00);
        sop00 = view.findViewById(R.id.shopping00);
        sop00.setOnClickListener(v->openDialog(name00,price00,iv00));

        iv01 = view.findViewById(R.id.iv01);
        name01 = view.findViewById(R.id.itemName01);
        price01 = view.findViewById(R.id.itemPrice01);
        sop01 = view.findViewById(R.id.shopping01);
        sop01.setOnClickListener(v -> openDialog(name01,price01,iv01));

        iv02 = view.findViewById(R.id.iv02);
        name02 = view.findViewById(R.id.itemName02);
        price02 = view.findViewById(R.id.itemPrice02);
        sop02 = view.findViewById(R.id.shopping02);
        sop02.setOnClickListener(v -> openDialog(name02,price02,iv02));


    }

    private void findView1(View view) {

        ImageView iv10,iv11,iv12;
        ImageView sop10,sop11,sop12;
        TextView name10,name11,name12;
        TextView price10,price11,price12;


        iv10 = view.findViewById(R.id.iv10);
        name10 = view.findViewById(R.id.itemName10);
        price10 = view.findViewById(R.id.itemPrice10);
        sop10 = view.findViewById(R.id.shopping10);
        sop10.setOnClickListener(v->openDialog(name10,price10,iv10));

        iv11 = view.findViewById(R.id.iv11);
        name11 = view.findViewById(R.id.itemName11);
        price11 = view.findViewById(R.id.itemPrice11);
        sop11 = view.findViewById(R.id.shopping11);
        sop11.setOnClickListener(v -> openDialog(name11,price11,iv11));

        iv12 = view.findViewById(R.id.iv12);
        name12 = view.findViewById(R.id.itemName12);
        price12 = view.findViewById(R.id.itemPrice12);
        sop12 = view.findViewById(R.id.shopping12);
        sop12.setOnClickListener(v -> openDialog(name12,price12,iv12));


    }

    private void findView2(View view) {

        ImageView iv20,iv21,iv22;
        ImageView sop20,sop21,sop22;
        TextView name20,name21,name22;
        TextView price20,price21,price22;


        iv20 = view.findViewById(R.id.iv20);
        name20 = view.findViewById(R.id.itemName20);
        price20 = view.findViewById(R.id.itemPrice20);
        sop20 = view.findViewById(R.id.shopping20);
        sop20.setOnClickListener(v->openDialog(name20,price20,iv20));

        iv21 = view.findViewById(R.id.iv21);
        name21 = view.findViewById(R.id.itemName21);
        price21 = view.findViewById(R.id.itemPrice21);
        sop21 = view.findViewById(R.id.shopping21);
        sop21.setOnClickListener(v -> openDialog(name21,price21,iv21));

        iv22 = view.findViewById(R.id.iv22);
        name22 = view.findViewById(R.id.itemName22);
        price22 = view.findViewById(R.id.itemPrice22);
        sop22 = view.findViewById(R.id.shopping22);
        sop22.setOnClickListener(v -> openDialog(name22,price22,iv22));


    }

    private void findView3(View view) {

        ImageView iv30,iv31,iv32;
        ImageView sop30,sop31,sop32;
        TextView name30,name31,name32;
        TextView price30,price31,price32;


        iv30 = view.findViewById(R.id.iv30);
        name30 = view.findViewById(R.id.itemName30);
        price30 = view.findViewById(R.id.itemPrice30);
        sop30 = view.findViewById(R.id.shopping30);
        sop30.setOnClickListener(v->openDialog(name30,price30,iv30));

        iv31 = view.findViewById(R.id.iv31);
        name31 = view.findViewById(R.id.itemName31);
        price31 = view.findViewById(R.id.itemPrice31);
        sop31 = view.findViewById(R.id.shopping31);
        sop31.setOnClickListener(v -> openDialog(name31,price31,iv31));

        iv32 = view.findViewById(R.id.iv32);
        name32 = view.findViewById(R.id.itemName32);
        price32 = view.findViewById(R.id.itemPrice32);
        sop32 = view.findViewById(R.id.shopping32);
        sop32.setOnClickListener(v -> openDialog(name32,price32,iv32));


    }






    private void openDialog(TextView name, TextView price, ImageView iv ) {

        ImageView close;
        TextView sum;
        Button order;
        Spinner sp;
        ArrayList<String> pieces;


        dialog.setContentView(R.layout.invitation_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);


        pieces = new ArrayList<String>();

        for (int i=1;i<100;i++){
            pieces.add(String.valueOf(i));
        }



        sp = dialog.findViewById(R.id.spinner1);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item,
                        pieces); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp.setAdapter(spinnerArrayAdapter);


        sum = dialog.findViewById(R.id.userOrder);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sum.setText(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            sum.setText(String.valueOf(1));
            }
        });

        String itemName = name.getText().toString();


        int Price2= Integer.parseInt(price.getText().toString());

        if(ordersDB.itemExists(itemName)){

            Toast.makeText(getContext(), "הפריט כבר קיים ברשימה", Toast.LENGTH_SHORT).show();



            dialog.dismiss();

        }else {


            order = dialog.findViewById(R.id.Order);
            order.setOnClickListener(v -> {

                String unit = sum.getText().toString();
                int counter = Integer.parseInt(unit);
                String itemPrice = String.valueOf(Price2 * counter);
                try {

                    boolean insert = ordersDB.insertOrdersData(itemName, itemPrice, unit, imageViewToBy(iv));

                    if (insert) {
                        Toast.makeText(getContext(), "נוסף לרשימה", Toast.LENGTH_SHORT).show();


                        HomePage.notificationCounter++;
                        HomePage.counter(HomePage.notificationCounter, HomePage.navigationView, R.id.nav_shopp);

                        dialog.dismiss();
                    }

                } catch (Exception e) {

                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            });


            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v1 -> dialog.dismiss());
            dialog.show();
        }
    }


    public static byte[] imageViewToBy(ImageView avatar) {

        Bitmap bitmap = ((BitmapDrawable) avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


}