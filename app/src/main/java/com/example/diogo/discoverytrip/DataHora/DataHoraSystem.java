package com.example.diogo.discoverytrip.DataHora;

/**
 * Created by diogo on 05/01/17.
 */

public class DataHoraSystem {

    public static String hora(){
        return (new DataHoraFormat()).getHora();
    }

    public static String data(){
        return (new DataHoraFormat()).getData();
    }
}
