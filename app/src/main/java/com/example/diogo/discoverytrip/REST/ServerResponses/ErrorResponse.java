package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 16/12/2016.
 *
 * Classe que representa as respostas de erro do servidor. Todas as respostas de erro seguem este formato.
 */
public class ErrorResponse{

    @SerializedName("error")
    private String errorType;

    @SerializedName("error_description")
    private String errorDescription;

    /**
     * Retorna qual o tipo do erro que ocorreu na requisição.
     * @return tipo de erro
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Retorna uma descrição sobre o erro que ocorreu na requisição
     * @return descrição do erro
     */
    public String getErrorDescription() {
        return errorDescription;
    }
}
