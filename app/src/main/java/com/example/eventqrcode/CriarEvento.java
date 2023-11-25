package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CriarEvento extends AppCompatActivity {

    Button btnCriarEvento;
    EditText edtCapacidadeMaxima, edtNomeEvento;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        edtNomeEvento = findViewById(R.id.edtNomeEvento);
        edtCapacidadeMaxima = findViewById(R.id.edtNomeEvento);
        btnCriarEvento = findViewById(R.id.btnCriarEvento);
    }
}