package com.mohamedamgd.chatapp.ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamedamgd.chatapp.R;
import com.mohamedamgd.chatapp.models.Message;

class MessageViewHolder extends RecyclerView.ViewHolder {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ImageView mImageView;
    private TextView mAuthor;
    private TextView mBody;
    private TextView mSentIn;

    MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        mImageView = itemView.findViewById(R.id.image_message_profile);
        mAuthor = itemView.findViewById(R.id.author_field);
        mBody = itemView.findViewById(R.id.body_field);
        mSentIn = itemView.findViewById(R.id.message_time);
    }

    void bindToMessage(Message message) {
        if (mAuthor != null) {
            mAuthor.setText(message.getAuthor());
        }
        mBody.setText(message.getBody());
        mSentIn.setText(DateUtils.formatDateTime(mContext, message.getSentIn()
                , DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL));
    }
}
