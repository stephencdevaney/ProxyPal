package com.example.ppstart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class inventoryAdapter extends RecyclerView.Adapter<inventoryAdapter.ViewHolder>{

    // database tools
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor profile_cursor;



    private Context context;
    private ArrayList<item> items = new ArrayList<>();
    int owner_id;
    String business_name;
    boolean edit;

    public inventoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item_fragment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // setup database
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getReadableDatabase();


        holder.inventory_business_name.setText(business_name);

            //for opening business profile when the profile avatar is tapped on
            holder.inventoryAdapterParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, inventory_list.class);
                    Bundle profile_bundle = new Bundle();
                    profile_bundle.putInt("owner_id", items.get(position).getOwnerId());
                    profile_bundle.putInt("supporter_id", owner_id);
                    intent.putExtra("profile_bundle", profile_bundle);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView inventory_business_name;
        private MaterialCardView inventoryAdapterParent;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            inventoryAdapterParent = itemView.findViewById(R.id.inventory_item_parent);
            inventory_business_name = itemView.findViewById(R.id.inventory_business_name);
        }
    }

    public void setInventory(ArrayList<item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void passUserInfo(int id, String business_name, boolean edit){
        this.owner_id = id;
        this.edit = edit;
        this.business_name = business_name;
    }
}
