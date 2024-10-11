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
        AtomicBoolean isRunning = new AtomicBoolean(true);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                if(btnStart.getText().equals("Start")){
                    String networkStatus = networkUtil.getNetworkStatus(context);
                    txtTypeNetwork.setText("Type: " + networkStatus);

                    if (networkStatus.equals("4G/5G")) {
                        btnStart.setText("Stop");
                        // Sử dụng ExecutorService để thực hiện tác vụ bất đồng bộ
                        while (isRunning.get()){
                            Callable<List<Long>> dataUsageTask = new Callable<List<Long>>() {
                                @Override
                                public List<Long> call() throws Exception {
                                    long uploadBytes = NetworkUtil.getMobileUpload();
                                    long downloadBytes = NetworkUtil.getMobileDownload();
                                    List<Long> resultNetworkTraffic = new ArrayList<>();
                                    resultNetworkTraffic.add(uploadBytes);
                                    resultNetworkTraffic.add(downloadBytes);
                                    return resultNetworkTraffic;
                                }
                            };

                            // Gửi Callable tới ExecutorService và nhận Future object
                            Future<List<Long>> futureResult = executor.submit(dataUsageTask);

                            // Sử dụng Handler để cập nhật giao diện người dùng trên UI thread
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                try {
                                    List<Long> result = futureResult.get();  // Đợi kết quả từ Callable
                                    txtUpLoad.setText("UpLoad: " + result.get(0));  // Hiển thị dữ liệu lên UI
                                    txtDownLoad.setText("DownLoad: " + result.get(1));  // Hiển thị dữ liệu lên UI
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    } else {
                        btnStart.setText("Start");
                        txtUpLoad.setText("Not using 4G/5G network");
                        txtDownLoad.setText("Not using 4G/5G network");
                        executor.shutdown();
                    }
                }
                else {
                    btnStart.setText("Start");
                    isRunning.set(false);
                    executor.shutdown();
                }
            }
        });
    }
}