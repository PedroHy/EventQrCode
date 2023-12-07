package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.eventqrcode.controller.EventController;
import com.example.eventqrcode.model.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btnAddEvent;
    private ListView eventList;
    private Intent it;
    private ArrayAdapter<Evento> adapter;
    private ArrayList<Evento> eventos;
    private Integer idEvento;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddEvent = findViewById(R.id.btnAddEvent);
        eventList = findViewById(R.id.eventList);

        listarEventos();
        popularLista();
        adicionarEventoDeCliqueNaLista();
    }

    public void btnAddEvento(View view){
        abrirActivityCriarEvento();
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

    private void abrirActivityDeEvento(){
        it = new Intent(this, EventoActivity.class);
        it.putExtra("idEvento", idEvento);
        startActivity(it);
    }

    private void abrirActivityCriarEvento(){
        it = new Intent(this, CriarEvento.class);
        startActivity(it);
        finish();
    }

    private void popularLista(){
        adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_1, eventos);
        eventList.setAdapter(adapter);
    }

    private void adicionarEventoDeCliqueNaLista(){
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getAdapter().getItem(i));
                idEvento = Integer.parseInt(item.split(" ")[0]);
                abrirActivityDeEvento();
            }
        });
    }

    public void showPopupWindow(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.drawer, null);

        int width = 580;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // Mostra o Drawer
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);

        // Remove o Drawer da tela quando tocar na tela
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void drawerCriarEvento(View v){
        abrirActivityCriarEvento();
    }

    public void drawerCadastrarEntrada(View v){
        Intent it = new Intent(this, CadastroEntrada.class);
        startActivity(it);
        finish();
    }
}