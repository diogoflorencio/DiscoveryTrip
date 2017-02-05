package com.example.diogo.discoverytrip.DataBase;

import android.provider.BaseColumns;

/**
 * Created by diogo on 04/02/17.
 */

public abstract class TableLembretes {

    public static class Column implements BaseColumns {
        public static final String COLUMN_Nome = "nome";
        public static final String COLUMN_Descricao = "descricao";
        public static final String COLUMN_Data = "data";
    }

    /* Script Lembretes.DML */
    public static final String TABLE_NAME = "Lembretes";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COLUMN_SEP = ",";
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +TABLE_NAME + " (" +
                    Column._ID + " INTEGER PRIMARY KEY," +
                    Column.COLUMN_Nome + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Descricao + TEXT_TYPE + COLUMN_SEP +
                    Column.COLUMN_Data + TEXT_TYPE + " )";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
