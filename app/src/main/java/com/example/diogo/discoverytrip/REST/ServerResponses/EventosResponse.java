package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Evento;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by renato on 07/02/17.
 *
 * Classe que representa a resposta de sucesso do servidor após uma requisição de getEventos
 */
public class EventosResponse extends ResponseAbst {

    @SerializedName("eventos")
    List<Evento> eventos;

    /**
     * Método que retorna uma lista com os eventos enviados pelo servidor
     * @return lista de eventos
     */
    public List<Evento> getEventos(){
        return eventos;
    }
}
