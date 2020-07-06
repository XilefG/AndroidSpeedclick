package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tutorial extends AppCompatActivity {

    public LocalDatabase data;
    public ArrayList<String> usernames = new ArrayList<>();
    public DatabaseReference highscoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        data = new LocalDatabase(this);
        checkAvailableUsername();
    }

    public void checkAvailableUsername() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    String getData = data.child("username").getValue(String.class);
                    usernames.add(getData);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void chooseUser(View v) {
        final Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.username_ask, null);
        final EditText usernameEdit = view.findViewById(R.id.usernameText);
        Button selectBtn = view.findViewById(R.id.submitButton);
        builder.setView(view);
        final AlertDialog aD = builder.create();

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usernameEdit.getText().toString().isEmpty()) {
                    if(!usernames.contains(usernameEdit.getText().toString()) || usernames.isEmpty()) {
                        data.addUser(usernameEdit.getText().toString());
                        highscoreDB = FirebaseDatabase.getInstance().getReference("users");
                        Toast.makeText(Tutorial.this, "Submitted", Toast.LENGTH_SHORT).show();
                        aD.dismiss();
                        aD.onBackPressed();
                        String username = data.getUsername();
                        OnlineUsername storeUsername = new OnlineUsername(username);
                        highscoreDB.push().setValue(storeUsername);
                        startActivity(intent);
                    } else Toast.makeText(Tutorial.this, "Username taken", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aD.show();
    }
}
