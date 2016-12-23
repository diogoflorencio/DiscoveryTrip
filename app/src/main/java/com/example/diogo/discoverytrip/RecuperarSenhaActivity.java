package com.example.diogo.discoverytrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class RecuperarSenhaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Logger", "RecuperarSenhaActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
    }
}
