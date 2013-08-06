package com.konashi.fashionstamp.ui.helper;

import java.io.ByteArrayOutputStream;

import org.apache.http.entity.mime.content.ByteArrayBody;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageHelper {
	public static Bitmap resize(Bitmap picture, int targetWidth, int targetHeight) {
	    if (picture == null || targetWidth < 0 || targetHeight < 0) {
	        return null;
	    }

	    int pictureWidth = picture.getWidth();
	    int pictureHeight = picture.getHeight();
	    float scale = Math.min((float) targetWidth / pictureWidth, (float) targetHeight / pictureHeight); // (1)

	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);
	    
	    return Bitmap.createBitmap(picture, 0, 0, pictureWidth, pictureHeight, matrix, true);
	}
	
	
    public static ByteArrayBody image2byteArrayBody(ImageView iv){
        //byte配列を読み込む準備をする
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //BitmapDrawableをimageViewから取得
        BitmapDrawable bd = (BitmapDrawable) iv.getDrawable();
        //BitmapDrawableからBitmapを取得し、outputStreamへ書き込む(保存形式,クオリティ,out)
        bd.getBitmap().compress(CompressFormat.JPEG, 100, out);
        //outputStreamからByteArrayBodyへ変換
        
        return new ByteArrayBody(out.toByteArray(),System.currentTimeMillis() + ".jpg");
    }
}