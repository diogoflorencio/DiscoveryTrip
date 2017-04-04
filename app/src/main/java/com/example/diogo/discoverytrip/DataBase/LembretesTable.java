package com.example.diogo.discoverytrip.DataBase;

import android.provider.BaseColumns;

/**
 * Created by diogo on 04/02/17.
 */

public abstract class LembretesTable {

    public static class Column implements BaseColumns {
        public static final String COLUMN_Id = "id";
        public static final String COLUMN_Nome = "nome";
        public static final String COLUMN_Descricao = "descricao";
        public static final String COLUMN_Data_Start = "dataStart";
        public static final String COLUMN_Data_End = "dataEnd";
        public static final String COLUMN_Latitude = "latitude";
        public static final String COLUMN_Longitude= "longitude";
        public static final String COLUMN_Pais = "pais";
        public static final String COLUMN_Cidade = "cidade";
        public static final String COLUMN_Rua = "rua";
        public static final String COLUMN_Numero = "numero";
        public static final String COLUMN_FotoID = "fotoId";
        public static final String COLUMN_King = "king";
        public static final String COLUMN_Price = "price";
        public static final String COLUMN_Type = "type";
    }

    /* Script Lembretes.DML */
    public static final String TABLE_NAME = "Lembretes";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COLUMN_SEP = ",";
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +TABLE_NAME + " (" +
                    Column._ID + " INTEGER PRIMARY KEY," +
                    Column.COLUMN_Id + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Nome + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Descricao + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Data_Start + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Data_End + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Latitude + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Longitude + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Pais + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Cidade + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Rua + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Numero + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_FotoID + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_King + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Price + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Type + TEXT_TYPE + " )";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
