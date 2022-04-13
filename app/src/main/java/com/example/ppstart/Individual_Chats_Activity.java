//CREATED BY BLAKE

package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Individual_Chats_Activity extends AppCompatActivity {

    private String supporter_username, owner_username;
    private int supporter_id, owner_id;
    private String message;
    private String sender;
    private String viewer;


    private FirebaseFirestore db;

    private EditText msg_edt_txt;
    private ImageButton send_message_img_btn;

    CardView individual_user_img_cv;
    androidx.appcompat.widget.Toolbar individual_chat_toolbar;
    ImageView individual_prof_pic;
    TextView individual_username;

    private String entered_message;
    private Intent intent;
    private String mrecievername,sendername,mrecieveruid,msenderuid;

    private FirebaseAuth firebaseAuth;

    private String senderroom, recieverroom;

    //private ImageButton mbackbuttonofspecificchat;

    private RecyclerView individual_chats_rec_view;

    /*
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

     */

    MessagesAdapter messagesAdapter;
    ArrayList<MessagesClass> messages_array_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_chats);

        db = FirebaseFirestore.getInstance();

        msg_edt_txt=findViewById(R.id.msg_edt_txt);
        individual_user_img_cv=findViewById(R.id.individual_user_img_cv);
        send_message_img_btn=findViewById(R.id.send_message_img_btn);
        individual_chat_toolbar=findViewById(R.id.individual_chat_toolbar);
        individual_username=findViewById(R.id.individual_username);
        individual_prof_pic=findViewById(R.id.individual_prof_pic);
        //mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);

        messages_array_list=new ArrayList<>();
        individual_chats_rec_view = findViewById(R.id.individual_chats_rec_view);

        /*
        all_chats_rec_view.setHasFixedSize(true);
        // all_chats_rec_view.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //USE WrapContentLinearLayoutManager CLASS TO FIX THE BACK-BUTTON CRASH BUG
        all_chats_rec_view.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        all_chats_rec_view.setAdapter(all_chats_adapter);

         */


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        individual_chats_rec_view.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(Individual_Chats_Activity.this, messages_array_list);
        individual_chats_rec_view.setAdapter(messagesAdapter);



/*
        Intent to_individual_chat = new Intent(getActivity(), Individual_Chats_Activity.class);
        Bundle chats_bundle = new Bundle();

        if(viewer == "supporter"){
            chats_bundle.putInt("supporter_id", supporter_id);
            chats_bundle.putInt("owner_id", model.getOwner_id());
            chats_bundle.putString("owner_username", model.getOwner_username());
            //supporter_bundle.putString("profile_pic", model.getImage());
            chats_bundle.putInt("viewer_id", supporter_id);
            GetLoggedInID.logged_in_id = supporter_id;
            to_individual_chat.putExtra("chats_bundle", chats_bundle);
            startActivity(to_individual_chat);


        }else if (viewer == "owner"){
            chats_bundle.putInt("supporter_id", owner_id);
            chats_bundle.putInt("owner_id", model.getSupporter_id());
            //supporter_bundle.putString("profile_pic", model.getImage());
            chats_bundle.putString("supporter_username", model.getSupporter_username());
            chats_bundle.putInt("viewer_id", owner_id);
            GetLoggedInID.logged_in_id = owner_id;
            to_individual_chat.putExtra("chats_bundle", chats_bundle);
            startActivity(to_individual_chat);

 */

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("chats_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
                owner_id = bundle.getInt("owner_id");
                owner_username = bundle.getString("owner_username");
                viewer = bundle.getString("viewer");
            }
        }


       // firebaseAuth=FirebaseAuth.getInstance();

        //firebaseDatabase=FirebaseDatabase.getInstance();

        // calendar=Calendar.getInstance();
        // simpleDateFormat=new SimpleDateFormat("hh:mm a");


        /*
        msenderuid = firebaseAuth.getUid();
        mrecieveruid=getIntent().getStringExtra("receiveruid");
        mrecievername=getIntent().getStringExtra("name");

         */


        //senderroom = "123456";
        System.out.println("DSSSSSSSFSDFSGSDFADAFSGSDFAWFADSC" + viewer);
        //?
        if(viewer.equals("supporter")){
            senderroom = String.valueOf(supporter_id) + String.valueOf(owner_id);
            System.out.println("DSSSSSSSFSDFSGSDFADAFSGSDFAWFADSC" + senderroom);
            recieverroom = String.valueOf(owner_id) + String.valueOf(supporter_id);
        }else if(viewer.equals("owner")){
            senderroom = String.valueOf(owner_id) + String.valueOf(supporter_id);
            recieverroom = String.valueOf(supporter_id) + String.valueOf(owner_id);
        }

        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("senderroom", senderroom).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setMessage(doc.get("message").toString());
                    message.setSender(doc.get("sender").toString());
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt((doc.get("owner_id").toString())));
                    message.setOwner_id(Integer.parseInt((doc.get("supporter_id").toString())));
                    messages_array_list.add(message);

                }
                //messagesAdapter.notifyDataSetChanged();
            }
        });

        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("receiverroom", senderroom).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setMessage(doc.get("message").toString());
                    message.setSender(doc.get("sender").toString());
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt((doc.get("owner_id").toString())));
                    message.setOwner_id(Integer.parseInt((doc.get("supporter_id").toString())));
                    messages_array_list.add(message);

                }
                messagesAdapter.notifyDataSetChanged();
            }
        });

        //.whereEqualTo("receiverroom", senderroom)


        /*
        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mnameofspecificuser.setText(mrecievername);
        String uri=intent.getStringExtra("imageuri");
        if(uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"null is recieved",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Picasso.get().load(uri).into(mimageviewofspecificuser);
        }


        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                }

                else

                {
                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    Messages messages=new Messages(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats")
                            .child(senderroom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(recieverroom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    mgetmessage.setText(null);




                }




            }
        });

         */




    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }

}