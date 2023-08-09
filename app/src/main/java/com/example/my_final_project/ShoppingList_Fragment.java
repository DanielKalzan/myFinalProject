package com.example.my_final_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ShoppingList_Fragment extends Fragment {

    GridView gv;
    ArrayList<getItem> arrayList;
    ItemAdp adp = null;
    OrdersDB ordersDB;
    OrderHistory history;
    int counter = 0;

    TextView tv_price, tv_Taxes, tv_total;
    Button endSopping;

    Dialog ordersDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list_, container, false);


        HomePage.notificationCounter = 0;
        HomePage.counter(HomePage.notificationCounter,HomePage.navigationView, R.id.nav_shopp);

        gv = view.findViewById(R.id.gridView);
        tv_price = view.findViewById(R.id.tv_price);
        tv_Taxes = view.findViewById(R.id.tv_Taxes);
        tv_total = view.findViewById(R.id.tv_total);
        endSopping = view.findViewById(R.id.end_btn);


        history = new OrderHistory(getContext(), HomePage.getNaem + "historyOrder.db");
        ordersDialog = new Dialog(getContext());


        try {
            ordersDB = new OrdersDB(getContext(), HomePage.getNaem + "order.db");

            arrayList = new ArrayList<>();

            adp = new ItemAdp(getContext(), R.layout.layout_for_listitem, arrayList);
            gv.setAdapter(adp);

            Cursor cursor = ordersDB.getData("SELECT * FROM Orders");
            arrayList.clear();

            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int price = cursor.getInt(2);
                int Unit = cursor.getInt(3);
                counter += price;
                byte[] image = cursor.getBlob(4);

                arrayList.add(new getItem(id, name, price, Unit, image));

            }

            double taxes = counter * 0.17;

            tv_price.setText(String.valueOf(counter));
            // ##.##
            tv_Taxes.setText(String.valueOf((float) Math.round(taxes * 100) / 100));
            tv_total.setText(String.valueOf(counter + taxes));


            adp.notifyDataSetChanged();

        } catch (Exception e) {

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        double taxes = counter * 0.17;


        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                Cursor cursor = ordersDB.getData("SELECT * FROM Orders");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (cursor.moveToNext()) {
                    arrID.add(cursor.getInt(0));
                }

                showDialogDelete(arrID.get(position));

                return true;
            }
        });

        endSopping.setOnClickListener(v -> {
            openSheetDialog(String.valueOf(counter + taxes));
            //openEndSopD(tv_price.getText().toString());
        });

        return view;
    }

    private String showDate() {

        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public String time() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private void openEndSopD(String price) {

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getContext());
        dialogDelete.setTitle("סיום קניה!!!");
        dialogDelete.setMessage("אתה בטוח שאתה רוצה לסיים את הקנייה??");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> {

            Cursor cursor = ordersDB.getData("SELECT * FROM Orders");
            ArrayList<Integer> arrID = new ArrayList<Integer>();
            while (cursor.moveToNext()) {
                arrID.add(cursor.getInt(0));
            }

            if (arrID.size() >= 1) {
                try {

                    int i = 0;
                    while (i < arrID.size()) {
                        ordersDB.deleteProduct(String.valueOf(arrID.get(i)));
                        i++;
                    }

                    history.insertData(price, showDate(), time());

                    Toast.makeText(getContext(), "ההזמנה יצאה לדרך", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            } else
                Toast.makeText(getContext(), "ההזמנה קטנה מדי", Toast.LENGTH_SHORT).show();


            UpdateList();
        });
        dialogDelete.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        dialogDelete.show();


    }

    public void openSheetDialog(String finalPrice){

        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext());
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setContentView(R.layout.seet_layout);

        TextView tp = sheetDialog.findViewById(R.id.totalPrice);
        tp.setText(finalPrice);

        Cursor cursor = ordersDB.getData("SELECT * FROM Orders");
        ArrayList<Integer> arrID = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            arrID.add(cursor.getInt(0));
        }

        CardForm cardForm = sheetDialog.findViewById(R.id.card_form);


        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .setup((Activity) getContext());


        Button buy = sheetDialog.findViewById(R.id.btnBuy);
        buy.setOnClickListener(view -> {

            if (cardForm.isValid()) {

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setTitle("אישור לפני שמסיימים");
                        alertBuilder.setMessage("מספר כרטיס: " + cardForm.getCardNumber() + "\n" +
                                " תוקף: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                                " CVV: " + cardForm.getCvv() + "\n" +
                                "מיקוד: " + cardForm.getPostalCode() + "\n" +
                                "מספר טלפון: " + cardForm.getMobileNumber());
                        alertBuilder.setPositiveButton("סיים קנייה", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();



                                if (arrID.size() >= 1) {
                                    try {

                                        int a = 0;
                                        while (a < arrID.size()) {
                                            ordersDB.deleteProduct(String.valueOf(arrID.get(a)));
                                            a++;
                                        }

                                        history.insertData(finalPrice, showDate(), time());
                                        HomePage.notificationCounter++;
                                        HomePage.counter(HomePage.notificationCounter,HomePage.navigationView, R.id.nav_list);

                                        Toast.makeText(getContext(), "תודה על הרכישה ההזמנה יצאה לדרך", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {

                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                } else
                                    Toast.makeText(getContext(), "ההזמנה קטנה מדי", Toast.LENGTH_SHORT).show();


                                UpdateList();


                                sheetDialog.cancel();
                            }
                        });
                        alertBuilder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "פעולת הרכישה התבטלה", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();

                            }
                        });
                        AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.show();





            }else {
                Toast.makeText(getContext(), "בבקשה תמלה את כל הפרטים", Toast.LENGTH_LONG).show();

            }

        });

        if (arrID.size() >1 )
              sheetDialog.show();
        else
            Toast.makeText(getContext(), "ההזמנה קטנה מדי", Toast.LENGTH_SHORT).show();


    }

    public void showDialogDelete(int id) {

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(getContext());
        dialogDelete.setTitle("אזהרה!!!");
        dialogDelete.setMessage("אתה בטוח שאתה רוצה לימחוק פריט זה");
        dialogDelete.setPositiveButton("אישור", (dialog, which) -> {

            try {
                ordersDB.deleteProduct(String.valueOf(id));
                Toast.makeText(getContext(), "הפריט נמחק בהצלחה", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            UpdateList();
        });
        dialogDelete.setNegativeButton("ביטול", (dialog, which) -> dialog.dismiss());

        dialogDelete.show();

    }


    private void UpdateList() {

        int c = 0;

        Cursor cursor = ordersDB.getData("SELECT * FROM Orders");
        arrayList.clear();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int price = cursor.getInt(2);
            int Unit = cursor.getInt(3);
            c += price;
            byte[] image = cursor.getBlob(4);

            arrayList.add(new getItem(id, name, price, Unit, image));
        }

        double taxes = c * 0.17;

        tv_price.setText(String.valueOf(c));
        tv_Taxes.setText(String.valueOf((float) Math.round(taxes * 100) / 100));
        tv_total.setText(String.valueOf(c + taxes));

        adp.notifyDataSetChanged();
    }

}