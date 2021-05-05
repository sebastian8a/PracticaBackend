package com.jorge.fcmup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView lblToken = (TextView)findViewById(R.id.lblToken);

        String Token = FirebaseInstanceId.getInstance().getToken();

        lblToken.setText(Token);
    }
}