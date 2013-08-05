package com.example.camerastamp;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {
	private static final int REQUEST_GALLERY = 100;
	private static final int REQUEST_CAMERA = 200;
	private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //buttonÇéÊìæ
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
        
        imgView = (ImageView)findViewById(R.id.imageView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		String[] items = {"é ê^ÇÇ∆ÇÈ", "âÊëúÇëIë"};

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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		
		if (requestCode == REQUEST_CAMERA) {
			try {
				Bitmap img = (Bitmap)data.getExtras().get("data");
				
				img = PictureUtil.resize(img, 300, 300);
				imgView.setImageBitmap(img);
			} catch (Exception e) {
				
			}
			
		}

		if (requestCode == REQUEST_GALLERY) {
			try {
				InputStream is = getContentResolver().openInputStream(data.getData());
				Bitmap img = BitmapFactory.decodeStream(is);
				is.close();

				img = PictureUtil.resize(img, 300, 300);
				imgView.setImageBitmap(img);
				
			} catch (Exception e) {
				
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
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
    
    
}
