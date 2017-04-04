package com.example.diogo.discoverytrip.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.diogo.discoverytrip.DataHora.DataHoraSystem;
import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.Localizacao;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by diogo on 04/02/17.
 *
 * Script DDL DiscoveryTrip.bd
 */

public class DiscoveryTripBD extends SQLiteOpenHelper {

    /* Constantes  do DiscoveryTrip.bd */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiscoveryTrip.bd";
    private String[] projection = {
            LembretesTable.Column.COLUMN_Id,
            LembretesTable.Column.COLUMN_Nome,
            LembretesTable.Column.COLUMN_Descricao,
            LembretesTable.Column.COLUMN_Data_Start,
            LembretesTable.Column.COLUMN_Data_End,
            LembretesTable.Column.COLUMN_Latitude,
            LembretesTable.Column.COLUMN_Longitude,
            LembretesTable.Column.COLUMN_Pais,
            LembretesTable.Column.COLUMN_Cidade,
            LembretesTable.Column.COLUMN_Rua,
            LembretesTable.Column.COLUMN_Numero,
            LembretesTable.Column.COLUMN_FotoID,
            LembretesTable.Column.COLUMN_King,
            LembretesTable.Column.COLUMN_Price,
            LembretesTable.Column.COLUMN_Type
    };

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

    public void insertLembretesTable(Atracao atracao){
        SQLiteDatabase db = this.getWritableDatabase();
        deleteLembreteTable(atracao);
        ContentValues values = new ContentValues();
        values.put(LembretesTable.Column.COLUMN_Id, atracao.getId());
        values.put(LembretesTable.Column.COLUMN_Nome, atracao.getName());
        values.put(LembretesTable.Column.COLUMN_Descricao, atracao.getDescription());
        values.put(LembretesTable.Column.COLUMN_Data_Start, atracao.getStartDate());
        values.put(LembretesTable.Column.COLUMN_Data_End, atracao.getEndDate());
        values.put(LembretesTable.Column.COLUMN_Latitude, atracao.getLocation().getLatitude());
        values.put(LembretesTable.Column.COLUMN_Longitude, atracao.getLocation().getLongitude());
        values.put(LembretesTable.Column.COLUMN_Pais, atracao.getLocation().getCountry());
        values.put(LembretesTable.Column.COLUMN_Cidade, atracao.getLocation().getCity());
        values.put(LembretesTable.Column.COLUMN_Rua, atracao.getLocation().getStreetName());
        values.put(LembretesTable.Column.COLUMN_Numero, atracao.getLocation().getStreetNumber());
        values.put(LembretesTable.Column.COLUMN_FotoID, atracao.getPhotoId());
        values.put(LembretesTable.Column.COLUMN_King, atracao.getKind());
        values.put(LembretesTable.Column.COLUMN_Price, atracao.getPrice());
        values.put(LembretesTable.Column.COLUMN_Type, atracao.getType());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(LembretesTable.TABLE_NAME, null, values);
    }

    /*recupera todos os lembretes do dia corrente*/
    public List<Atracao> selectDayLembretesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = LembretesTable.Column.COLUMN_Data_Start + " = ?";
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
        return parseCursoToAtracao(cursor);
    }

    /*recupera todos os lembretes da base de dados*/
    public List<Atracao> selectAllLembretesTable(){
        SQLiteDatabase db = this.getReadableDatabase();

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
        return parseCursoToAtracao(cursor);
    }

    /*deleta os lembretes do dia corrente*/
    public void deleteLembretesTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LembretesTable.Column.COLUMN_Data_Start + " LIKE ?";
        String[] selectionArgs = { DataHoraSystem.data() };
        db.delete(LembretesTable.TABLE_NAME, selection, selectionArgs);
    }

    /*deleta um lembrete por id*/
    public void deleteLembreteTable(Atracao atracao){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LembretesTable.Column.COLUMN_Id + " LIKE ?";
        String[] selectionArgs = { atracao.getId() };
        db.delete(LembretesTable.TABLE_NAME, selection, selectionArgs);
    }

    private List<Atracao> parseCursoToAtracao(Cursor cursor){
        List<Atracao> atracoes = new ArrayList<Atracao>();
        if(cursor.moveToFirst()){
            do {
                Atracao atracao = new Atracao();
                Localizacao localizacao = new Localizacao();

                localizacao.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Latitude)));
                localizacao.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Longitude)));
                localizacao.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Pais)));
                localizacao.setCity(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Cidade)));
                localizacao.setStreetName(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Rua)));
                localizacao.setStreetNumber(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Numero)));

                atracao.setId(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Id)));
                atracao.setNome(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Nome)));
                atracao.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Descricao)));
                atracao.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Data_Start)));
                atracao.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Data_End)));
                atracao.setLocalizacao(localizacao);
                atracao.setPhotoId(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_FotoID)));
                atracao.setKind(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_King)));
                atracao.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Price)));
                atracao.setType(cursor.getString(cursor.getColumnIndexOrThrow(LembretesTable.Column.COLUMN_Type)));

                atracoes.add(atracao);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return atracoes;
    }

    public void updateBD(List<Atracao> eventsOfDay){
        for (Atracao a : selectDayLembretesTable())
            if(!eventsOfDay.contains(a))
                deleteLembreteTable(a);
    }
}
