package com.example.iquarium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectionMenu extends AppCompatActivity {

    private ImageButton fishBttn, decorBttn;
    Integer selectedID;
    String selectedAquarium;
    public float aqgal, fishinchtotal, surfacearea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_menu);

        fishBttn =  findViewById(R.id.fishImg);
       decorBttn = findViewById(R.id.decorImg);

        Intent intent = getIntent();

        selectedID = intent.getIntExtra("id", -1);
        selectedAquarium = intent.getStringExtra("name");
        aqgal = intent.getFloatExtra("gallon", -1);
        fishinchtotal = intent.getFloatExtra("fitotal", -1);
        surfacearea = intent.getFloatExtra("sfarea", -1);

        addFish();
        addDecor();

    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }


    public void addFish()
    {
        fishBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newpage = new Intent(SelectionMenu.this, AddFishPage.class);
                newpage.putExtra("id", selectedID);
                newpage.putExtra("name", selectedAquarium);
                newpage.putExtra("gallon", aqgal);
                newpage.putExtra("fitotal", fishinchtotal);
                newpage.putExtra("sfarea", surfacearea);
                startActivity(newpage);
            }
        });
    }

    public void addDecor()
    {
        decorBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newpage = new Intent(SelectionMenu.this, DecorListPage.class);
                newpage.putExtra("id", selectedID);
                newpage.putExtra("name", selectedAquarium);
                startActivity(newpage);
            }
        });


    }


    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, Aquarium.class);
        intent.putExtra("id", selectedID);
        intent.putExtra("name", selectedAquarium);
        startActivity(intent);

    }

}
