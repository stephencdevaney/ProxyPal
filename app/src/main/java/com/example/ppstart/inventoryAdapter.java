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
import android.widget.Button;
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

    /*public inventoryAdapter(Context context) {
        this.context = context;
    }*/
    public inventoryAdapter(ArrayList<item> itemList){
        this.items = itemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //private TextView inventory_business_name;
        //private MaterialCardView inventoryAdapterParent;
        private TextView itemName;
        private TextView itemPrice;
        private Button add;
        private Button delete;
        private MaterialCardView invCard;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            //inventoryAdapterParent = itemView.findViewById(R.id.inventory_item_parent);
            //inventory_business_name = itemView.findViewById(R.id.inventory_business_name);
            itemName = itemView.findViewById(R.id.inventory_item_name);
            itemPrice = itemView.findViewById(R.id.inventory_price);
            add = itemView.findViewById(R.id.inventory_add);
            delete = itemView.findViewById(R.id.inventory_delete);
            invCard = itemView.findViewById(R.id.inventory_materialCard);
        }
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
        //databaseHelper = new DatabaseHelper(context);
        //db = databaseHelper.getReadableDatabase();

        //holder.inventory_business_name.setText(business_name);
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText("$" + items.get(position).getPrice());

            //for opening business profile when the profile avatar is tapped on
            holder.invCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Promo_Edit_Activity.class);
                    Bundle promo_edit_bundle = new Bundle();
                    promo_edit_bundle.putString("item_name", items.get(holder.getAbsoluteAdapterPosition()).getName());
                    promo_edit_bundle.putInt("store_id", items.get(holder.getAbsoluteAdapterPosition()).getOwnerID());
                    promo_edit_bundle.putInt("item_id", items.get(holder.getAbsoluteAdapterPosition()).getItemNumber());
                    promo_edit_bundle.putInt("owner_id", owner_id);
                    promo_edit_bundle.putString("mode", "add");
                    intent.putExtra("promo_edit_bundle", promo_edit_bundle);
                    view.getContext().startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void setInventory(ArrayList<item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void passUserInfo(int id, String business_name){
        this.owner_id = id;
        this.business_name = business_name;
    }
}
