package com.example.iquarium;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAquarium extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    DatabaseHelper myDb;
    private Button cbttn;
    private ImageButton backbttn;

    ListView listaquarium;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_aquarium);
        myDb = new DatabaseHelper(this);
    listaquarium = (ListView)findViewById(R.id.ListAquarium);
    cbttn = (Button)findViewById(R.id.button1);


      openCreateAquariumPage();
      populateListView();

    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


    public void openCreateAquariumPage()
    {
    cbttn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyAquarium.this, CreateAquarium.class);
            startActivity(intent);
        }
    });

    }


    private void populateListView()
    {

        Cursor data = myDb.getData();

        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext())
        {
            listData.add(data.getString(1));
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listaquarium.setAdapter(adapter);


        listaquarium.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = myDb.getAqID(name);
                int itemID = -1;
                int length = 0, width = 0, height = 0;
                while(data.moveToNext())
                {
                    itemID = data.getInt(0);
                    length = data.getInt(2);
                    width = data.getInt(3);
                    height = data.getInt(4);
                }
                if(itemID > -1)
                {
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent aquariumScreenIntent = new Intent(MyAquarium.this, Aquarium.class );
                    aquariumScreenIntent.putExtra("id", itemID);
                    aquariumScreenIntent.putExtra("name", name);
                    aquariumScreenIntent.putExtra("length", length);
                    aquariumScreenIntent.putExtra("width", width);
                    aquariumScreenIntent.putExtra("height", height);
                    startActivity(aquariumScreenIntent);
                }
                else
                {
                    toasMessage("No ID associated with that name");
                }
            }
        });


    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }



}