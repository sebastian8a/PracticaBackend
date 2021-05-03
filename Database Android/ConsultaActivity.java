package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class ConsultaActivity extends AppCompatActivity {

    String URL_SERVER = "https://practicaup2021.000webhostapp.com/Backend_Mobile/Clientes/getClients.php";
    ProgressDialog loading;
    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);

        new AsyncTaskServer().execute();
    }

    private void createColumns(JSONArray data) throws Exception {
        TableRow columns = new TableRow(this);
        table.addView(columns);

        Iterator<String> iterator = data.getJSONObject(0).keys();

        while (iterator.hasNext()){
            String key = iterator.next();

            TextView text = new TextView(this);
            text.setText(key);
            text.setBackgroundColor(Color.parseColor("#FF5555"));
            text.setGravity(Gravity.CENTER);
            columns.addView(text);
        }
    }

    private void createDetail(JSONArray detail) throws Exception{

        for(int i = 0; i < detail.length(); i++){
            TableRow row = new TableRow(this);
            table.addView(row);

            JSONObject obj = detail.getJSONObject(i);

            Iterator<String> iterator = obj.keys();

            while (iterator.hasNext()){
                String key = iterator.next();
                String value = obj.getString(key);

                TextView text = new TextView(this);
                text.setText(value);
                text.setTextColor(getColor(R.color.teal_700));
                text.setGravity(Gravity.CENTER);
                row.addView(text);
            }

        }
    }

    private void openLoading(String msj){
        loading = new ProgressDialog(this);
        loading.setMessage(msj);
        loading.setCancelable(false);
        loading.show();
    }

    private void closeLoading(){
        loading.dismiss();
    }

    class AsyncTaskServer extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            openLoading("Guardando...");
        }

        protected String doInBackground(String... params) {

            try {

                URL url = new URL(URL_SERVER);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setDoOutput(true);
                con.setDoInput(true);
                con.setReadTimeout(10000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");

                //Cabeceras
                con.setRequestProperty("Content-Type", "application/json;");
                con.setRequestProperty("Accept", "application/json;");
                con.setChunkedStreamingMode(0);

                String response = "";

                //Para poder recibir la contestacion del servidor
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String line;
                    StringBuilder sb = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    response = sb.toString();
                }

                con.disconnect();

                Thread.sleep(2000);

                return response;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }

        protected void onPostExecute(String result){

            closeLoading();

            try{

                JSONObject obj = new JSONObject(result);

                if(obj.getInt("estado") == 1){

                    JSONArray data = obj.getJSONArray("data");

                    //Construir el Table
                    createColumns(data);
                    createDetail(data);
                }

            }catch (Exception ex){

            }

        }

    }

}