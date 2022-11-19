package com.example.digitalsignmanagement;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class activity_sign extends AppCompatActivity {

    DrawableView drawableView;
    DrawableViewConfig config;
    Button increase, decrease, color, undo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        // initialise the value
        initiaselayout();

    }

    private void initiaselayout() {

        // initialise the layout
        drawableView = findViewById(R.id.paintView);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        color = findViewById(R.id.Save);
        undo = findViewById(R.id.undo);
        config = new DrawableViewConfig();

        // set stroke color as black initially
        config.setStrokeColor(getResources().getColor(android.R.color.black));

        // If the view is bigger than canvas,
        // with this the user will see the bounds (Recommended)
        config.setShowCanvasBounds(true);

        // set width as 20
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // set canvas height
        config.setCanvasHeight(1080);

        // set canvas width
        config.setCanvasWidth(width);
        drawableView.setConfig(config);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // increase the stroke width by 10
                config.setStrokeWidth(config.getStrokeWidth() + 10);
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // decrease stroke width by 10
                config.setStrokeWidth(config.getStrokeWidth() - 10);
            }
        });
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View content = drawableView;
                content.setDrawingCacheEnabled(true);
                content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                Bitmap bitmap = content.getDrawingCache();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File file = new File("sdcard/Pictures/image.png");
                FileOutputStream ostream;
                try {
                    file.createNewFile();
                    ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                    Toast.makeText(getApplicationContext(), "image saved", 5000).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error", 5000).show();
                }
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // undo the most recent changes
                drawableView.clear();
            }
        });
    }
}