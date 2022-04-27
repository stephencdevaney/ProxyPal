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

public class ListViewAdapter extends ArrayAdapter {
    ArrayList<String> list;
    Context context;

    //set context for adapter
    public ListViewAdapter(Context context, ArrayList<String> items){
        super(context, R.layout.list_row, items);
        this.context =context;
        list = items;
    }

    @NonNull
    @Override
    //overides when view used
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView ==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //used in xml formatting, and sets data types
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            TextView name = convertView.findViewById(R.id.name);
            ImageView remove = convertView.findViewById(R.id.remove);
            TextView number = convertView.findViewById(R.id.number);

            //inumerate the item list starting a index 1
            number.setText(position+1 +".");
            name.setText(list.get(position));

            //call removeItem function when pressed to remove item from list
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Shopping_List_Activity.removeItem(position);
                }
            });
        }
        return convertView;
    }
}
