package com.example.diogo.discoverytrip.Model;

import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String nome;
    @SerializedName("email")
    private String email;
    @SerializedName("photo_url")
    private String foto_url;
    @SerializedName("created")
    private String createdOn;

    public User(String id, String nome, String email, String foto_url, String createdOn){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.foto_url = foto_url;
        this.createdOn = createdOn;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

}
