package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventqrcode.controller.EventController;

public class CriarEvento extends AppCompatActivity {
    Button btnCriarEvento;
    EditText edtCapacidadeMaxima, edtNomeEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        edtNomeEvento = findViewById(R.id.edtNomeEvento);
        edtCapacidadeMaxima = findViewById(R.id.edtCapacidadeMaxima);
        btnCriarEvento = findViewById(R.id.btnCriarEvento);
    }

    public void buttonCriarEvento(View view){
        if(!camposEstaoPreenchidos()) return;
        cadastrarEvento();
    }

    public void btnCancelar(View view){
        retornarParaMainActivity();
    }

    private boolean camposEstaoPreenchidos(){
        if(edtNomeEvento.getText().toString().equals("") || edtCapacidadeMaxima.getText().toString().equals("")){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void cadastrarEvento(){
        try{
            EventController controller = new EventController();
            controller.criarEvento(this, edtNomeEvento.getText().toString(), Integer.parseInt(edtCapacidadeMaxima.getText().toString()));
            Toast.makeText(this, "Evento cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            retornarParaMainActivity();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void retornarParaMainActivity(){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }
}