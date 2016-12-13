package com.example.jax.Note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.jax.Note.model.NoteInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jax on 08-Dec-16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TB_NAME = "tb_note";

    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String NOTE = "note";

    private static final String DB_NAME = "db_note";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context, String title, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, title, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TB_NAME + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT DEFAULT Untitle, "
                + NOTE + " TEXT " + " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
            onCreate(db);
        }
    }

    private static DBHelper mDBHelper;

    public static synchronized DBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(context.getApplicationContext());
        }
        return mDBHelper;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public void insertNote(NoteInfo noteInfo) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, noteInfo.title);
            contentValues.put(NOTE, noteInfo.note);
            database.insert(TB_NAME, null, contentValues);
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    public void updateNote(NoteInfo noteInfo) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, noteInfo.title);
            contentValues.put(NOTE, noteInfo.note);
            database.update(TB_NAME, contentValues, ID + " = ?", new String[]{
                    String.valueOf(noteInfo.id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public void deleteNote(int id) {
        SQLiteDatabase database = getWritableDatabase();

        try {
            database.beginTransaction();
            database.delete(TB_NAME, ID + "=" + id, null);
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.d("DBHelper", "Error while trying delete");
        } finally {
            database.endTransaction();
        }
    }


    public NoteInfo getSingleNote(int id) {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(TB_NAME, new String[]{ID, TITLE, NOTE},
                ID + "=", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        NoteInfo noteInfo = new NoteInfo();
        noteInfo.title = cursor.getString(cursor.getColumnIndex(TITLE));
        noteInfo.note = cursor.getString(cursor.getColumnIndex(NOTE));
      //  noteInfo.id = cursor.getInt(cursor.getColumnIndex(ID));

        return noteInfo;
    }

    public List<NoteInfo> getAllNote() {
        List<NoteInfo> noteList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TB_NAME;

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {

                    NoteInfo noteInfo = new NoteInfo();
                    noteInfo.title = cursor.getString(cursor.getColumnIndex(TITLE));
                    noteInfo.note = cursor.getString(cursor.getColumnIndex(NOTE));
                    noteInfo.id = cursor.getInt(cursor.getColumnIndex(ID));

                    noteList.add(noteInfo);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBHelper", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return noteList;
    }

}
