package com.example.scheduleprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by kenny on 09.04.14.
 */
public class VenueTableDataBaseHelper extends SQLiteOpenHelper {
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_AUDIENCE = "audience";
    private static final String KEY_HOUSING = "housing";

    public VenueTableDataBaseHelper(Context context) {
        super(context, Constants.VENUES_DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VENUE_TABLE = "CREATE TABLE " + Constants.TABLE_VENUE + "("
                + KEY_ID + " INTEGER,"
                + KEY_AUDIENCE + " INTEGER,"
                + KEY_HOUSING + " INTEGER,"
                + "UNIQUE ("+ KEY_ID +") ON CONFLICT REPLACE)";
        db.execSQL(CREATE_VENUE_TABLE);
    }

    public void dropDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_VENUE);

        onCreate(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_VENUE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Venue
    void addVenue(Venue venue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, venue.GetId());
        values.put(KEY_AUDIENCE, venue.GetAudience());
        values.put(KEY_HOUSING, venue.GetHousing());

        // Inserting Row
        assert db != null;

        long id = db.insert(Constants.TABLE_VENUE, null, values);
        //Log.i(Constants.LogTag, "Venue: " + String.valueOf(venue.GetId()) + "  " + String.valueOf(id));
        db.close(); // Closing database connection
    }

    // Getting single venue
    Venue getVenue(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(Constants.LOG_TAG, "Getting venue, id is " + id);
        assert db != null;

        Cursor cursor = db.query(
                Constants.TABLE_VENUE,
                new String[] { KEY_ID, KEY_AUDIENCE, KEY_HOUSING},
                KEY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Log.i(Constants.LOG_TAG, "Getting process");

        Venue venue = new Venue();
        venue.SetId(Integer.parseInt(cursor.getString(0)));
        venue.SetAudience(Integer.parseInt(cursor.getString(1)));
        venue.SetHousing(Integer.parseInt(cursor.getString(2)));

        Log.i(Constants.LOG_TAG, "Venue with " + id + " getted");

        // return user
        return venue;
    }

    // Updating single contact
    public int updateVenue(Venue venue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, venue.GetId());
        values.put(KEY_AUDIENCE, venue.GetAudience());
        values.put(KEY_HOUSING, venue.GetHousing());

        // updating row
        assert db != null;
        return db.update(Constants.TABLE_VENUE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(venue.GetId()) });
    }

    // Deleting single venue
    public void deleteVenue(Venue venue) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_VENUE, KEY_ID + " = ?",
                new String[] { String.valueOf(venue.GetId()) });
        db.close();
    }

    // Deleting single venue by id
    public void deleteVenue(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_VENUE, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public int getVenueCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_VENUE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteAllVenue(){
        for (int i = 0; i < getVenueCount(); i++){
            deleteVenue(i);
        }
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
