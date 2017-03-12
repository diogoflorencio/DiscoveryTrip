package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 *
 * Classe que representa um ponto turístico
 */
public class PontoTuristico {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("localization")
    private Localizacao location;

    @SerializedName("photos")
    private List<String> photosID;

    @SerializedName("state")
    private String state;

    @SerializedName("created")
    private String created;

    /**
     * Retorna o nome do ponto turístico
     * @return nome
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna uma descrição sobre o ponto turístico
     * @return descrição
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retorna a latitude do ponto turístico
     * @return latitude
     */
    public Localizacao getLocation() {
        return location;
    }

    /**
     * Retorna uma lista com as fotos do ponto turístico, a lista contém pelo menos uma foto e no máximo dez.
     * @return lista de fotos
     */
    public List<String> getPhotos() {
        return photosID;
    }
}
