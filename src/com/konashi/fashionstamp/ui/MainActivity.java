package com.konashi.fashionstamp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.camerastamp.R;

public class MainActivity extends Activity implements OnClickListener {
    private static final int REQUEST_GALLERY = 100;
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_CROP = 300;
    private static final int REQUEST_UPLOAD = 400;
    
    private Uri mSaveUri;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(this);
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
        if (id == R.id.button2) {
            Intent intent = new Intent(this, ItemShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", 21);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            wakeUpCrop(mSaveUri);
        }

        if (requestCode == REQUEST_GALLERY) {
            wakeUpCrop(data.getData());
        }
        
        if (requestCode == REQUEST_CROP) {
            Bundle ext = data.getExtras();

            if (ext != null) {
                Bitmap img = ext.getParcelable("data");

                Intent intent = new Intent(this, UploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("image", img);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_UPLOAD);
            }
        }
        
        if (requestCode == REQUEST_UPLOAD) {
            String result = (String)data.getExtras().get("url");
            Log.d("result", result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void selectImage() {
        String[] items = {"写真を撮る", "画像を選択"};

    }

    private void wakeUpCamera(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append(".jpg");
 
        String fileName = stringBuilder.toString();
 
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        ContentResolver contentResolver = getContentResolver();
        mSaveUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
 
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSaveUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void wakeUpGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }
    
    private void wakeUpCrop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        
        // Crop to 200 * 200
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
       
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP);
    }
}
