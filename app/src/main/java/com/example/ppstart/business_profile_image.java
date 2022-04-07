package com.example.ppstart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class business_profile_image extends Fragment {

    // the fragment initialization parameters
    private TextView name;
    private TextView noInfo;
    private ImageView image;
    private String business_name;
    private String information;


    public business_profile_image() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_profile_image, container, false);
        initViews(view);

        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            business_name = main_fragment_bundle.getString("name");
            information = main_fragment_bundle.getString("info");
        }

        name.setText(business_name);
        if(information.equals("NULL")){
            image.setVisibility(View.GONE);
            noInfo.setVisibility(View.VISIBLE);
        }
        else{
            image.setVisibility(View.VISIBLE);
            noInfo.setVisibility(View.GONE);
            Glide.with(this).asBitmap()
                    .load(information)
                    .into(image);
        }

        return view;
    }

    //initialize UI elements
    private void initViews(View view){
        name = view.findViewById(R.id.business_name_image);
        image = view.findViewById(R.id.layout);
        noInfo = view.findViewById(R.id.not_provided);
    }
}