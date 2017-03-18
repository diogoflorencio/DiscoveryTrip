package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Evento;
import com.google.gson.annotations.SerializedName;

/**
 * Created by diogo on 18/03/17.
 */

public class DeleteEventoResponse extends ResponseAbst{

    @SerializedName("event")
    private Evento evento;

    public Evento getEvento() {
        return evento;
    }
}
