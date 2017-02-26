package ru.electronim.acsmb;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tim on 08.03.2016.
 */
public class ListViewAdapter extends CursorAdapter {

    TextView tvDepartment, tvState;

    ArrayList<Integer> arrayListID = new ArrayList<Integer>();

    public ListViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fill_list_activity, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Context cont = context;

        tvDepartment = (TextView) view.findViewById(R.id.tv_department);
        tvState = (TextView) view.findViewById(R.id.tv_state);

        final int id = cursor.getInt(cursor.getColumnIndex("_id"));
        final int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
        final String nameDe = cursor.getString(cursor.getColumnIndex("work_department"));
       // final String workState = cursor.getString(cursor.getColumnIndex("work_state"));
        arrayListID.add(userId); // передаем в ListActivity и затем в слушатель ListView (определяем нажатие по id)

        tvDepartment.setText(nameDe);
       // tvState.setText(workState);
    }
}
