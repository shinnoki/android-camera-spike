package com.konashi.fashionstamp.ui;

import java.util.List;

import com.konashi.fashionstamp.entity.Item;

import android.content.Context;
import android.widget.ArrayAdapter;

public class FeedAdapter extends ArrayAdapter<Item> {

	public FeedAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO 自動生成されたコンストラクター・スタブ
	}

}
