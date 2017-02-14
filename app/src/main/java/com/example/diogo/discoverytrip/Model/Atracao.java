package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 */
public class Atracao {

    @SerializedName("name")
    private String nome;

    @SerializedName("description")
    private String descricao;

    @SerializedName("localization")
    private Localizacao localizacao;

    public String getName() {
        return nome;
    }

    public String getDescription() {
        return descricao;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }
}
