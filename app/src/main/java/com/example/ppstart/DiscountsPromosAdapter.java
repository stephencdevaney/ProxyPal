package com.example.ppstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class DiscountsPromosAdapter extends RecyclerView.Adapter<DiscountsPromosAdapter.PromosViewHolder> {

    private ArrayList<Promo> promosList = new ArrayList<>();
    private int supporter_id;
    private String supporter_username;
    private int owner_id;
    private String owner_username;

    public DiscountsPromosAdapter(ArrayList<Promo> promosList){
        this.promosList = promosList;
    }

    public class PromosViewHolder extends RecyclerView.ViewHolder{
        private TextView storeName;
        private TextView itemName;
        private TextView itemDesc;
        private ImageView itemImage;
        private MaterialCardView promoCard;


        public PromosViewHolder(@NonNull View itemView) { //Get the information needed for the display
            super(itemView);

            storeName = itemView.findViewById(R.id.dp_store_tv);
            itemName = itemView.findViewById(R.id.dp_itemName_tv);
            itemDesc = itemView.findViewById(R.id.dp_itemDesc_tv);
            itemImage = itemView.findViewById(R.id.dp_itemImage_iv);
            promoCard = itemView.findViewById(R.id.dp_fullCard_mc);
        }
    }

    @NonNull
    @Override
    public PromosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View promosView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discounts_promos_view_layout, parent, false);
        return new PromosViewHolder(promosView);
    }

    @Override
    public void onBindViewHolder(@NonNull PromosViewHolder holder, int position) { //Interfaces with the UI to display the proper information
        holder.storeName.setText(promosList.get(position).getStore_name());
        holder.itemName.setText(promosList.get(position).getItem_name());
        holder.itemDesc.setText(promosList.get(position).getDp_desc());
        Glide.with(holder.itemView.getContext()).load(promosList.get(position).getItem_image()).placeholder(R.drawable.ic_action_name).into(holder.itemImage);

        if(supporter_id > 0) {
            holder.promoCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(@NonNull View view) {
                    Intent intent = new Intent(view.getContext(), Business_Profile_Activity.class);
                    Bundle profile_bundle = new Bundle();
                    profile_bundle.putInt("owner_id", promosList.get(holder.getAbsoluteAdapterPosition()).getStore_id());
                    profile_bundle.putInt("supporter_id", supporter_id);
                    profile_bundle.putString("supporter_username", supporter_username);
                    intent.putExtra("profile_bundle", profile_bundle);
                    view.getContext().startActivity(intent);
                }
            });
        }
        if(owner_id > 0){
            holder.promoCard.setOnClickListener(new View.OnClickListener() {
                public void onClick(@NonNull View view) {
                    Intent intent = new Intent(view.getContext(), Promo_Edit_Activity.class);
                    Bundle promo_edit_bundle = new Bundle();
                    promo_edit_bundle.putString("item_name", promosList.get(holder.getAbsoluteAdapterPosition()).getItem_name());
                    promo_edit_bundle.putString("promo_desc", promosList.get(holder.getAbsoluteAdapterPosition()).getDp_desc());
                    promo_edit_bundle.putInt("promo_id", promosList.get(holder.getAbsoluteAdapterPosition()).getDp_id());
                    promo_edit_bundle.putInt("store_id", promosList.get(holder.getAbsoluteAdapterPosition()).getStore_id());
                    promo_edit_bundle.putInt("item_id", promosList.get(holder.getAbsoluteAdapterPosition()).getItem_id());
                    promo_edit_bundle.putInt("owner_id", owner_id);
                    promo_edit_bundle.putString("mode", "edit");
                    intent.putExtra("promo_edit_bundle", promo_edit_bundle);
                    view.getContext().startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return promosList.size();
    }

    public void passUserInfo(int id, String username){
        this.supporter_id = id;
        this.supporter_username = username;
    }
    public void passOwnerInfo(int id, String username){
        this.owner_id = id;
        this.owner_username = username;
    }
}
