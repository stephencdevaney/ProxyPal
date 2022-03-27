package com.example.ppstart;

import android.content.Context;
import android.content.Intent;
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

public class BrowseProfilesAdapter extends RecyclerView.Adapter<BrowseProfilesAdapter.ViewHolder>{



    private Context context;
    private ArrayList<Profile> browsable_profiles = new ArrayList<>();

    public BrowseProfilesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_profiles_rec_view_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .asBitmap()
                .load(browsable_profiles.get(position).getProfile_avatar_image())
                .into(holder.browse_rec_view_avatar);

        holder.browse_rec_view_name.setText(browsable_profiles.get(position).getBusiness_name());


        //for opening business profile when the profile avatar is tapped on
        holder.browse_rec_view_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Business_Profile_Activity.class);
                Bundle profile_bundle = new Bundle();
                profile_bundle.putInt("owner_id", browsable_profiles.get(position).getOwner_id());
                intent.putExtra("profile_bundle", profile_bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return browsable_profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView browse_rec_view_avatar;
        private TextView browse_rec_view_name;
        private MaterialCardView browse_rec_view_parent;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);


            browse_rec_view_avatar = itemView.findViewById(R.id.browse_rec_view_avatar);
            browse_rec_view_name = itemView.findViewById(R.id.browse_rec_view_name);
            browse_rec_view_parent = itemView.findViewById(R.id.browse_rec_view_parent);
        }
    }

    public void setBrowsable_profiles(ArrayList<Profile> browsable_profiles) {
        this.browsable_profiles = browsable_profiles;
        notifyDataSetChanged();
    }
}
