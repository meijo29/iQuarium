package com.example.iquarium;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AquariumSettings extends AppCompatActivity {


    private TextView aqname;
    private Integer selectedID;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquarium_settings);
        myDb = new DatabaseHelper(this);
        aqname = findViewById(R.id.aqName);

        Intent receivedintent = getIntent();
        selectedID = receivedintent.getIntExtra("id", -1);

        Cursor cursor = myDb.getAqData(selectedID);
        while (cursor.moveToNext())
        {

            aqname.setText(cursor.getString(1));

        }

    }


    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, Aquarium.class);
        intent.putExtra("id", selectedID);
        startActivity(intent);

    }
}
