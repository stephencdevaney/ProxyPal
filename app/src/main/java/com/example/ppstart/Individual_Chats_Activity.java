//CREATED BY BLAKE

package com.example.ppstart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
        //individual_user_img_cv=findViewById(R.id.individual_user_img_cv);
        send_message_img_btn=findViewById(R.id.send_message_img_btn);
        individual_chat_toolbar=findViewById(R.id.individual_chat_toolbar);
        individual_username=findViewById(R.id.individual_username);
        //individual_prof_pic=findViewById(R.id.individual_prof_pic);

        messages_array_list=new ArrayList<>();
        individual_chats_rec_view = findViewById(R.id.individual_chats_rec_view);



        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(Individual_Chats_Activity.this, RecyclerView.VERTICAL, false);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
       messagesAdapter = new MessagesAdapter(Individual_Chats_Activity.this, messages_array_list);
       individual_chats_rec_view.setLayoutManager(wrapContentLinearLayoutManager);
       individual_chats_rec_view.setAdapter(messagesAdapter);





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



        if(viewer_username.equals(supporter_username)){
            viewer_room = supporter_username + owner_username;
            viewed_room = owner_username + supporter_username;
        }else if(viewer_username.equals(owner_username)){
            viewer_room = owner_username + supporter_username;
            viewed_room = supporter_username + owner_username;
        }



        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("viewer_room", viewer_room).orderBy("timestamp_ms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //messages_array_list.clear();
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setTimestamp_ms(Long.parseLong(doc.get("timestamp_ms").toString()));
                    message.setMessage(doc.get("message").toString());
                    message.setViewer_username(doc.get("viewer_username").toString());
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt((doc.get("owner_id").toString())));
                    message.setSupporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                    messages_array_list.add(message);

                }

                Collections.sort(messages_array_list);
                messagesAdapter.notifyDataSetChanged();
            }
        });



        FirebaseFirestore.getInstance().collection("Messages").whereEqualTo("viewer_room", viewed_room).orderBy("timestamp_ms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // messages_array_list.clear();
                for(QueryDocumentSnapshot doc : task.getResult()){
                    MessagesClass message = new MessagesClass();
                    message.setMessage(doc.get("message").toString());
                    message.setTimestamp_ms(Long.parseLong(doc.get("timestamp_ms").toString()));
                    message.setViewer_username(doc.get("viewer_username").toString());
                    message.setOwner_username(doc.get("owner_username").toString());
                    message.setSupporter_username(doc.get("supporter_username").toString());
                    message.setOwner_id(Integer.parseInt(doc.get("owner_id").toString()));
                    message.setSupporter_id(Integer.parseInt(doc.get("supporter_id").toString()));
                    messages_array_list.add(message);

                }
                Collections.sort(messages_array_list);
                messagesAdapter.notifyDataSetChanged();
            }
        });





        //Collections.sort(messages_array_list);
        //messagesAdapter.notifyDataSetChanged();









        send_message_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                entered_message = msg_edt_txt.getText().toString();
                if(entered_message.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter a message to send.",Toast.LENGTH_SHORT).show();
                }
                else {


                    Date date = new Date();
                    long timestamp_ms = date.getTime();


                    Map<String, Object> message = new HashMap<>();
                    message.put("timestamp_ms", timestamp_ms);
                    message.put("message", entered_message);
                    message.put("owner_id", owner_id);
                    message.put("supporter_id", supporter_id);
                    message.put("owner_username", owner_username);
                    message.put("supporter_username", supporter_username);
                    message.put("viewer_room", viewer_room);
                    message.put("viewed_room", viewed_room);
                    message.put("viewer_username", viewer_username);


                    FirebaseFirestore.getInstance().collection("Messages").add(message).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Individual_Chats_Activity.this, "Message 2 Sent", Toast.LENGTH_SHORT).show();
                                //Collections.sort(messages_array_list);
                                messagesAdapter.notifyDataSetChanged();
                                recreate();

                            }
                        }
                    });


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