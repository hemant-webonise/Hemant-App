package com.example.webonise.blooddonation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.webonise.blooddonation.adapter.ContactAdapter;
import com.example.webonise.blooddonation.model.ContactInfo;

import java.util.ArrayList;
import java.util.List;


public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAddHistory;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ContactAdapter contactAdapter;
    int Clicked =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        btnAddHistory=(Button)findViewById(R.id.btnAddHistory);
        btnAddHistory.setOnClickListener(this);
    }
    private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = ContactInfo.NAME_PREFIX + i;
            ci.location = ContactInfo.SURNAME_PREFIX + i;
            ci.phoneNumber = ContactInfo.EMAIL_PREFIX + i + "1";
            result.add(ci);
        }

        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddHistory:
                contactAdapter = new ContactAdapter(createList(Clicked));
                recyclerView.setAdapter(contactAdapter);
                Clicked++;
                break;

        }

    }
}
