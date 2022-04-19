//CREATED BY BLAKE

package com.example.ppstart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<MessagesClass> messages_array_list;

    private int ITEM_SEND = 1;
    private int ITEM_RECEIVE = 2;


    public MessagesAdapter(Context context, ArrayList<MessagesClass> messages_array_list) {
        this.context = context;
        this.messages_array_list = messages_array_list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        System.out.println("on create reached");
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_ind_chat_rec_layout, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_ind_chat_rec_layout, parent, false);
            return new RecieverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesClass messages = messages_array_list.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.message_sender.setText(messages.getMessage());
        }
        else{
            RecieverViewHolder viewHolder=(RecieverViewHolder)holder;
            viewHolder.message_receiver.setText(messages.getMessage());
        }

    }

    @Override
    public int getItemViewType(int position) {
        System.out.println("get view type reached");
        //Messages messages=messagesArrayList.get(position);
        //if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))

        //fix this
        MessagesClass messages = messages_array_list.get(position);
           // if(messages.getSender().equals("owner"))

                if(GetLoggedInID.logged_in_username.equals(messages.getViewer_username())){
                    return  ITEM_SEND;
                }else{
                    return ITEM_RECEIVE;
                }



            //else if(messages.getSender().equals("supporter")){

              //  if(GetLoggedInID.logged_in_id == messages.getOwner_id()){
                //    return  ITEM_SEND;
                //}else{
                  //  return ITEM_RECEIVE;
                //}
            //}else{
              //  return -1;
           // }

    }

    @Override
    public int getItemCount() {
        return messages_array_list.size();
    }

    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView message_sender;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message_sender = itemView.findViewById(R.id.message_sender);
        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder
    {

        TextView message_receiver;


        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            message_receiver = itemView.findViewById(R.id.message_receiver);
        }
    }



}
