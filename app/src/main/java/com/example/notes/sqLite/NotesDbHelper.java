package com.example.notes.sqLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.notes.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DB_Notes";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "Note";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "titre";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";


    public NotesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    // Creation de base de données avec la table "Note"
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_DESCRIPTION + " TEXT NOT NULL, " +
                KEY_DATE + " TEXT NOT NULL" +
                ")";
        db.execSQL(SQL_CREATE_TABLE);
        Log.i("DATABASE","onCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTES);
        onCreate(db);
        Log.i("DATABASE","onUpgrade invoked");
    }

    public void addNote(Note note){
        Log.i("DATABASE","addNote started");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,note.getTitre());
        values.put(KEY_DESCRIPTION,note.getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss ");
        values.put(KEY_DATE,dateFormat.format(note.getDate()));
        db.insert(TABLE_NOTES,null,values);
        Log.i("DATABASE","addNote finished");
    }

    public void deleteNote(Note note){
        Log.i("DATABASE","deleteNote started");
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_TITLE+" = ?", new String[]{note.getTitre()});
        db.close();
        Log.i("DATABASE","deleteNote finished");
    }

    public List<Note> getAllNotes() throws ParseException {
        Log.i("DATABASE","getAllNotes started");
        SQLiteDatabase db= getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,null,null,null,null,null,KEY_DATE+" DESC");
        List<Note> notes = new ArrayList<>();
        while(cursor != null && cursor.moveToNext()){
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))+"\n";
            @SuppressLint("Range") String  date_str = cursor.getString(cursor.getColumnIndex(KEY_DATE));
            SimpleDateFormat dateformater = new SimpleDateFormat("dd-MM-yyy", Locale.FRANCE);
            Date date = dateformater.parse(date_str);
            Note note = new Note(title,description,date);
            notes.add(note);
        }
        cursor.close();
        db.close();
        Log.i("DATABASE","getAllNotes finished");
        System.out.println("Notes: "+notes);
        return notes;
    }
}
