package com.example.webonise.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.webonise.blooddonation.adapter.HistoryAdapter;
import com.example.webonise.blooddonation.adapter.HistoryDBAdapter;
import com.example.webonise.blooddonation.model.ContactInfo;
import com.example.webonise.blooddonation.model.History;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HistoryActivityWithSQL extends AppCompatActivity implements View.OnClickListener, HistoryAdapter.CallBack {
    Button btnToHistoryData;
    private ListView listView;
    private HistoryAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_activity_with_sql);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_history));
        toolbar.setSubtitle(getString(R.string.subtitle_history));
        setSupportActionBar(toolbar);
        updateList();
        btnToHistoryData=(Button)findViewById(R.id.btnToHistoryData);
        btnToHistoryData.setOnClickListener(this);

    }

    private List<History> createList(int size) {

        List<History> result = new ArrayList<History>();
        for (int i=1; i <= size; i++) {
            History history = new History();
            history.setLocation("Pune"+i);
            final Calendar c = Calendar.getInstance();
            String date = String.valueOf(new StringBuilder().append(c.get(Calendar.YEAR)).append(" ").append("-").append(c.get(Calendar.MONTH)+ 1).append("-").append(c.get(Calendar.DAY_OF_MONTH)));
            history.setDate(date);
            history.setImage("/null"+i);
            result.add(history);
        }

        return result;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnToHistoryData:
                Intent fillHistory = new Intent(this,FillHistoryActivity.class);
                startActivity(fillHistory);
        }

    }
    private void updateList() {
        HistoryDBAdapter historyDBAdapter = new HistoryDBAdapter(this);
        listView = (ListView) findViewById(R.id.lvDetailsList);
        listAdapter = new HistoryAdapter(historyDBAdapter.fetchAllDetails(),this);
        listView.setAdapter(listAdapter);
        historyDBAdapter.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();

    }


    @Override
    public void onDeleted() {
        updateList();
    }



    public void onImageChanged(){
        updateList();

    }
}
