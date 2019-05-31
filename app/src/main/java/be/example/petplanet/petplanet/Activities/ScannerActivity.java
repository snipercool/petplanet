package be.example.petplanet.petplanet.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Scanner;

import be.example.petplanet.petplanet.R;



public class ScannerActivity extends AppCompatActivity {

    SurfaceView CameraPreview;
    TextView TextResult;
    BarcodeDetector BarcodeDetector;
    CameraSource CameraSource;
    final int RequestCameraPermissionID =1001;

    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mUniqueReference;



    Button back;



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
        TextResult = findViewById(R.id.TxtResult);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ScannerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        CameraPreview = (SurfaceView) findViewById(R.id.CameraPreview);
        BarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        CameraSource = new CameraSource
                .Builder(this, BarcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(900, 900)
                .build();
        CameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    CameraSource.start(CameraPreview.getHolder());
                    CameraPreview.findFocus();
                } catch (IOException e) {
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
                if (qrcodes.size() != 0) {
                    TextResult.post(new Runnable() {
                        @Override
                        public void run() {
                            TextResult.setText(qrcodes.valueAt(0).displayValue);
                            openDialog();

                        }
                    });
                }
            }
            public void openDialog(){
                scoreDialog ScoreDialog = new scoreDialog();
                ScoreDialog.show(getSupportFragmentManager(), "Score");
            }
        });

    }
}
