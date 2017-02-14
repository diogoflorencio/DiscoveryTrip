package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Renato on 11/12/2016.
 *
 * Classe que representa as respostas do servidor quando retorna informações de um usuário
 * Essas chamadas são: Create User,Read a User, Update a User, Delete a User
 */

public class ServerResponse extends ResponseAbst {

    @SerializedName("user")
    private User usuario;

    /**
     * Retorna o usuário envolvido na chamada ao servidor
     * @return usuário
     */
    public User getUsuario() {
        return usuario;
    }

    private void setUsuario(User usuario) {this.usuario = usuario;}

}
