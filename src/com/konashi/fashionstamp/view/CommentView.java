package com.konashi.fashionstamp.view;

import com.example.camerastamp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class CommentView extends FrameLayout {
    public CommentView(Context context) {
        super(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        View layout = inflator.inflate(R.layout.comment, null);
        this.addView(layout);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
