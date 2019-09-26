package com.example.iquarium;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FishListPage extends AppCompatActivity {

    private ImageButton homePage;
    ListView Listfishes;
    View view;
    private ArrayList<Integer> imgid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fish_list_page);
        Listfishes = (ListView) findViewById(R.id.decoration);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        homePage = findViewById(R.id.backBttn);
        boolean aquariumpage = false;
        ArrayList<Integer> fishID = new ArrayList<>();
        Integer aqId = 0; // just ignore
        fishImgID();
        returnHomepage();


        Cursor data = databaseAccess.getFishData();
        ArrayList<String> arrayList = new ArrayList<>();
        while(data.moveToNext())
        {
                arrayList.add(data.getString(1));
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        CustomListView customListView = new CustomListView(this,arrayList,imgid, aquariumpage, fishID, aqId, 0, null);
        Listfishes.setAdapter(customListView);
        databaseAccess.close();

    }

    @Override
    public void onBackPressed()
    {


    }

    public void returnHomepage()
    {
        homePage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {Intent intent = new Intent(FishListPage.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }

        );

    }

    public void fishImgID()
    {

        imgid.add(R.drawable.bettas);
        imgid.add(R.drawable.angelfish);
        imgid.add(R.drawable.shark);
        imgid.add(R.drawable.goldfish);
        imgid.add(R.drawable.koi);
        imgid.add(R.drawable.guppy);
        imgid.add(R.drawable.oscar);
        imgid.add(R.drawable.loach);
        imgid.add(R.drawable.molly);
        imgid.add(R.drawable.blue_gourami);
    }




}

