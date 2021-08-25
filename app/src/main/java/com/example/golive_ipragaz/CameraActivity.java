package com.example.golive_ipragaz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    Camera camera;
    FrameLayout frameLayout;
    ImageButton button;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ImageButton flash;
    View viewImg;
    TextView str2;
    ProgressDialog progressDialog;
    ArrayList<Bitmap> bitmapArray;
    Drawable drawable;
    Camera.Parameters params;
    String result;
    public static boolean previewing = false;
    static int maskWidth;
    static int maskHeigth;
    ImageView image;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        progressDialog = new ProgressDialog(this);
        bitmapArray = new ArrayList<Bitmap>();
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Görüntü yükleniyor...");

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        button = (ImageButton) findViewById(R.id.button);
        flash = (ImageButton) findViewById(R.id.flash);
        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashoff_foreground, getApplicationContext().getTheme()));;
        viewImg = (View) findViewById(R.id.border_camera);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_fra);
        FrameLayout.LayoutParams paramslayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        str2 = new TextView(this);
//        str2.setText("Mekanik Sayac numaralarını okutun");
        str2.setTextColor(Color.GREEN);
        str2.setTextSize(20);
        contentFrame.addView(str2,paramslayout);
        surfaceView = new SurfaceView(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        frameLayout.addView(surfaceView);

        if(!previewing){
            camera = Camera.open();
            if (camera != null){
                try {
                    camera.setDisplayOrientation(0);
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                    previewing = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(camera != null)
                {   //Toast.makeText(maskCamera.this, PostInfo.modelName, Toast.LENGTH_SHORT).show();

                    progressDialog.show();
                    camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
//                    progressDialog.dismiss();
                }
            }
        });
        flash.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                drawable = flash.getDrawable();

                // TODO Auto-generated method stub
                if(camera != null)
                {
                    if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashon_foreground).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashoff_foreground, getApplicationContext().getTheme()));
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(params);
                    }
                    else if (drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashoff_foreground).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashauto_foreground, getApplicationContext().getTheme()));;
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                        camera.setParameters(params);
                    }
                    else if(drawable.getConstantState().equals(getResources().getDrawable(R.drawable.ic_flashauto_foreground).getConstantState())){
                        flash.setImageDrawable(getResources().getDrawable(R.drawable.ic_flashon_foreground, getApplicationContext().getTheme()));;
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(params);
                    }
                }
            }
        });
    }
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback(){

        public void onShutter() {
            // TODO Auto-generated method stub
        }};
    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
        }};
    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback(){

        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub
            camera.stopPreview();
//            progressDialog.show();
            Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

            try {
                if (bitmapPicture.getWidth()>bitmapPicture.getHeight()){
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    bitmapPicture = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            float x=(viewImg.getX()*bitmapPicture.getWidth())/frameLayout.getWidth();
            float y=(viewImg.getY()*bitmapPicture.getHeight())/frameLayout.getHeight();
            float w=(viewImg.getWidth()*bitmapPicture.getWidth())/frameLayout.getWidth();
            float h=(viewImg.getHeight()*bitmapPicture.getHeight())/frameLayout.getHeight();
            Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture,  (int) x, (int) y, (int) w , (int) h, null, true);
            ResultActivity.image = correctBmp;
            ModelCall.bmp = correctBmp;
            ModelCall.detect();
//            Bitmap unet = ModelCall.detect();
//            bitmapArray.add(unet);
//
//            Bitmap topBmp = bitmapArray.get(0);
//            for(int j = 1; j< bitmapArray.size();j++)
//            {
//                topBmp = combineImages(topBmp,bitmapArray.get(j));
//            }
//            bitmapArray.clear();
//            PostInfo.image = topBmp;
//
//            PostInfo.PhotoPath=saveToInternalStorage(topBmp);
//            progressDialog.show();
//            ServerCall server = new ServerCall();
//            uploadFile(saveToInternalStorage(correctBmp));
//            ResultActivity.serverResult = res;
//            System.out.println("saveToInternalStorage(correctBmp)     " + res);

            finish();
            launchActivity(ResultActivity.class);
//            camera.startPreview();
            progressDialog.dismiss();
        }};
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            launchActivity(MainActivity.class);
        }
    };
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=getOutputMediaFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }
//    public void uploadFile(String Path) {
//
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
////                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        // Map is used to multipart the file using okhttp3.RequestBody
//        File file = new File(Path);
//        System.out.println("***************"+file);
//        // Parsing any Media type file
//        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("key", file.getName(), requestBody);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//        final ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//        Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, filename);
//        call.enqueue(new Callback<ServerResponse>() {
//            @Override
//            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
//                System.out.println(response.body());
//                ServerResponse serverResponse = response.body();
////                while (serverResponse == null){
////                    System.out.println("---");
////                }
//                result = serverResponse.getMessage();
////                System.out.println("--------------"+serverResponse.getMessage());
//                if (serverResponse != null) {
//                    System.out.println("--------------"+serverResponse.getMessage());
//
//                } else {
//                    assert serverResponse != null;
//
//                }
//                ResultActivity.serverResult = serverResponse.getMessage();
//                progressDialog.dismiss();
//                finish();
//                launchActivity(ResultActivity.class);
//                camera.startPreview();
//            }
//            @Override
//            public void onFailure(Call<ServerResponse> call, Throwable t) {
//                System.out.println(t.getMessage());
//            }
//        });
////        return result;
////        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        params = camera.getParameters();

        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //params.setSceneMode(Camera.Parameters.SC);
        params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSizes = sizes.get(0);
        for(int i=0;i<sizes.size();i++)
        {
            if(sizes.get(i).width > mSizes.width)
                mSizes = sizes.get(i);
        }
        params.setPictureSize(mSizes.width, mSizes.height);
        params.set("orientation", "portrait");
        camera.setDisplayOrientation(90);
        params.setRotation(90);
        camera.setParameters(params);
        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        if(previewing){
            camera.stopPreview();
            previewing = false;
        }
        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }
}
