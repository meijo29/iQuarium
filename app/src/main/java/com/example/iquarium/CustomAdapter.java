package com.example.iquarium;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.logging.Level;

public class CustomAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<Fishes> fishes;
    DatabaseHelper myDb;
    Cursor aqfishdata;
    String name;
    Fishes  f;
    StringBuffer buffer;
    ArrayList<String> notcomp;
    DatabaseAccess databaseAccess;

    private LayoutInflater inflater;


    public CustomAdapter(Context c, ArrayList<Fishes> fishes)
    {
        this.c = c;
        this.fishes = fishes;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getGroupCount() {
        return fishes.size();
    }

    @Override
    public int getChildrenCount(int groupPosw) {
        return fishes.get(groupPosw).players.size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return fishes.get(groupPos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return fishes.get(groupPos).players.get(childPos);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.expandable_list_group, null);
        }

        f = (Fishes) getGroup(groupPosition);

        ImageView img = convertView.findViewById(R.id.fishImage);
        TextView nameTv =  convertView.findViewById(R.id.textViewSize);
        Button checkbttn = convertView.findViewById(R.id.checkbutton);

        name = f.Name;
        nameTv.setText(name);


        checkbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDb = new DatabaseHelper(c);
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(c);
                databaseAccess.open();
                Cursor aqfishdata = myDb.getFishData(f.aqID);
                Cursor aqdecordata = myDb.getAqDecorData(f.aqID);
                buffer = new StringBuffer();
                ArrayList<String> notcomp = new ArrayList<>();

                Fishes f = (Fishes) getGroup(groupPosition);


                while (aqfishdata.moveToNext())
                {
                    if(f.fishID != aqfishdata.getInt(0))
                    {
                        Cursor compdata = databaseAccess.getCompData(getGroup(groupPosition).toString());
                        while(compdata.moveToNext())
                        {
                            if(aqfishdata.getString(1).equalsIgnoreCase(compdata.getString(0)))
                            {
                                   if(!notcomp.contains(aqfishdata.getString(1))) {
                                       notcomp.add(aqfishdata.getString(1));
                                   }
                                   break;
                            }
                        }
                    }
                }

                while (aqdecordata.moveToNext())
                {
                    if(f.fishID != aqdecordata.getInt(0))
                    {
                        Cursor compdata = databaseAccess.getCompData(getGroup(groupPosition).toString());
                        while(compdata.moveToNext())
                        {
                            if(aqdecordata.getString(1).equalsIgnoreCase(compdata.getString(0)))
                            {
                                notcomp.add(aqdecordata.getString(1));
                                break;
                            }
                        }
                    }
                }

                if(!notcomp.isEmpty()) {
                    buffer.append(getGroup(groupPosition) + " is not compatible with: " + "\n");
                    for (int n = 0; n < notcomp.size(); n++) {
                        buffer.append(n + 1 + ". " + notcomp.get(n) + "\n");
                    }

                }
                else
                {
                    buffer.append("NO INCOMPATIBILITY FOUND");
                }

               showMessage("COMPATIBILITY", buffer.toString());
            }
        });


