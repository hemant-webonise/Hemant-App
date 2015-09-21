package com.example.webonise.blooddonation.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.webonise.blooddonation.R;

public class ImageAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    int[] rank;

    LayoutInflater inflater;

    public ImageAdapter(Context context, int[] rank) {
        this.context = context;
        this.rank = rank;

    }

    @Override
    public int getCount() {
        return rank.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Declare Variables
        ImageView txtrank;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,false);
        // Locate the TextViews in viewpager_item.xml
        txtrank = (ImageView) itemView.findViewById(R.id.rank);
        // Capture position and set to the TextViews
        txtrank.setImageResource(rank[position]);
        // Add viewpager_item.xml to ViewPager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        container.removeView((RelativeLayout) object);

    }
}