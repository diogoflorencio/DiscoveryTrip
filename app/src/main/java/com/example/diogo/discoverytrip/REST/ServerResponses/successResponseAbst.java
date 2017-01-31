package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 16/12/2016.
 *
 * Classe abstrata que representa as respostas de sucesso nos acessos ao servidor
 */

public class successResponseAbst extends ResponseAbst{

    @SerializedName("message")
    private String message;

    /**
     * Retorna a menssagem enviada pelo servidor na requisição
     * @return menssagem
     */
    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
