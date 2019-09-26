package com.example.iquarium;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, List<String>> mStringListHashMap;
    private String[] mListHeaderGroup;
    private ArrayList<Integer> imgID;

    public MyExpandableListAdapter(HashMap<String, List<String>> stringListHashMap){
        mStringListHashMap = stringListHashMap;
        mListHeaderGroup = mStringListHashMap.keySet().toArray(new String[0]);
    }



    @Override
    public int getGroupCount() {
        return mListHeaderGroup.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return mStringListHashMap.get(mListHeaderGroup[i]).size();
    }

    @Override
    public Object getGroup(int i) {
        return mListHeaderGroup[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return mStringListHashMap.get(mListHeaderGroup[i]).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expandable_list_group, viewGroup, false);

        }

        TextView textView = view.findViewById(R.id.textViewSize);
        textView.setText(String.valueOf(getGroup(i)));



        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        if(view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_layoutt, viewGroup, false);

            TextView textView = view.findViewById(R.id.textViewSize);
            ImageView imgView = view.findViewById(R.id.fishImage);

            if((getGroup(groupPosition).toString()).equalsIgnoreCase("Betta"))
           {
                imgView.setImageResource(R.drawable.bettas);
           }
           else if((getGroup(groupPosition).toString()).equalsIgnoreCase("Angel Fish"))
           {
                imgView.setImageResource(R.drawable.angelfish);
           }

            textView.setText(String.valueOf(getChild(groupPosition, childPosition)));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
