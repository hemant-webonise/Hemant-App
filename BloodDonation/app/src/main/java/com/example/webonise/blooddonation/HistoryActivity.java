package com.example.webonise.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.webonise.blooddonation.adapter.ContactAdapter;
import com.example.webonise.blooddonation.model.ContactInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAddHistory;
    Button btnToHistoryData;
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
        btnToHistoryData=(Button)findViewById(R.id.btnToHistoryData);

        btnAddHistory.setOnClickListener(this);
        btnToHistoryData.setOnClickListener(this);
    }
    private List<ContactInfo> createList(int size) {

        List<ContactInfo> result = new ArrayList<ContactInfo>();
        for (int i=1; i <= size; i++) {
            ContactInfo ci = new ContactInfo();
            ci.name = ContactInfo.NAME_PREFIX + i;
            ci.location = ContactInfo.SURNAME_PREFIX + i;


            final Calendar c = Calendar.getInstance();
            String date = String.valueOf(new StringBuilder().append(c.get(Calendar.YEAR)).append(" ").append("-").append(c.get(Calendar.MONTH)+ 1).append("-").append(c.get(Calendar.DAY_OF_MONTH)));

            ci.date = date;
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
            case R.id.btnToHistoryData:
                Intent fillHistory = new Intent(this,FillHistoryActivity.class);
                startActivity(fillHistory);

        }

    }
}
