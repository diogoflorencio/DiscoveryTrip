package com.example.diogo.discoverytrip.Model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 *
 * Classe que representa um evento
 */
public class Evento {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("photo")
    private String photo;

    @SerializedName("kind")
    private String kind;

    @SerializedName("price")
    private String price;

    @SerializedName("keywords")
    private List<String> keywords;

    @SerializedName("startDate")
    private String startDate;

    public Evento(@NonNull String name,@NonNull String description,@NonNull String endDate, String photo,
                  String kind, String price, List<String> keywords, String startDate){

        this.name = name;
        this.description = description;
        this.endDate = endDate;
        this.photo = photo;
        this.kind = kind;
        this.keywords = keywords;
        this.startDate = startDate;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPhoto() {
        return photo;
    }

    public String getKind() {
        return kind;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public String getStartDate() {
        return startDate;
    }
}
