package com.example.webonise.blooddonation.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.webonise.blooddonation.app.Constant;
import com.example.webonise.blooddonation.model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryDBAdapter extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DATABASE_NAME = "BloodDonation";
    public static final String TABLE_DETAILS = "History";
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_LOCATION = "Location";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_IMAGE = "Image";
    public static final int ID = 0;

    public HistoryDBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_DETAILS + " ( " + COLUMN_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LOCATION + " TEXT, " + COLUMN_DATE + " TEXT ," + COLUMN_IMAGE + " TEXT ) ";
         sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
        onCreate(db);
    }

    public void createDetails(History history) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*ContentValues Approach*/
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_LOCATION, history.getLocation());
        initialValues.put(COLUMN_DATE, history.getDate());
        initialValues.put(COLUMN_IMAGE, history.getImage());


        if(db!=null && db.isOpen()) {
            db.insert(TABLE_DETAILS, null, initialValues);
        }
        db.close();
    }

    public void deleteCertainDetail(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DETAILS, COLUMN_ID + "=" + id, null);
        db.close();
    }


    public List<History> fetchAllDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<History> personDetailsList = new ArrayList<History>();

        String selectCount = "SELECT COUNT(*) FROM " + TABLE_DETAILS;
        Cursor cursor = db.rawQuery(selectCount, null);
        cursor.moveToFirst();
        if (cursor.getInt(ID) > ID) {
            String selectAll = "SELECT * FROM " + TABLE_DETAILS;
            cursor = db.rawQuery(selectAll, null);
            cursor.moveToFirst();
            do {
                History history = new History();
                history.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                history.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)));
                history.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                history.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));


                personDetailsList.add(history);
            }
            while (cursor.moveToNext());
        } else {
            History history = new History();
            history.setLocation(Constant.EMPTY);
            personDetailsList.add(history);
        }
        return personDetailsList;
    }
}
