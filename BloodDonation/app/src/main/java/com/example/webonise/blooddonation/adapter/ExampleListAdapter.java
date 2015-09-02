package com.example.webonise.blooddonation.adapter;

/**
 * Created by webonise on 2/9/15.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.app.Activity;

import com.example.webonise.blooddonation.model.ExampleListItem;
import com.example.webonise.blooddonation.R;

import java.util.List;

public class ExampleListAdapter extends ArrayAdapter {

    private Context context;
    private boolean useList = true;

    public ExampleListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView titleText;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ExampleListItem item = (ExampleListItem)getItem(position);
        View viewToUse = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.fragment_item_list_item, null);
            } else {
                /*viewToUse = mInflater.inflate(R.layout.example_grid_item, null);*/
            }

            holder = new ViewHolder();
            holder.titleText = (TextView)viewToUse.findViewById(R.id.text);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.titleText.setText(item.getItemTitle());
        return viewToUse;
    }
}
