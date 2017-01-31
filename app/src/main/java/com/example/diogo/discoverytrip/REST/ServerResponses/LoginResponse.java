package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 12/12/2016.
 *
 * Classe que representa a resposta de sucesso do servidor após feita uma requisição de login ou atualização da seção
 */
public class LoginResponse extends ResponseAbst {

    @SerializedName("access_token")
    private String accesstoken;

    @SerializedName("refresh_token")
    private String refreshtoken;

    @SerializedName("expires_in")
    private int expires;

    @SerializedName("token_type")
    private String tokentype;

    /**
     * Retorna o token de acesso para os futuros acessos ao servidor. Todas as requisições ao servidor após o login devem conter este token.
     * @return token
     */
    public String getAccesstoken() {
        return accesstoken;
    }

    private void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    /**
     * Retorna o token que deverá ser usado para a atualização da seção do usuário.
     * @return token
     */
    public String getRefreshtoken() {
        return refreshtoken;
    }

    private void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    /**
     * Retorna quanto tempo o access token fica ativo. Esse tempo não é atualizado nesse objeto.
     * @return tempo do token
     */
    public int getExpires() {
        return expires;
    }

    private void setExpires(int expires) {
        this.expires = expires;
    }

    /**
     * Retorna o tipo do token
     * @return tipo do token
     */
    public String getTokentype() {
        return tokentype;
    }

    private void setTokentype(String tokentype) {
        this.tokentype = tokentype;
    }
}
