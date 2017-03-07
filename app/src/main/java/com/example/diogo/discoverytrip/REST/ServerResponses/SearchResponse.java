package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Atracao;
import com.example.diogo.discoverytrip.Model.PontoTuristico;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 14/02/17.
 */

public class SearchResponse {

    @SerializedName("attractions")
    List<Atracao> pontosTuristicos;


    public List<Atracao> getAtracoes(){
        return pontosTuristicos;
    }
}
