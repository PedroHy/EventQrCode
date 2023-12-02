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

        controller = new EventController();

        lblQtdPessoas = findViewById(R.id.lblQtdPessoas);
        lblNomeEvento = findViewById(R.id.lblNomeEvento);

        listPessoasEvento = findViewById(R.id.listPessoasEvento);

        btnRegistrarSaida = findViewById(R.id.btnRegistrarSaida);
        btnRegistrarEntrada = findViewById(R.id.btnRegistrarEntrada);
        btnEncerrarEvento = findViewById(R.id.btnEncerrarEvento);

        Bundle extra = getIntent().getExtras();
        id = extra.getInt("idEvento");
        setEvento(id);

        lblQtdPessoas.setText(String.format(evento.getPessoas()+"/"+evento.getMaximoPessoas()));
        lblNomeEvento.setText(evento.getNome());

        listarPessoas(id);

        adapter = new ArrayAdapter<Pessoa>(this, android.R.layout.simple_list_item_1, pessoas);
        listPessoasEvento.setAdapter(adapter);
    }

    public void buttonRegistrarEntrada(View v){
        Intent it = new Intent(this, CadastroEntrada.class);
        it.putExtra("idEvento", id);
        startActivity(it);
    }

    public void buttonRegistrarSaida(View v){
        controller.lerQrCode(this);
        //registrarSaida(Context context, Integer idPessoa)
    }

    //Função executada ao finalizar o scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanner cancelado", Toast.LENGTH_LONG).show();
            } else {
                controller.registrarSaida(this, Integer.parseInt(result.getContents()));
                Toast.makeText(this, "Saída registrada com sucesso", Toast.LENGTH_SHORT).show();

                Intent it = new Intent(this, MainActivity.class);
                this.startActivity(it);
                this.finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void buttonFinalizarEvento(View v){
        EventController controller = new EventController();
        controller.finalizarEvento(this, id);
    }

    private void setEvento(Integer idEvento){
        EventController controller = new EventController();
        evento = controller.pegarEvento(this, id);
    }

    private void listarPessoas(Integer id){
        EventController controller = new EventController();
        pessoas = controller.listarPessoas(this, id);
    }
}