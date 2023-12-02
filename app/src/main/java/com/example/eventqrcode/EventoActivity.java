package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventqrcode.controller.EventController;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;

import java.util.ArrayList;

public class EventoActivity extends AppCompatActivity {

    TextView lblQtdPessoas, lblNomeEvento;
    ListView listPessoasEvento;
    Integer id;
    Button btnRegistrarEntrada, btnRegistrarSaida, btnEncerrarEvento;
    Evento evento;
    private ArrayAdapter<Pessoa> adapter;
    private ArrayList<Pessoa> pessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

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
        //lerQrCode()
        //registrarSaida(Context context, Integer idPessoa)
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