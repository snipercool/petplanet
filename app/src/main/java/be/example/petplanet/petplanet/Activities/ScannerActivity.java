package be.example.petplanet.petplanet.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import be.example.petplanet.petplanet.R;



public class ScannerActivity extends AppCompatActivity {

    SurfaceView CameraPreview;
    TextView TextResult;
    BarcodeDetector BarcodeDetector;
    CameraSource CameraSource;
    final int RequestCameraPermissionID =1001;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID:{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        CameraSource.start(CameraPreview.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        CameraPreview = (SurfaceView)findViewById(R.id.CameraPreview);
        TextResult = (TextView)findViewById(R.id.TextResult);
        BarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        CameraSource = new CameraSource
                .Builder(this,BarcodeDetector)
                .setRequestedPreviewSize(640,640)
                .build();
        CameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    CameraSource.start(CameraPreview.getHolder());
                    CameraPreview.findFocus();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                CameraSource.stop();
            }
        });

        BarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0){
                    TextResult.post(new Runnable() {
                        @Override
                        public void run() {
                            TextResult.setText(qrcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }
}
