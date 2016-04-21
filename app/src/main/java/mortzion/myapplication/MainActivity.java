package mortzion.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        ListView listview = (ListView)findViewById(R.id.listView);
        registerForContextMenu(listview);
        
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Remover item?");
        menu.add(0, v.getId(), 0, "Sim");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "Não");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle()== "Sim"){
            lista.remove(info.position);
            atualizarLista();
        }else if(item.getTitle() == "Não"){}
        else{
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        armazenarListaArquivo();
        lista.removeAll(lista);
    }

    protected void onStart(){
        super.onStart();

        lerListaArquivo();
        atualizarLista();
    }

    public void removerItensSelecionados(){

    }


    public void clearLista(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remover todos os itens");
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                lista.removeAll(lista);
                atualizarLista();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void lerListaArquivo(){
        try{
            FileInputStream fis = new FileInputStream(getBaseContext().getFilesDir().getPath() + "/lista.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String linhaAtual;

            while( (linhaAtual = br.readLine()) != null){
                if(linhaAtual.length() > 1) {
                    lista.add(linhaAtual);
                }

            }

            br.close();
            isr.close();
            fis.close();
        }catch(IOException io){
            String erro = io.getMessage();
        }
    }

    private void armazenarListaArquivo(){
        try{
            File arquivo = new File(getBaseContext().getFilesDir().getPath() + "/lista.txt");
            if(arquivo.exists() == false){
                arquivo.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(arquivo);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);

            if(lista.size() == 0){
                arquivo.delete();
                return;
            }

            bw.write(lista.get(0));
            for(int i=1;i<lista.size();i++){
                bw.newLine();
                bw.append(lista.get(i));
            }


            bw.close();
            osw.close();
            fos.close();

        }catch(IOException io){
            String stringAtual = io.getMessage();
        }
    }

    private void atualizarLista() {
        ListView listview = (ListView) findViewById(R.id.listView);
        String[] novaLista;

        if(lista.size() != 0) {
            novaLista = new String[lista.size()];
            for (int i = 0; i < novaLista.length; i++) {
                novaLista[i] = (i + 1) + " - " + lista.get(i);
            }
        }else{
            novaLista = new String[0];
        }
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, novaLista);

        listview.setAdapter(myAdapter);
    }




    public void adcionarItemLista(View v){
        EditText caixaTexto = (EditText)findViewById(R.id.editText);
        lista.add(caixaTexto.getText().toString());
        caixaTexto.setText("");

        atualizarLista();
    }
}
