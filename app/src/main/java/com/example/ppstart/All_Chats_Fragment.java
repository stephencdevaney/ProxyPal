//CREATED BY BLAKE

package com.example.ppstart;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class All_Chats_Fragment extends Fragment {

    private int supporter_id, owner_id;
    private String supporter_username, owner_username, business_name;
    private String viewer;

    private RecyclerView all_chats_rec_view;

    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter<AllChatsClass, FRA_ViewHolder> all_chats_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_chats_fragment_layout, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            supporter_id = bundle.getInt("supporter_id");
            supporter_username = bundle.getString("supporter_username");
            owner_id = bundle.getInt("owner_id");
            owner_username = bundle.getString("owner_username");
        }


        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query;

        /* make this better
        if(supporter_username == null){
            System.out.println("This is an owner account");
            viewer = "owner";
            query = firebaseFirestore.collection("Chats").whereEqualTo("owner_id", owner_id);
        }
        else{
            System.out.println("This is a supporter account");
            viewer = "supporter";

        }

         */




        all_chats_rec_view =  view.findViewById(R.id.all_chats_rec_view);



        //pre-set for demo
        supporter_id = 115;
        supporter_username = "test1";

        query = firebaseFirestore.collection("Chats").whereEqualTo("supporter_id", supporter_id);

        FirestoreRecyclerOptions<AllChatsClass> all_chats = new FirestoreRecyclerOptions.Builder<AllChatsClass>().setQuery(query, AllChatsClass.class).build();

        all_chats_adapter = new FirestoreRecyclerAdapter<AllChatsClass, FRA_ViewHolder>(all_chats) {
            @Override
            protected void onBindViewHolder(@NonNull FRA_ViewHolder holder, int position, @NonNull AllChatsClass model) {

                //if(viewer == "supporter"){
                    System.out.println("#####################################################" + model.getBusiness_name());
                    holder.username_txt.setText(model.getBusiness_name());
               // }else if(viewer == "owner"){
                  //  holder.username_txt.setText(model.getSupporter_username());
               // }

                String uri = model.getBusiness_pic();
                Glide.with(getContext()).asBitmap().load(uri).into(holder.profile_pic);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        Intent to_individual_chat = new Intent(getActivity(), Individual_Chats_Activity.class);
                        Bundle chats_bundle = new Bundle();

                        //if(viewer == "supporter"){
                            chats_bundle.putInt("supporter_id", supporter_id);
                            chats_bundle.putInt("owner_id", model.getOwner_id());
                            chats_bundle.putString("owner_username", model.getOwner_username());
                            chats_bundle.putString("supporter_username", supporter_username);
                            //supporter_bundle.putString("profile_pic", model.getImage());
                            chats_bundle.putString("viewer", "supporter");
                            GetLoggedInID.logged_in_id = supporter_id;
                            to_individual_chat.putExtra("chats_bundle", chats_bundle);
                            startActivity(to_individual_chat);

                            /*

                        }else if (viewer == "owner"){
                            chats_bundle.putInt("supporter_id", owner_id);
                            chats_bundle.putInt("owner_id", model.getSupporter_id());
                            //supporter_bundle.putString("profile_pic", model.getImage());
                            chats_bundle.putString("supporter_username", model.getSupporter_username());
                            chats_bundle.putString("owner_username", owner_username);
                            chats_bundle.putString("viewer", "owner");
                            GetLoggedInID.logged_in_id = owner_id;
                            to_individual_chat.putExtra("chats_bundle", chats_bundle);
                            startActivity(to_individual_chat);
                        }

                             */

                    }
                });



            }

            @NonNull
            @Override
            public FRA_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_chats_rec_view_layout, parent,false);
                return new FRA_ViewHolder(view);

            }
        };



        all_chats_rec_view.setHasFixedSize(true);
        // all_chats_rec_view.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //USE WrapContentLinearLayoutManager CLASS TO FIX THE BACK-BUTTON CRASH BUG
        all_chats_rec_view.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        all_chats_rec_view.setAdapter(all_chats_adapter);


        return view;
    }




    public class FRA_ViewHolder extends RecyclerView.ViewHolder{
        private TextView username_txt;
        private ImageView profile_pic;

        public FRA_ViewHolder(@NonNull View itemView) {
            super(itemView);
            username_txt = itemView.findViewById(R.id.username_txt);
            profile_pic = itemView.findViewById(R.id.profile_pic);
        }

    }


    //this ensures the database works in real-time
    @Override
    public void onStart() {
        super.onStart();
        all_chats_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(all_chats_adapter!=null)
        {
            all_chats_adapter.stopListening();
        }
    }
}
