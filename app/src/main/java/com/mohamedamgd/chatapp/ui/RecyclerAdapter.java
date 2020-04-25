package com.mohamedamgd.chatapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedamgd.chatapp.models.Message;
import com.mohamedamgd.chatapp.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private String TAG = getClass().getSimpleName();
    private ArrayList<Message> messages;

    RecyclerAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getUserId().equals(MainActivity.mAuth.getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sent_message_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.received_message_item, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bindToMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
