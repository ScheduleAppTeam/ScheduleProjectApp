package com.example.scheduleprojectapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kenny on 09.04.14.
 */
public class CommentTableDatabaseHelper extends SQLiteOpenHelper {
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EDITABLEDAY = "editableDay";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_SNAME = "sname";
    private static final String KEY_PERMISSION = "permission";
    private static final String KEY_DATACOMMENT = "dataComment";
    private static final String KEY_TEXT = "text";

    public CommentTableDatabaseHelper(Context context) {
        super(context, Constants.COMMENTS_DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COMMENTS_TABLE = "CREATE TABLE " + Constants.TABLE_COMMENTS + "("
                + KEY_ID + " INTEGER,"
                + KEY_EDITABLEDAY + " INTEGER,"
                + KEY_FNAME + " TEXT,"
                + KEY_SNAME + " TEXT,"
                + KEY_PERMISSION + " INTEGER,"
                + KEY_DATACOMMENT + " INTEGER,"
                + KEY_TEXT + " TEXT" + ")";
        db.execSQL(CREATE_COMMENTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COMMENTS);

        // Create tables again
        onCreate(db);
    }

    public void dropDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_COMMENTS);

        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new Comment
    void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, comment.GetId());
        values.put(KEY_EDITABLEDAY, comment.GetEditableDay());
        values.put(KEY_FNAME, comment.GetFname());
        values.put(KEY_SNAME, comment.GetSname());
        values.put(KEY_PERMISSION, comment.GetPermission());
        values.put(KEY_DATACOMMENT, comment.GetDataComment());
        values.put(KEY_TEXT, comment.GetText());

        // Inserting Row
        assert db != null;

        long id = db.replace(Constants.TABLE_COMMENTS, null, values);
        Log.i(Constants.LOG_TAG, "Comment: " + String.valueOf(comment.GetDataComment()) + "  " + String.valueOf(id));

        db.close(); // Closing database connection
    }

    // Getting single comment
    ArrayList<Comment> getComments(int value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.i(Constants.LOG_TAG, "Getting comment, id is " + value);
        assert db != null;

        Cursor cursor = db.query(
                Constants.TABLE_COMMENTS,
                new String[] { KEY_EDITABLEDAY, KEY_FNAME, KEY_SNAME, KEY_PERMISSION, KEY_DATACOMMENT, KEY_TEXT},
                KEY_EDITABLEDAY + " = ?",
                new String[] { String.valueOf(value) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Log.i(Constants.LOG_TAG, "Getting process");
        ArrayList<Comment> comments = new ArrayList<Comment>();
        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)),
                        Long.parseLong(cursor.getString(4)),
                        cursor.getString(5));
                comments.add(comment);
            } while (cursor.moveToNext());
        }

        Log.i(Constants.LOG_TAG, "Comment with date "+ value + " getted");

        return comments;
    }

    // Updating single contact
    public int updateComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EDITABLEDAY, comment.GetEditableDay());
        values.put(KEY_FNAME, comment.GetFname());
        values.put(KEY_SNAME, comment.GetSname());
        values.put(KEY_PERMISSION, comment.GetPermission());
        values.put(KEY_DATACOMMENT, comment.GetDataComment());
        values.put(KEY_TEXT, comment.GetText());

        // updating row
        assert db != null;
        return db.update(Constants.TABLE_COMMENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(comment.GetId()) });
    }

    // Deleting single venue
    public void deleteComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_COMMENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(comment.GetId()) });
        db.close();
    }

    // Deleting single venue by id
    public void deleteComment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        db.delete(Constants.TABLE_COMMENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}