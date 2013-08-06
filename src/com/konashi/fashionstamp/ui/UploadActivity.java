package com.konashi.fashionstamp.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.camerastamp.R;
import com.konashi.fashionstamp.ui.helper.ImageHelper;
import com.konashi.fashionstamp.ui.helper.UploadAsyncTask;

public class UploadActivity extends Activity implements OnClickListener {
    private ImageView imgView;
    private EditText titleEdit;
    private EditText descriptionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imgView = (ImageView)findViewById(R.id.imageView1);
        titleEdit = (EditText)findViewById(R.id.editText1);
        descriptionEdit = (EditText)findViewById(R.id.editText2);

        Button upButton = (Button)findViewById(R.id.button1);
        upButton.setOnClickListener(this);

        Intent i = getIntent();
        Bitmap img = (Bitmap)i.getParcelableExtra("image");
        imgView.setImageBitmap(img);
    }
    
    @Override
    public void onClick(View v) {
       int id = v.getId();
       if (id == R.id.button1) {
           postItem();
       }
    }
    
    private void postItem() {
        try {
            MultipartEntity entity = new MultipartEntity() ;

            ByteArrayBody imgBody = ImageHelper.image2byteArrayBody(this.imgView);
            entity.addPart("item[image]", imgBody);

            StringBody titleBody = new StringBody(titleEdit.getText().toString(), Charset.forName("UTF-8"));
            entity.addPart("item[title]", titleBody);
            
            StringBody descriptionBody = new StringBody(descriptionEdit.getText().toString(), Charset.forName("UTF-8"));
            entity.addPart("item[description]", descriptionBody);
        
            new UploadAsyncTask(this).execute(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    


}
