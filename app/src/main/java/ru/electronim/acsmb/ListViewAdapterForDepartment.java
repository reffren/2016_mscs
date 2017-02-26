package ru.electronim.acsmb;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Tim on 11.03.2016.
 */
public class ListViewAdapterForDepartment extends CursorAdapter {

    TextView workName;
    TextView workState;
    DataBaseAdapter dataBaseAdapter;

    public ListViewAdapterForDepartment(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fill_for_department, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        workName = (TextView) view.findViewById(R.id.workName);
        workState = (TextView) view.findViewById(R.id.workState);

        final long id = cursor.getLong(cursor.getColumnIndex("_id"));
        final String name = cursor.getString(cursor.getColumnIndex("work_name"));
        final String state = cursor.getString(cursor.getColumnIndex("work_state"));

        workName.setText(name);
        workState.setText(state);

    }
}
