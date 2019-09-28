package com.example.iquarium;

import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditAquarium extends AppCompatActivity {

    DatabaseHelper myDb;
    private int selectedID,aqlength,aqwidth,aqheight, fqty;
    private String selectedAquarium;
    private EditText id,length,name,height,width,ph, temp;
    private Button edtButton;
    String textph = "", texttemp = "";
    private ImageButton backbttn;
    private TextView txtph, txttemp;
    boolean updatename = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_aquarium);
        Intent receivedIntent = getIntent();
        myDb = new DatabaseHelper(this);

    name = (EditText) findViewById(R.id.cAqname);
    length = (EditText) findViewById(R.id.cAqlength);
    height = (EditText) findViewById(R.id.cAqheight);
    width = (EditText) findViewById(R.id.cAqwidth);
    edtButton = (Button) findViewById(R.id.createBttn);
    ph = findViewById(R.id.cAqph);
    temp = findViewById(R.id.cAqtemp);
    txtph = findViewById(R.id.editheight2);
    txttemp = findViewById(R.id.editheight3);
    selectedID = receivedIntent.getIntExtra("id", -1);

    selectedAquarium = receivedIntent.getStringExtra("name");
    aqlength = receivedIntent.getIntExtra("length", - 1);
    aqwidth = receivedIntent.getIntExtra("width", - 1);
    aqheight = receivedIntent.getIntExtra("height", -1);
    phTempControl();
    phTempControl2();
    Cursor data = myDb.getAqData(selectedID);

    while (data.moveToNext()) {
        name.setText(selectedAquarium);
        if(data.getFloat(2) != 0) {
            length.setText(data.getFloat(2) + "");
        }
        if(data.getFloat(3) != 0){
            width.setText(data.getFloat(3) + "");
        }
        if(data.getFloat(4) != 0) {
            height.setText(data.getFloat(4) + "");
        }
            ph.setText(data.getFloat(6) + "");
            if(data.getFloat(7) != 0) {
                temp.setText(data.getFloat(7) + "");
            }
            fqty = data.getInt(5);



    }

    edtButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Cursor aqdata = myDb.getData();
            if(name.getText().toString().matches(""))
            {
                Toast.makeText(EditAquarium.this, "AQUARUIM NAME IS REQUIRED", Toast.LENGTH_LONG).show();
                return;
            }
            else
            {
                while (aqdata.moveToNext())
                {
                    if(name.getText().toString().equalsIgnoreCase(aqdata.getString(1)) && selectedID != aqdata.getInt(0))
                    {
                        Toast.makeText(EditAquarium.this, "AQUARIUM NAME ALREADY EXIST", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

             if(length.getText().toString().equalsIgnoreCase(".") ){
                Toast.makeText(EditAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                return;
            }
            else if(width.getText().toString().equalsIgnoreCase(".")){
                Toast.makeText(EditAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                return;
            }
            else if(height.getText().toString().equalsIgnoreCase(".")){
                Toast.makeText(EditAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                return;
            }
             else if(ph.getText().toString().equalsIgnoreCase(".")){
                 Toast.makeText(EditAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                 return;
             }
             else if(temp.getText().toString().equalsIgnoreCase(".")){
                 Toast.makeText(EditAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                 return;
             }

            Cursor data = myDb.getAqData(selectedID);

            while (data.moveToNext()) {

               /* if(data.getFloat(2) == Integer.parseInt(length.getText().toString()) && data.getFloat(3) == Integer.parseInt(width.getText().toString())
                && data.getFloat(4) == Integer.parseInt(height.getText().toString()) && data.getFloat(6) == Integer.parseInt(ph.getText().toString())
                        && data.getFloat(7) == Integer.parseInt(temp.getText().toString())){*/
               if(!length.getText().toString().matches("") && !width.getText().toString().matches("") && !height.getText().toString().matches("")) {
                   if (data.getFloat(2) == Float.parseFloat(length.getText().toString()) && data.getFloat(3) == Float.parseFloat(width.getText().toString())
                   && data.getFloat(4) == Float.parseFloat(height.getText().toString())) {
                       updatename = true;
                   }
               }


               if(length.getText().toString().matches("") && width.getText().toString().matches("") && height.getText().toString().matches("")
               && data.getFloat(2) == 0 && data.getFloat(3) == 0 && data.getFloat(4) == 0){
                   updatename = true;
               }


                if(updatename == true)
                {
                    float textph = 0, texttemp = 0;
                    if(!name.getText().toString().matches("") || !ph.getText().toString().matches("") || !temp.getText().toString().matches("")) {

                        if(ph.getText().toString().matches("")){
                            textph = 0;
                        }
                        else {
                            textph = Float.parseFloat(ph.getText().toString());

                            if(textph < 5 && textph != 0){
                                toasMessage("The minimum aquarium pH level is 5");
                                return;
                            }
                            else if(textph > 9 && textph != 0){
                                toasMessage("The maximum aquarium pH level is 9");
                                return;
                            }
                        }

                        if(temp.getText().toString().matches("")){
                            texttemp = 0;
                        }
                        else{
                            texttemp = Float.parseFloat(temp.getText().toString());

                            if(texttemp < 20 && texttemp != 0){
                                toasMessage("The mimimum aquarium temperature is 20");
                                return;
                            }
                            else if(texttemp > 33 && texttemp != 0){
                                toasMessage("The maximum aquarium temperature is 33");
                            }

                        }

                        if (!data.getString(1).equalsIgnoreCase(name.getText().toString()) || data.getFloat(6) != textph
                             || data.getFloat(7) != texttemp) {


                            if(data.getFloat(6) != textph  && data.getFloat(7) != texttemp)
                            {
                                if(textph == 0 && texttemp == 0){
                                    EditNameOnly();
                                    return;
                                }
                                else {
                                    EditTempPHConfirm();
                                    return;
                                }
                            }
                            else if(data.getFloat(6) != textph){

                                if(textph == 0)
                                {
                                    EditNameOnly();
                                    return;
                                }
                                else{
                                    EditTempPHConfirm();
                                    return;
                                }
                            }
                            else if(data.getFloat(7) != texttemp){

                                if(texttemp == 0)
                                {
                                    EditNameOnly();
                                    return;
                                }
                                else{
                                    EditTempPHConfirm();
                                    return;
                                }
                            }
                            else{
                                EditNameOnly();
                                return;
                            }
                        } else {
                            Intent intent = new Intent(EditAquarium.this, Aquarium.class);
                            intent.putExtra("id", selectedID);
                            intent.putExtra("name", name.getText().toString());
                            startActivity(intent);
                            return;
                        }
                    }
                    else{
                        if(ph.getText().toString().matches("") || temp.getText().toString().matches("")){

                            boolean isUpdate = myDb.updateAquarium(selectedID, name.getText().toString(), length.getText().toString(), width.getText().toString(), height.getText().toString(),
                                    ph.getText().toString(), temp.getText().toString());

                            if (isUpdate == true) {

                             Toast.makeText(EditAquarium.this, "Data Updated Else", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(EditAquarium.this, Aquarium.class);
                                intent.putExtra("id", selectedID);
                                intent.putExtra("name", name.getText().toString());

                                startActivity(intent);
                                return;

                            } else {
                                Toast.makeText(EditAquarium.this, "Aquarium Name Not Updated", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }

            if(!length.getText().toString().matches("") || !width.getText().toString().matches("") || !height.getText().toString().matches(""))
                   {

                if (length.getText().toString().matches("")) {

                    Toast.makeText(EditAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                    return;
                } else if (width.getText().toString().matches("")) {
                    Toast.makeText(EditAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                    return;
                } else if (height.getText().toString().matches("")) {
                    Toast.makeText(EditAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(length.getText().toString()) < 5){
                    Toast.makeText(EditAquarium.this, "The minimum aquarium length is 12 inches", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(width.getText().toString()) < 5){
                    Toast.makeText(EditAquarium.this, "The minimum aquarium width is 6 inches", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(height.getText().toString()) < 5){
                    Toast.makeText(EditAquarium.this, "The minimum aquarium height is 8 inches", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(length.getText().toString()) > 200){
                    Toast.makeText(EditAquarium.this, "The maximum aquarium length is 200 inches", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(width.getText().toString()) > 200){
                    Toast.makeText(EditAquarium.this, "The maximum aquarium width is 200 inches", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(Float.parseFloat(height.getText().toString()) > 200){
                    Toast.makeText(EditAquarium.this, "The maximum aquarium height is 200 inches", Toast.LENGTH_LONG).show();
                    return;
                }

              float gal = 0;
              float volume = 0;
              volume = Float.parseFloat(length.getText().toString()) * Float.parseFloat(width.getText().toString()) * Float.parseFloat(height.getText().toString());
              gal = volume / 231;

              if(gal > 180){
                  toasMessage("The maximum aquarium size is only 180 gallons");
                  return;
              }

          }
            EditConfirm();
        }

    });

    }

    public void EditNameOnly(){

        boolean isUpdate = myDb.updateAquarium(selectedID, name.getText().toString(), length.getText().toString(), width.getText().toString(), height.getText().toString(), ph.getText().toString(),
                temp.getText().toString());

        if (isUpdate == true) {

            Toast.makeText(EditAquarium.this, "Data Updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditAquarium.this, Aquarium.class);
            intent.putExtra("id", selectedID);
            intent.putExtra("name", name.getText().toString());

            startActivity(intent);
            return;

        } else {
            Toast.makeText(EditAquarium.this, "Aquarium Name Not Updated", Toast.LENGTH_LONG).show();
        }
    }

    public void EditTempPHConfirm(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("EDIT CONFIRMATION");
        builder.setMessage("Changing the aquarium pH Level or Temperature will remove the existing fish in the aquarium that are not compatible with new pH Level or temperature data");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean isUpdate = myDb.updateAquarium(selectedID, name.getText().toString(), length.getText().toString(), width.getText().toString(), height.getText().toString(), ph.getText().toString(),
                                temp.getText().toString());

                        Cursor fishdata = myDb.getFishData(selectedID);

                        Cursor aqdata = myDb.getAqData(selectedID);
                        while(aqdata.moveToNext()) {

                            if (aqdata.getFloat(6) != 0) {
                                // Looping for delete fish that are not compatible with the new pH level of aquarium
                                while (fishdata.moveToNext()) {

                                    if (Float.parseFloat(ph.getText().toString()) >= fishdata.getFloat(3) && Float.parseFloat(ph.getText().toString()) <= fishdata.getFloat(4)) {

                                    } else {
                                        myDb.deleteFishAquarium(fishdata.getInt(0), selectedID, fishdata.getInt(12));

                                    }

                                }
                            }

                            if (aqdata.getFloat(7) != 0) {
                                fishdata = myDb.getFishData(selectedID);
                                // Looping for delete fish that are not compatible with the new temperature of aquarium
                                while (fishdata.moveToNext()) {

                                    if (Float.parseFloat(temp.getText().toString()) >= fishdata.getFloat(5) && Float.parseFloat(temp.getText().toString()) <= fishdata.getFloat(6)) {

                                    } else {
                                        myDb.deleteFishAquarium(fishdata.getInt(0), selectedID, fishdata.getInt(12));
                                    }

                                }
                            }
                        }

                        if (isUpdate == true) {

                            Toast.makeText(EditAquarium.this, "Data Updated", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EditAquarium.this, Aquarium.class);
                            intent.putExtra("id", selectedID);
                            intent.putExtra("name", name.getText().toString());

                            startActivity(intent);
                            return;

                        } else {
                            Toast.makeText(EditAquarium.this, "Aquarium Name Not Updated", Toast.LENGTH_LONG).show();
                        }

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                return;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void phTempControl2(){

      length.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              ShowingPhTemp();
          }
      });

      width.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              ShowingPhTemp();
          }
      });

      height.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {
              ShowingPhTemp();
          }
      });

    }

    public void ShowingPhTemp(){

        if(!length.getText().toString().matches("") && !length.getText().toString().equals(".") &&
                !width.getText().toString().matches("") && !width.getText().toString().equals(".") &&
                !height.getText().toString().matches("") && !height.getText().toString().equals(".") &&
                Float.parseFloat(length.getText().toString()) != 0
                && Float.parseFloat(width.getText().toString()) != 0
                && Float.parseFloat(height.getText().toString()) != 0) {
            ph.setFocusableInTouchMode(true);
            temp.setFocusableInTouchMode(true);
            txtph.setTextColor(Color.parseColor("#000000"));
            txttemp.setTextColor(Color.parseColor("#000000"));
        }
        else{
            ph.setFocusableInTouchMode(false);
            temp.setFocusableInTouchMode(false);
            txtph.setTextColor(Color.parseColor("#929c95"));
            txttemp.setTextColor(Color.parseColor("#929c95"));
        }

    }

    public void phTempControl(){

        Cursor aqdata = myDb.getAqData(selectedID);

       while (aqdata.moveToNext()){

            if(aqdata.getFloat(2) == 0)
            {
                ph.setFocusableInTouchMode(false);
                temp.setFocusableInTouchMode(false);
                txtph.setTextColor(Color.parseColor("#929c95"));
                txttemp.setTextColor(Color.parseColor("#929c95"));
            }

       }

    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    public void EditConfirm(){

        textph = ph.getText().toString();
        texttemp = temp.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("EDIT CONFIRMATION");
        Cursor aqdata = myDb.getAqData(selectedID);
        while (aqdata.moveToNext()){

            if(aqdata.getFloat(2) == 0 && aqdata.getFloat(3) == 0 && aqdata.getFloat(4) == 0){
                builder.setMessage("Setting up the aquarium size will remove all existing fish");
            }
            else
            {
                builder.setMessage("Changing aquarium size will remove all existing fish");
            }

        }


        if(length.getText().toString().matches("") && width.getText().toString().matches("") && height.getText().toString().matches("")){
            textph = "";
            texttemp = "";
            builder.setMessage("Removing aquarium size will remove all existing fish, ph and temperature data");
        }
        else if(Float.parseFloat(length.getText().toString()) == 0 && Float.parseFloat(width.getText().toString()) == 0 && Float.parseFloat(height.getText().toString()) == 0){
            textph = "";
            texttemp = "";
            builder.setMessage("Removing aquarium size will remove all existing fish, ph and temperature data");
        }

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                boolean isUpdate = myDb.updateAquarium(selectedID, name.getText().toString(), length.getText().toString(), width.getText().toString(), height.getText().toString(), textph, texttemp);

                if(isUpdate == true) {

                    Toast.makeText(EditAquarium.this, "Data Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditAquarium.this, Aquarium.class);
                    intent.putExtra("id", selectedID);
                    intent.putExtra("name", name.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(EditAquarium.this, "Data Not Updated", Toast.LENGTH_LONG).show();
                }

                myDb.RemoveAllFish(selectedID, fqty);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Aquarium.class);
        intent.putExtra("id", selectedID);
        startActivity(intent);

    }


}
