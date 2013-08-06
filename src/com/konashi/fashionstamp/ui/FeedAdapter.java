package com.konashi.fashionstamp.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.konashi.fashionstamp.entity.Item;

public class FeedAdapter extends ArrayAdapter<Item> {

	public FeedAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public FeedAdapter(Context context, ArrayList<Item> feed) {
		super(context, 0, feed);
	}

}
