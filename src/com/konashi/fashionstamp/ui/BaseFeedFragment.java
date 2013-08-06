package com.konashi.fashionstamp.ui;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.camerastamp.R;
import com.example.camerastamp.model.Feed;
import com.konashi.fashionstamp.entity.Item;


public class BaseFeedFragment extends Fragment {
	
	protected RequestQueue mQueue;
	protected Feed mFeed;
	protected FeedAdapter mAdapter;
	protected ListView mListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mFeed = new Feed();
		
		mQueue = Volley.newRequestQueue(getActivity());
		JsonArrayRequest req = new JsonArrayRequest("http://still-ocean-5133.herokuapp.com/items.json",
	            new Response.Listener<JSONArray>() {
	                @Override public void onResponse(JSONArray response) {
	                    ArrayList<Item> feed = mFeed.parseFeedJson(response);
	                    FeedAdapter mAdapter = new FeedAdapter(getActivity(), feed);
	                }
	            },
	            new Response.ErrorListener() {
	                @Override public void onErrorResponse(VolleyError error) {
	                    // エラー時の処理...
	                }
	            });
		mQueue.add(req);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.feed_list, null);
		mListView = (ListView)view.findViewById(R.id.listView);
		return view;
	}

}
