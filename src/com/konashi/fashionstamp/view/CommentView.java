package com.konashi.fashionstamp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.camerastamp.R;

public class CommentView extends FrameLayout {
    public CommentView(Context context) {
        super(context);
        LayoutInflater inflator = LayoutInflater.from(context);
        View layout = inflator.inflate(R.layout.comment_edit, null);
        this.addView(layout);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO 自動生成されたメソッド・スタブ

    }

}
