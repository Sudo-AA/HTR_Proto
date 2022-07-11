package com.example.htr_proto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Jata extends SQLiteOpenHelper {

    public static final String HTR_TABLE = "HTR_TABLE";
    public static final String COL_DATE_TIME = "DATE_TIME";
    public static final String COL_TREMOR = "TREMOR";
    public static final String COL_VIBRATION = "VIBRATION";
    public static final String COL_ID = "ID";

    public Jata(@Nullable Context context) {
        super(context, "recordHTR.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + HTR_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DATE_TIME + " TEXT, " + COL_TREMOR + " TEXT, " + COL_VIBRATION + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addone(RecordModel recordModel){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE_TIME, recordModel.getDatetime());
        cv.put(COL_TREMOR, recordModel.getTremor());
        cv.put(COL_VIBRATION, recordModel.getVibrate());
        long insert = db.insert(HTR_TABLE, null, cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }


    Cursor readalldata(){
        String qs =  "SELECT * FROM " + HTR_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
             cursor = db.rawQuery(qs, null);
        }
            return cursor;

    }


}
