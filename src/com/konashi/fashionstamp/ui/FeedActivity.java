package com.konashi.fashionstamp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.example.camerastamp.R;

public class FeedActivity extends FragmentActivity {

	private Fragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed);
		
		if (savedInstanceState != null)
			mFragment = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mFragment == null)
			mFragment = new BaseFeedFragment();
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mFragment)
		.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed, menu);
		return true;
	}

}
