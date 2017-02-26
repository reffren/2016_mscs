package ru.electronim.acsmb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tim on 08.03.2016.
 */
public class GetDataFromServer {
    DataBaseAdapter dataBase;

    private JSONArray data = null;
    int anchor=0;

    private static final String JSON_ARRAY ="result";
    private static final String ID = "id";
    private static final String USER_ID= "user_id";
    private static final String WORK_NAME= "work_name";
    private static final String WORK_STATE = "work_state";
    private static final String WORK_CHECK = "work_check";
    private static final String WORK_DATE_CHECK = "work_date_check";
    private static final String KEY_WORK_DEPARTMENT = "name_department";
    private static final String JSON_URL = "http://www.electronim.ru/get_data_to_boss.php";

    protected void getJSON(DataBaseAdapter dataBaseAdapter) {
        dataBase = dataBaseAdapter;
        BufferedReader bufferedReader = null;
        try {

            URL url = new URL(JSON_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            extractJSON(sb.toString().trim());

        }catch(Exception e){
            // return null;
        }
    }
    private void extractJSON(String myJSONString){
        try {
            JSONObject jsonObject = new JSONObject(myJSONString);
            data = jsonObject.getJSONArray(JSON_ARRAY);
            if(dataBase.getDataForListActivity().getCount()==0) { // если в бд приложения нет записей, то count=0 и anchor==0, мы записываем данные
            } else {anchor=1;} // если anchor==1, то обновляем таблицу
            for (int i = 0; i < data.length(); i++) {
                insertDataToAndroidSql(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertDataToAndroidSql(int i) {
        try {
            JSONObject jsonObject = data.getJSONObject(i);

            String _id = jsonObject.getString(ID);
            int userId = jsonObject.getInt(USER_ID);
            String workName = jsonObject.getString(WORK_NAME);
            String workState = jsonObject.getString(WORK_STATE);
            int workCheck = jsonObject.getInt(WORK_CHECK);
            String workDateCheck = jsonObject.getString(WORK_DATE_CHECK);
            String workNameDepartment = jsonObject.getString(KEY_WORK_DEPARTMENT);
          //  String work_downloaded = new CurrentData().yearMonth();
            dataBase.writeDataWork(_id, workNameDepartment, userId, workName, workState, workCheck, workDateCheck, anchor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

