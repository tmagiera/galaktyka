package com.codeme.tmagiera.galaktykadnia;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageRequester {

    public interface ImageRequesterResponse {
        void receivedNewPhoto(Photo newPhoto);
    }

    private ImageRequesterResponse mResponseListener;
    private Context mContext;
    private OkHttpClient mClient;
    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod?";
    private static final String DATE_PARAMETER = "date=";
    private static final String API_KEY_PARAMETER = "&api_key=";

    public ImageRequester(Activity listeningActivity) {
        mResponseListener = (ImageRequesterResponse) listeningActivity;
        mContext = listeningActivity.getApplicationContext();
        mClient = new OkHttpClient();
    }

    public void getPhoto() throws IOException {
        String date = new SimpleDateFormat("yyyy-MM-dd", new Locale("pl-PL")).format(Calendar.getInstance().getTime());

        String urlRequest = BASE_URL + DATE_PARAMETER + date + API_KEY_PARAMETER +
                mContext.getString(R.string.nasa_api_key);
        Request request = new Request.Builder().url(urlRequest).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject photoJSON = new JSONObject(response.body().string());

                    Photo receivedPhoto = new Photo(photoJSON);
                    mResponseListener.receivedNewPhoto(receivedPhoto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
