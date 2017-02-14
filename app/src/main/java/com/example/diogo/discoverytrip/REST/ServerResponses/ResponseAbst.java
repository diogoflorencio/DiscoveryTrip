package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 18/12/2016.
 *
 * Classe que representa uma resposta de sucesso genérica do servidor.
 */

public class ResponseAbst{

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    /**
     * Retorna o status da requisição
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retorna a mensagem enviada pelo servidor após feita uma requisição. Essa mensagem é NULL caso
     * não seja uma resposta de sucesso, nesse caso a mensagem de erro será retornada pelos métodos
     * getErrorType e getErrorDescription.
     * @return mensagem de resposta do servidor.
     */
    public String getMessage(){
        return message;
    }
}
