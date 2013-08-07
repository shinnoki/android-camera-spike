package com.konashi.fashionstamp.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
            View view = this.getLayoutInflater().inflate(R.layout.comment, null);
            
            // textを書き換える
            TextView textView = (TextView)view.findViewById(R.id.body);
            textView.setText("text");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = touchX;
            params.topMargin = touchY;
            mLayout.addView(view, params);
            
            float x = (float)100 * touchX/mLayout.getWidth();
            float y = (float)100 * touchY/mLayout.getHeight();
            Log.d("comment", "x="+x+", y="+y);
            try {

                MultipartEntity entity = new MultipartEntity() ;

                entity.addPart("comment[stamp]", new StringBody("1"));
                entity.addPart("comment[x]", new StringBody(Float.toString(x)));
                entity.addPart("comment[y]", new StringBody(Float.toString(y)));
                entity.addPart("comment[item_id]", new StringBody(Integer.toString(mItem.getId())));

                StringBody descriptionBody = new StringBody("コメント", Charset.forName("UTF-8"));
                entity.addPart("comment[body]", descriptionBody);

                String url = "http://still-ocean-5133.herokuapp.com/comments.json";
                new UploadAsyncTask(this, url).execute(entity);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