/*
        if(name.equalsIgnoreCase("Betta"))
        {
            img.setImageResource(R.drawable.bettas);
        }
        else if(name.equalsIgnoreCase("Angel Fish"))
        {
            img.setImageResource(R.drawable.angelfish);
        }
        else if(name.equalsIgnoreCase("Bala Shark"))
        {
            img.setImageResource(R.drawable.shark);
        }
        else if(name.equalsIgnoreCase("Gold Fish"))
        {
            img.setImageResource(R.drawable.goldfish);
        }
        else if(name.equalsIgnoreCase("Koi"))
        {
            img.setImageResource(R.drawable.koi);
        }
        else if(name.equalsIgnoreCase("Guppy"))
        {
            img.setImageResource(R.drawable.guppy);
        }
        else if(name.equalsIgnoreCase("Oscar"))
        {
            img.setImageResource(R.drawable.oscar);
        }
        else if(name.equalsIgnoreCase("Clown Loach"))
        {
            img.setImageResource(R.drawable.loach);
        }
        else if(name.equalsIgnoreCase("Molly"))
        {
            img.setImageResource(R.drawable.molly);
        }
        else if(name.equalsIgnoreCase("Blue Gourami"))
        {
            img.setImageResource(R.drawable.blue_gourami);
        }
*/
        return convertView;
    }


    @Override
    public View getChildView(int groupPos, int childPos, boolean b, View convertView, ViewGroup parent) {

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_layoutt, null);
        }

        Fishes f = (Fishes) getGroup(groupPos);

        String child = (String) getChild(groupPos, childPos);


        myDb = new DatabaseHelper(c);
        Cursor aqdata = myDb.getAqData(f.aqID);
        Integer aqPh = 0, aqTemp = 0;


        TextView nameTv,ph,size,temp,tankmin,level,qty,txtviewsize2,txtview11,txtview14,txtview17,txtview18,txtview12;
        txtview18 = convertView.findViewById(R.id.textView18);
        txtview12 = convertView.findViewById(R.id.textView12);
        txtview17 = convertView.findViewById(R.id.textView17);
        txtviewsize2 = convertView.findViewById(R.id.textViewSize2);
        txtview11 = convertView.findViewById(R.id.textView11);
        txtview14 = convertView.findViewById(R.id.textView14);
        nameTv = convertView.findViewById(R.id.textViewName);
        ph = convertView.findViewById(R.id.textViewpH);
        size = convertView.findViewById(R.id.textViewSize);
        temp = convertView.findViewById(R.id.textViewTemp);
        level = convertView.findViewById(R.id.textViewLevel);
        tankmin = convertView.findViewById(R.id.textViewTankMin);
        qty = convertView.findViewById(R.id.textViewQty);


        String Size, pH, Temp, TopD, MidD, BotD,TankMin,Qty;
        Size = f.size.toString() + " inches";
        Temp = f.tempMin + " - " + f.tempMax + " Celsius";
        pH = f.phMin + " - " + f.pHMax;
        TankMin = f.tankMin + " gallons";
        Qty = f.qty + "";
        TopD = f.topDwell;
        MidD = f.midDwell;
        BotD = f.botDwell;




        if(TopD.equalsIgnoreCase("Yes") && MidD.equalsIgnoreCase("Yes") && BotD.equalsIgnoreCase("Yes"))
        {
            level.setText("All Levels");
        }
        else{

            if(TopD.equalsIgnoreCase("No") && BotD.equalsIgnoreCase("Yes") && MidD.equalsIgnoreCase("Yes"))
            {
                level.setText("Bottom and Mid Dweller");
            }
            else if(BotD.equalsIgnoreCase("No") && TopD.equalsIgnoreCase("Yes") && MidD.equalsIgnoreCase("Yes"))
            {
                level.setText("Top and Mid Dweller");
            }
            else if(MidD.equalsIgnoreCase("No") && TopD.equalsIgnoreCase("Yes") && BotD.equalsIgnoreCase("Yes")){
                level.setText("Top and Bottom Dweller");
            }
            else if(TopD.equalsIgnoreCase("No") && MidD.equalsIgnoreCase("No") && BotD.equalsIgnoreCase("Yes"))
            {
                level.setText("Bottom Dweller");
            }
            else if(TopD.equalsIgnoreCase("No") && BotD.equalsIgnoreCase("No") && MidD.equalsIgnoreCase("Yes"))
            {
                level.setText("Mid Dweller");
            }
            else if(MidD.equalsIgnoreCase("No") && BotD.equalsIgnoreCase("No") && TopD.equalsIgnoreCase("Yes"))
            {
                level.setText("Top Dweller");
            }

        }

        if(f.tankMin > f.aqGal)
        {
            tankmin.setTextColor(Color.RED);
        }
        else
        {
            tankmin.setTextColor(Color.BLACK);
        }

        while(aqdata.moveToNext())
        {
              if(aqdata.getFloat(7) >= f.phMin && aqdata.getFloat(7) <= f.pHMax)
              {
                  ph.setTextColor(Color.BLACK);
              }
              else
              {
                  ph.setTextColor(Color.RED);
              }

              if(aqdata.getFloat(8) >= f.tempMin && aqdata.getFloat(8) <= f.tempMax)
              {
                  temp.setTextColor(Color.BLACK);
              }
              else
              {
                  temp.setTextColor(Color.RED);
              }

        }

        nameTv.setText(child);
        size.setText(Size);
        temp.setText(Temp);
        ph.setText(pH);
        tankmin.setText(TankMin);
        qty.setText(Qty);

        if(f.res.equalsIgnoreCase("decoration"))
        {
              nameTv.setVisibility(View.GONE);
              size.setVisibility(View.GONE);
              temp.setVisibility(View.GONE);
              ph.setVisibility(View.GONE);
              tankmin.setVisibility(View.GONE);
              qty.setVisibility(View.GONE);
              txtviewsize2.setVisibility(View.GONE);
              txtview11.setVisibility(View.GONE);
              txtview12.setVisibility(View.GONE);
              txtview14.setVisibility(View.GONE);
              txtview17.setVisibility(View.GONE);
              txtview18.setVisibility(View.GONE);
              level.setVisibility(View.GONE);
        }
        else if(f.res.equalsIgnoreCase("fish"))
        {
            nameTv.setVisibility(View.VISIBLE);
            size.setVisibility(View.VISIBLE);
            temp.setVisibility(View.VISIBLE);
            ph.setVisibility(View.VISIBLE);
            tankmin.setVisibility(View.VISIBLE);
            qty.setVisibility(View.VISIBLE);
            txtviewsize2.setVisibility(View.VISIBLE);
            txtview11.setVisibility(View.VISIBLE);
            txtview12.setVisibility(View.VISIBLE);
            txtview14.setVisibility(View.VISIBLE);
            txtview17.setVisibility(View.VISIBLE);
            txtview18.setVisibility(View.VISIBLE);
            level.setVisibility(View.VISIBLE);

        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }


    public void showMessage(String title, String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    private void toasMessage(String message)
    {
        Toast.makeText(c,message, Toast.LENGTH_LONG).show();
    }


}
