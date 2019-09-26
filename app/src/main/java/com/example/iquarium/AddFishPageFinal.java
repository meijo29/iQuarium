package com.example.iquarium;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddFishPageFinal extends AppCompatActivity {


    String fishName;
    private TextView fName;
    private ImageView imgid;
    private EditText fishsize, fishqty;
    private Button addbtn;
    private ImageButton stockinfo;
    DatabaseHelper myDb;
    Integer aqID, qty = 0;
    public float fishinchtotal, surfacearea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fish_page_final);

        Intent received = getIntent();

        addbtn = findViewById(R.id.addBtn);
        fName = findViewById(R.id.textViewFishName);
        imgid = findViewById(R.id.imageViewFish);
        fishsize = findViewById(R.id.editTextSize);
        fishqty = findViewById(R.id.editTextQty);
        stockinfo = findViewById(R.id.stockInfo);

        myDb = new DatabaseHelper(this);
        fishName = received.getStringExtra("selected");
        aqID = received.getIntExtra("aqID", -1);
        fishinchtotal = received.getFloatExtra("fitotal", -1);
        surfacearea = received.getFloatExtra("sfarea", -1);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        Cursor data = databaseAccess.getFishID(fishName);

        while (data.moveToNext()) {
            fishsize.setText(data.getInt(2) + "");
        }


        databaseAccess.close();

        SizeChangeListener();
        fName.setText(fishName);

        fishImgID(fishName);

        addFish();
        RemainingFish();

    }


    public void addFish()
    {
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fishsize.getText().toString().matches("") || fishqty.getText().toString().matches("")) {

                    toasMessage("PLEASE FILL ALL TEXT FIELDS");
                    return;
                }
                String decimal = ".";
                if(fishsize.getText().toString().equalsIgnoreCase(decimal)){
                    toasMessage("Enter a valid number");
                    return;
                }

                if(Float.parseFloat(fishsize.getText().toString()) == 0 || Integer.parseInt(fishqty.getText().toString()) == 0){
                    toasMessage("PLEASE ENTER A NON ZERO NUMBER");
                    return;
                }

                if(fishName.equalsIgnoreCase("Betta (male)")){

                    if(Integer.parseInt(fishqty.getText().toString()) > 1){
                        toasMessage("You can only add 1 betta (male) in 1 aquarium");
                        return;
                    }

                }


                qty = 0;

                // stocking calculation

                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                Cursor data = databaseAccess.getFishID(fishName);

                while(data.moveToNext()) {

                    if (!fishsize.getText().toString().matches("")) {
                        if (Float.parseFloat(fishsize.getText().toString()) != 0) {
                            float finaltotal = fishinchtotal;
                            boolean overstock = false;
                            if (data.getString(11).equalsIgnoreCase("Slender")) {
                                if (finaltotal + Float.parseFloat(fishsize.getText().toString()) * 12 > surfacearea) {
                                    overstock = true;
                                }
                            } else {
                                if (finaltotal + Float.parseFloat(fishsize.getText().toString()) * 20 > surfacearea) {
                                    overstock = true;
                                }
                            }
                            if (overstock == false) {
                                while (finaltotal < surfacearea) {
                                    if (data.getString(11).equalsIgnoreCase("Slender")) {

                                        finaltotal += Float.parseFloat(fishsize.getText().toString()) * 12;

                                    } else {
                                        finaltotal += Float.parseFloat(fishsize.getText().toString()) * 20;
                                    }

                                    qty++;

                                    if (data.getString(11).equalsIgnoreCase("Slender")) {
                                        if (finaltotal + Float.parseFloat(fishsize.getText().toString()) * 12 > surfacearea) {
                                            break;
                                        }
                                    } else {
                                        if (finaltotal + Float.parseFloat(fishsize.getText().toString()) * 20 > surfacearea) {
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }

                }

                if(Float.parseFloat(fishqty.getText().toString()) > qty){

                    toasMessage("You can only add "+ qty + " " + fishName + " of this size");
                    return;
                }


                addConfirm(fishName);

            }
        });


    }

    public void SizeChangeListener()
    {
        fishsize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void RemainingFish(){

        stockinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuffer buffer = new StringBuffer();

                buffer.append("The existing number in the textfield is the average adult size of "+ fishName + ".");

                showMessage("INFORMATION", buffer.toString());
            }
        });

    }

    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void addConfirm(final String selectedFish)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("ADD CONFIRMATION");
        builder.setMessage("Adding " + selectedFish + "" + " to your aquarium");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                        databaseAccess.open();
                        Cursor data = databaseAccess.getFishID(selectedFish);
                        Cursor aqfdata = myDb.getFishData(aqID);
                        int fid = 0, fqty = Integer.parseInt(fishqty.getText().toString());
                        boolean updatefish = false;
                        while(aqfdata.moveToNext()){

                           if(aqfdata.getString(1).equalsIgnoreCase(selectedFish)){

                               if(aqfdata.getFloat(2) == Float.parseFloat(fishsize.getText().toString())){
                                   fid = aqfdata.getInt(0);
                                   fqty = aqfdata.getInt(12) + Integer.parseInt(fishqty.getText().toString()); ;
                                   updatefish = true;
                                   toasMessage(updatefish + "");

                               }

                           }

                        }

                        boolean fishAdded = false;

                        while(data.moveToNext())
                        {
                            fishAdded = myDb.insertFishData(data.getString(1), fishsize.getText().toString(), aqID, data.getFloat(3), data.getFloat(4),
                                    data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8), data.getString(9), data.getString(10),
                                    fqty, updatefish, fid, Integer.parseInt(fishqty.getText().toString()), data.getString(11));

                        }
                        if (fishAdded == true) {
                            Toast.makeText(AddFishPageFinal.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddFishPageFinal.this, Aquarium.class);
                            intent.putExtra("id", aqID);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddFishPageFinal.this, "Data not Inserted", Toast.LENGTH_LONG).show();
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



    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }


    public void fishImgID(String fishname)
    {
        if (fishname.equalsIgnoreCase("Gold Fish")) {
            imgid.setImageResource(R.drawable.goldfish);
        } else if (fishname.equalsIgnoreCase("Angel Fish")) {
            imgid.setImageResource(R.drawable.angelfish);
        } else if (fishname.equalsIgnoreCase("Betta (male)")) {
            imgid.setImageResource(R.drawable.bettas);
        } else if (fishname.equalsIgnoreCase("Koi")) {
            imgid.setImageResource(R.drawable.koi);
        } else if (fishname.equalsIgnoreCase("Guppy")) {
            imgid.setImageResource(R.drawable.guppy);
        } else if (fishname.equalsIgnoreCase("Oscar")) {
            imgid.setImageResource(R.drawable.oscar);
        } else if (fishname.equalsIgnoreCase("Clown Loach")) {
            imgid.setImageResource(R.drawable.loach);
        } else if (fishname.equalsIgnoreCase("Molly")) {
            imgid.setImageResource(R.drawable.molly);
        } else if (fishname.equalsIgnoreCase("Blue Gourami")) {
            imgid.setImageResource(R.drawable.blue_gourami);
        } else if(fishname.equalsIgnoreCase("Bala Shark")){
            imgid.setImageResource(R.drawable.shark);
        }  else if(fishname.equalsIgnoreCase("Betta (female)")){
            imgid.setImageResource(R.drawable.femalebetta);
        } else if(fishname.equalsIgnoreCase("Chocolate Gourami")){
            imgid.setImageResource(R.drawable.chocogourami);
        } else if(fishname.equalsIgnoreCase("Dwarf Gourami")){
            imgid.setImageResource(R.drawable.dwarfgourami);
        } else if(fishname.equalsIgnoreCase("Kissing Gourami")){
            imgid.setImageResource(R.drawable.kissinggourami);
        } else if(fishname.equalsIgnoreCase("Moonlight Gourami")){
            imgid.setImageResource(R.drawable.moonlightgourami);
        } else if(fishname.equalsIgnoreCase("Pearl Gourami")){
            imgid.setImageResource(R.drawable.pearlgourami);
        } else if(fishname.equalsIgnoreCase("Black Ruby Barb")){
            imgid.setImageResource(R.drawable.blackrubybarb);
        } else if(fishname.equalsIgnoreCase("Denison Barb")){
            imgid.setImageResource(R.drawable.denisonbarb);
        } else if(fishname.equalsIgnoreCase("Gold Barb")){
            imgid.setImageResource(R.drawable.goldbarb);
        } else if(fishname.equalsIgnoreCase("Rosy Barb")){
            imgid.setImageResource(R.drawable.rosybarb);
        } else if(fishname.equalsIgnoreCase("Tiger Barb")){
            imgid.setImageResource(R.drawable.tigerbarb);
        } else if(fishname.equalsIgnoreCase("Tinfoil Barb")){
            imgid.setImageResource(R.drawable.tinfoilbarb);
        } else if(fishname.equalsIgnoreCase("Zebra Barb")){
            imgid.setImageResource(R.drawable.zebrabarb);
        } else if(fishname.equalsIgnoreCase("Dwarf Spotted Danio")){
            imgid.setImageResource(R.drawable.dwarfspotteddanio);
        } else if(fishname.equalsIgnoreCase("Giant Danio")){
            imgid.setImageResource(R.drawable.giantdanio);
        } else if(fishname.equalsIgnoreCase("Pearl Danio")){
            imgid.setImageResource(R.drawable.pearldanio);
        } else if(fishname.equalsIgnoreCase("Rosy Danio")){
            imgid.setImageResource(R.drawable.rosydanio);
        } else if(fishname.equalsIgnoreCase("White Cloud Mountain Minnow")){
            imgid.setImageResource(R.drawable.whitecloudminnow);
        } else if(fishname.equalsIgnoreCase("Zebra Danio")){
            imgid.setImageResource(R.drawable.zebradanio);
        } else if(fishname.equalsIgnoreCase("Harlequin Rasbora")){
            imgid.setImageResource(R.drawable.harlequinrasbora);
        } else if(fishname.equalsIgnoreCase("Scissortail Rasbora")){
            imgid.setImageResource(R.drawable.scissortailrasbora);
        } else if(fishname.equalsIgnoreCase("Bandit Cory")){
            imgid.setImageResource(R.drawable.banditcory);
        } else if(fishname.equalsIgnoreCase("Bronze Cory")){
            imgid.setImageResource(R.drawable.bronzecory);
        } else if(fishname.equalsIgnoreCase("Julii Cory")){
            imgid.setImageResource(R.drawable.juliicory);
        } else if(fishname.equalsIgnoreCase("Panda Cory")){
            imgid.setImageResource(R.drawable.pandacory);
        } else if(fishname.equalsIgnoreCase("Pepper Cory")){
            imgid.setImageResource(R.drawable.peppercory);
        } else if(fishname.equalsIgnoreCase("Skunk Cory")){
            imgid.setImageResource(R.drawable.skunkcory);
        } else if(fishname.equalsIgnoreCase("Three Stripe Cory")){
            imgid.setImageResource(R.drawable.threestripecory);
        } else if(fishname.equalsIgnoreCase("Bristlenose Pleco")){
            imgid.setImageResource(R.drawable.bristlenosepleco);
        } else if(fishname.equalsIgnoreCase("Upside-Down Catfish")){
            imgid.setImageResource(R.drawable.upsidedowncatfish);
        } else if(fishname.equalsIgnoreCase("Black Neon Tetra")){
            imgid.setImageResource(R.drawable.blackneontetra);
        } else if(fishname.equalsIgnoreCase("Black Phantom Tetra")){
            imgid.setImageResource(R.drawable.blackphantomtetra);
        } else if(fishname.equalsIgnoreCase("Black Widow Tetra")){
            imgid.setImageResource(R.drawable.blackwidowtetra);
        } else if(fishname.equalsIgnoreCase("Buenos Aires Tetra")){
            imgid.setImageResource(R.drawable.buenosairestetra);
        } else if(fishname.equalsIgnoreCase("Red Neon Tetra")){
            imgid.setImageResource(R.drawable.redneontetra);
        } else if(fishname.equalsIgnoreCase("Emperor Tetra")){
            imgid.setImageResource(R.drawable.emperortetra);
        } else if(fishname.equalsIgnoreCase("Glowlight Tetra")){
            imgid.setImageResource(R.drawable.glowlighttetra);
        } else if(fishname.equalsIgnoreCase("Beacon tetra")){
            imgid.setImageResource(R.drawable.beacontetra);
        } else if(fishname.equalsIgnoreCase("Neon Tetra")){
            imgid.setImageResource(R.drawable.neontetra);
        } else if(fishname.equalsIgnoreCase("Red Eye Tetra")){
            imgid.setImageResource(R.drawable.redeyetetra);
        } else if(fishname.equalsIgnoreCase("Serpae Tetra")){
            imgid.setImageResource(R.drawable.serpaetetra);
        } else if(fishname.equalsIgnoreCase("Mickey Mouse Platy")){
            imgid.setImageResource(R.drawable.mickeymouseplaty);
        } else if(fishname.equalsIgnoreCase("Red Wagtail Platy")){
            imgid.setImageResource(R.drawable.redwagtailplaty);
        } else if(fishname.equalsIgnoreCase("Swordtail")){
            imgid.setImageResource(R.drawable.swordtail);
        } else if(fishname.equalsIgnoreCase("Coolie Loach")){
            imgid.setImageResource(R.drawable.coolieloach);
        } else if(fishname.equalsIgnoreCase("Horseface Loach ")){
            imgid.setImageResource(R.drawable.horsefaceloach);
        } else if(fishname.equalsIgnoreCase("Weather Loach")){
            imgid.setImageResource(R.drawable.weatherloach);
        } else if(fishname.equalsIgnoreCase("Yo Yo Loach")){
            imgid.setImageResource(R.drawable.yoyoloach);
        } else if(fishname.equalsIgnoreCase("Zebra Loach")){
            imgid.setImageResource(R.drawable.zebraloach);
        } else if(fishname.equalsIgnoreCase("Axelrods Rainbowfish")){
            imgid.setImageResource(R.drawable.axelrodrainbowfish);
        } else if(fishname.equalsIgnoreCase("Desert Rainbowfish")){
            imgid.setImageResource(R.drawable.desertrainbowfish);
        } else if(fishname.equalsIgnoreCase("Lake Kutubu Rainbowfish")){
            imgid.setImageResource(R.drawable.latekutuburainbowfish);
        } else if(fishname.equalsIgnoreCase("Lake Wanam Rainbowfish")){
            imgid.setImageResource(R.drawable.lakewanamrainbowfish);
        } else if(fishname.equalsIgnoreCase("Madagascar Rainbowfish")){
            imgid.setImageResource(R.drawable.madagascarrainbowfish);
        } else if(fishname.equalsIgnoreCase("Neon Rainbowfish")){
            imgid.setImageResource(R.drawable.neonrainbowfish);
        } else if(fishname.equalsIgnoreCase("Red Rainbowfish")){
            imgid.setImageResource(R.drawable.redrainbowfish);
        }
    }




}
