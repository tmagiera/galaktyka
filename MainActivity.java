package com.codeme.tmagiera.galaktykadnia;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ImageRequester.ImageRequesterResponse {
    private ImageRequester mImageRequester;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageRequester = new ImageRequester(this);
        mActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            mImageRequester.getPhoto();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedNewPhoto(final Photo photo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView mItemImage = (ImageView) mActivity.findViewById(R.id.item_image);
                TextView mItemDate = (TextView) mActivity.findViewById(R.id.item_date);
                TextView mItemDescription = (TextView) mActivity.findViewById(R.id.item_description);

                Picasso.with(mItemImage.getContext()).load(photo.getUrl()).into(mItemImage);
                mItemDate.setText(photo.getHumanDate());
                mItemDescription.setText(photo.getExplanation());
            }
        });
    }
}
