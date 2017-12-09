package com.dennistjahyadigotama.soaya.Menu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.dennistjahyadigotama.soaya.Menu.fragment.FriendRequest;
import com.dennistjahyadigotama.soaya.Menu.fragment.NotificationInfo;
import com.dennistjahyadigotama.soaya.R;


/**
 * Created by Denn on 7/25/2016.
 */
public class Notification extends Fragment {
    ViewPager vpPager;
    MyPagerAdapter adapterViewPager;
    PagerSlidingTabStrip tabs;
    public static boolean active = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        vpPager.setAdapter(adapterViewPager);
        tabs.setTextColor(Color.parseColor("#e0f2f1"));
        tabs.setViewPager(vpPager);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active=false;

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Notice", "Friend Request" };

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
                    return NotificationInfo.newInstance(0, "Page # 1");
                case 1:
                    return FriendRequest.newInstance(1, "Page # 2");
                default:
                    return null;
            }

        }

    }

}
