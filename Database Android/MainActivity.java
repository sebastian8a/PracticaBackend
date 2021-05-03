package com.example.database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String URL_SERVER = "https://practicaup2021.000webhostapp.com/Backend_Mobile/Clientes/insertClient.php";
    //String URL_SERVER = "https://practica-test-up.000webhostapp.com/Backend_Mobile/Clientes/insertClient.php";
    EditText txtName, txtLastName, txtAge, txtEmail;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (EditText)findViewById(R.id.txtName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtAge = (EditText)findViewById(R.id.txtAge);
        txtEmail = (EditText)findViewById(R.id.txtEmail);

        Button btnSave = (Button)findViewById(R.id.btnSave);

        FloatingActionButton btnList = (FloatingActionButton)findViewById(R.id.btnList);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Name = txtName.getText().toString();
                String Lastname = txtLastName.getText().toString();
                String Age = txtAge.getText().toString();
                String Email = txtEmail.getText().toString();

                new AsyncTaskServer().execute(Name, Lastname, Age, Email);

            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ConsultaActivity.class);
                startActivity(i);
            }
        });
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

    private void openAlert(String title, String msj){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(msj);
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alert.create();
        alert.show();
    }

    class AsyncTaskServer extends AsyncTask <String, String, String> {

        protected void onPreExecute(){
            openLoading("Guardando...");
        }

        protected String doInBackground(String... params) {

            try {

                JSONObject parameter = new JSONObject();
                parameter.put("Nombre", params[0]);
                parameter.put("Apellido", params[1]);
                parameter.put("Edad", params[2]);
                parameter.put("Correo", params[3]);

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

                //Para poder enviar los parametros al servidor
                try (OutputStream out = con.getOutputStream()) {
                    out.write(parameter.toString().getBytes());
                    out.flush();
                }

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
                    openAlert("Excelente", "Se guardo exitosamente!!!");
                }else{
                    openAlert("Lo sentimos!", obj.getString("mensaje"));
                }

            }catch (Exception ex){

            }

        }

    }
}