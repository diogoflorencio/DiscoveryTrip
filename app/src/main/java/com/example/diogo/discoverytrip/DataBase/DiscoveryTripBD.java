package com.example.diogo.discoverytrip.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;

/**
 * Created by diogo on 04/02/17.
 *
 * Script DDL DiscoveryTrip.bd
 */

public class DiscoveryTripBD extends SQLiteOpenHelper {

    /* Constantes  do DiscoveryTrip.bd */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiscoveryTrip.db";


    public DiscoveryTripBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TableLembretes.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /* onUpgrade atualização e/ou sincronização de dados
        * onCreate(sqLiteDatabase);*/
    }

    public void insertTableLembrete(String nome, String descricao, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TableLembretes.Column.COLUMN_Nome, nome);
        values.put(TableLembretes.Column.COLUMN_Descricao, descricao);
        values.put(TableLembretes.Column.COLUMN_Data, data);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TableLembretes.TABLE_NAME, null, values);
    }

    public Cursor selectTableLembrete(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                TableLembretes.Column.COLUMN_Nome,
                TableLembretes.Column.COLUMN_Descricao
        };


        String selection = TableLembretes.Column.COLUMN_Data + " = ?";
        String[] selectionArgs = {DataHoraSystem.data()};

        String sortOrder =
                TableLembretes.Column.COLUMN_Nome + " DESC";

        Cursor cursor = db.query(
                TableLembretes.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    public void deleteTableLembrete(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TableLembretes.Column.COLUMN_Data + " LIKE ?";
        String[] selectionArgs = { DataHoraSystem.data() };
        db.delete(TableLembretes.TABLE_NAME, selection, selectionArgs);
    }
}
