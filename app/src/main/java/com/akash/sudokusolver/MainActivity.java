package com.akash.sudokusolver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED ) {
                Log.v("permission","Permission is granted");
            } else {

                Log.v("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("permission","Permission is granted");
        }
        /*Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraOpen.class);
                startActivityForResult(intent,1313);
            }
        });*/
        String DATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SudokuSolver/tessdata/";
        Log.d("path", DATA_PATH);

        AssetManager am = getAssets();
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.d("DIR", "ERROR: Creation of directory " + DATA_PATH + " on sdcard failed");
                return;
            } else {
                Log.d("EDIR", "Created directory " + DATA_PATH + " on sdcard");
            }

        }


        File data = new File(DATA_PATH + "eng.traineddata");
        if (!data.exists()) {
            try {
                InputStream is = am.open("tessdata/eng.traineddata");
                OutputStream out = new FileOutputStream(DATA_PATH + "eng.traineddata");
                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                is.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("errorFNF", e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("errorIO", e.toString());
            }
        }
        if(OpenCVLoader.initDebug()) {
            Log.d("opencv","loaded");
        }
        else
        {
            Log.d("opencv","Not loaded");
        }

        Intent intent=new Intent(MainActivity.this, SudokuSolver.class);
        finish();
        startActivity(intent);

}
}