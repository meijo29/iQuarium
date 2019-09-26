package com.example.iquarium;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DecorListPage extends AppCompatActivity {


    private ImageButton homePage;
    ListView ListDecor;
    View view;
    int aqID;
    DatabaseHelper myDb;
    String aqName;
    private ArrayList<Integer> imgid = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor_list_page);
        myDb = new DatabaseHelper(this);
        Intent received = getIntent();
        ListDecor = (ListView) findViewById(R.id.decoration);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        homePage = findViewById(R.id.backBttn);
        boolean aquariumpage = false;
        ArrayList<Integer> fishID = new ArrayList<>();
        Integer aqId = 0; // just ignore
        decorImgID();
        aqID = received.getIntExtra("id", -1);
        aqName = received.getStringExtra("name");


        Cursor data = databaseAccess.getDecorData();
        ArrayList<String> arrayList = new ArrayList<>();
        while(data.moveToNext())
        {
            arrayList.add(data.getString(1));
         }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        CustomListView customListView = new CustomListView(this,arrayList,imgid, aquariumpage, fishID, aqId, 0, null);
        ListDecor.setAdapter(customListView);
        databaseAccess.close();


        addDecor();

    }


    public void decorImgID()
    {

        imgid.add(R.drawable.aqplant);

    }

    public void addDecor()
    {
        ListDecor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String selectedfish = adapterView.getItemAtPosition(i).toString();

            addConfirm(selectedfish);

        }
    });

    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    public void addConfirm(final String selectedDecor)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("ADD CONFIRMATION");
        builder.setMessage("Adding " + selectedDecor + "" + " to your aquarium");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                        databaseAccess.open();
                        Cursor data = databaseAccess.getDecorID(selectedDecor);

                        boolean decorAdded = false;

                        Cursor aqdata = myDb.getAqDecorData(aqID);
                        while(aqdata.moveToNext()){

                            if(aqdata.getString(1).equalsIgnoreCase(selectedDecor)){
                                toasMessage(selectedDecor + " already exist in the aquarium");
                                return;
                            }

                        }

                        while(data.moveToNext())
                        {
                             decorAdded = myDb.insertDecorData(data.getString(1), aqID);

                        }
                        if (decorAdded == true) {
                            Toast.makeText(DecorListPage.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(DecorListPage.this, Aquarium.class);
                            intent.putExtra("id", aqID);
                            startActivity(intent);

                        } else {
                            Toast.makeText(DecorListPage.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }

                        databaseAccess.close();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
