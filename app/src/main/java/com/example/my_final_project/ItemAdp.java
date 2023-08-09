package com.example.my_final_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdp extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final ArrayList<getItem> arrayList;
    public ItemAdp(Context context, int layout, ArrayList<getItem> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    private static class ViewAOlder{
        ImageView image;
        TextView name,price,unit;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        ViewAOlder older = new ViewAOlder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            older.name = row.findViewById(R.id.listItemName);
            older.price = row.findViewById(R.id.listPrice);
            older.unit = row.findViewById(R.id.listUnit);
            older.image = row.findViewById(R.id.imageForList);

            row.setTag(older);
        }else {
            older = (ViewAOlder) row.getTag();
        }
        getItem item = arrayList.get(position);

        older.name.setText(item.getName());
        String itemPrice = String.valueOf(item.getPrice());
        older.price.setText(itemPrice);
        older.unit.setText(String.valueOf(item.getUnit()));

        byte[] itemImage = item.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(itemImage,0,itemImage.length);
        older.image.setImageBitmap(bitmap);

        return row;
    }
}
