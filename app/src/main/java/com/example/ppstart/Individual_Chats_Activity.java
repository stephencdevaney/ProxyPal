//CREATED BY BLAKE

package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Individual_Chats_Activity extends AppCompatActivity {

    private String supporter_username, owner_username;
    private int supporter_id, owner_id;
    private int viewer_id, viewed_id;
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

    //private FirebaseAuth firebaseAuth;

    private String viewer_room, viewed_room;

    private String viewer_username, viewed_username;


    private RecyclerView individual_chats_rec_view;

    /*
    String current_time;
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

        messages_array_list=new ArrayList<>();
        individual_chats_rec_view = findViewById(R.id.individual_chats_rec_view);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        individual_chats_rec_view.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(Individual_Chats_Activity.this, messages_array_list);
        individual_chats_rec_view.setAdapter(messagesAdapter);



/*
        Intent to_individual_chat = new Intent(getActivity(), Individual_Chats_Activity.class);
                        Bundle chats_bundle = new Bundle();

                        if(viewer_id == supporter_id){
                            chats_bundle.putInt("supporter_id", supporter_id);
                            chats_bundle.putInt("owner_id", model.getOwner_id());
                            chats_bundle.putString("owner_username", model.getOwner_username());
                            chats_bundle.putString("supporter_username", supporter_username);
                            chats_bundle.putInt("viewer_id", supporter_id);
                            chats_bundle.putInt("viewed_id", owner_id);
                            to_individual_chat.putExtra("chats_bundle", chats_bundle);
                            startActivity(to_individual_chat);



                        }else if (viewer_id == owner_id){
                            chats_bundle.putInt("supporter_id", owner_id);
                            chats_bundle.putInt("owner_id", model.getSupporter_id());
                            chats_bundle.putString("supporter_username", model.getSupporter_username());
                            chats_bundle.putString("owner_username", owner_username);
                            chats_bundle.putInt("viewer_id", owner_id);
                            chats_bundle.putInt("viewed_id", supporter_id);
                            to_individual_chat.putExtra("chats_bundle", chats_bundle);
                            startActivity(to_individual_chat);
                        }

 */

        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle = intent.getBundleExtra("chats_bundle");
            if(bundle != null){
                supporter_username = bundle.getString("supporter_username");
                supporter_id = bundle.getInt("supporter_id");
                owner_id = bundle.getInt("owner_id");
                owner_username = bundle.getString("owner_username");
                viewer_username = bundle.getString("viewer_username");
                viewed_username = bundle.getString("viewed_username");
            }
        }


       // firebaseAuth=FirebaseAuth.getInstance();

        //firebaseDatabase=FirebaseDatabase.getInstance();

        // calendar=Calendar.getInstance();
        // simpleDateFormat=new SimpleDateFormat("hh:mm a");








        //sender_room = "123456";
        //?
        if(viewer_username.equals(supporter_username)){
            viewer_room = supporter_username + owner_username;
            viewed_room = owner_username + supporter_username;
        }else if(viewer_username.equals(owner_username)){
            viewer_room = owner_username + supporter_username;
            viewed_room = supporter_username + owner_username;
        }



        System.out.println("_____________+++++++++++++++__________________" + owner_id + " " + viewer_room  + " " + viewed_room);

        //for testing:
       //viewer_room: James_SmithBobs_Antiques viewed_room: Bobs_AntiquesJames_Smith

        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("viewer_room", viewer_room).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setMessage(doc.get("message").toString());
                    //message.setSender(doc.get("sender").toString());
                    message.setViewer_username(viewer_username);
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt((doc.get("owner_id").toString())));
                    message.setSupporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                    messages_array_list.add(message);

                }
                messagesAdapter.notifyDataSetChanged();
            }
        });


        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("viewer_room", viewed_room).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setMessage(doc.get("message").toString());
                    //message.setSender(doc.get("sender").toString());
                    //message.setViewer_id(viewer_id);
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt(doc.get("owner_id").toString()));
                    message.setSupporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                    messages_array_list.add(message);

                }
                messagesAdapter.notifyDataSetChanged();
            }
        });





        send_message_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entered_message = msg_edt_txt.getText().toString();
                if(entered_message.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter a message to send.",Toast.LENGTH_SHORT).show();
                }
                else {
                   // Date date=new Date();
                    //currenttime=simpleDateFormat.format(calendar.getTime());


                    //add business to the Favorited_Profiles collection of Firestore database  -Blake
                    Map<String, Object> message1 = new HashMap<>();
                    message1.put("message", entered_message);
                    message1.put("owner_id", owner_id);
                    message1.put("supporter_id", supporter_id);
                    message1.put("owner_username", owner_username);
                    message1.put("supporter_username", supporter_username);
                    message1.put("viewer_room", viewer_room);
                    message1.put("viewed_room", viewed_room);

                    /*
                    Map<String, Object> message2 = new HashMap<>();
                    message2.put("message", entered_message);
                    message2.put("owner_id", owner_id);
                    message2.put("supporter_id", supporter_id);
                    message2.put("owner_username", owner_username);
                    message2.put("supporter_username", supporter_username);
                    message2.put("viewer_room", viewed_room);
                    message2.put("viewed_room", viewer_room);

                     */

                    FirebaseFirestore.getInstance().collection("Messages").add(message1).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Individual_Chats_Activity.this, "Message 2 Sent", Toast.LENGTH_SHORT).show();
                                messagesAdapter.notifyDataSetChanged();
                                recreate();

                            }
                        }
                    });

                    /*
                    FirebaseFirestore.getInstance().collection("Messages").add(message2).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Individual_Chats_Activity.this, "Message 1 Sent", Toast.LENGTH_SHORT).show();
                                messagesAdapter.notifyDataSetChanged();

                            }
                        }
                    });

                     */


                    msg_edt_txt.setText("");




                }





            }


        });










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