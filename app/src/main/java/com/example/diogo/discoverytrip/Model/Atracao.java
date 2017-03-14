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

    @SerializedName("photos")
    List<String> photosId;

    @SerializedName("photo")
    private String photoId;

    @SerializedName("kind")
    private String kind;

    @SerializedName("price")
    private String price;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;

    public String getName() {
        return nome;
    }

    public String getDescription() {
        return descricao;
    }

    public Localizacao getLocation() {
        return localizacao;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getKind() {
        return kind;
    }

    public String getPrice() {
        return price;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public List<String> getPhotos(){
        return photosId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public void setPhotosId(List<String> photosId) {
        this.photosId = photosId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
