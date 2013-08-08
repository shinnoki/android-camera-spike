package com.konashi.fashionstamp.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.camerastamp.R;
import com.konashi.fashionstamp.entity.Item;
import com.konashi.fashionstamp.ui.helper.BitmapCache;
import com.konashi.fashionstamp.ui.helper.FadeInNetworkImageView;

public class FeedAdapter extends ArrayAdapter<Item> {
    private LayoutInflater mLayoutInflator;
    private ImageLoader mImageLoader;
    private RequestQueue mQueue;
    private ArrayList<Item> feed;
    
	public FeedAdapter(Context context, ArrayList<Item> feed) {
		super(context, 0, feed);
		this.feed = feed;
		mQueue = Volley.newRequestQueue(context);
		mLayoutInflator = ((Activity)context).getLayoutInflater();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        View view = convertView;
        if (view == null) {
            view = mLayoutInflator.inflate(R.layout.feed_item, parent, false);

            holder = new ViewHolder();
            holder.titleTextView = (TextView)view.findViewById(R.id.title);
            holder.likeCountTextView = (TextView)view.findViewById(R.id.like_count);
            holder.questionCountTextView = (TextView)view.findViewById(R.id.question_count);
            holder.dislikeCountTextView = (TextView)view.findViewById(R.id.dislike_count);
            holder.descriptionTextView = (TextView)view.findViewById(R.id.description);
            holder.createdAtTextView = (TextView)view.findViewById(R.id.created_at);
            holder.imageView = (FadeInNetworkImageView)view.findViewById(R.id.thumbnail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        Item item = feed.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.descriptionTextView.setText(item.getDescription());
        holder.likeCountTextView.setText(Integer.toString(item.getStampCount()[0]));
        holder.questionCountTextView.setText(Integer.toString(item.getStampCount()[1]));
        holder.dislikeCountTextView.setText(Integer.toString(item.getStampCount()[2]));
        holder.createdAtTextView.setText(item.getCreatedAt());

        holder.imageView.setImageUrl(item.getImage(), new ImageLoader(mQueue, new BitmapCache()));
        
        return view;
	}
	
	public void setImageLoader(ImageLoader imageLoader) {
		mImageLoader = imageLoader;
	}
	
    static class ViewHolder {
    	FadeInNetworkImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView likeCountTextView;
        TextView questionCountTextView;
        TextView dislikeCountTextView;
        TextView createdAtTextView;
    }

}
