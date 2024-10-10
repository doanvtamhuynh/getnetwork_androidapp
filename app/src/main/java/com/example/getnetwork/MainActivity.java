package com.example.getnetwork;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.getnetwork.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = (Context) this;
        Button btnStart = findViewById(R.id.btnStart);
        TextView txtTypeNetwork = findViewById(R.id.txtTypeNetwork);
        TextView txtUpLoad = findViewById(R.id.txtUpLoad);
        TextView txtDownLoad = findViewById(R.id.txtDownLoad);
        NetworkUtil networkUtil = new NetworkUtil();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTypeNetwork.setText(networkUtil.getNetworkStatus(context));
            }
        });
    }
}