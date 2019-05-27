package com.theftbuster.theftbuster.Helpers;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.FaceDetector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Controllers.Variables;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;

/**
 * Created by Julien M. on 08/03/2018.
 */


public class TakePhoto extends Service {
    private SurfaceHolder sHolder;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private boolean success = false;
    static Variables var;
    static String phoneNumber = "No Number";

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onCreate();
        Log.e("TakePhoto", "create");
        Bundle bundle = intent.getExtras();
        if(bundle.getString("phoneNumber") != null)
        phoneNumber = bundle.getString("phoneNumber");
        initPhoto();
    }

    private void initPhoto() {
        if (Camera.getNumberOfCameras() >= 2) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); }
        if (Camera.getNumberOfCameras() < 2) {
            mCamera = Camera.open(); }

        SurfaceView sv = new SurfaceView(getApplicationContext());
        SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);

        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            mCamera.setPreviewTexture(st);
            parameters = mCamera.getParameters();
            List<Integer> frameRates = parameters.getSupportedPreviewFrameRates();
            if (frameRates != null) {
                Integer max = Collections.max(frameRates);
                parameters.setPreviewFrameRate(max);
            }
            if(parameters.isAutoExposureLockSupported()) {
                parameters.setAutoExposureLock(true);
            }
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sHolder = sv.getHolder();
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    Camera.PictureCallback mCall = new Camera.PictureCallback()
    {
        public void onPictureTaken(final byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length); // On crée un bitmap a partir des données data

                FaceDetector detector = new FaceDetector.Builder(getApplicationContext()) // On instancie le détecteur
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();


                int[] rotations = new int[]{0, 90, 180, 270};

                for (int rotation : rotations) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(1, 1);
                    matrix.preRotate(rotation);
                    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    Frame frame = new Frame.Builder().setBitmap(newBitmap).build();
                    SparseArray faces = detector.detect(frame);
                    int facesDetected = faces.size();
                    if (facesDetected > 0) {
                        Log.e("TakePhoto", "Faces detected: " + facesDetected);
                        File dir = new File(Environment.getExternalStorageDirectory() + "/TheftBuster", "");
                        if (!dir.exists()) {
                            dir.mkdirs(); // On crée le dossier TheftBuster s'il n'existe pas
                        }
                        // On enregistre l'image
                        File file = new File(dir, System.currentTimeMillis() + ".jpeg");
                        FileOutputStream out2 = new FileOutputStream(file);
                        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        out2.flush();
                        out2.close();


                        if (!phoneNumber.equals("No Number"))
                            SmsSender.SendMMS(getApplicationContext(), file, phoneNumber);
                        States photo = StatesDAO.selectState("photo");
                        photo.setValue(0);
                        StatesDAO.updateState(Core.mDb, photo);
                        success = true;
                        break; // On sort de la loop
                    }
                }
                detector.release();
                camkapa(sHolder); // On arrête la caméra
            } catch (Exception e) { }

            if (success) {
                stopSelf();
            } else { // On relance dans 5 secondes la détection du visage
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initPhoto();
                    }
                }, 5000);
            }
        }

    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void camkapa(SurfaceHolder sHolder) {
        if (null == mCamera)
            return;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

}

