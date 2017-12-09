package com.dennistjahyadigotama.soaya;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Denn on 9/17/2016.
 */
public class PopupAdsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_ads_activity);
        TextView tvClose = (TextView)findViewById(R.id.tvClose);
        ImageView iv = (ImageView)findViewById(R.id.resultImageView);
        String url = getIntent().getStringExtra("url");
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Picasso.with(getApplicationContext()).load(url).into(iv);


    }


}
