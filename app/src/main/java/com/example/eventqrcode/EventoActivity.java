package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        Bundle extra = getIntent().getExtras();
        Integer id = extra.getInt("idEvento");

        lblQtdPessoas.setText(id.toString());
        //listarPessoas(Context context, Integer idEvento)
        //pegarEvento(Context context, Integer id)
    }

    public void buttonRegistrarEntrada(View v){
        Intent it = new Intent(this, CadastroEntrada.class);
        startActivity(it);
    }

    public void buttonRegistrarSaida(View v){
        //lerQrCode()
        //registrarSaida(Context context, Integer idPessoa)
    }

    public void buttonFinalizarEvento(View v){
        //finalizarEvento(Context context, Integer idEvento)
    }






}