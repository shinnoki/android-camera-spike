package com.konashi.fashionstamp.ui.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.konashi.fashionstamp.entity.Comment;
import com.konashi.fashionstamp.entity.Item;
import com.konashi.fashionstamp.model.Feed;
import com.konashi.fashionstamp.ui.ItemShowActivity;
import com.konashi.fashionstamp.ui.UploadActivity;

public class UploadAsyncTask extends AsyncTask<MultipartEntity, Integer, String> {

    ProgressDialog dialog;
    Context context;
    String mUrl;

    public UploadAsyncTask(Context context, String url){
        this.context = context;
        this.mUrl = url;
    }

    @Override
    protected String doInBackground(MultipartEntity... params) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(mUrl);

            httpPost.setEntity(params[0]);
            HttpResponse response = httpClient.execute(httpPost);
            
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED){
                OutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String result = out.toString();

                return result;
            }
            
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(dialog != null){
            dialog.dismiss();
        }

        if (result == null) {
            return;
        }
        
        
        
        try {
            if (context.getClass() == UploadActivity.class) {
                UploadActivity activity = (UploadActivity)context;
                JSONObject jsonObj = new JSONObject(result);
                Item item = Feed.parseItem(jsonObj);
                
                Intent data = new Intent();
                data.putExtra("item", item);
                activity.setResult(Activity.RESULT_OK, data);
                activity.finish();
            }
            
            if (context.getClass() == ItemShowActivity.class) {
                ItemShowActivity activity = (ItemShowActivity)context;
                JSONObject jsonObj = new JSONObject(result);
                Comment comment = Feed.parseComment(jsonObj);
                
                activity.drawComment(comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // TODO: Save to db

    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Uploading...");
        dialog.show();
    }  
}