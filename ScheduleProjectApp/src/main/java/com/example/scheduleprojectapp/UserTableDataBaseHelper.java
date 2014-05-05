package com.example.scheduleprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class UserTableDataBaseHelper extends SQLiteOpenHelper {

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_SNAME = "sname";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_AUT = "autoupdatetime";
    private static final String KEY_PERMISSION = "permission";

    public UserTableDataBaseHelper(Context context) {
        super(context, Constants.USER_DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Constants.TABLE_USER + "("
                + KEY_ID + " INTEGER,"
                + KEY_LOGIN + " TEXT,"
                + KEY_SNAME + " TEXT,"
                + KEY_FNAME + " TEXT,"
                + KEY_AUT + " INTEGER,"
                + KEY_PERMISSION + " INTEGER" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    public void dropDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USER);

        onCreate(db);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    public void dropDB(SQLiteDatabase db) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_USER);

        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new User
    void addUser(User user) {
        Log.i(Constants.LOG_TAG, "Adding new user, login is " + user.GetEmail() + " and id " + user.GetId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.GetId()); //User ID
        values.put(KEY_LOGIN, user.GetEmail()); // User login
        values.put(KEY_SNAME, user.GetSecondName()); // User sname
        values.put(KEY_FNAME, user.GetFirstName()); // User fname
        values.put(KEY_AUT, user.GetAutoUpdateTime()); // User aut
        values.put(KEY_PERMISSION, user.GetPermission()); // User permission

        // Inserting Row
        assert db != null;

        db.replace(Constants.TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    // Getting single user
    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(Constants.LOG_TAG, "Getting user, id is " + id);
        assert db != null;

        Cursor cursor = db.query(
                Constants.TABLE_USER,
                new String[] { KEY_ID, KEY_LOGIN, KEY_SNAME, KEY_FNAME, KEY_AUT, KEY_PERMISSION},
                KEY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Log.i(Constants.LOG_TAG, "Getting process");

        User user = new User(
                Integer.parseInt(cursor.getString(0)), //id
                cursor.getString(1), //login
                cursor.getString(2), //sname
                cursor.getString(3), //fname
                Integer.parseInt(cursor.getString(4)), //aut
                Integer.parseInt(cursor.getString(5))); //permission

        Log.i(Constants.LOG_TAG, "User with " + id + " getted");

        // return user
        return user;
    }

    // Updating single Userw
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.GetId());
        values.put(KEY_LOGIN, user.GetEmail());
        values.put(KEY_SNAME, user.GetSecondName());
        values.put(KEY_FNAME, user.GetFirstName());
        values.put(KEY_AUT, user.GetAutoUpdateTime());
        values.put(KEY_PERMISSION, user.GetPermission());

        // updating row
        assert db != null;
        return db.update(Constants.TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.GetId()) });
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(user.GetId()) });
        db.close();
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + Constants.TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
