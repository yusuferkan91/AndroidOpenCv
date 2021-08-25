package com.example.golive_ipragaz;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    public static Bitmap image;
    public static String serverResult;
    public static String modelResult;
//    TextView txt_server;
    ImageView ok_img;
    ImageView nok_img;
    TextView txt_model;
    ImageView view;
    Button btn_geri;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ok_img = (ImageView) findViewById(R.id.ok_img);
        nok_img = (ImageView) findViewById(R.id.nok_img);
        if(modelResult == "İpragaz"){
            nok_img.setVisibility(View.INVISIBLE);
        }
        else{
            ok_img.setVisibility(View.INVISIBLE);
        }
        view = (ImageView) findViewById(R.id.imageView);
//        txt_server = (TextView) findViewById(R.id.txtServerResult);
        txt_model = (TextView) findViewById(R.id.txtModelResult);
//        serverResult = ServerCall.result;
//        txt_server.setText(serverResult);
//        txt_model.setText(modelResult);

        if(image != null){
            view.setImageBitmap(image);
        }
        btn_geri = (Button) findViewById(R.id.btn_geri);
        btn_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
