package com.example.iquarium;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Aquarium extends AppCompatActivity {

    DatabaseHelper myDb;
    private TextView aqname, tanksize,fishqty,srfcarea, srfcfish, txt6, txt15, txtvwsize, txt7;
    private String selectedAquarium;
    private int selectedID,aqlength,aqheight,aqwidth,aqfqty;
    private Button delaqm, viewaqm,editaqm,addfish,checkcomp;
    private ImageButton backBttn, settingsBttn;
    ListView fishlist;
    ListView complist;
    ArrayList<Integer> imgid = new ArrayList<>();
    public float gal = 0, surfaceArea = 0, fishInchTotal = 0;
    public ArrayList<Integer> arrayFishID = new ArrayList<>();
    public int lastexpanded = -1, groupCount = 0;
    ArrayList<String> incompFishes = new ArrayList<>();
    StringBuffer buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquarium);

        //  INITIALIZING

        //BUTTONS
        delaqm =  findViewById(R.id.delAqm);
        viewaqm =  findViewById(R.id.viewAqm);
        editaqm =  findViewById(R.id.editAqm);

        //TEXTVIEW
        txt6 = findViewById(R.id.textView6);
        txt15 = findViewById(R.id.textView15);
        txt7 = findViewById(R.id.textView7);
        txtvwsize = findViewById(R.id.textViewSize);
        aqname =  findViewById(R.id.aqName);
        tanksize =  findViewById(R.id.tankSize);
        srfcarea =  findViewById(R.id.srfcArea);
        srfcfish = findViewById(R.id.srfcFish);

        addfish = findViewById(R.id.addFish);
        fishqty = findViewById(R.id.fishQty);
        fishlist = findViewById(R.id.fishList);
        myDb = new DatabaseHelper(this);


        // get the intent extra from the MyAquarium
        Intent receivedIntent = getIntent();


        // now get the data we passed as an extra
        selectedID = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        surfaceArea = computeSA(selectedID);
        gal = computeGallons(selectedID);
        getNLWH();


        //set the text to show the current seleccted name
        aqname.setText(selectedAquarium);
        tanksize.setText(gal + "" + " gallons");
        fishqty.setText(aqfqty + "");
        srfcarea.setText(surfaceArea + "" + " sq. in.");

        populateFishListView();
       deleteAquarium();
       viewAquarium();
       editAquarium();
       addFish();
       deleteAqFish();
       ViewFishDetails();
     //  aqCheckComp();
       fishInchTotal= aqCheckCompatibility();

       srfcfish.setText(fishInchTotal + " sq. in.");

        if(gal == 0){
            tanksize.setVisibility(View.GONE);
            fishqty.setVisibility(View.GONE);
            srfcarea.setVisibility(View.GONE);
            srfcfish.setVisibility(View.GONE);
            txt6.setVisibility(View.GONE);
            txtvwsize.setVisibility(View.GONE);
            txt15.setVisibility(View.GONE);
            txt7.setVisibility(View.GONE);
        }
        else{
            tanksize.setVisibility(View.VISIBLE);
            fishqty.setVisibility(View.VISIBLE);
            srfcarea.setVisibility(View.VISIBLE);
            srfcfish.setVisibility(View.VISIBLE);
            txt6.setVisibility(View.VISIBLE);
            txtvwsize.setVisibility(View.VISIBLE);
            txt15.setVisibility(View.VISIBLE);
            txt7.setVisibility(View.VISIBLE);
        }

       //showCompatibility();
    //   showAqFishData();


