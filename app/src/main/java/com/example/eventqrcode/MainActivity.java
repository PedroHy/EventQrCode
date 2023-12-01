package com.example.eventqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eventqrcode.controller.EventController;
import com.example.eventqrcode.model.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addEvent;
    ListView eventList;
    Intent it;

    private ArrayAdapter<Evento> adapter;
    private ArrayList<Evento> eventos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addEvent = findViewById(R.id.addEvent);
        eventList = findViewById(R.id.eventList);

        listarEventos();

        adapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_list_item_1, eventos);
        eventList.setAdapter(adapter);
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = String.valueOf(adapterView.getAdapter().getItem(i));
                Integer id = Integer.parseInt(item.split(" ")[0]);
                openEventActivity(id);
            }
        });
    }

    public void telaAddEvento(View view){
        it = new Intent(this, CriarEvento.class);
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

    private void openEventActivity(Integer id){
        it = new Intent(this, EventoActivity.class);
        it.putExtra("idEvento", id);
        startActivity(it);
    }
}