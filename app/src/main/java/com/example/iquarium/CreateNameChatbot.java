package com.example.iquarium;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateNameChatbot extends AppCompatActivity {


    DatabaseReference ref;
    Button createbttn,enterbttn;
    EditText nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_name_chatbot);

        ref = FirebaseDatabase.getInstance().getReference();

        nickname = findViewById(R.id.namecreated);
        enterbttn = findViewById(R.id.enterbttn);


       enterbttn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(CreateNameChatbot.this, Chatbot.class);
               intent.putExtra("nickname", nickname.getText().toString());
               startActivity(intent);

           }
       });

    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }


    private void readData()
    {

    }
}