/*
        fishlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                incompFishes = new ArrayList<>();
                groupCount = expandableListView.getExpandableListAdapter().getGroupCount();
                String ftype = expandableListView.getItemAtPosition(i).toString();

                incompFishes  = checkFishComp(ftype);

                return false;
            }
        });


        fishlist.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {


                View child = fishlist.getChildAt(lastexpanded);
                child.setBackgroundColor(Color.parseColor("#1ea8a8"));
                lastexpanded = i;
            //   toasMessage("Collaps " + lastexpanded + "");

            }
        });

        fishlist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

                if (lastexpanded != -1
                        && i != lastexpanded) {
                    fishlist.collapseGroup(lastexpanded);
                }

                lastexpanded = i;

                View childs = fishlist.getChildAt(i);
              //  toasMessage("Expand "+ i + "");
                childs.setBackgroundColor(Color.parseColor("#126be0"));

            }
        });

*/

    }

    public ArrayList<String> checkFishComp(String ftype)
    {
        myDb = new DatabaseHelper(this);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        Cursor aqfishdata = myDb.getFishData(selectedID);
        buffer = new StringBuffer();
        ArrayList<String> notcomp = new ArrayList<>();

        while (aqfishdata.moveToNext())
        {
            if(!ftype.toString().equalsIgnoreCase(aqfishdata.getString(1)))
            {
                Cursor compdata = databaseAccess.getCompData(ftype.toString());
                while(compdata.moveToNext())
                {
                    if(aqfishdata.getString(1).equalsIgnoreCase(compdata.getString(0)))
                    {
                        notcomp.add(aqfishdata.getString(1));
                        break;
                    }
                }
            }
        }
/*
        if(!notcomp.isEmpty()) {
            buffer.append(ftype + " is not compatible with: " + "\n");
            for (int n = 0; n < notcomp.size(); n++) {
                buffer.append(n + 1 + ". " + notcomp.get(n) + "\n");
            }

        }
*/
       // showMessage("COMPATIBILITY", buffer.toString());

        return notcomp;
    }


    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(this, MyAquarium.class);
        startActivity(intent);

    }

    // Acquiring data from selected Aquarium
    public void getNLWH()
    {
        Cursor data = myDb.getAqData(selectedID);
        while(data.moveToNext()) {
            selectedAquarium = data.getString(1);
            aqlength = data.getInt(2);
            aqheight = data.getInt(4);
            aqwidth = data.getInt(3);
            aqfqty = data.getInt(5);
        }


    }


    public float  aqCheckCompatibility() {
        Cursor data = myDb.getFishData(selectedID);
        float fitotal = 0;
        while (data.moveToNext()) {
            if (data.getInt(2) <= 3) {
                fitotal += (data.getFloat(2) * data.getInt(12)) * 12;

            } else {
                fitotal += (data.getFloat(2) * data.getInt(12)) * 20;

            }
        }
        return  fitotal;
    }

    public void deleteAquarium()
    {
        delaqm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delConfirm();
            }
        });

    }

