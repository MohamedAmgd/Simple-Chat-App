package com.mohamedamgd.chatapp.ui;

/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
