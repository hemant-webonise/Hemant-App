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
public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private List<ContactInfo> contactList;

    public ContactAdapter(List<ContactInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        ContactInfo ci = contactList.get(i);
        contactViewHolder.vName.setText(ci.name);
        contactViewHolder.tvLocation.setText(ci.location);
        contactViewHolder.tvphoneNumber.setText(ci.phoneNumber);
        contactViewHolder.vTitle.setText(ci.name + " " + ci.location);
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_item, viewGroup, false);
        return new ContactViewHolder(itemView);
    }


}