package com.example.android.nbaschedule;

/**
 * Created by justas on 06/02/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ScheduleAdapter {

    private static final String LOG_TAG = ScheduleAdapter.class.getSimpleName();
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;

    private static String DB_PATH = "/data/data/com.example.android.nbaschedule/databases/";
    private static final String DATABASE_NAME = "schedules2.db";

    //public final String TABLE_NAME = "schedule";


    private static final int DATABASE_VERSION = 6;

    private final Context adapterContext;

    public ScheduleAdapter(Context context) {
        this.adapterContext = context;
    }

    public ScheduleAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(adapterContext);

        try {
            mDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            mDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        return this;
    }



    public Cursor getFutureRows(){
        final int GAME_TIME = 2; //to show currently played games
        String currentETtime = Utility.getCurrentETtime(-GAME_TIME); //yyyyMMddHHmm
        String orderBy = "ORDER BY datetime asc";
    //    Log.v("DATE", currentTime);
        String sql = "SELECT * FROM schedule where datetime >= " + currentETtime + " " + orderBy;
        return mDb.rawQuery(sql, null);
    }

    public String getTeamPictureURL(String team){
        String sql = "SELECT picture_url from Pictures where team='" + team + "'";
        Cursor c = mDb.rawQuery(sql, null);
        String pic_url = team;
        if (c.moveToNext()){
            pic_url = c.getString(0);
        }
        c.close();
        return pic_url;
    }

    public Cursor getAllRows(){
        return mDb.rawQuery("SELECT * FROM schedule", null);
    }

    public void close() {
        mDbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        Context helperContext;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG_TAG, "Upgrading database");
            db.execSQL("DROP TABLE IF EXISTS schedule");
            db.execSQL("DROP TABLE IF EXISTS Pictures");
            onCreate(db);
        }

        public void createDataBase() throws IOException {
            boolean dbExist = false; //checkDataBase();
            Log.v(LOG_TAG, "createDataBase");
            if (dbExist) {
            } else {

                //make sure your database has this table already created in it
                //this does not actually work here
                /*
                 * db.execSQL("CREATE TABLE IF NOT EXISTS \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')"
                 * );
                 * db.execSQL("INSERT INTO \"android_metadata\" VALUES ('en_US')"
                 * );
                 */
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error("Error copying database");
                }
            }
        }

        public SQLiteDatabase getDatabase() {
            String myPath = DB_PATH + DATABASE_NAME;
            return SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;
            try {
                String myPath = DB_PATH + DATABASE_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
                int version = checkDB.getVersion();
                Log.v(LOG_TAG, "current db version " + version);
            } catch (SQLiteException e) {
            }
            if (checkDB != null) {
                checkDB.close();
            }
            return checkDB != null ? true : false;
        }

        private void copyDataBase() throws IOException {

            // Open your local db as the input stream
            InputStream myInput = helperContext.getAssets().open(DATABASE_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DATABASE_NAME;

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }

        public void openDataBase() throws SQLException {
            // Open the database
            String myPath = DB_PATH + DATABASE_NAME;
            mDb = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            mDb.setVersion(DATABASE_VERSION);
            Log.v(LOG_TAG, "openDatabase");
        }

        @Override
        public synchronized void close() {

            if (mDb != null)
                mDb.close();

            super.close();

        }


    }

}

