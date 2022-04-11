package com.example.ppstart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiscountsPromosAdapter extends RecyclerView.Adapter<DiscountsPromosAdapter.PromosViewHolder> {

    private ArrayList<Promo> promosList = new ArrayList<>();

    public DiscountsPromosAdapter(ArrayList<Promo> promosList){
        this.promosList = promosList;
    }

    public class PromosViewHolder extends RecyclerView.ViewHolder{
        private TextView storeName;
        private TextView itemName;
        private TextView itemDesc;
        private ImageView itemImage;


        public PromosViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.dp_store_tv);
            itemName = itemView.findViewById(R.id.dp_itemName_tv);
            itemDesc = itemView.findViewById(R.id.dp_itemDesc_tv);
            itemImage = itemView.findViewById(R.id.dp_itemImage_iv);
        }
    }

    @NonNull
    @Override
    public PromosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View promosView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discounts_promos_view_layout, parent, false);
        return new PromosViewHolder(promosView);
    }

    @Override
    public void onBindViewHolder(@NonNull PromosViewHolder holder, int position) {
        holder.storeName.setText(promosList.get(position).getStore_name());
        holder.itemName.setText(promosList.get(position).getItem_name());
        holder.itemDesc.setText(promosList.get(position).getDp_desc());
        if(promosList.get(position).getItem_image() != null)
            holder.itemImage.setImageBitmap(DbBitmapUtility.getImage(promosList.get(position).getItem_image()));
        else
            holder.itemImage.setImageBitmap(null);

    }

    @Override
    public int getItemCount() {
        return promosList.size();
    }

}
