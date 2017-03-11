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
    public static final String DATABASE_NAME = "DiscoveryTrip.bd";

    public DiscoveryTripBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LembretesTable.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /* onUpgrade atualização e/ou sincronização de dados
        * onCreate(sqLiteDatabase);*/
    }

    public void insertLembretesTable(Integer id, String nome, String descricao, String data){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LembretesTable.Column._ID, id);
        values.put(LembretesTable.Column.COLUMN_Nome, nome);
        values.put(LembretesTable.Column.COLUMN_Descricao, descricao);
        values.put(LembretesTable.Column.COLUMN_Data, data);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(LembretesTable.TABLE_NAME, null, values);
    }

    /*recupera todos os lembretes do dia corrente*/
    public Cursor selectLembretesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                LembretesTable.Column.COLUMN_Nome,
                LembretesTable.Column.COLUMN_Descricao
        };


        String selection = LembretesTable.Column.COLUMN_Data + " = ?";
        String[] selectionArgs = {DataHoraSystem.data()};

        String sortOrder =
                LembretesTable.Column.COLUMN_Nome + " DESC";

        Cursor cursor = db.query(
                LembretesTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    /*recupera todos os lembretes da base de dados*/
    public Cursor selectAllLembretesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                LembretesTable.Column._ID,
                LembretesTable.Column.COLUMN_Nome,
                LembretesTable.Column.COLUMN_Descricao
        };

        String sortOrder =
                LembretesTable.Column.COLUMN_Nome + " DESC";

        Cursor cursor = db.query(
                LembretesTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    /*deleta os lembretes do dia corrente*/
    public void deleteLembretesTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LembretesTable.Column.COLUMN_Data + " LIKE ?";
        String[] selectionArgs = { DataHoraSystem.data() };
        db.delete(LembretesTable.TABLE_NAME, selection, selectionArgs);
    }

    /*deleta um lembrete por id*/
    public void deleteLembreteTable(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LembretesTable.Column._ID + " LIKE ?";
        String[] selectionArgs = { id };
        db.delete(LembretesTable.TABLE_NAME, selection, selectionArgs);
    }
}
