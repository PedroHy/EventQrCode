package com.example.eventqrcode;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventqrcode.controller.EventController;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class EventoActivity extends AppCompatActivity {

    TextView lblQtdPessoas, lblNomeEvento;
    ListView listPessoasEvento;
    Integer id;
    Button btnRegistrarEntrada, btnRegistrarSaida, btnEncerrarEvento;
    Evento evento;

    EventController controller;

    private ArrayAdapter<Pessoa> adapter;
    private ArrayList<Pessoa> pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        iniciarVariaveis();
    }

    public void buttonRegistrarEntrada(View v){
        Evento evento = controller.pegarEvento(this, id);

        if(evento.getPessoas() >= evento.getMaximoPessoas()){
            Toast.makeText(this, "Limite de entradas atingido", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent it = new Intent(this, CadastroEntrada.class);
        it.putExtra("idEvento", id);
        startActivity(it);
        finish();
    }

    public void buttonRegistrarSaida(View v){
        Evento evento = controller.pegarEvento(this, id);

        if(evento.getPessoas() <= 0){
            Toast.makeText(this, "Não há pessoas no evento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inicia a camera, que ao finalizada executa a função 'onActivityResult'
        controller.lerQrCode(this);
    }

    public void buttonFinalizarEvento(View v){
        controller.finalizarEvento(this, id);

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    private void listarPessoas(Integer id){
        pessoas = controller.listarPessoas(this, id);
    }

    private void iniciarVariaveis(){
        controller = new EventController();

        lblQtdPessoas = findViewById(R.id.lblQtdPessoas);
        lblNomeEvento = findViewById(R.id.lblNomeEvento);

        listPessoasEvento = findViewById(R.id.listPessoasEvento);

        btnRegistrarSaida = findViewById(R.id.btnRegistrarSaida);
        btnRegistrarEntrada = findViewById(R.id.btnRegistrarEntrada);
        btnEncerrarEvento = findViewById(R.id.btnEncerrarEvento);

        Bundle extra = getIntent().getExtras();
        id = extra.getInt("idEvento");
        evento = controller.pegarEvento(this, id);

        lblQtdPessoas.setText(String.format(evento.getPessoas()+"/"+evento.getMaximoPessoas()));
        lblNomeEvento.setText(evento.getNome());

        listarPessoas(id);

        adapter = new ArrayAdapter<Pessoa>(this, android.R.layout.simple_list_item_1, pessoas);
        listPessoasEvento.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result == null){
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (result.getContents() == null) {
            Toast.makeText(this, "Scanner cancelado", Toast.LENGTH_SHORT).show();
        } else {
            controller.registrarSaida(this, Integer.parseInt(result.getContents()));

            Intent it = new Intent(this, EventoActivity.class);
            it.putExtra("idEvento", id);
            startActivity(it);
            finish();

        }
    }
}