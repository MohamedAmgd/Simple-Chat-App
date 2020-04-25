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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mohamedamgd.chatapp.models.Message;
import com.mohamedamgd.chatapp.R;
import com.mohamedamgd.chatapp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static FirebaseAuth mAuth;
    private String TAG = getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Button mSendDataButton;
    private TextView mInputData;
    private DatabaseReference myRef;

    private ArrayList<Message> messages = new ArrayList<>();

    private boolean isBackClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (!checkUser(mAuth.getCurrentUser())) {
            moveToLogin();
        }

        myRef = FirebaseDatabase.getInstance().getReference("messages");

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mAdapter = new RecyclerAdapter(messages);

        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        mInputData = findViewById(R.id.input_data);

        mSendDataButton = findViewById(R.id.send_data);
        mSendDataButton.setOnClickListener(this);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Message message = item.getValue(Message.class);
                    messages.add(message);
                }
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, error.toException().toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_data) {
            if (!checkUser(mAuth.getCurrentUser())) {
                moveToLogin();
            } else {
                Message message = new Message(mAuth.getCurrentUser().getUid()
                        , mAuth.getCurrentUser().getDisplayName()
                        , mInputData.getText().toString()
                        , Calendar.getInstance().getTimeInMillis());
                mInputData.setText("");
                // Write a message to the database
                String key = myRef.push().getKey();
                myRef.child(key).setValue(message);
            }
        }
    }

    boolean checkUser(FirebaseUser user) {
        return user != null;
    }

    void moveToLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            moveToLogin();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackClicked) {
            super.onBackPressed();
            finish();

        } else {
            Toast.makeText(this
                    , getString(R.string.back_again_to_exit)
                    , Toast.LENGTH_SHORT).show();

            isBackClicked = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackClicked = false;
                }
            }, 2000);
        }

    }
}
