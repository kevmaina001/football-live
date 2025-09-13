package com.tvfootballhd.liveandstream.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_MATCh_ADAPTER_TABLE = "CREATE TABLE MatchAdapter (id INTEGER PRIMARY KEY AUTOINCREMENT,fixture_id INTEGER, time INTEGER, is_selected INTEGER)";
    private static final String CREATE_TODAY_MATCH_TABLE = "CREATE TABLE TodayMatch (id INTEGER PRIMARY KEY AUTOINCREMENT,fixture_id INTEGER, time INTEGER, is_selected INTEGER)";
    private static final String DATABASE_NAME = "NotificationHistory.db";
    private static final String DROP_TODAY_MATCH_ADAPTER_TABLE = "DROP TABLE IF EXISTS MatchAdapter";
    private static final String DROP_TODAY_MATCH_TABLE = "DROP TABLE IF EXISTS TodayMatch";
    private static final String FIXTURE_ID = "fixture_id";
    private static final String ID = "id";
    private static final String IS_SELECTED = "is_selected";
    private static final String MATCH_ADAPTER_TABLE = "MatchAdapter";
    private static final String TIME = "time";
    private static final String TODAY_MATCH = "TodayMatch";
    private static final int VERSION = 1;
    private Context context;

    public MyDatabaseHelper(Context context2) {
        super(context2, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.context = context2;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            sQLiteDatabase.execSQL(CREATE_TODAY_MATCH_TABLE);
            sQLiteDatabase.execSQL(CREATE_MATCh_ADAPTER_TABLE);
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), 0).show();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        try {
            sQLiteDatabase.execSQL(TODAY_MATCH);
            sQLiteDatabase.execSQL(MATCH_ADAPTER_TABLE);
            onCreate(sQLiteDatabase);
        } catch (Exception e) {
            Toast.makeText(this.context, e.getMessage(), 0).show();
        }
    }

    public long insertDataInTodayMatchTable(int i, int i2, int i3) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIXTURE_ID, Integer.valueOf(i));
        contentValues.put("time", Integer.valueOf(i2));
        contentValues.put(IS_SELECTED, Integer.valueOf(i3));
        return writableDatabase.insert(TODAY_MATCH, (String) null, contentValues);
    }

    public long insertDataInMatchAdapterTable(int i, int i2, int i3) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIXTURE_ID, Integer.valueOf(i));
        contentValues.put("time", Integer.valueOf(i2));
        contentValues.put(IS_SELECTED, Integer.valueOf(i3));
        return writableDatabase.insert(MATCH_ADAPTER_TABLE, (String) null, contentValues);
    }

    public Cursor getAllTodayMatchTable() {
        return getWritableDatabase().rawQuery("SELECT*FROM TodayMatch", (String[]) null);
    }

    public Cursor getAllMatchAdapterTable() {
        return getWritableDatabase().rawQuery("SELECT*FROM MatchAdapter", (String[]) null);
    }

    public void deleteTodayMatchNotificationItem(int i) {
        getWritableDatabase().delete(TODAY_MATCH, "id=?", new String[]{i + ""});
    }

    public void deleteMatchAdapterNotificationItem(int i) {
        getWritableDatabase().delete(MATCH_ADAPTER_TABLE, "id=?", new String[]{i + ""});
    }
}
