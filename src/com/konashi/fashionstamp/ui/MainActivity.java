package com.konashi.fashionstamp.ui;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.camerastamp.R;
import com.konashi.fashionstamp.view.SampleView;

public class MainActivity extends Activity implements OnClickListener {
    private static final int REQUEST_GALLERY = 100;
    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_CROP = 300;
    private static final int REQUEST_UPLOAD = 400;
    
    private ImageView imgView;

    private ImageView droid_kun;
    
    private boolean isFirst = true;
    private RelativeLayout parent;
    private int parentX;
    private int parentY;
    private Uri mSaveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button���擾
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        
        // �ʐ^�\���pImageView
        imgView = (ImageView)findViewById(R.id.imageView);

        // �h���C�h�N
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
            wakeUpCrop(mSaveUri);
        }

        if (requestCode == REQUEST_GALLERY) {
            wakeUpCrop(data.getData());
        }
        
        if (requestCode == REQUEST_CROP) {
            Bundle ext = data.getExtras();

            if (ext != null) {
                Bitmap img = ext.getParcelable("data");

                Intent i = new Intent(this, UploadActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("image", img);
                i.putExtras(b);
                startActivityForResult(i, REQUEST_UPLOAD);
            }
        }
        
        if (requestCode == REQUEST_UPLOAD) {
            String result = (String)data.getExtras().get("url");
            Log.d("result", result);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void selectImage() {
        String[] items = {"写真を撮影", "写真を選択"};

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
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_GALLERY);
    }
    
    private void wakeUpCrop(Uri uri) {
        Intent i = new Intent();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setData(uri); // �g���~���O�ɓn���摜�p�X
        intent.putExtra("outputX", 200); // �g���~���O��̉摜�̕�
        intent.putExtra("outputY", 200); // �g���~���O��̉摜�̍���
        intent.putExtra("aspectX", 1); // �g���~���O��̉摜�̃A�X�y�N�g��iX�j
        intent.putExtra("aspectY", 1); // �g���~���O��̉摜�̃A�X�y�N�g��iY�j
        intent.putExtra("scale", true); // �g���~���O���̘g���g��k�������邩
        intent.putExtra("return-data", true); // �g���~���O�����f�[�^��Ԃ���
        startActivityForResult(intent, REQUEST_CROP);

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
