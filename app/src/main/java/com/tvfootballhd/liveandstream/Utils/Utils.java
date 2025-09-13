package com.tvfootballhd.liveandstream.Utils;

import android.os.Build;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String convertLocalDateToString(LocalDate localDate) {
        if (Build.VERSION.SDK_INT >= 26) {
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
}
