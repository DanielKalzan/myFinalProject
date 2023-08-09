package com.example.my_final_project;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class List_Fragment extends Fragment {

    LinearLayout main;
    OrderHistory history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_list_, container, false);

        HomePage.notificationCounter = 0;
        HomePage.counter(HomePage.notificationCounter,HomePage.navigationView, R.id.nav_list);

       main = view.findViewById(R.id.main);
       history = new OrderHistory(getContext(),HomePage.getNaem + "historyOrder.db");

        if (history.isEmpty()){
            main.setBackgroundResource(R.drawable.add_item);
        }
       goAllView();

        return view;
    }


    private void goAllView() {

        String s0, s1, s2, s3;



       Cursor res = history.getAllData();

        while (res.moveToNext()) {
            s0 = "הזמנה מספר: "+ res.getInt(0);
            s1 = "סך הכל לתשלום: " + res.getString(1)+"$";
            s2 =   res.getString(2);
            s3 =  res.getString(3);

            addCard(s0, s1, s2, s3);
        }


        if (!history.isEmpty()){
            main.setBackgroundResource(R.color.page_background);
        }


    }


    private void addCard(String id,String sum, String time, String date) {

        TextView item_id,counter,tv_time,tv_date;

        final View view = getLayoutInflater().inflate(R.layout.itmefororderslist, null);

        item_id = view.findViewById(R.id.id);
        counter = view.findViewById(R.id.sum);
        tv_time = view.findViewById(R.id.time);
        tv_date = view.findViewById(R.id.date);


        item_id.setText(id);
        counter.setText(sum);
        tv_time.setText(time);
        tv_date.setText(date);



        main.addView(view);
    }
}