package com.example.ppstart;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class business_profile_text extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView name;
    private TextView info;
    private String business_name;
    private String information;

    public business_profile_text() {
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
        View view = inflater.inflate(R.layout.fragment_business_profile_text, container, false);
        initViews(view);

        Bundle main_fragment_bundle = this.getArguments();
        if(main_fragment_bundle != null){
            business_name = main_fragment_bundle.getString("name");
            information = main_fragment_bundle.getString("info");
        }

        name.setText(business_name);
        info.setText(information);

        return view;
    }

    //initialize UI elements
    private void initViews(View view){
        name = view.findViewById(R.id.business_name_text);
        info = view.findViewById(R.id.about_hours_faq);
    }

}