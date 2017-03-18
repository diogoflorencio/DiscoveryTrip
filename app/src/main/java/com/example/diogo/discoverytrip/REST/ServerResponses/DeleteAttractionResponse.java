package com.example.diogo.discoverytrip.REST.ServerResponses;

import com.example.diogo.discoverytrip.Model.Atracao;
import com.google.gson.annotations.SerializedName;

/**
 * Created by diogo on 18/03/17.
 */

public class DeleteAttractionResponse {
    @SerializedName("attraction")
    private Atracao atracao;
}
