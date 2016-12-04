package vera.moon.com.e3_appnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class ActivityNotas extends AppCompatActivity implements TextWatcher{

    EditText texto;

    int valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_notas);

        texto = (EditText)findViewById(R.id.editText);

        Intent i = getIntent();
        valor = i.getIntExtra("noteId",-1);

        if(valor != -1){
            texto.setText(MainActivity.guardadas.get(valor));
        }else{
            texto.addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        MainActivity.guardadas.set(valor,String.valueOf(charSequence));
        MainActivity.adapter.notifyDataSetChanged();

        SharedPreferences sha = getSharedPreferences("vera.moon.com.e3_appnotes", Context.MODE_PRIVATE);

        if(MainActivity.set == null){
            MainActivity.set = new HashSet<String>();
        }else{
            MainActivity.set.clear();
        }

        MainActivity.set.addAll(MainActivity.guardadas);
        sha.edit().remove("notes").apply();
        sha.edit().putStringSet("notes",MainActivity.set).apply();

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
