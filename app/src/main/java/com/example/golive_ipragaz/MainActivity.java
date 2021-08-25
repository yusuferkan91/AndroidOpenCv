package com.example.golive_ipragaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn_detect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_detect = (Button) findViewById(R.id.btn_detect);
        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(CameraActivity.class);
            }
        });
        ModelCall.modelname = "ipragaz.onnx";
//        ModelCall.modelname = "ipragaz_100.pb";
//        ModelCall.modelpbtxt = "ipragaz_100.pbtxt";
        ModelCall.context = MainActivity.this;
        ModelCall.modelLoad();
        requestPermissions();
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }, 0);
            }
        }
    }
}