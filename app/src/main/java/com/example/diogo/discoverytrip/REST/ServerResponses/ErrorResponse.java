package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 16/12/2016.
 *
 * Classe que representa as resposta de erro do servidor. Todas as respostas de erro seguem este formato
 */
public class ErrorResponse extends ResponseAbst{

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

    private void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
     * Retorna uma descrição sobre o erro que ocorreu na requisição
     * @return descrição do erro
     */
    public String getErrorDescription() {
        return errorDescription;
    }

    private void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
