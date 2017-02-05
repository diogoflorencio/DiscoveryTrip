package com.example.diogo.discoverytrip.DataHora;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by diogo on 05/01/17.
 */

public class DataHoraFormat {
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat_hora;
    private Date date;
    private Calendar cal;
    private Date data_atual;

    public DataHoraFormat(){}

    protected String getData(){
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        data_atual = cal.getTime();
        return  dateFormat.format(data_atual);
    }

    protected String getHora(){
        dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
        date = new Date();
        cal = Calendar.getInstance();
        cal.setTime(date);
        data_atual = cal.getTime();
        return dateFormat_hora.format(data_atual);
    }
}
