package com.example.webonise.blooddonation.Fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;


import com.example.webonise.blooddonation.adapter.ExampleListAdapter;
import com.example.webonise.blooddonation.model.ExampleListItem;
import com.example.webonise.blooddonation.R;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private List exampleListItemList;
    private AbsListView mListView;
    private ListAdapter mAdapter;
    

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exampleListItemList = new ArrayList();
        setListItem("Example 1");
        setListItem("Example 2");
        setListItem("Example 3");
        new ExampleListItem("Example 3").setItemTitle("Example");

        mAdapter = new ExampleListAdapter(getActivity(), exampleListItemList);
    }

    private void setListItem(String put) {
        exampleListItemList.add(new ExampleListItem(put));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExampleListItem item = (ExampleListItem) this.exampleListItemList.get(position);
        Toast.makeText(getActivity(), item.getItemTitle() + " Clicked!", Toast.LENGTH_SHORT).show();
    }

}




