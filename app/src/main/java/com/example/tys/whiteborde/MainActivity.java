package com.example.tys.whiteborde;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private WhiteBroad wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wb = (WhiteBroad) findViewById(R.id.wb);

    }

    public void clean(View view) {
        wb.clean();
    }

    public void save(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int ret = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (ret == PackageManager.PERMISSION_GRANTED) {
                saveSign();
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "need permission to save file", Snackbar.LENGTH_LONG)
                            .setAction("ok", new View.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(View v) {
                                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                                }
                            })
                            .setActionTextColor(Color.GREEN)
                            .show();
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                }
            }
        } else {
            saveSign();
        }
    }

    private void saveSign() {
        if(wb.save(Environment.getExternalStorageDirectory().getAbsolutePath(), "sign.png")){
            Toast.makeText(this, "save success", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "save failed", Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveSign();
                } else {
                    if (!shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Snackbar.make(wb, "need permission to save file", Snackbar.LENGTH_LONG)
                                .setAction("setting", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }else {
                        Toast.makeText(this, "Insufficient permissions to save failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

}

