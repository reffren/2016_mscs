package ru.electronim.acsmb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.sql.SQLException;

public class InfoActivity extends AppCompatActivity {

    int position = 0;
    String userId;
    DataBaseAdapter dataBaseAdapter;
    private ListViewAdapterForDepartment adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", position);
        userId = String.valueOf(position);
        listView = (ListView) findViewById(R.id.ListDepartment);
        dataBaseAdapter = new DataBaseAdapter(this);
        try {
            dataBaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        displayResultList();

    }

    private void displayResultList() {
        adapter = new ListViewAdapterForDepartment(this, dataBaseAdapter.getDataForInfoActivity(userId),0);
        listView.setAdapter(adapter);
    }
}
