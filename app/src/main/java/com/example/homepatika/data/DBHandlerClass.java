package com.example.homepatika.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.String;
import java.util.ArrayList;

public class DBHandlerClass extends SQLiteOpenHelper{
    //Adatbázis kezelő osztály
    //Ezzel lesz beállítva az eszközön az adatbázis fájl, amit használni fog az alkalmazás,
    //illetve az adatbázis szerkezete is itt kerül kialakításra
    //Ez az osztály tartalmazza az adatbázis kezeléssel kapcsolatos függvényeket:
    //olvasás, írás, frissítés, törlés, keresés

    //Adatbázis adatok
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gyogyszerekDB.db";
    private static final String TABLE_NAME = "Gyogyszerek";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MEGNEVEZES = "megnevezes";
    private static final String COLUMN_LEIRAS = "leiras";
    private static final String COLUMN_SZAVATOSSAG = "szavatossag";
    private static final String COLUMN_MENNYISEG = "mennyiseg";
    private static final String COLUMN_RECEPTES = "receptes";

    //Adatbázis inicializálás
    public DBHandlerClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    //Adatbázis létrehozása
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_MEGNEVEZES + " TEXT NOT NULL, "
                + COLUMN_LEIRAS + " TEXT, "
                + COLUMN_SZAVATOSSAG + " TEXT, "
                + COLUMN_MENNYISEG + " INTEGER, "
                + COLUMN_RECEPTES + " INTEGER);";
        db.execSQL(CREATE_TABLE);
    }

    //Kötelezően felülírandó metódus
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    //Új elem hozzáadása az adatbázishoz
    public void addHandler(Gyogyszer gyogyszer) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEGNEVEZES, gyogyszer.getMegnevezes());
        values.put(COLUMN_LEIRAS, gyogyszer.getLeiras());
        values.put(COLUMN_SZAVATOSSAG, gyogyszer.getSzavatossag());
        values.put(COLUMN_MENNYISEG, gyogyszer.getMennyiseg());
        values.put(COLUMN_RECEPTES, gyogyszer.getReceptes());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Az id alapján egy elem kiválasztása az adatbázisból
    public Gyogyszer loadOneByIdHandler(int id) {
        Gyogyszer result = new Gyogyszer();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + String.valueOf(id);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        result.setId(cursor.getInt(0));
        result.setMegnevezes(cursor.getString(1));
        result.setLeiras(cursor.getString(2));
        result.setSzavatossag(cursor.getString(3));
        result.setMennyiseg(cursor.getInt(4));
        result.setReceptes(cursor.getInt(5));

        cursor.close();;
        db.close();
        return result;
    }

    //Az adatbázisban lévő összes rekord id-ját és megnevezését adja vissza egy listában
    public ArrayList<Object[]> loadNameListHandler() {
        ArrayList<Object[]> result = new ArrayList<Object[]>();
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_MEGNEVEZES + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            result.add(new Object[]{cursor.getString(0),cursor.getString(1)});
        }
        cursor.close();;
        db.close();
        return result;
    }

    //Az adatbázis minden rekordjának minden elemét adja vissza egy listában
    public ArrayList<Gyogyszer> loadAllListHandler() {
        ArrayList<Gyogyszer> result = new ArrayList<Gyogyszer>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            result.add(new Gyogyszer(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(1)
            ));
        }
        cursor.close();;
        db.close();
        return result;
    }

    //Az adatbázis egy rekordját törli id alapján
    public boolean deleteHandler(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=" + String.valueOf(ID), null) > 0;
    }

    //Az adatbázis egy rekordjának minden elemét frissíti
    public boolean updateHandler(Gyogyszer gyogyszer) {
        int ID = gyogyszer.getId();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEGNEVEZES, gyogyszer.getMegnevezes());
        values.put(COLUMN_LEIRAS, gyogyszer.getLeiras());
        values.put(COLUMN_SZAVATOSSAG, gyogyszer.getSzavatossag());
        values.put(COLUMN_MENNYISEG, gyogyszer.getMennyiseg());
        values.put(COLUMN_RECEPTES, gyogyszer.getReceptes());

        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, values, COLUMN_ID + "=" + String.valueOf(ID), null) >0;
    }

}
