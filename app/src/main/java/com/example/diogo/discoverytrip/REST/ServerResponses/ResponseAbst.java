package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 18/12/2016.
 *
 * Classe abstrata que representa uma resposta genérica do servidor, independente de ser de sucesso ou erro
 */

public class ResponseAbst {

    @SerializedName("status")
    private String status;

    /**
     * Retorna o status da requisição
     * @return status
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
