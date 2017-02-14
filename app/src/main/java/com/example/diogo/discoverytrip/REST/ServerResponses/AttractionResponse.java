package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Atracao;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 */
public class AttractionResponse {

    //@SerializedName("attraction")
    //private Atracao atracao;

    @SerializedName("photos")
    private List<String> fotos;

    @SerializedName("state")
    private String estado;

    @SerializedName("created")
    private String criacao;

//    public Atracao getAtracao() {
//        return atracao;
//    }

    public List<String> getFotos() {
        return fotos;
    }

    public String getEstado() {
        return estado;
    }

    public String getCriacao() {
        return criacao;
    }
}