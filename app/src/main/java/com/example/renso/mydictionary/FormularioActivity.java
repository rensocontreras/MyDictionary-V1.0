package com.example.renso.mydictionary;

import android.content.Intent;
        import android.support.design.widget.TextInputLayout;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

public class FormularioActivity extends AppCompatActivity {

    TextInputLayout mtiTitulo, mtiTexto;
    TextView mtvCategoria, mtvEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formulario);

        Intent i=getIntent();
        String categoria = i.getStringExtra("cat");


        mtiTitulo = findViewById(R.id.tiTitulo);
        mtiTexto = findViewById(R.id.tiTexto);

        mtvCategoria = findViewById(R.id.tvCategoria);
        mtvEstado = findViewById(R.id.tvEstado);

        mtvCategoria.setText(categoria);
    }

    public void agregar(View view) {

        String vcategoria = mtvCategoria.getText().toString();
        String vtitulo = mtiTitulo.getEditText().getText().toString();
        String vtexto = mtiTexto.getEditText().getText().toString();
        String vestado;

        if(vtexto.equals("") || vtexto.equals(" "))
            vestado = "INCOMPLETO";
        else vestado = "COMPLETO";

        Datos datos = new Datos(this);
        datos.agregarCliente(datos, vcategoria, vtitulo, vtexto, vestado);

        startActivity(new Intent(this, ListaActivity.class));
        finish();
    }

    public void verLista(View view) {
        startActivity(new Intent(this, ListaActivity.class));
        finish();
    }
}
