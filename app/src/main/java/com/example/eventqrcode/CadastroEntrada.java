package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.itextpdf.text.pdf.qrcode.WriterException;

import java.util.ArrayList;

public class CadastroEntrada extends AppCompatActivity {

    Button btnGerarQrCode, btnCancelar;
    EditText edtNomePessoa, edtCPFPessoa;
    Spinner comboEventos;

    EventController controller;

    private ArrayAdapter<Evento> adapter;
    private ArrayList<Evento> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entrada);

        controller = new EventController();

        btnGerarQrCode = findViewById(R.id.btnGerarQrCode);
        btnCancelar = findViewById(R.id.btnCancelar);

        edtNomePessoa = findViewById(R.id.edtNomePessoa);
        edtCPFPessoa = findViewById(R.id.edtCPFPessoa);

        listarEventos();

        comboEventos = findViewById(R.id.comboEventos);
        adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_1, eventos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        comboEventos.setAdapter(adapter);
        Bundle extras = getIntent().getExtras();

        //TODO NICOLAS
        //SE VIER DE OUTRA TELA COLOCAR SELECIONADO

    }

    public void buttonGerarQrCode(View view) {
        try{
            String item = comboEventos.getSelectedItem().toString();
            Integer id = Integer.parseInt(item.split(" ")[0]);
            controller.registrarEntrada(this, edtNomePessoa.getText().toString(), edtCPFPessoa.getText().toString(), id);
            Toast.makeText(this, "Salvo com sucesso", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this, "Erro ao salvar pessoa", Toast.LENGTH_LONG).show();
        }

        try{
            Pessoa pessoa = controller.pegarPessoa(this, edtCPFPessoa.getText().toString());
            byte[] qrCode = controller.gerarQrCode(pessoa.getId());
            // imprimir()
        }catch (Exception e){
            Toast.makeText(this, "Erro ao gerar QRCode", Toast.LENGTH_LONG).show();
        }
    }

    public void buttonCancelarCadastroEntrada(View view){

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

}