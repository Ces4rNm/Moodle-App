package com.proyect.moodle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class docente_gestiona extends AppCompatActivity {
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);

    private RecyclerView rv_listado_horario;
    private rv_horario_docente_adaptador listado_horario_adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_gestiona);

        TextView tv_titulo = findViewById(R.id.text_docente_gestiona);
        tv_titulo.setText(getIntent().getExtras().getString("nombre")+" - "+getIntent().getExtras().getString("facultad"));

        rv_listado_horario = (RecyclerView)findViewById(R.id.rv_asignar_asignaturas);
        rv_listado_horario.setLayoutManager(new LinearLayoutManager(this));

        listado_horario_adaptador = new rv_horario_docente_adaptador(obtener_horarios());
        listado_horario_adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void  onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Texto:"+obtener_docentes().get(rv_vista_semanas.getChildAdapterPosition(view)).getNombre(), Toast.LENGTH_SHORT).show();
//                horario_docente(view);
            }
        });
        rv_listado_horario.setAdapter(listado_horario_adaptador);
    }

    public List<materia_modelo> obtener_horarios() {
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor filas = bd.rawQuery("select b.dia_semana, b.hora, c.nombre " +
                                        "from Usuario a, Horario b, Asignatura c " +
                                        "where a.ID_usuario=b.ID_docente and b.cod_asignatura=c.cod_asignatura and a.ID_usuario="+getIntent().getExtras().getString("ID_usuario")+";", null);
        List<materia_modelo> docentes = new ArrayList<>();
        if(filas.moveToFirst()) {
            do {
                docentes.add(new materia_modelo( filas.getString(2),filas.getString(0),filas.getString(1)));
            } while (filas.moveToNext());
        }
        return docentes;
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(docente_gestiona.this, listado_docentes.class));
        finish();

    }

    public void crear_horario(View view) {
        Intent i = new Intent(this, crear_horario.class );
        i.putExtra("ID_usuario", getIntent().getExtras().getString("ID_usuario"));
        i.putExtra("nombre", getIntent().getExtras().getString("nombre"));
        i.putExtra("facultad", getIntent().getExtras().getString("facultad"));
        startActivity(i);

    }
}