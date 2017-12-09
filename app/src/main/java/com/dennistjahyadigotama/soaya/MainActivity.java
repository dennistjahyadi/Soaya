package com.dennistjahyadigotama.soaya;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CropImage.CropActivity;
import com.dennistjahyadigotama.soaya.Menu.Feature;
import com.dennistjahyadigotama.soaya.Menu.Home;
import com.dennistjahyadigotama.soaya.Menu.Notification;
import com.dennistjahyadigotama.soaya.Menu.Official;
import com.dennistjahyadigotama.soaya.Menu.Profile;
import com.dennistjahyadigotama.soaya.activities.CreateThreadActivity;
import com.dennistjahyadigotama.soaya.activities.CreateThreadOfficialActivity;
import com.dennistjahyadigotama.soaya.activities.ProfileOptionsActivity;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.SearchActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String url= User.mainActivityUrl;
    RequestQueue requestQueue;
    private Uri mCropImageUri;
    String url1= User.myFirebaseInstanceIDServiceUrl;
    SharedPreferences sharedPreferences;
    String token;
    TextView tvHeader;
    InterstitialAd mInterstitialAd;
   public Toolbar toolbar;
    int pos;
    public TabLayout tabLayout;
    private ViewPager viewPager;
    LinearLayout layout;
    //toolbar menu
    ImageView ivCreateThread,ivCreateThreadOfficial,ivSearch,ivMore;
    //--------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (LinearLayout)findViewById(R.id.layout);
        layout.setVisibility(View.GONE);

        requestQueue = Volley.newRequestQueue(this);
        CheckPopup();

        mInterstitialAd = new InterstitialAd(this);



        mInterstitialAd.setAdUnitId("ca-app-pub-4908922088432819/4232670087");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        tvHeader = (TextView)findViewById(R.id.textViewHeader);

        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);

        if(sharedPreferences.getBoolean(QuickstartPreferences.Notification,true))
        {
            sharedPreferences.edit().putBoolean(QuickstartPreferences.Notification,true).apply();
        }else
        {
            boolean a = sharedPreferences.getBoolean(QuickstartPreferences.Notification,true);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.Notification,a).apply();

        }
        if(sharedPreferences.getBoolean(QuickstartPreferences.CouponNotification,true))
        {
            sharedPreferences.edit().putBoolean(QuickstartPreferences.CouponNotification,true).apply();
        }else
        {
            boolean a = sharedPreferences.getBoolean(QuickstartPreferences.CouponNotification,true);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.CouponNotification,a).apply();

        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("mainactivity"));


        //----------------------SETTING TOOLBAR MENU-------------------------------------------------

        ivCreateThreadOfficial=(ImageView) findViewById(R.id.imageViewCreateThreadOfficial);
        ivCreateThreadOfficial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateThreadOfficialActivity.class);
                startActivity(i);
            }
        });


        ivSearch=(ImageView)findViewById(R.id.imageViewSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });



        ivCreateThread=(ImageView) findViewById(R.id.imageViewCreateThread);
        ivCreateThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateThreadActivity.class);
                startActivity(intent);
            }
        });

        ivMore = (ImageView)findViewById(R.id.imageViewMore);
        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileOptionsActivity.class);
                startActivity(i);
            }
        });


        //--------------------------------------------------------------

        String userid=sharedPreferences.getString(QuickstartPreferences.USERID,null);
        String username= sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        token = FirebaseInstanceId.getInstance().getToken();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                switch (position) {
                    case 0:
                        TabProfile();

                        break;
                    case 1:
                        TabHome();

                        break;
                    case 2:
                        TabOfficial();


                        break;
                    case 3:
                        TabNotification();


                        break;
                    case 4:
                        TabMessages();

                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupTabIcons();
        CheckBanned();

        if(userid==null || username==null)
        {
            GetUserData();
        }else
        {
            User.id = userid;
            User.username = username;
            if(token!=null)
            {
                sendRegistrationToServer();
            }
        }
        GetSubs();

    }

    private void setupTabIcons() {
       TabHome();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Profile(), "ONE");
        adapter.addFrag(new Home(), "TWO");
        adapter.addFrag(new Official(), "THREE");
        adapter.addFrag(new Notification(), "FOUR");
        adapter.addFrag(new Feature(), "FIVE");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
    private void SetMark() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        int markNotice = sharedPreferences.getInt(QuickstartPreferences.MARKNOTICE, 0);
        if (markNotice > 0) {
            if (pos == 3) {
                tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_white_mark);
            } else {
                tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black_mark);

            }

        } else {
            if (pos == 3) {
                tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_white);
            } else {
                tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black);

            }
        }

    }

    public void TabHome()
    {
        tvHeader.setText("Home");
        ivCreateThread.setVisibility(View.VISIBLE);
        ivMore.setVisibility(View.GONE);
        ivSearch.setVisibility(View.VISIBLE);
        ivCreateThreadOfficial.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_feature_black);

    }
    public void TabNotification()
    {
        tvHeader.setText("Recent");

        ivCreateThread.setVisibility(View.GONE);
        ivMore.setVisibility(View.GONE);
        ivSearch.setVisibility(View.VISIBLE);
        ivCreateThreadOfficial.setVisibility(View.GONE);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_white);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_feature_black);
    }

    public void TabOfficial()
    {
        tvHeader.setText("Official");
        ivCreateThread.setVisibility(View.GONE);
        ivMore.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);

        //ivCreateThreadOfficial.setVisibility(View.VISIBLE);
        ivCreateThreadOfficial.setVisibility(View.GONE);
        CheckOfficialUsers();
        //CHECK USERS WHO IS ALLOWED
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_feature_black);

    }

    public void TabMessages()
    {
        sharedPreferences.edit().putInt(QuickstartPreferences.MARKMESSAGE,0).apply();
        tvHeader.setText("Feature");

        ivCreateThread.setVisibility(View.GONE);
        ivMore.setVisibility(View.GONE);
        ivSearch.setVisibility(View.VISIBLE);
        ivCreateThreadOfficial.setVisibility(View.GONE);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);

        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_feature_white);
    }
    public void TabProfile()
    {
        tvHeader.setText("Profile");

        ivCreateThread.setVisibility(View.GONE);
        ivMore.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.GONE);
        ivCreateThreadOfficial.setVisibility(View.GONE);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_home_black);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);

        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notifications_black);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_feature_black);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {
            SetMark();
            SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);

            int click = sharedPreferences.getInt(QuickstartPreferences.Click,0);

            if(click>4) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    sharedPreferences.edit().putInt(QuickstartPreferences.Click,0).apply();

                }
            }
        }

    };

    private void CheckOfficialUsers()
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.checkOfficialUsers, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if(jsonObject.getString("username").equals(User.username))
                        {
                            if(pos==2) {
                                ivCreateThreadOfficial.setVisibility(View.VISIBLE);
                            }
                            break;
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonArrayRequest);
    }

    private void GetSubs()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.mainActivityUrl+"?getSubs="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    FirebaseMessaging.getInstance().subscribeToTopic("Coupon");
                    FirebaseMessaging.getInstance().subscribeToTopic("AllUsers");

                    for (int i = 0; i < response.length(); i++) {
                       final String sub;

                        JSONObject jsonObject = response.getJSONObject(i);
                        sub = jsonObject.getString("subscategory");
                        FirebaseMessaging.getInstance().subscribeToTopic(jsonObject.getString("subscategory"));
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                FirebaseMessaging.getInstance().subscribeToTopic(sub);

                            }
                        },200);
                        FirebaseMessaging.getInstance().subscribeToTopic(jsonObject.getString("subscategory"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonArrayRequest);

    }

    public void sendRegistrationToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username", User.username);
                hashMap.put("token", token);
                return hashMap;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void GetUserData()
    {
       String email1= sharedPreferences.getString(QuickstartPreferences.EMAIL,null);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?poi="+ email1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = response.getJSONObject(i);
                        User.id = person.getString("id");
                        User.username = person.getString("username");
                        sharedPreferences.edit().putString(QuickstartPreferences.USERID,person.getString("id")).apply();
                        sharedPreferences.edit().putString(QuickstartPreferences.USERNAME,person.getString("username")).apply();
                    }

                   /* if(token!=null)
                    {
                        sendRegistrationToServer();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonArrayRequest);
    }

    private void CheckBanned()
    {
        String email1= sharedPreferences.getString(QuickstartPreferences.EMAIL,null);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?poi="+ email1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = response.getJSONObject(i);

                        if(person.getString("username").equals("banned user"))
                        {
                            Toast.makeText(getApplicationContext(),"you are banned",Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.this.finishAffinity();
                                }
                            }, 3000);
                        }else
                        {
                            if(token!=null)
                            {
                                sendRegistrationToServer();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonArrayRequest);

    }

    private void CheckPopup()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.popUpAds+"?jumlah=asd", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = response.getJSONObject(i);
                        if(person.getString("jumlah").equals("1"))
                        {
                            GetAdsUrl();
                        }
                    }
                    layout.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    layout.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Koneksi Bermasalah (Check Internetmu)..",Toast.LENGTH_SHORT).show();

            }
        });


        requestQueue.add(jsonArrayRequest);
    }

    private void GetAdsUrl()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.popUpAds+"?geturl=asd", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        final JSONObject person = response.getJSONObject(i);


                        Picasso.with(getApplicationContext()).load(person.getString("url")).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Intent intent = new Intent(getApplicationContext(),PopupAdsActivity.class);
                                try {
                                    intent.putExtra("url",person.getString("url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });



                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(jsonArrayRequest);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {

                Intent i = new Intent(this, CropActivity.class);
                i.putExtra("imageUri",imageUri.toString());
                startActivity(i);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent i = new Intent(this, CropActivity.class);
                i.putExtra("imageUri",mCropImageUri.toString());
                startActivity(i);


            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }




}
