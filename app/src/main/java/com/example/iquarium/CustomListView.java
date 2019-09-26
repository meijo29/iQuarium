package com.example.iquarium;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {

    DatabaseHelper myDb;
    private ArrayList<String> fishName = new ArrayList<>();
    private ArrayList<Integer> imgID;
    private ArrayList<Integer> arrayFishID;
    private Integer aqID, aqGal;
    boolean aquariumpage = false;
    TextView txtView;
    ArrayList<String> cautionfishes = new ArrayList<>();
    private Activity context;
        public CustomListView(Activity context, ArrayList<String> fishName, ArrayList<Integer> imgID, boolean aquariumpage, ArrayList<Integer> arrayFishID, Integer aqID, Integer aqGal, ArrayList<String> cautionfishes) {
            super(context, R.layout.listview_layout2, fishName);

            this.context = context;
            this.fishName = fishName;
            this.imgID = imgID;
            this.aquariumpage = aquariumpage;
            this.arrayFishID = arrayFishID;
            this.aqID = aqID;
            this.aqGal = aqGal;
            this.cautionfishes = cautionfishes;

        }

    @NonNull
    @Override
    public View getView(final int position, @NonNull View convertView, @NonNull ViewGroup parent) {


       View r = convertView;

       ViewHolder viewHolder = null;

       if( r == null)
       {
           LayoutInflater layoutInflater = context.getLayoutInflater();
           r = layoutInflater.inflate(R.layout.listview_layout2, null, true);
           r.setBackgroundColor(Color.RED);
           viewHolder = new ViewHolder(r);
           r.setTag(viewHolder);

       }
        else
       {

           viewHolder = (ViewHolder) r.getTag();

       }

        viewHolder.ivw.setImageResource(imgID.get(position));
       viewHolder.tvw1.setText(fishName.get(position));

/*



        if (aquariumpage == false)
        {
            viewHolder.checkbttn.setVisibility(View.GONE);
            viewHolder.detbttn.setVisibility(View.GONE);
        }
        else if(aquariumpage == true)
        {
            viewHolder.checkbttn.setVisibility(View.VISIBLE);
            viewHolder.detbttn.setVisibility(View.VISIBLE);
        }
        */
/*
        viewHolder.checkbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer buffer = new StringBuffer();
                myDb = new DatabaseHelper(getContext());
                boolean phnot = false, tanknot = false, tempnot = false;
                //Toast.makeText(getContext(), fishName.get(position) +" " + arrayFishID.get(position), Toast.LENGTH_SHORT).show();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext());
                databaseAccess.open();
                Cursor aqfishdata = myDb.getFishData(aqID);
                ArrayList<String> notcomp = new ArrayList<>();
                while(aqfishdata.moveToNext())
                {
                    if(!fishName.get(position).equalsIgnoreCase(aqfishdata.getString(1)))
                    {
                        Cursor compdata = databaseAccess.getCompData(fishName.get(position));
                        while(compdata.moveToNext())
                        {

                            if (aqfishdata.getString(1).equalsIgnoreCase(compdata.getString(0)))
                            {
                                notcomp.add(aqfishdata.getString(1));
                                break;
                            }
                        }
                    }

                }

                phnot = checkPhComp(position);
                tanknot = checkTankMin(position);
                tempnot = checkTemp(position);

                if(notcomp.isEmpty() && phnot == false && tanknot == false && tempnot == false)
                {
                    showMessage("RESULT", "No incompatibility found");
                    return;
                }
                else {
                    if(!notcomp.isEmpty()) {

                        buffer.append(fishName.get(position) + " is not compatible with: " + "\n");
                        for (int n = 0; n < notcomp.size(); n++) {
                            buffer.append(n + 1 + ". " + notcomp.get(n) + "\n");
                        }
                    }
                    if(phnot  == true)
                    {
                        buffer.append("\n" + "Aquarium pH level is not prefered for "+ fishName.get(position) + "\n\n");
                    }

                    if(tanknot == true)
                    {
                        buffer.append("\n" + "Aquarium tank size is not prefered for "+ fishName.get(position) + "\n\n");
                    }
                    if(tempnot == true)
                    {
                        buffer.append("\n" + "Aquarium temperature is not prefered for "+ fishName.get(position) + "\n\n");
                    }


                    showMessage(  "STATUS", buffer.toString());
                }

                databaseAccess.close();
            }
        });

     viewHolder.detbttn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             myDb = new DatabaseHelper(getContext());
             Cursor aqfishdata = myDb.getOneFishData(aqID, arrayFishID.get(position));
             Cursor aqdata = myDb.getAqData(aqID);
             StringBuffer buffer = new StringBuffer();
             String red = "this is red";

             while (aqfishdata.moveToNext())
           {

               buffer.append("Type: " + aqfishdata.getString(1) + "\n");
               buffer.append("Size: " + aqfishdata.getInt(2) + " inches" + "\n");
               buffer.append("pH: " + aqfishdata.getFloat(3) + " - " + aqfishdata.getFloat(4) + "\n");
               buffer.append("Temperature: " + aqfishdata.getInt(5) + " - " + aqfishdata.getInt(6) + "C\n");
              buffer.append("Minimum Tank Size: " + aqfishdata.getInt(7) + " gallons\n");
              buffer.append("Tank Level: ");


               if( (aqfishdata.getString(8).equals("Yes")) && (aqfishdata.getString(9).equals("Yes")) && (aqfishdata.getString(10).equals("Yes"))) {

                   buffer.append("All levels");
               }
               else
               {
                   if (aqfishdata.getString(8).equals("Yes")) {
                       buffer.append("top ");
                   }
                   if (aqfishdata.getString(9).equals("Yes")) {
                       buffer.append("mid ");
                   }
                   if (aqfishdata.getString(10).equals("Yes")) {
                       buffer.append("bottom ");
                   }

                   buffer.append("dweller");
               }

           }

             showMessage("FISH DETAILS",buffer.toString());

         }
     });
*/
        return r;

    }



    public boolean checkPhComp(int position)
    {
        Cursor aqdata = myDb.getAqData(aqID);
        Cursor aqfishdata = myDb.getOneFishData(aqID, arrayFishID.get(position));
        boolean notpref = true;

        while (aqdata.moveToNext())
        {
            while(aqfishdata.moveToNext()) {

                if ((aqdata.getFloat(7) >= aqfishdata.getFloat(3)) && ((aqdata.getFloat(7) <= aqfishdata.getFloat(4)))) {
                    notpref = false;
                }else{
                    notpref = true;
                }

            }
        }

        return  notpref;
    }

    private void toasMessage(String message)
    {
        Toast.makeText(getContext(),message, Toast.LENGTH_LONG).show();
    }

    public boolean checkTankMin(int position)
    {
        Cursor aqdata = myDb.getAqData(aqID);
        Cursor aqfishdata = myDb.getOneFishData(aqID, arrayFishID.get(position));
        boolean notpref = true;

        while (aqdata.moveToNext())
        {
            while(aqfishdata.moveToNext()) {

                if (aqGal > aqfishdata.getInt(7)) {
                    notpref = false;
                }else{
                    notpref = true;
                }

            }
        }

        return  notpref;

    }

    public boolean checkTemp(int position)
    {
        Cursor aqdata = myDb.getAqData(aqID);
        Cursor aqfishdata = myDb.getOneFishData(aqID, arrayFishID.get(position));
        boolean notpref = true;

        while (aqdata.moveToNext())
        {
            while(aqfishdata.moveToNext()) {

                if ((aqdata.getFloat(8) >= aqfishdata.getFloat(5)) && ((aqdata.getFloat(8) <= aqfishdata.getFloat(6)))) {
                    notpref = false;
                }else{
                    notpref = true;
                }

            }
        }

        return  notpref;
    }


    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }



    class ViewHolder
    {

        TextView tvw1;
        ImageView ivw;
        Button checkbttn,detbttn;
        ViewHolder(View v)
        {
            tvw1 = v.findViewById(R.id.textViewSize);
            ivw = v.findViewById(R.id.fishImage);

       //     checkbttn = v.findViewById(R.id.checkBttn);
        //    detbttn = v.findViewById(R.id.detBttn);

        }


    }


}
