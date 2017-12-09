package com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.adapter.FullScreenImageAdapter;

import java.util.ArrayList;

/**
 * Created by Denn on 9/20/2016.
 */
public class WartaUbayaActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView tvPage;
    ArrayList<String> imagePath = new ArrayList<>();
    Toolbar toolbar;
    int totalpage;
    String edisi,location;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.warta_ubaya_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        totalpage = getIntent().getIntExtra("totalpage",0);
        edisi = getIntent().getStringExtra("edisi");
        location = getIntent().getStringExtra("location");
        getSupportActionBar().setTitle("Edisi "+edisi);

        tvPage = (TextView)findViewById(R.id.tvPage);
        tvPage.setText("Page 1");
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPage.setText("Page "+(position+1));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for(int i=1;i<=totalpage;i++)
        {
            imagePath.add(location+i+".jpg");

        }

        FullScreenImageAdapter adapter = new FullScreenImageAdapter(this,imagePath);
        viewPager.setAdapter(adapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
