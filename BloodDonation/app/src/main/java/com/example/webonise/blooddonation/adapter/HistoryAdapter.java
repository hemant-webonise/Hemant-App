package com.example.webonise.blooddonation.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webonise.blooddonation.HistoryActivityWithSQL;
import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.model.History;

import java.util.List;

/**
 * Created by webonise on 10/9/15.
 */
public class HistoryAdapter extends BaseAdapter{

    List<History> historyList;
    Context context;

    public HistoryAdapter(List<History> historyList, HistoryActivityWithSQL context) {
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_history, null);
        }
        TextView tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        ImageButton  btnImage=(ImageButton) view.findViewById(R.id.btnImage);
        btnImage.setImageBitmap(BitmapFactory.decodeFile(historyList.get(i).getImage()));
        tvLocation.setText(historyList.get(i).getLocation());
        tvDate.setText(historyList.get(i).getDate());
        ImageButton imgButton = (ImageButton) view.findViewById(R.id.deletor);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Delete",Toast.LENGTH_LONG).show();
            }


        });


        return view;
    }
}
