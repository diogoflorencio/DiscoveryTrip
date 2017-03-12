package com.example.diogo.discoverytrip.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Model.ReminderJson;
import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ReminderResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe activity responsavel pela activity de recuperacao de senha na aplicação
 */
public class RecuperarSenhaActivity extends AppCompatActivity {

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'RecuperarSenhaActivity'
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "RecuperarSenhaActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        Button btnRecuperarSenha = (Button) findViewById(R.id.recuperar_senha_btnRecuperar);
        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = (EditText) findViewById(R.id.recuperar_senha_txtEmail);
                if(email.getText().toString().trim().equals("")){
                    Toast.makeText(RecuperarSenhaActivity.this,"Digite o seu email ou user name",Toast.LENGTH_SHORT).show();
                }
                else {

                    final AlertDialog waitDialog = new AlertDialog.Builder(RecuperarSenhaActivity.this).create();
                    waitDialog.setCancelable(false);
                    waitDialog.setView(getLayoutInflater().inflate(R.layout.dialog_aguarde,null));
                    waitDialog.show();

                    ReminderJson reminderJson = new ReminderJson(email.getText().toString());

                    Call<ReminderResponse> call = ApiClient.API_SERVICE.passwordReminder(reminderJson);
                    call.enqueue(new Callback<ReminderResponse>(){

                        @Override
                        public void onResponse(Call<ReminderResponse> call, Response<ReminderResponse> response) {
                            waitDialog.dismiss();
                            if(response.isSuccessful()){
                                final AlertDialog alertDialog = new AlertDialog.Builder(RecuperarSenhaActivity.this).create();
                                alertDialog.setCancelable(false);
                                alertDialog.setTitle("Sucesso");
                                alertDialog.setMessage("Uma mensagem foi enviada para o seu email.");
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(RecuperarSenhaActivity.this,LoginActivity.class));
                                        alertDialog.dismiss();
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                            else{
                                try {
                                    ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                                    final AlertDialog alertDialog = new AlertDialog.Builder(RecuperarSenhaActivity.this).create();
                                    alertDialog.setTitle("Erro");
                                    alertDialog.setMessage(error.getErrorDescription());
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ReminderResponse> call, Throwable t) {
                            waitDialog.dismiss();
                            Toast.makeText(RecuperarSenhaActivity.this,R.string.falha_conexao,Toast.LENGTH_SHORT).show();
                            Log.e("Comunication Error",t.toString());
                        }
                    });
                }

            }
        });
    }
}
