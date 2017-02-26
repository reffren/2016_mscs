package ru.electronim.acsmb;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tim on 11.03.2016.
 */
public class CurrentData {
    public String yearMonthDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yearMotnhDay = sdf.format(new Date());
        return yearMotnhDay;
    }
}
