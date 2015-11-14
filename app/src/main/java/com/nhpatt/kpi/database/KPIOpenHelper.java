package com.nhpatt.kpi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nhpatt.kpi.models.Commit;

/**
 * @author Javier Gamarra
 */
public class KPIOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "KPI_DB";
    private static final int DATABASE_VERSION = 8;

    private static final String CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + Commit.TABLE_NAME +
                    "("
                    + Commit.WEEK + " INTEGER PRIMARY KEY,"
                    + Commit.TOTAL + " INTEGER" +
                    ")";

    public KPIOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Commit.TABLE_NAME);

        onCreate(db);
    }
}
