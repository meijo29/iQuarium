package com.example.iquarium;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

public class CreateAquarium extends AppCompatActivity {

    DatabaseHelper myDb;
    private Button btnCrt;
    private EditText name, length, width, height, pH, temp;
    private ImageButton backbttn;
    private TextView txtph, txttemp;
    int Aquariumqty = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_aquarium);
        myDb = new DatabaseHelper(this);
        name = findViewById(R.id.cAqname);
        length = findViewById(R.id.cAqlength);
        width = findViewById(R.id.cAqwidth);
        height = findViewById(R.id.cAqheight);
        pH = findViewById(R.id.cAqph);
        txtph = findViewById(R.id.editheight2);
        txttemp = findViewById(R.id.editheight3);
        temp = findViewById(R.id.cAqtemp);
        btnCrt = findViewById(R.id.createBttn);

        phTempControl();
        phTempControl2();

        AddData();


    }


    @Override
    public void onBackPressed()
    {
        Intent MyAquariumPage = new Intent(CreateAquarium.this, MyAquarium.class) ;
        startActivity(MyAquariumPage);

    }


    public void AddData()
    {
        btnCrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                Cursor aqdata = myDb.getData();
                if(n.matches("")) {
                    Toast.makeText(CreateAquarium.this, "AQUARUIM NAME IS REQUIRED", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    while (aqdata.moveToNext())
                    {
                        if(n.equalsIgnoreCase(aqdata.getString(1)))
                        {
                            Toast.makeText(CreateAquarium.this, "AQUARIUM NAME ALREADY EXIST", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }

                if(length.getText().toString().equalsIgnoreCase(".") ){
                    Toast.makeText(CreateAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(width.getText().toString().equalsIgnoreCase(".")){
                    Toast.makeText(CreateAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(height.getText().toString().equalsIgnoreCase(".")){
                    Toast.makeText(CreateAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(pH.getText().toString().equalsIgnoreCase(".")){
                    Toast.makeText(CreateAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(temp.getText().toString().equalsIgnoreCase(".")){
                    Toast.makeText(CreateAquarium.this, "Enter a valid number", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!length.getText().toString().matches("") || !width.getText().toString().matches("") || !height.getText().toString().matches("")) {

                    if (length.getText().toString().matches("")) {

                        Toast.makeText(CreateAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                        return;
                    } else if (width.getText().toString().matches("")) {
                        Toast.makeText(CreateAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                        return;
                    } else if (height.getText().toString().matches("")) {
                        Toast.makeText(CreateAquarium.this, "Please fill the remaining measurement", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(length.getText().toString()) < 12){
                        Toast.makeText(CreateAquarium.this, "The minimum aquarium length is 12 inches", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(width.getText().toString()) < 6){
                        Toast.makeText(CreateAquarium.this, "The minimum aquarium width is 6 inches", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(height.getText().toString()) < 8){
                        Toast.makeText(CreateAquarium.this, "The minimum aquarium height is 8 inches", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(length.getText().toString()) > 72){
                        Toast.makeText(CreateAquarium.this, "The maximum aquarium length is 72 inches", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(width.getText().toString()) > 24){
                        Toast.makeText(CreateAquarium.this, "The maximum aquarium width is 24 inches", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(Float.parseFloat(height.getText().toString()) > 25){
                        Toast.makeText(CreateAquarium.this, "The maximum aquarium height is 25 inches", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                 Aquariumqty = AquariumQTY();
                if(Aquariumqty == 10)
                {
                    toasMessage("Only 10 virtual aquarium can be created");
                    return;
                }

                boolean isInserted = myDb.insertData(name.getText().toString(), length.getText().toString(), width.getText().toString(), height.getText().toString(), pH.getText().toString(),
                temp.getText().toString());

                if (isInserted = true) {
                    Toast.makeText(CreateAquarium.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateAquarium.this, MyAquarium.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAquarium.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                }

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
            pH.setFocusableInTouchMode(true);
            temp.setFocusableInTouchMode(true);
            txtph.setTextColor(Color.parseColor("#000000"));
            txttemp.setTextColor(Color.parseColor("#000000"));

        }
        else{
            pH.setFocusableInTouchMode(false);
            temp.setFocusableInTouchMode(false);
            txtph.setTextColor(Color.parseColor("#929c95"));
            txttemp.setTextColor(Color.parseColor("#929c95"));
        }

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


    public int AquariumQTY(){

        Cursor data = myDb.getData();
        int aqqty = 0;
        while (data.moveToNext()){

            aqqty++;

        }

        return  aqqty;
    }

    public void phTempControl() {

        if (length.getText().toString().matches("") && width.getText().toString().matches("")
                && height.getText().toString().matches("")) {

            pH.setFocusableInTouchMode(false);
            temp.setFocusableInTouchMode(false);
            txtph.setTextColor(Color.parseColor("#929c95"));
            txttemp.setTextColor(Color.parseColor("#929c95"));

        }
    }

    private void toasMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

}
