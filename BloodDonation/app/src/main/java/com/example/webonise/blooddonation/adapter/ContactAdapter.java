package com.example.webonise.blooddonation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.model.ContactInfo;

import java.util.List;

/**
 * Created by webonise on 2/9/15.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder>  {

    private List<ContactInfo> contactList;
    ContactInfo contactInfo;


    public ContactAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        contactInfo = contactList.get(i);
        contactViewHolder.vName.setText(contactInfo.name);
        contactViewHolder.tvLocation.setText(contactInfo.location);
        contactViewHolder.tvDate.setText(contactInfo.date);
        contactViewHolder.vTitle.setText(contactInfo.name + " " + contactInfo.location);


    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_item, viewGroup, false);
        return new ContactViewHolder(itemView);
    }



}