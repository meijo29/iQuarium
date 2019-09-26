package com.example.iquarium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.database.sqlite.SQLiteOpenHelper;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;


    private Button fishList,myAqButton,chatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);


        fishList = (Button) findViewById(R.id.fishButton);
        myAqButton = (Button) findViewById(R.id.myAquaBttn);
        chatbot = (Button) findViewById(R.id.chatBot);
        fishList.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            openFishListPage();
                                        }
                                    }

        );

        myAqButton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              openmyAquariumPage();
                                          }
                                      }
        );

        openChatBot();

    }


    public void openmyAquariumPage()
    {
        Intent intent = new Intent(this, MyAquarium.class);
        startActivity(intent);

    }

    public void openFishListPage()
    {
        Intent intent = new Intent(this, Main2ActivityFish.class);
        startActivity(intent);

    }

    public void openChatBot()
    {
        final Intent intent = new Intent(this, Chatbot.class);
        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

}
