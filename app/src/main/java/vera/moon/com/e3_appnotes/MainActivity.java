package vera.moon.com.e3_appnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView notas;
    static ArrayList<String> guardadas = new ArrayList<>();
    static ArrayAdapter adapter;

    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        notas = (ListView)findViewById(R.id.listView);

        //Get data from the sharedPreferences
        SharedPreferences shared = this.getSharedPreferences("vera.moon.com.e3_appnotes", Context.MODE_PRIVATE);
        set = shared.getStringSet("notes",null);
        guardadas.clear();

        if(set != null){
            guardadas.addAll(set);
        }else{
            guardadas.add("Add a note");
            set = new HashSet<String>();
            set.addAll(guardadas);
            shared.edit().putStringSet("notes",set).apply();
        }

        //guardadas = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,guardadas);
        notas.setAdapter(adapter);

        notas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent inte = new Intent(getApplicationContext(),ActivityNotas.class);
                inte.putExtra("noteId",i);
                startActivity(inte);
            }
        });

        notas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                guardadas.remove(position);

                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("vera.moon.com.e3_appnotes", Context.MODE_PRIVATE);

                                if (set == null) {

                                    set = new HashSet<String>();

                                } else {

                                    set.clear();

                                }

                                set.addAll(guardadas);
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addNote) {
            //Open new activity o add note

            guardadas.add("");

            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.robpercival.notes", Context.MODE_PRIVATE);

            if (set == null) {

                set = new HashSet<String>();

            } else {

                set.clear();

            }

            set.addAll(guardadas);
            adapter.notifyDataSetChanged();

            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes", set).apply();

            Intent i = new Intent(getApplicationContext(), ActivityNotas.class);
            i.putExtra("noteId", guardadas.size() - 1);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
