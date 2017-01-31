package com.example.diogo.discoverytrip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diogo.discoverytrip.Exceptions.DataInputException;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.ResponseAbst;
import com.example.diogo.discoverytrip.REST.ServerResponses.ServerResponse;
import com.example.diogo.discoverytrip.Model.UsuarioEnvio;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Classe activity responsavel pela activity de cadastro de usuario na aplicação
 */
public class CadastroActivity extends AppCompatActivity {

    private EditText txtnome,email,senha,confsenha;

    /**
     * Metodo responsavel por gerenciar a criacao de um objeto 'CadastroActivity'
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "CadastroActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        txtnome = (EditText) findViewById(R.id.txtNome);
        email = (EditText) findViewById(R.id.txtEmail);
        senha = (EditText) findViewById(R.id.txtSenha);
        confsenha = (EditText) findViewById(R.id.txtConfSenha);
        Button btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });

    }

    private void cadastrarUsuario(){
        try {
            verificarDados();

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<ServerResponse> call = apiService.cadastrarUsuario(new UsuarioEnvio(txtnome.getText().toString(), email.getText().toString(), senha.getText().toString()));
            call.enqueue(new Callback<ServerResponse>() {

                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    if(response.isSuccessful()) {
                        ServerResponse serverResponse = (ServerResponse) response.body();
                        Log.d("Server Response",serverResponse.getMessage());
                        Toast.makeText(CadastroActivity.this, R.string.cadastro_sucesso, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
                        finish();
                    }else{

                        Toast.makeText(CadastroActivity.this, "Erro: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(CadastroActivity.this, R.string.cadastro_falha, Toast.LENGTH_SHORT).show();
                    Log.e("App Server Error", t.toString());
                }


            });
        }catch (DataInputException e) {
            Toast.makeText(CadastroActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificarDados() throws DataInputException{
        Log.d("Logger", "verificarDados onCreate");
        if(txtnome.getText().toString().trim().isEmpty()){
            throw new DataInputException("Digite seu nome!");
        }
        if(email.getText().toString().trim().isEmpty()){
            throw new DataInputException("Digite um email válido!");
        }
        if(!senha.getText().toString().equals(confsenha.getText().toString())){
            throw new DataInputException("Senhas estão diferentes!");
        }
        if(senha.getText().toString().length() < 6){
            throw new DataInputException("Senha deve ter no mínimo 6 caracteres!");
        }
        if(senha.getText().toString().isEmpty()){
            throw new DataInputException("Senha não pode ser vazia!");
        }
        return true;
    }
}
