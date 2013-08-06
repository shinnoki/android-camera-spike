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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UploadAsyncTask 
extends AsyncTask<MultipartEntity, Integer, String> {
    private static final String POST_URL = "http://still-ocean-5133.herokuapp.com/items.json";

    ProgressDialog dialog;
    Context context;

    public UploadAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(MultipartEntity... params) {

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(POST_URL);

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
            // Parse json object
            JSONObject jsonObj = new JSONObject(result);
            String id = jsonObj.getString("id");
            // String created_at = jsonObj.getString("created_at");
            
            Log.d("response id", id);
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