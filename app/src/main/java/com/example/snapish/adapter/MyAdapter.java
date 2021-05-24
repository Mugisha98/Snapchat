package com.example.snapish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.snapish.R;
import com.example.snapish.model.Snap;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    //Storing the data of the object snap
    private List<Snap> items;
    //"inflate" layout file."Converts Xml files to java objects"
    private LayoutInflater layoutInflater;

    public MyAdapter(List<Snap> items, Context context) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }

    // getting the size of the items
    @Override
    public int getCount() {
        return items.size();
    }
    //getting the specific item
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
    // finding the id of each item
    @Override
    public long getItemId(int i) {
        return i;
    }

    //combining all the above methods to a view
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
        LinearLayout linearLayout = (LinearLayout)view;
        TextView textView = view.findViewById(R.id.textView1);
        if(textView != null) {
            textView.setText(items.get(i).getId()); // adding the items to the list view in "my_row.xml"
        }
        return linearLayout;
    }
}
