package ru.electronim.acsmb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Tim on 08.03.2016.
 */
public class DataBaseAdapter {

    private static final String DATABASE_TABLE = "acsmb_user";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    private static final String DATABASE_WORK_TABLE = "acsmb_work";
    public static final String KEY_WORK_ID = "_id";
    public static final String KEY_WORK_DEPARTMENT = "work_department";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_WORK_NAME = "work_name";
    public static final String KEY_WORK_TIME = "work_time";
    public static final String KEY_WORK_STATE = "work_state";
    public static final String KEY_WORK_END_TIME = "work_end_time";
    public static final String KEY_WORK_CHECK = "work_check";
    public static final String KEY_WORK_DATE_CHECK = "work_date_check";
    public static final String KEY_DATE_DOWNLOADED = "work_date_downloaded";

    SQLiteDatabase sqLiteDatabase;
    Context context;
    DataBaseHelper dataBaseHelper;

    ContentValues contentValues = new ContentValues();

    public DataBaseAdapter(Context context) {
        this.context = context;
    }

    public DataBaseAdapter open() throws SQLException {
        dataBaseHelper = new DataBaseHelper(context);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public long register(String user, String pass) {
        contentValues.put(KEY_USERNAME, user);
        contentValues.put(KEY_PASSWORD, pass);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public boolean Login(String username, String password) throws SQLException {
        Cursor mCursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?", new String[]{username, password});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    //put data into work table, it is for PlanActivity
    public long writeDataWork(String _id, String workNameDepartment, int userId, String work_name, String workState, int workCheck, String workDateCheck, int anchor) { //записываем данные с сервера по работе на месяц
        contentValues.put(KEY_USER_ID, userId);
        contentValues.put(KEY_WORK_NAME, work_name);
        contentValues.put(KEY_WORK_STATE, workState);
        contentValues.put(KEY_WORK_CHECK, workCheck);
        contentValues.put(KEY_WORK_DATE_CHECK, workDateCheck);
        contentValues.put(KEY_WORK_DEPARTMENT, workNameDepartment);
        if (anchor == 0) {
            return sqLiteDatabase.insert(DATABASE_WORK_TABLE, null, contentValues); // если anchor==0, то вставляем данные в бд
        } else {
            Cursor check = sqLiteDatabase.rawQuery("SELECT " + KEY_WORK_ID + " FROM " + DATABASE_WORK_TABLE + " WHERE _id=" + _id, null); // выбираем запись с id которое поступило с сервера
            if (!(check.getCount() ==0)) { // если такое _id сущесствует, то обновляем БД, если _id в бд с сервера новое, а в бд телефона его нет, то записываем его, миную код обновления **
                return sqLiteDatabase.update(DATABASE_WORK_TABLE, contentValues, "_id=" + _id, null); // если нет, то обновляем бд по id
            } else { //**
                return sqLiteDatabase.insert(DATABASE_WORK_TABLE, "WHERE _id="+_id, contentValues); // если anchor==0, то вставляем данные в бд
            }
        }
    }

    public Cursor getDataForListActivity() {
        return sqLiteDatabase.rawQuery("SELECT MAX(user_id) AS " + KEY_USER_ID + "," + KEY_WORK_ID + "," + KEY_WORK_DEPARTMENT +" FROM " + DATABASE_WORK_TABLE + " GROUP BY " + KEY_WORK_DEPARTMENT, null); // выбираем KEY_WORK_DEPARTMENT без дубликатов (повторений)
    }

    public Cursor getDataForInfoActivity(String userId) {
        return sqLiteDatabase.rawQuery("SELECT " + KEY_WORK_ID + "," + KEY_WORK_NAME + "," + KEY_WORK_STATE +" FROM " + DATABASE_WORK_TABLE + " WHERE (" + KEY_USER_ID + "=" + Integer.valueOf(userId) + ") AND (" + KEY_WORK_DATE_CHECK + "=" + new CurrentData().yearMonthDay() + ")", null); // выбираем KEY_WORK_DEPARTMENT без дубликатов (повторений)
    }
}
