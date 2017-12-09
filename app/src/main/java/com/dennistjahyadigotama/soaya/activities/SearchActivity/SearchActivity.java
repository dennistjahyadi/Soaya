package com.dennistjahyadigotama.soaya.activities.SearchActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.SearchFragmentPeople;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.SearchFragmentThread;


/**
 * Created by Denn on 7/11/2016.
 */
public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageViewSearch;
    ViewPager pager;
    MyPagerAdapter adapter;
    private PagerSlidingTabStrip tabs;
    public static String text="";
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
        tabs.setTextColor(Color.parseColor("#ffffff"));
        editText = (EditText)findViewById(R.id.editTextSearch);
        imageViewSearch = (ImageView)findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=editText.getText().toString();
                text=text.replace(" ","&");
                adapter.notifyDataSetChanged();
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                     text=editText.getText().toString();
                   text= text.replace(" ","&");
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    text=editText.getText().toString();
                    text=text.replace(" ","&");
                    adapter.notifyDataSetChanged();
                    handled = true;
                }
                return handled;
            }
        });

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

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Thread", "People" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {


            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    return SearchFragmentThread.newInstance(0, "Page # 1");
                case 1:
                    return SearchFragmentPeople.newInstance(1, "Page # 2");
                default:
                    return null;
            }

        }

    }

}
