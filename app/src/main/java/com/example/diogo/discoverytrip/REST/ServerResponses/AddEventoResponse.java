package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Evento;
import com.google.gson.annotations.SerializedName;

/**
 * Created by renato on 07/02/17.
 */
public class AddEventoResponse extends ResponseAbst{

    @SerializedName("event")
    private Evento evento;
}
