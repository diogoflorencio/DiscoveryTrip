package com.example.diogo.discoverytrip.Model;

import android.support.annotation.NonNull;

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

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("photos")
    private List<Byte> photos;

    public PontoTuristico(@NonNull String name,@NonNull String description,@NonNull String latitude,
                          @NonNull String longitude,@NonNull List<Byte> photos) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photos = photos;
    }

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
    public String getLatitude() {
        return latitude;
    }

    /**
     * Retorna a longitude do ponto turístico
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Retorna uma lista com as fotos do ponto turístico, a lista contém pelo menos uma foto e no máximo dez.
     * @return lista de fotos
     */
    public List<Byte> getPhotos() {
        return photos;
    }
}