/*
    public void aqCheckComp()
    {
        checkcomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> fishincmp = new ArrayList<>();
                Cursor data = myDb.getFishData(selectedID);
                StringBuffer buffer = new StringBuffer();



                int fitotal = 0;
                while (data.moveToNext())
                {
                    if(data.getInt(2) <= 3) {

                        fitotal += (data.getInt(2) * data.getInt(12)) * 12;

                    }
                    else {
                        fitotal += (data.getInt(2) * data.getInt(12)) * 20;

                    }
                }

                if(fitotal <= surfaceArea)
                {
                    buffer.append("Aquarium is understock" + "\n");
                }
                else
                {
                    buffer.append("Aquarium is overstock" + "\n");

                }


               fishincmp = checkAllFishComp();


                buffer.append("fish sq. inches occupied: " + fitotal + "\n\n");


                if(!fishincmp.isEmpty()){

                     buffer.append("Incompatibility found on: " + "\n");
                    for(int i = 0; i < fishincmp.size(); i++)
                    {

                        buffer.append(i + 1 + ". " +fishincmp.get(i) + "\n");
                    }

                }

                showMessage("AQUARIUM STATUS", buffer.toString());


            }
        });

    }
*/
    public ArrayList checkAllFishComp()
    {
       ArrayList<String> tempfish = new ArrayList<>();
       ArrayList<String> fish = new ArrayList<>();
       Cursor data = myDb.getFishData(selectedID), aqdata;
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

       while(data.moveToNext())
       {
           tempfish.add(data.getString(1));
       }

       for(int i = 0; i < tempfish.size(); i++)
       {

           data = myDb.getFishData(selectedID);
           while(data.moveToNext())
           {
               Cursor compdata = databaseAccess.getCompData(tempfish.get(i));

               if(!tempfish.get(i).equals(data.getString(1)))
               {

                   while(compdata.moveToNext())
                   {
                       if(data.getString(1).equals(compdata.getString(0)))
                       {
                           if(!(fish.contains(tempfish.get(i)))) {
                               fish.add(tempfish.get(i));
                           }
                             break;
                       }
                   }

               }

           }
       }

       data = myDb.getFishData(selectedID);
       aqdata = myDb.getAqData(selectedID);
       while (aqdata.moveToNext())
       {
           while(data.moveToNext())
           {

               // CHECK FISH PREFERED PH LEVEL
               if(!(aqdata.getFloat(7) >= data.getFloat(3)))
               {

                   if(!(fish.contains(data.getString(1))))
                   {
                       fish.add(data.getString(1));
                   }
               }
               // CHECK FISH PREFERED PH LEVEL
               else if(!((aqdata.getFloat(7) <= data.getFloat(4))))
               {
                   if(!fish.contains(data.getString(1)))
                   {
                       fish.add(data.getString(1));
                   }
               }
               else if(gal < data.getInt(7))
               {
                   if(!fish.contains(data.getString(1)))
                   {
                       fish.add(data.getString(1));
                   }
               }
               else if(!((aqdata.getFloat(8) >= data.getFloat(5))))
               {
                   if(!fish.contains(data.getString(1)))
                   {
                       fish.add(data.getString(1));
                   }
               }
               else if(!((aqdata.getFloat(8) <= data.getFloat(6))))
               {
                   if(!fish.contains(data.getString(1)))
                   {
                       fish.add(data.getString(1));
                   }
               }

           }
       }

       databaseAccess.close();

       return  fish;
    }

    public void viewAquarium()
    {
        viewaqm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAqData(selectedID);

                if(res.getCount() == 0)
                {
                    // show message
                    showMessage("Error", "NO DATA FOUND");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext())
                {

                    buffer.append("\nName:       " + res.getString(1));
                    buffer.append("\nLength:     " + res.getFloat(2) + " inches");
                    buffer.append("\nWidth:       " + res.getFloat(3) + " inches");
                    buffer.append("\nHeight:      " + res.getFloat(4) + " inches");
                    buffer.append("\nFish Qty:   " + res.getInt(5));
                    buffer.append("\npH:             " + res.getFloat(6));
                   buffer.append("\nTemp:        " + res.getFloat(7) +" Celsius\n\n");
                }

                // Show all data
                showMessage("AQUARIUM DETAILS", buffer.toString());

            }
        });
    }

    public void editAquarium()
    {
        editaqm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myAquariumScreen = new Intent(Aquarium.this, EditAquarium.class);

                myAquariumScreen.putExtra("id", selectedID);
                myAquariumScreen.putExtra("name", selectedAquarium);
                myAquariumScreen.putExtra("length", aqlength);
                myAquariumScreen.putExtra("height", aqheight);
                myAquariumScreen.putExtra("width", aqwidth);
                startActivity(myAquariumScreen);

            }
        });
    }

    public void addFish()
    {
        addfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newpage = new Intent(Aquarium.this, SelectionMenu.class);
                newpage.putExtra("id", selectedID);
                newpage.putExtra("name", selectedAquarium);
                newpage.putExtra("gallon", gal);
                newpage.putExtra("fitotal", fishInchTotal);
                newpage.putExtra("sfarea", surfaceArea);
                startActivity(newpage);
            }
        });
    }


    public void delConfirm()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("DELETE CONFIRMATION");
        builder.setMessage("Are you sure you want to delete? " + selectedAquarium);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDb.deleteAquarium(selectedID,selectedAquarium);
                        Intent myAquariumScreen = new Intent(Aquarium.this, MyAquarium.class);
                         startActivity(myAquariumScreen);
                        toasMessage("AQUARIUM DELETED");
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

    public float computeGallons(int id)
    {
        float gallons = 0;
        int volume = 0;
        int l = 0, w = 0, h = 0;

       Cursor data = myDb.getAqData(id);

       while (data.moveToNext()) {
           l = data.getInt(2);
           w = data.getInt(3);
           h = data.getInt(4);
       }
        volume = l * w * h;
        gallons = volume / 231;

        return gallons;
    }

    public float computeSA(int id)
    {

        float sa = 0;
        float l = 0, w = 0;

        Cursor data = myDb.getAqData(id);

        while(data.moveToNext())
        {
            l = data.getInt(2);
            w = data.getInt(3);
        }

        sa = l * w;

        return sa;
    }

