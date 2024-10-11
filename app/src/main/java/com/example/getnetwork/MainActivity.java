package com.example.getnetwork;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainActivity extends AppCompatActivity {
    AtomicBoolean isRunning = new AtomicBoolean(false);
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = this;
        Button btnStart = findViewById(R.id.btnStart);
        TextView txtTypeNetwork = findViewById(R.id.txtTypeNetwork);
        TextView txtUpLoad = findViewById(R.id.txtUpLoad);
        TextView txtDownLoad = findViewById(R.id.txtDownLoad);
        NetworkUtil networkUtil = new NetworkUtil();


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStart.getText().equals("Start")) {
                    String networkStatus = networkUtil.getNetworkStatus(context);
                    txtTypeNetwork.setText("Type: " + networkStatus);

                    if (networkStatus.equals("LTE")) {
                        btnStart.setText("Stop");
                        isRunning.set(true);

                        executorService.submit(() -> {
                            while (isRunning.get()) {
                                long uploadBytes = NetworkUtil.getMobileUpload(context);
                                long downloadBytes = NetworkUtil.getMobileDownload(context);

                                runOnUiThread(() -> {
                                    txtUpLoad.setText("UpLoad: " + uploadBytes);
                                    txtDownLoad.setText("DownLoad: " + downloadBytes);
                                });

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                        });
                    } else {
                        btnStart.setText("Start");
                        txtUpLoad.setText("Not using 4G/5G network");
                        txtDownLoad.setText("Not using 4G/5G network");
                    }
                } else {
                    btnStart.setText("Start");
                    isRunning.set(false);
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning.set(false);
        executorService.shutdown();
    }

}