package com.example.ppstart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter_closest_business extends ArrayAdapter {

    ArrayList<String> list_items;
    ArrayList<Float> list_distances;
    Context context;

    public ListViewAdapter_closest_business(Context context, ArrayList<String> items, ArrayList<Float> distances){
        super(context, R.layout.store_list, items);
        this.context =context;
        list_items = items;
        list_distances = distances;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView ==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.store_list, null);
            TextView business_name = convertView.findViewById(R.id.business_name);
            TextView dist_val = convertView.findViewById(R.id.dist_val);
            TextView number = convertView.findViewById(R.id.number);


            number.setText(position +".");
            business_name.setText(list_items.get(position));
            dist_val.setText(Float.toString(list_distances.get(position)));
        }
        return convertView;
    }
}


