package com.example.golive_ipragaz;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelCall {
    public static Net net;
    public static Context context;
    public static Bitmap bmp;
    public static String modelname;
    public static String modelpbtxt;
    public static Map<Double,String> mpx = new HashMap<Double, String>();
    public static String[] Labels = new String[]{"Diğer", "Diğer", "İpragaz"};

    public static void modelLoad(){
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        String modelName = modelname;
        String weights = getPath(modelName,context);
//        String proto = getPath(modelpbtxt,context);
        net = Dnn.readNetFromONNX(weights);
//        net = Dnn.readNetFromTensorflow(weights,proto);
//        net.setPreferableTarget(Dnn.DNN_TARGET_OPENCL);
//        System.out.println(modelName);
//        ResultActivity.modelResult = "ok";
    }
    private static BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    private static String getPath(String file, Context context) {
        System.out.println("*************"+context.getAssets());
        AssetManager assetManager = context.getAssets();
        BufferedInputStream inputStream = null;
        try {
            // Read data from assets.
            inputStream = new BufferedInputStream(assetManager.open(file));
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            // Create copy file in storage.
            File outFile = new File(context.getFilesDir(), file);
            FileOutputStream os = new FileOutputStream(outFile);
            os.write(data);
            os.close();
            // Return a path to file which may be read in common way.
            return outFile.getAbsolutePath();
        } catch (IOException ex) {

        }
        return "";
    }
    public static Bitmap detect(){
        Mat image = new Mat();
        Bitmap bmp32 = bmp.copy(Bitmap.Config.ARGB_8888, true);
//        String path_bitmap = saveToInternalStorage(bmp32);
//        Bitmap bitmap = BitmapFactory.decodeFile(path_bitmap);
//        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
//        System.out.println("bitmap shape: " + bitmap.getWidth()+"*"+bitmap.getHeight());
        Utils.bitmapToMat(bmp32, image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);
//yeni resim resize et (227,227) input olarak ver servera gönder
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(227,227),new Scalar(0,0,0),true,false);
        net.setInput(blob);
        Mat detections = net.forward();
        System.out.println("detection     ::::" + detections);
        System.out.println("detection rows     ::::" + detections.rows());
        System.out.println("detection cols     ::::" + detections.cols());
        double classId = -1;
        for (int i = 0; i < 3; ++i) {
            if(detections.col(i).get(0,0)[0] > classId){
                classId = detections.col(i).get(0,0)[0];
                ResultActivity.modelResult = Labels[i];
            }
//            if(classId < (int)detections.get(0, i)[0])
//                classId = (int)detections.get(0, i)[0];
            System.out.println("!!!!!!!!!!!!!!!!"+detections.col(i).get(0,0)[0]);
            System.out.println("----------------"+detections.row(0).get(0,i)[0]);
            System.out.println("+++++++++++++++++"+detections.col(i).get(0,0).toString());
            System.out.println("+++++++++++++++++"+detections.get(0,i)[0]);
        }
//        System.out.println("!!!!!!!!!!!!!!!!-------"+Labels[classId]);
        Utils.bitmapToMat(bmp32, image);
        return bmp32;
    }
    private static String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
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
}
