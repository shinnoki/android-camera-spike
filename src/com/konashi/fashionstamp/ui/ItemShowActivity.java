package com.konashi.fashionstamp.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.camerastamp.R;
import com.konashi.fashionstamp.entity.Comment;
import com.konashi.fashionstamp.entity.Item;
import com.konashi.fashionstamp.model.Feed;
import com.konashi.fashionstamp.ui.helper.BitmapCache;
import com.konashi.fashionstamp.ui.helper.UploadAsyncTask;

public class ItemShowActivity extends Activity implements OnTouchListener, OnCheckedChangeListener {

    private RelativeLayout mLayout;
    private RequestQueue mQueue;
    private Item mItem;
    private int mItemId;
    
    private ToggleButton mtb_like;
    private ToggleButton mtb_question;
    private ToggleButton mtb_dislike;
    private ToggleButton mtb_all;
    
    private Button iwaseteButton;
    
    private View mCommentEdit = null;
    private float mCommentX;
    private float mCommentY;
    
    private int mStamp;
    
    private List<View> likeViews = new ArrayList<View>();
    private List<View> questionViews = new ArrayList<View>();
    private List<View> dislikeViews = new ArrayList<View>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item);
        
        Intent intent = getIntent();
        mItem = (Item)intent.getSerializableExtra("item");
        mItemId = mItem.getId();
        
        Boolean isFirstBoot = intent.getBooleanExtra("isFirstBoot?", false);
        if(isFirstBoot){
        	Toast.makeText(getApplicationContext(), "↓重なって見づらい時は非表示で快適に", Toast.LENGTH_LONG).show();
        }
        
        setTitle(mItem.getTitle());
        
        mLayout = (RelativeLayout)this.findViewById(R.id.rLayout);
        mLayout.setOnTouchListener(this);
        
        // toggleBttons
        mtb_like = (ToggleButton)findViewById(R.id.toggle_like);
        mtb_like.setOnCheckedChangeListener(this);
        mtb_question = (ToggleButton)findViewById(R.id.toggle_question);
        mtb_question.setOnCheckedChangeListener(this);
        mtb_dislike = (ToggleButton)findViewById(R.id.toggle_dislike);
        mtb_dislike.setOnCheckedChangeListener(this);
        mtb_all = (ToggleButton)findViewById(R.id.toggle_all);
        mtb_all.setOnCheckedChangeListener(this);
        
        iwaseteButton = (Button)findViewById(R.id.iwaseteButton);
        iwaseteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				selectStamp();
			}
		});
        
        mQueue = Volley.newRequestQueue(this);
        
        // Image Loader
        String url = "http://still-ocean-5133.herokuapp.com/items/" + mItemId + ".json";
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mItem = Feed.parseItem(response);
                        
                        TextView titleView = (TextView)findViewById(R.id.item_title);
                        titleView.setText(mItem.getTitle());
                        
                        TextView descrptionView = (TextView)findViewById(R.id.item_description);
                        descrptionView.setText(mItem.getDescription());
                        
                        int[] stampCount = mItem.getStampCount();
                        TextView likeView = (TextView)findViewById(R.id.flag_like);
                        likeView.setText(Integer.toString(stampCount[0]));
                        TextView questionView = (TextView)findViewById(R.id.flag_question);
                        questionView.setText(Integer.toString(stampCount[1]));
                        TextView dislikeView = (TextView)findViewById(R.id.flag_dislike);
                        dislikeView.setText(Integer.toString(stampCount[2]));
                        TextView allView = (TextView)findViewById(R.id.flag_all);
                        allView.setText(Integer.toString(stampCount[0]+stampCount[1]+stampCount[2]));

                        NetworkImageView itemImg = (NetworkImageView)findViewById(R.id.itemImageView);
                        itemImg.setImageUrl(mItem.getImage(), new ImageLoader(mQueue, new BitmapCache()));
                        drawComments(mItem.getComments());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO error handling
                    }
                });
        mQueue.add(req);
    }

	private void selectStamp() {
		String[] items = { "褒メサセテ！", "質問サセテ！", "物申サセテ！" };

		new AlertDialog.Builder(this).setItems(items,
		        new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        switch (item) {
                    case 0:
                        setStamp(1); 
                        Toast.makeText(getApplicationContext(), "写真の褒めたい所をクリック", Toast.LENGTH_LONG).show();
                        break;
                    case 1: 
                        setStamp(2); 
                        Toast.makeText(getApplicationContext(), "写真の質問したい所をクリック", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        setStamp(3); 
                        Toast.makeText(getApplicationContext(), "写真の物申したい所をクリック", Toast.LENGTH_LONG).show();
                        break;
		        }
		    }
		}).show();
	}

	private void setStamp(int stamp) {
	    mStamp = stamp;
    }
    
    private void drawComments(List<Comment> comments) {
        for (Comment comment : comments) {
            drawComment(comment);
        }
    }
    
    public void drawComment(Comment comment) {
        View view = this.getLayoutInflater().inflate(R.layout.comment, null);

        TextView textView = (TextView)view.findViewById(R.id.body);
        textView.setText(comment.getBody());
        
        ImageView imgview = (ImageView)view.findViewById(R.id.stamp);
        switch (comment.getStamp()) {
        case 1:
            imgview.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_star)); break;
        case 2:
            imgview.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_help));
            imgview.setBackgroundColor(Color.argb(88, 0, 191, 255));
            break;
        case 3:
            imgview.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_start_conversation));
            imgview.setBackgroundColor(Color.argb(88, 255, 51, 153));
            break;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        float x = comment.getX() * mLayout.getWidth() / 100;
        float y = comment.getY() * mLayout.getHeight() / 100;
        params.leftMargin = (int)x;
        params.topMargin = (int)y;

        switch (comment.getStamp()) {
            case 1:
                likeViews.add(view); break;
            case 2:
                questionViews.add(view); break;
            case 3:
                dislikeViews.add(view); break;
        }

        mLayout.addView(view, params);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_comment);
        view.startAnimation(animation);
    }
    
    private void setCommentsVisiblity(int stamp, boolean visible) {
        List<View> views = null;
        switch (stamp) {
            case 1:
                views = likeViews; break;
            case 2:
                views = questionViews; break;
            case 3:
                views = dislikeViews; break;
        }
        if (views == null) return;
        
        Animation animation = AnimationUtils.loadAnimation(this, visible ? R.anim.fade_in_comment : R.anim.fade_out_comment);
        
        for (View v: views) {
            v.startAnimation(animation);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            if (mStamp != 0) {
                if (mCommentEdit == null) {
                    mCommentEdit = this.getLayoutInflater().inflate(R.layout.comment_edit, null);
                    mLayout.addView(mCommentEdit);

                    EditText editText = (EditText)mCommentEdit.findViewById(R.id.editBody);

                    // Move focus to edit text and open softkeyboard
                    editText.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
                    inputMethodManager.showSoftInput(editText, 0);  

                    // Set event when input finished
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEND) {
                                // close softkeyboard
                                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                                v.setFocusable(false);

                                float x = mCommentX / mLayout.getWidth() * 100;
                                float y = mCommentY / mLayout.getHeight() * 100;

                                submitComment(mStamp, x, y, v.getText().toString());
                                mLayout.removeView(mCommentEdit);
                                mCommentEdit = null;
                                mStamp = 0;

                                return true;
                            }
                            return false;
                        }
                    });
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                mCommentEdit.setLayoutParams(params);

                params.leftMargin = touchX;
                params.topMargin = touchY;

                mCommentX = touchX;
                mCommentY = touchY;
            }

        }

        return false;
    }
    
    private void submitComment(int stamp, float x, float y, String body) {
        try {
            MultipartEntity entity = new MultipartEntity() ;

            entity.addPart("comment[item_id]", new StringBody(Integer.toString(mItem.getId())));
            entity.addPart("comment[stamp]", new StringBody(Integer.toString(stamp)));
            entity.addPart("comment[x]", new StringBody(Float.toString(x)));
            entity.addPart("comment[y]", new StringBody(Float.toString(y)));

            StringBody commentBody = new StringBody(body, Charset.forName("UTF-8"));
            entity.addPart("comment[body]", commentBody);

            String url = "http://still-ocean-5133.herokuapp.com/comments.json";
            new UploadAsyncTask(this, url).execute(entity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.swipe_in_right, R.anim.swipe_out_right);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.toggle_all:
                mtb_like.setChecked(isChecked);
                mtb_question.setChecked(isChecked);
                mtb_dislike.setChecked(isChecked);
                break;
            case R.id.toggle_like:
                setCommentsVisiblity(1, isChecked);
                break;
            case R.id.toggle_question:
                setCommentsVisiblity(2, isChecked);
                break;
            case R.id.toggle_dislike:
                setCommentsVisiblity(3, isChecked);
                break;
        }
    }
    
    @Override
    public void onBackPressed() {
        if (mCommentEdit != null) {
            mLayout.removeView(mCommentEdit);
            mCommentEdit = null;
            mStamp = 0;
        } else {
            super.onBackPressed();
        }
    }
    
}
