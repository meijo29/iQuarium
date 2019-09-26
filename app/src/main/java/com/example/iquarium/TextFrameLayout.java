package com.example.iquarium;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TextFrameLayout extends SurfaceView {

    private Activity context;

    public TextFrameLayout(Activity context){
        super(context, null, R.layout.fishmeasurementinfo);
        this.context = context;


    }


    @NonNull
    public View getView(@NonNull View convertView) {

        View r = convertView;

        TextFrameLayout.ViewHolder viewHolder = null;

        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout2, null, true);
            r.setBackgroundColor(Color.RED);
            viewHolder = new TextFrameLayout.ViewHolder(r);
            r.setTag(viewHolder);

        } else {

            viewHolder = (TextFrameLayout.ViewHolder) r.getTag();

        }
        return r;

    }


    class ViewHolder
    {

        TextView tvw1;
        ImageView ivw;
        Button checkbttn,detbttn;
        ViewHolder(View v)
        {


            //     checkbttn = v.findViewById(R.id.checkBttn);
            //    detbttn = v.findViewById(R.id.detBttn);

        }


    }


}



