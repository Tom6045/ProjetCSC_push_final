package com.example.tom.projetcsc;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncBitmapDownloader extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;

    public AsyncBitmapDownloader(ImageView iv){
        imageViewReference = new WeakReference<ImageView>(iv);
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlString = strings[0];
        URL url = null;
        try{
            url =new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try{
                InputStream in =new BufferedInputStream(urlConnection.getInputStream());
                return BitmapFactory.decodeStream(in);
            } finally{
                urlConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bm) {
        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bm);
            }
        }
    }
}