/*
    public ArrayList<Fishes> getData()
    {

        Cursor data = myDb.getFishData(selectedID);
        ArrayList<Fishes> allFish = new ArrayList<Fishes>();

        while (data.moveToNext())
        {

            Fishes fishes = new Fishes(data.getInt(0),data.getString(1), data.getInt(2), data.getFloat(3), data.getFloat(4), data.getInt(5), data.getInt(6),
                    data.getInt(7), data.getString(8), data.getString(9), data.getString(10), selectedID, gal, data.getInt(12), "fish");
            fishes.players.add("DETAILS:");
            allFish.add(fishes);
            arrayFishID.add(data.getInt(0));
        }

        data = myDb.getAqDecorData(selectedID);

        while (data.moveToNext())
        {
            float phmin = 5;
            float phmax = 5;
            Fishes fishes = new Fishes(data.getInt(0),data.getString(1), 50, phmin, phmax, 5, 5, 5,
                    "Yes", "Yes", "Yes", selectedID, gal, 5, "decoration");
            fishes.players.add("DETAILS");
            allFish.add(fishes);
            arrayFishID.add(data.getInt(0));
        }


        return  allFish;
    }
*/

    public void ViewFishDetails()
    {
        fishlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ftype = adapterView.getItemAtPosition(i).toString();
                Cursor fishdata = myDb.getOneFishData(selectedID, arrayFishID.get(i));
                StringBuffer buffer = new StringBuffer();
                int qty = 0;
                while (fishdata.moveToNext())
                {
                   // buffer.append("Size:                              " + fishdata.getFloat(2)+ " inches\n");
                    buffer.append("Size: ......................... " + fishdata.getFloat(2)+ " inches\n");
                 //   buffer.append("Quantity:                      "+ fishdata.getInt(12)+ "\n");
                    buffer.append("Quantity: .................. "+ fishdata.getInt(12)+ "\n");
                  //  buffer.append("pH:                                "+ fishdata.getFloat(3) + " - " + fishdata.getFloat(4) + "\n");
                    buffer.append("pH: ........................... "+ fishdata.getFloat(3) + " - " + fishdata.getFloat(4) + "\n");
                //    buffer.append("Temperature:              "+ fishdata.getFloat(5) + " - " + fishdata.getFloat(6) + " celsius\n");
                    buffer.append("Temperature: .......... "+ fishdata.getFloat(5) + " - " + fishdata.getFloat(6) + " celsius\n");
                   // buffer.append("Tank Minimum:          "+ fishdata.getInt(7) + " gallons\n");
                    buffer.append("Tank Minimum: ....... "+ fishdata.getInt(7) + " gallons\n");




                    qty = fishdata.getInt(12);
                }
                if(qty != 0) {
                    showMessage("FISH DETAILS", buffer.toString());
                }
            }
        });

    }


    public void populateFishListView()
    {


        Cursor data = myDb.getFishData(selectedID);

        ArrayList<String> arrayList = new ArrayList<>();
        boolean aquariumpage = true;

        while (data.moveToNext())
        {
            arrayList.add(data.getString(1));
            arrayFishID.add(data.getInt(0));

            fishImgID(data.getString(1));
        }

        data = myDb.getAqDecorData(selectedID);

        while (data.moveToNext()){
              arrayList.add(data.getString(1));
            arrayFishID.add(data.getInt(0));
              if(data.getString(1).equalsIgnoreCase("Plant")){
                  imgid.add(R.drawable.aqplant);

              }
        }

       // final ArrayList<Fishes> fishes = getData();
        CustomListView adapter = new CustomListView(this, arrayList, imgid, aquariumpage, arrayFishID, selectedID, 0, null);
       fishlist.setAdapter(adapter);

/*
        while(data.moveToNext())
        {
            ArrayList<String> fish = new ArrayList<>();
            fish.add("FISH");
            item.put(data.getString(1), fish);
            arrayFishID.add(data.getInt(0));
        }


        MyExpandableListAdapter adapter = new MyExpandableListAdapter(item);
        fishlist.setAdapter(adapter);
*/
        // DISPLAYING IMAGES OF ADDED FISH
     // CustomListView customListView = new CustomListView(this, arrayList, imgList, aquariumpage, arrayFishID, selectedID, gal);
      //  ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        //fishlist.setAdapter(new MyListAdapter(this, R.layout.listview_layoutt, arrayList));
      //  fishlist.setAdapter(arrayAdapter);

    }
    // Delete Decor and Fish
     public void deleteAqFish()
    {
        fishlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
               String ftype = adapterView.getItemAtPosition(i).toString();
                deleteAqFishConfirm(arrayFishID.get(i), ftype);
                return true;
            }
        });


    }

    // Delete Decor and Fish
    public void deleteAqFishConfirm(final int id, final String ftype)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("DELETE CONFIRMATION");
        builder.setMessage("Are you sure you want to delete? " + ftype);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Aquarium.this, Aquarium.class);
                        if(ftype.equalsIgnoreCase("Plant"))
                        {
                            myDb.deleteDecorAquarium(id);
                            toasMessage("DECORATION DELETED");
                        }
                        else{
                            int fqty = myDb.getFishQty(id);
                            myDb.deleteFishAquarium(id, selectedID, fqty);
                            toasMessage("FISH DELETED TEST");
                        }

                        intent.putExtra("id", selectedID);
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
        }  else if (fishname.equalsIgnoreCase("Koi")) {
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
        } else if(fishname.equalsIgnoreCase("Silver Dollar")){
            imgid.add(R.drawable.silverdollar);
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
        } else if(fishname.equalsIgnoreCase("Blood Parrot Cichlid")){
            imgid.add(R.drawable.bloodparrotcichlid);
        } else if(fishname.equalsIgnoreCase("Kribensis Cichlid")){
            imgid.add(R.drawable.kribensiscichlid);
        } else if(fishname.equalsIgnoreCase("Texas Cichlid")){
            imgid.add(R.drawable.texascichlid);
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
