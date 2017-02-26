package ru.electronim.acsmb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    DataBaseAdapter dataBaseAdapter;
    private ListViewAdapter adapter;
    private ListView listView;
    ArrayList<Integer> arrayList = new ArrayList<Integer>();
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.ListPlan);

        dataBaseAdapter = new DataBaseAdapter(this);
        try {
            dataBaseAdapter.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        displayResultList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                arrayList = adapter.arrayListID; // get the id from database and ListViewAdapterForWorkToday
                int _id = arrayList.get(position);

                Intent intent = new Intent(ListActivity.this, InfoActivity.class);
                intent.putExtra("position", _id);
                startActivity(intent);
            }
        });
    }



    private void displayResultList() {
        adapter = new ListViewAdapter(this, dataBaseAdapter.getDataForListActivity(),0);
        listView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_for_list_view, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.id_update:
                dialog = ProgressDialog.show(ListActivity.this, "", "Синхронизация с сервером...", true);
                new Thread(new Runnable() {
                    public void run() {
                            GetDataFromServer getData = new GetDataFromServer();
                            getData.getJSON(dataBaseAdapter); //записываем данные месячных работ из БД сервера в текущую БД

                        dialog.dismiss();
                        Intent intent = new Intent(ListActivity.this, ListActivity.class);
                        startActivity(intent);
                        finish();
                        showToast(); // show dialog
                    }
                }).start();

                break;
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------------------
    // show dialog
    public void showToast() {
        ListActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ListActivity.this, "Данные успешно обновлены", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
