package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eventqrcode.controller.EventController;
import com.example.eventqrcode.model.Evento;
import com.example.eventqrcode.model.Pessoa;
import com.itextpdf.text.Image;

import java.util.ArrayList;

public class CadastroEntrada extends AppCompatActivity {

    private Button btnGerarQrCode, btnCancelar;
    private EditText edtNomePessoa, edtCPFPessoa;
    private Spinner comboEventos;
    private EventController controller;
    private Integer idEvento;
    private ArrayAdapter<Evento> adapter;
    private ArrayList<Evento> eventos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entrada);

        controller = new EventController();
        btnGerarQrCode = findViewById(R.id.btnGerarQrCode);
        btnCancelar = findViewById(R.id.btnCancelar);
        edtNomePessoa = findViewById(R.id.edtNomePessoa);
        edtCPFPessoa = findViewById(R.id.edtCPFPessoa);
        comboEventos = findViewById(R.id.comboEventos);

        listarEventos();
        verificarIdEvento();
        preencherComboEventos();

    }

    public void buttonGerarQrCode(View view) {
        if (!camposEstaoPreenchidos()) return;
        registrarPessoa();
        gerarQrCode();
        abrirNovaTela();
    }

    public void buttonCancelarCadastroEntrada(View view){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

    private void listarEventos(){
        try{
            EventController controller = new EventController();
            eventos = controller.listarEventos(this);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }
    }

    private boolean camposEstaoPreenchidos(){
        if(edtNomePessoa.getText().toString().equals("") || edtCPFPessoa.getText().toString().equals("")){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void abrirNovaTela(){
        Intent it;
        if(idEvento != null){
            it = new Intent(this, EventoActivity.class);
            it.putExtra("idEvento", idEvento);
        } else{
            it = new Intent(this, MainActivity.class);
        }
        startActivity(it);
        finish();
    }

    private void registrarPessoa(){
        try{
            String item = comboEventos.getSelectedItem().toString();
            Integer id = Integer.parseInt(item.split(" ")[0]);
            controller.registrarEntrada(this, edtNomePessoa.getText().toString(), edtCPFPessoa.getText().toString(), id);
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Erro ao salvar pessoa", Toast.LENGTH_SHORT).show();
        }
    }

    private void gerarQrCode(){
        try{
            Pessoa pessoa = controller.pegarPessoa(this, edtCPFPessoa.getText().toString());
            Image qrCode = controller.gerarQrCode(pessoa.getId());
            Uri uri = controller.gerarPdf(this, pessoa.getId().toString(), qrCode);
            //Tamo com erro aqui, em vizualiar o pdf, mas ta gerando;

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void preencherComboEventos(){
        adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_1, eventos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboEventos.setAdapter(adapter);
    }

    private void verificarIdEvento(){

    }
}