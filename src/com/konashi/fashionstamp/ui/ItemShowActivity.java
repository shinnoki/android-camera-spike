package com.konashi.fashionstamp.ui;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.example.camerastamp.R;
import com.konashi.fashionstamp.ui.helper.BitmapCache;
import com.konashi.fashionstamp.ui.helper.ImageHelper;
import com.konashi.fashionstamp.ui.helper.UploadAsyncTask;

public class ItemShowActivity extends Activity implements OnTouchListener {

    private RelativeLayout mLayout;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_item);
        
        mLayout = (RelativeLayout)this.findViewById(R.id.rLayout);
        mLayout.setOnTouchListener(this);
        
        mQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
         
        if (id >= 0) {
            // get http://still-ocean-5133.herokuapp.com/items/:id.json
            
            // parse "url"
            
             // Load image
            ImageView itemImg = (ImageView)findViewById(R.id.itemImageView);

            String url = "http://res.cloudinary.com/hcttyxbgd/image/upload/v1375784060/utqhnbxxblzgdbzjkwpc.jpg";
            ImageListener listener = ImageLoader.getImageListener(itemImg, R.drawable.ic_launcher, android.R.drawable.ic_delete);
            mImageLoader.get(url, listener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_item, menu);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            View view = this.getLayoutInflater().inflate(R.layout.test, null);

            // ImageView itemImg = new ImageView(this);
            // itemImg.setImageDrawable(getResources().getDrawable(R.drawable.ecalic019_088));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = touchX;
            params.topMargin = touchY;
            mLayout.addView(view, params);
            
            /*
            float x = (float)100 * touchX/mLayout.getWidth();
            float y = (float)100 * touchY/mLayout.getHeight();
            Log.d("comment", "x="+x+", y="+y);
            try {

                MultipartEntity entity = new MultipartEntity() ;

                entity.addPart("comment[stamp]", new StringBody("1"));
                entity.addPart("comment[x]", new StringBody(Float.toString(x)));
                entity.addPart("comment[y]", new StringBody(Float.toString(y)));
                entity.addPart("comment[item_id]", new StringBody("21"));

                StringBody descriptionBody = new StringBody("コメント", Charset.forName("UTF-8"));
                entity.addPart("comment[body]", descriptionBody);

                String url = "http://still-ocean-5133.herokuapp.com/comments.json";
                new UploadAsyncTask(this, url).execute(entity);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            */
        
        }

        return false;
    }
}
