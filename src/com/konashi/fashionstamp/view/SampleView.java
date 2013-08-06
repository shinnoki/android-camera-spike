package com.konashi.fashionstamp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.example.camerastamp.R;

public class SampleView extends View{

    private Bitmap mBitmap;
    private Bitmap stamp_img;
    private Canvas mCanvas;

    // コンストラクタ
    public SampleView(Context context) {
        super(context);
        stamp_img = BitmapFactory.decodeResource( getResources(), R.drawable.ic_launcher );
    }

    // 画面変更時
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        //キャンバス作成
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // 描画関数
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    // タッチされた時
    public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    mCanvas.drawBitmap( stamp_img , x, y, null);
                    invalidate();
                    break;
            }
        return true;
    }
}
