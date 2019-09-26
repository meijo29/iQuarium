package com.example.iquarium;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.grpc.netty.shaded.io.netty.channel.MessageSizeEstimator;

public class AddFishPage extends AppCompatActivity {

    ListView fishlist;
    int aqID;
    public float aqtgal, fishinchtotal, surfacearea;
    DatabaseHelper myDb;
    private ArrayList<Integer> imgid = new ArrayList<>();
    String aqName;
    ArrayList<String> cautionfishes = new ArrayList<>();
    private ImageButton backbttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fish_page);

        myDb = new DatabaseHelper(this);
        fishlist = findViewById(R.id.fishList);
        Intent received = getIntent();

        aqID = received.getIntExtra("id", -1);
        aqName = received.getStringExtra("name");
        aqtgal = received.getFloatExtra("gallon", 1);
        fishinchtotal = received.getFloatExtra("fitotal", -1);
        surfacearea = received.getFloatExtra("sfarea", -1);

        populateFishListView();
        addFish();
        //   FilterFish();
        FishCaution();

    }

    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, SelectionMenu.class);
        intent.putExtra("id", aqID);
        intent.putExtra("name", aqName);
        intent.putExtra("gallon", aqtgal);
        intent.putExtra("fitotal", fishinchtotal);
        intent.putExtra("sfarea", surfacearea);
        startActivity(intent);

    }

    public void FishCaution(){

        myDb = new DatabaseHelper(this);
        Cursor fishdata = myDb.getFishData(aqID);
        StringBuffer buffer = new StringBuffer();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();


        while (fishdata.moveToNext()) {
            Cursor compdata = databaseAccess.getCautionData(fishdata.getString(1));

            while (compdata.moveToNext()){
                if(compdata.getString(0) != null) {
                    Cursor compData2 = databaseAccess.getCompData2(compdata.getString(0));
                    while (compData2.moveToNext()){

                        if(compData2.getString(0) != null) {
                            if(!cautionfishes.contains(compData2.getString(0))){
                                cautionfishes.add(compData2.getString(0));
                            }


                        }
                    }

                }

            }
        }
/*
            for(int i = 0; i < cautionfishes.size(); i++){
                buffer.append(cautionfishes.get(i));
            }
*/

    }


    public ArrayList<String> FilterFish()
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor aqfishdata = myDb.getFishData(aqID);
        Cursor aqdata = myDb.getAqData(aqID);
        ArrayList<String> notcomp = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        Cursor decordata = myDb.getAqDecorData(aqID);

        // loop for filtering fish to fish compatibility
        while (aqfishdata.moveToNext()) {
            Cursor compdata = databaseAccess.getCompData(aqfishdata.getString(1));

             while (compdata.moveToNext()){
                 if(compdata.getString(0) != null) {
                    Cursor compData2 = databaseAccess.getCompData2(compdata.getString(0));
                     while (compData2.moveToNext()){

                         if(compData2.getString(0) != null) {
                             if(!notcomp.contains(compData2.getString(0))){
                                 notcomp.add(compData2.getString(0));
                             }

                         }
                     }

                     }

                 }
             }



       // loop for filtering plant to fish compatibility
        while(decordata.moveToNext()){

            Cursor compdata = databaseAccess.getCompData(decordata.getString(1));
            while (compdata.moveToNext()) {
                if (!notcomp.contains(compdata.getString(0))) {
                    notcomp.add(compdata.getString(0));
                }

            }
        }

        //loop for filtering fish to ph, temp & tank minimum
        while(aqdata.moveToNext())
        {
            Cursor fdata = databaseAccess.getFishData();

            if (aqdata.getFloat(6) != 0) {

              while(fdata.moveToNext()) {



                  if (aqdata.getFloat(6) >= fdata.getFloat(3) && aqdata.getFloat(6) <= fdata.getFloat(4)) {

                  } else {
                      if (!notcomp.contains(fdata.getString(1))) {
                          notcomp.add(fdata.getString(1));

                      }

                  }
              }
            }


            if(aqdata.getFloat(7) != 0){

                fdata = databaseAccess.getFishData();
                while(fdata.moveToNext()){
                    if(aqdata.getFloat(7) >= fdata.getFloat(5) && aqdata.getFloat(7) <= fdata.getFloat(6))
                    {

                    }
                    else{
                        if (!notcomp.contains(fdata.getString(1))) {
                            notcomp.add(fdata.getString(1));

                        }
                    }
                }
            }

            if(aqdata.getFloat(2) != 0 && aqdata.getFloat(3) != 0 && aqdata.getFloat(4) != 0)
            {
                fdata = databaseAccess.getFishData();
                while (fdata.moveToNext()){
                    if(fdata.getFloat(7) > aqtgal){

                        if (!notcomp.contains(fdata.getString(1))) {
                            notcomp.add(fdata.getString(1));
                        }
                    }

                }

            }

        }

