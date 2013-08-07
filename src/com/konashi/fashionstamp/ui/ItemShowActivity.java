package com.konashi.fashionstamp.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class ItemShowActivity extends Activity implements OnTouchListener {

    private RelativeLayout mLayout;
    private RequestQueue mQueue;
    private Item mItem;
    private int mItemId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item);
        
        mLayout = (RelativeLayout)this.findViewById(R.id.rLayout);
        mLayout.setOnTouchListener(this);
        
        mQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        mItem = (Item)intent.getSerializableExtra("item");
        mItemId = mItem.getId();
        
        String url = "http://still-ocean-5133.herokuapp.com/items/" + mItemId + ".json";
        JsonObjectRequest req = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mItem = Feed.parseItem(response);
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
    
    private void drawComments(List<Comment> comments) {
        for (Comment comment : comments) {
            View view = this.getLayoutInflater().inflate(R.layout.comment, null);
            
            TextView textView = (TextView)view.findViewById(R.id.body);
            textView.setText(comment.getBody());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            
            float x = comment.getX() * mLayout.getWidth() / 100;
            float y = comment.getY() * mLayout.getHeight() / 100;
            params.leftMargin = (int)x;
            params.topMargin = (int)y;
            mLayout.addView(view, params);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            // View commentView = new CommentView(this);
            View commentEdit = this.getLayoutInflater().inflate(R.layout.comment_edit, null);
            
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = touchX;
            params.topMargin = touchY;
            mLayout.addView(commentEdit, params);

            EditText editText = (EditText)commentEdit.findViewById(R.id.editBody);

            // Move focus to edit text and open softkeyboard
            editText.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
            inputMethodManager.showSoftInput(editText, 0);  
            
            // Set event when input finished
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if(event.getAction() == KeyEvent.ACTION_UP) {
                            // close softkeyboard
                            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        return true;
                    }
                    return false;
                }
            });
         
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
}
