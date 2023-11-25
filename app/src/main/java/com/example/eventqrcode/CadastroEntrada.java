    package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

    public class CadastroEntrada extends AppCompatActivity {

    Button btnGerarQrCode, btnCancelar;
    EditText edtNomePessoa, edtCPFPessoa;
    Spinner comboEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entrada);

        btnGerarQrCode = findViewById(R.id.btnGerarQrCode);
        btnCancelar = findViewById(R.id.btnCancelar);

        edtNomePessoa = findViewById(R.id.edtNomePessoa);
        edtCPFPessoa = findViewById(R.id.edtCPFPessoa);

        comboEventos = findViewById(R.id.comboEventos);
    }
}