/*
        for(int i = 0; i < notcomp.size(); i++)
        {

            buffer.append(notcomp.get(i) + "\n");
        }

        showMessage("REMOVE FROM LIST", buffer.toString());
*/
         return notcomp;
    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void populateFishListView()
    {
       // fishImgID();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        Cursor data = databaseAccess.getFishData();
        ArrayList<String> arrayList = new ArrayList<>();
        boolean aquariumpage = false;
        ArrayList<Integer> fishID = new ArrayList<>(); // just ignore
        Integer aqId = 0; // just ignore
        ArrayList<String> removelist = FilterFish();

            while (data.moveToNext()) {

                if(removelist.isEmpty()) {
                    arrayList.add(data.getString(1));
                    fishImgID(data.getString(1));
              }
                else {
                    if (!removelist.contains(data.getString(1))) {
                        arrayList.add(data.getString(1));
                        fishImgID(data.getString(1));
                    }
                }

            }

           // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
            CustomListView customListView = new CustomListView(this, arrayList, imgid, aquariumpage, fishID, aqId, 0, cautionfishes);
            fishlist.setAdapter(customListView);


        databaseAccess.close();
    }



    public void addFish()
    {

        fishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedfish = adapterView.getItemAtPosition(i).toString();
                if(surfacearea == 0){

                    addConfirm(selectedfish);
                }
                else {

                    if(cautionfishes.contains(selectedfish)){

                        CautionConfirm(selectedfish);
                        return;
                    }
                    else {
                        nextIntent(selectedfish);
                    }
                }

            }
        });

    }

    public void nextIntent(String selectedfish){


        Intent intent = new Intent(AddFishPage.this, AddFishPageFinal.class);
        intent.putExtra("selected", selectedfish);
        intent.putExtra("aqID", aqID);
        intent.putExtra("fitotal", fishinchtotal);
        intent.putExtra("sfarea", surfacearea);
        startActivity(intent);

    }


    public void CautionConfirm(final String selectedFish){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("SELECT CONFIRMATION");
        builder.setMessage(selectedFish +" Usually Coexist with some of the fish in the aquarium. Adding "+ selectedFish + " require caution.");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                         nextIntent(selectedFish);

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

    public void addConfirm(final String selectedFish)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("ADD CONFIRMATION");

        if(cautionfishes.contains(selectedFish)){
            builder.setMessage(selectedFish +" Usually Coexist with some of the fish in the aquarium. Adding "+ selectedFish + " require caution.");
        }
        else {
            builder.setMessage("Adding " + selectedFish + "" + " to your aquarium");
        }
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                        databaseAccess.open();
                        Cursor data = databaseAccess.getFishID(selectedFish);
                        boolean fishAdded = false;

                        Cursor aqdata = myDb.getFishData(aqID);
                        while(aqdata.moveToNext()){

                            if(aqdata.getString(1).equalsIgnoreCase(selectedFish)){
                                toasMessage(selectedFish + " already exist in the aquarium");
                                return;
                            }

                        }


                        while(data.moveToNext())
                        {
                            fishAdded = myDb.insertFishData(data.getString(1), data.getString(2), aqID, data.getFloat(3), data.getFloat(4),
                                    data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8), data.getString(9), data.getString(10),
                                    0, false, 0, 0, data.getString(11));

                        }
                        if (fishAdded = true) {
                            Toast.makeText(AddFishPage.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddFishPage.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }

                        databaseAccess.close();

                        Intent intent = new Intent(AddFishPage.this, Aquarium.class);
                        intent.putExtra("id", aqID);
                        startActivity(intent);
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

    public void fishImgID(String fishname)
    {
        if (fishname.equalsIgnoreCase("Gold Fish")) {
            imgid.add(R.drawable.goldfish);
        } else if (fishname.equalsIgnoreCase("Angel Fish")) {
            imgid.add(R.drawable.angelfish);
        } else if (fishname.equalsIgnoreCase("Koi")) {
            imgid.add(R.drawable.koi);
        } else if (fishname.equalsIgnoreCase("Guppy")) {
            imgid.add(R.drawable.guppy);
        } else if (fishname.equalsIgnoreCase("Oscar")) {
            imgid.add(R.drawable.oscar);
        } else if (fishname.equalsIgnoreCase("Clown Loach")) {
            imgid.add(R.drawable.loach);
        } else if (fishname.equalsIgnoreCase("Molly")) {
            imgid.add(R.drawable.molly);
        } else if (fishname.equalsIgnoreCase("Blue Gourami")) {
            imgid.add(R.drawable.blue_gourami);
        } else if(fishname.equalsIgnoreCase("Bala Shark")){
            imgid.add(R.drawable.shark);
        }  else if(fishname.equalsIgnoreCase("Betta (female)")){
            imgid.add(R.drawable.femalebetta);
        } else if(fishname.equalsIgnoreCase("Chocolate Gourami")){
            imgid.add(R.drawable.chocogourami);
        } else if(fishname.equalsIgnoreCase("Dwarf Gourami")){
            imgid.add(R.drawable.dwarfgourami);
        } else if(fishname.equalsIgnoreCase("Kissing Gourami")){
            imgid.add(R.drawable.kissinggourami);
        } else if(fishname.equalsIgnoreCase("Moonlight Gourami")){
            imgid.add(R.drawable.moonlightgourami);
        } else if(fishname.equalsIgnoreCase("Pearl Gourami")){
            imgid.add(R.drawable.pearlgourami);
        } else if(fishname.equalsIgnoreCase("Black Ruby Barb")){
            imgid.add(R.drawable.blackrubybarb);
        } else if(fishname.equalsIgnoreCase("Denison Barb")){
            imgid.add(R.drawable.denisonbarb);
        } else if(fishname.equalsIgnoreCase("Gold Barb")){
            imgid.add(R.drawable.goldbarb);
        } else if(fishname.equalsIgnoreCase("Rosy Barb")){
            imgid.add(R.drawable.rosybarb);
        } else if(fishname.equalsIgnoreCase("Tiger Barb")){
            imgid.add(R.drawable.tigerbarb);
        } else if(fishname.equalsIgnoreCase("Tinfoil Barb")){
            imgid.add(R.drawable.tinfoilbarb);
        } else if(fishname.equalsIgnoreCase("Zebra Barb")){
            imgid.add(R.drawable.zebrabarb);
        } else if(fishname.equalsIgnoreCase("Dwarf Spotted Danio")){
            imgid.add(R.drawable.dwarfspotteddanio);
        } else if(fishname.equalsIgnoreCase("Giant Danio")){
            imgid.add(R.drawable.giantdanio);
        } else if(fishname.equalsIgnoreCase("Pearl Danio")){
            imgid.add(R.drawable.pearldanio);
        } else if(fishname.equalsIgnoreCase("Rosy Danio")){
            imgid.add(R.drawable.rosydanio);
        } else if(fishname.equalsIgnoreCase("White Cloud Mountain Minnow")){
            imgid.add(R.drawable.whitecloudminnow);
        } else if(fishname.equalsIgnoreCase("Zebra Danio")){
            imgid.add(R.drawable.zebradanio);
        } else if(fishname.equalsIgnoreCase("Harlequin Rasbora")){
            imgid.add(R.drawable.harlequinrasbora);
        } else if(fishname.equalsIgnoreCase("Scissortail Rasbora")){
            imgid.add(R.drawable.scissortailrasbora);
        } else if(fishname.equalsIgnoreCase("Bandit Cory")){
            imgid.add(R.drawable.banditcory);
        } else if(fishname.equalsIgnoreCase("Bronze Cory")){
            imgid.add(R.drawable.bronzecory);
        } else if(fishname.equalsIgnoreCase("Julii Cory")){
            imgid.add(R.drawable.juliicory);
        } else if(fishname.equalsIgnoreCase("Panda Cory")){
            imgid.add(R.drawable.pandacory);
        } else if(fishname.equalsIgnoreCase("Pepper Cory")){
            imgid.add(R.drawable.peppercory);
        } else if(fishname.equalsIgnoreCase("Skunk Cory")){
            imgid.add(R.drawable.skunkcory);
        } else if(fishname.equalsIgnoreCase("Three Stripe Cory")){
            imgid.add(R.drawable.threestripecory);
        } else if(fishname.equalsIgnoreCase("Bristlenose Pleco")){
            imgid.add(R.drawable.bristlenosepleco);
        } else if(fishname.equalsIgnoreCase("Upside-Down Catfish")){
            imgid.add(R.drawable.upsidedowncatfish);
        } else if(fishname.equalsIgnoreCase("Black Neon Tetra")){
            imgid.add(R.drawable.blackneontetra);
        } else if(fishname.equalsIgnoreCase("Black Phantom Tetra")){
            imgid.add(R.drawable.blackphantomtetra);
        } else if(fishname.equalsIgnoreCase("Black Widow Tetra")){
            imgid.add(R.drawable.blackwidowtetra);
        } else if(fishname.equalsIgnoreCase("Buenos Aires Tetra")){
            imgid.add(R.drawable.buenosairestetra);
        } else if(fishname.equalsIgnoreCase("Red Neon Tetra")){
            imgid.add(R.drawable.redneontetra);
        } else if(fishname.equalsIgnoreCase("Emperor Tetra")){
            imgid.add(R.drawable.emperortetra);
        } else if(fishname.equalsIgnoreCase("Glowlight Tetra")){
            imgid.add(R.drawable.glowlighttetra);
        } else if(fishname.equalsIgnoreCase("Beacon tetra")){
            imgid.add(R.drawable.beacontetra);
        } else if(fishname.equalsIgnoreCase("Neon Tetra")){
            imgid.add(R.drawable.neontetra);
        } else if(fishname.equalsIgnoreCase("Red Eye Tetra")){
            imgid.add(R.drawable.redeyetetra);
        } else if(fishname.equalsIgnoreCase("Serpae Tetra")){
            imgid.add(R.drawable.serpaetetra);
        } else if(fishname.equalsIgnoreCase("Mickey Mouse Platy")){
            imgid.add(R.drawable.mickeymouseplaty);
        } else if(fishname.equalsIgnoreCase("Red Wagtail Platy")){
            imgid.add(R.drawable.redwagtailplaty);
        } else if(fishname.equalsIgnoreCase("Swordtail")){
            imgid.add(R.drawable.swordtail);
        } else if(fishname.equalsIgnoreCase("Coolie Loach")){
            imgid.add(R.drawable.coolieloach);
        } else if(fishname.equalsIgnoreCase("Horseface Loach ")){
            imgid.add(R.drawable.horsefaceloach);
        } else if(fishname.equalsIgnoreCase("Weather Loach")){
            imgid.add(R.drawable.weatherloach);
        } else if(fishname.equalsIgnoreCase("Yo Yo Loach")){
            imgid.add(R.drawable.yoyoloach);
        } else if(fishname.equalsIgnoreCase("Zebra Loach")){
            imgid.add(R.drawable.zebraloach);
        } else if(fishname.equalsIgnoreCase("Axelrods Rainbowfish")){
            imgid.add(R.drawable.axelrodrainbowfish);
        } else if(fishname.equalsIgnoreCase("Desert Rainbowfish")){
            imgid.add(R.drawable.desertrainbowfish);
        } else if(fishname.equalsIgnoreCase("Lake Kutubu Rainbowfish")){
            imgid.add(R.drawable.latekutuburainbowfish);
        } else if(fishname.equalsIgnoreCase("Lake Wanam Rainbowfish")){
            imgid.add(R.drawable.lakewanamrainbowfish);
        } else if(fishname.equalsIgnoreCase("Madagascar Rainbowfish")){
            imgid.add(R.drawable.madagascarrainbowfish);
        } else if(fishname.equalsIgnoreCase("Neon Rainbowfish")){
            imgid.add(R.drawable.neonrainbowfish);
        } else if(fishname.equalsIgnoreCase("Red Rainbowfish")){
            imgid.add(R.drawable.redrainbowfish);
        }
    }

}
