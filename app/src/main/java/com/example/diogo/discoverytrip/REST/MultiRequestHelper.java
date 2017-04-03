package com.example.diogo.discoverytrip.REST;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultiRequestHelper {

    Context context;

    public MultiRequestHelper(Context context){
        this.context = context;
    }

    /**
     * Metodo que cria a parte da request do servidor que contém uma foto. Esse método carrega uma foto a partir da uri fornecida
     * e adiciona a request com o nome fornecido ao método.
     * @param partName nome do parâmetro na requisição ao servidor
     * @param imageUri uri da imagem
     * @return parte da request contendo a foto desejada
     */
    public MultipartBody.Part loadPhoto(String partName, Uri imageUri){
        Log.d("Logger", "MultiRequestHelper loadPhoto");
        /*InputStream stream = null;
        stream = context.getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);*/

        File file = new File(getRealPathFromURI(imageUri));
        Log.d("Logger", "MultiRequestHelper loadPhoto1");

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(context.getContentResolver().getType(imageUri)), file);
        Log.d("Logger", "MultiRequestHelper loadPhoto2");

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     * Metodo que cria uma parte do form da request. Essas partes devem ser adicionadas no mapa que será enviado na request.
     * @param parameterValue valor do parâmetro - o nome do parâmetro é adicionado como chave no mapa passado na request.
     * @return parte do form para ser adicionada no mapa
     */
    public RequestBody createPartFrom(String parameterValue) {
        Log.d("Logger", "MultiRequestHelper createPartFrom");
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, parameterValue);
    }

    private String getRealPathFromURI(Uri uri) {
        Log.d("Logger", "MultiRequestHelper getRealPathFromURI");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
