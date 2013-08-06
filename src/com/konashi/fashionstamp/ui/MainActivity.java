package com.konashi.fashionstamp.ui;

import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.camerastamp.R;
import com.konashi.fashionstamp.ui.helper.ImageHelper;
import com.konashi.fashionstamp.view.SampleView;

public class MainActivity extends Activity implements OnClickListener {
    private static final int REQUEST_GALLERY = 100;
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_UPLOAD = 300;
    
    private static final String POST_URL = "";

    private ImageView imgView;

    private ImageView droid_kun;
    
    private boolean isFirst = true;
    private RelativeLayout parent;
    private int parentX;
    private int parentY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buttonを取得
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        
        // 写真表示用ImageView
        imgView = (ImageView)findViewById(R.id.imageView);

        // ドロイド君
        // droid_kun = (ImageView)findViewById(R.id.droid_kun);
        // droid_kun.setVisibility(View.INVISIBLE);

        parent = (RelativeLayout)findViewById(R.id.rLayout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        SampleView sVew = new SampleView(this);
        parent.addView(sVew, params);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button1) {
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            try {
                Bitmap img = (Bitmap)data.getExtras().get("data");
                img = ImageHelper.resize(img, 300, 300);
                
                Bundle b = new Bundle();
                b.putParcelable("image", img);
                Intent i = new Intent(this, UploadActivity.class);
                i.putExtras(b);
                startActivityForResult(i, REQUEST_UPLOAD);
            } catch (Exception e) {

            }

        }

        if (requestCode == REQUEST_GALLERY) {
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(is);
                is.close();

                img = ImageHelper.resize(img, 300, 300);

                Bundle b = new Bundle();
                b.putParcelable("image", img);
                Intent i = new Intent(this, UploadActivity.class);
                i.putExtras(b);
                startActivityForResult(i, REQUEST_UPLOAD);

            } catch (Exception e) {

            }
        }
        
        if (requestCode == REQUEST_UPLOAD) {
            String result = (String)data.getExtras().get("url");
            Log.d("result", result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void selectImage() {
        String[] items = {"写真をとる", "画像を選択"};

        new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    wakeUpCamera();
                }
                if (item == 1) {
                    wakeUpGallery();
                }
            }
        })
        .show();

    }

    private void wakeUpCamera(){
        Intent i = new Intent();
        i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_CAMERA);
    }

    private void wakeUpGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_GALLERY);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
        int action = event.getAction();
        int touchX = (int)event.getRawX();
        int touchY = (int)event.getRawY();

        if (isFirst) {
            parent = (RelativeLayout)findViewById(R.id.rLayout);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            parentX = location[0];
            parentY = location[1];
            
            isFirst = false;
        }

        if (action == MotionEvent.ACTION_DOWN) {
            droid_kun = new ImageView(this);
            droid_kun.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            parent.addView(droid_kun, params);

            int left = touchX - parentX;
            int top = touchY - parentY;
            droid_kun.layout(left, top, left + droid_kun.getWidth(), top + droid_kun.getHeight());

        }

        if (action == MotionEvent.ACTION_MOVE) {
            int left = touchX - parentX;
            int top = touchY - parentY;
            droid_kun.layout(left, top, left + droid_kun.getWidth(), top + droid_kun.getHeight());
        }

        if (action == MotionEvent.ACTION_UP) {
            // droid_kun.setVisibility(View.INVISIBLE);
            
            // droid_kun = new ImageView(this);
            // droid_kun.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
            // flLayout.addView(droid_kun);
        }
    */

        return super.onTouchEvent(event);
    }


}
