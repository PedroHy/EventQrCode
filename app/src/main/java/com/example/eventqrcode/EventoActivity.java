package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class EventoActivity extends AppCompatActivity {

    TextView lblQtdPessoas;
    ListView listPessoasEvento;

    Button btnRegistrarEntrada, btnRegistrarSaida, btnEncerrarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        lblQtdPessoas = findViewById(R.id.lblQtdPessoas);

        listPessoasEvento = findViewById(R.id.listPessoasEvento);

        btnRegistrarSaida = findViewById(R.id.btnRegistrarSaida);
        btnRegistrarEntrada = findViewById(R.id.btnRegistrarEntrada);
        btnEncerrarEvento = findViewById(R.id.btnEncerrarEvento);
    }
}