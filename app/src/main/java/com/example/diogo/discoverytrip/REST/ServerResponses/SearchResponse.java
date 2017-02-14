package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.PontoTuristico;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 14/02/17.
 */

public class SearchResponse {

    @SerializedName("attractions")
    List<PontoTuristico> pontosTuristicos;


    public List<PontoTuristico> getPontosTuristicos(){
        return pontosTuristicos;
    }
}
