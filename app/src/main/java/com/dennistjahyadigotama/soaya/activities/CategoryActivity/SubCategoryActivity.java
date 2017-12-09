package com.dennistjahyadigotama.soaya.activities.CategoryActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListGetter;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.SubCategoryAdapter;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.SubCategoryGetter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Denn on 7/9/2016.
 */
public class SubCategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvHorizontal;
    RecyclerView rvVertical;
    List<SubCategoryGetter> subCategoryGetterList = new ArrayList<>();
    SubCategoryAdapter subCategoryAdapter;
    String categoryid,categoryname;
    RequestQueue requestQueue;
    String url = User.categoryActivityUrl;
    List<ThreadListGetter> threadListGetterList = new ArrayList<>();
    List<String> subscribeList = new ArrayList<>();
    ThreadListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etSearch;
    TextView tvCategoryName,tvSortIndicator;
    TextView buttonSubscribe;
    ImageView ivSearch;
    String sortBy;
    FloatingActionButton fabSortBy;
    LinearLayout linlin;

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < (-HIDE_THRESHOLD-600)&& !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }
        }

        public abstract void onHide();
        public abstract void onShow();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_category_activity);
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryid = getIntent().getStringExtra("categoryid");
        categoryname= getIntent().getStringExtra("categoryname");
        String titlename= getIntent().getStringExtra("titlename");
        linlin = (LinearLayout)findViewById(R.id.linlin);
        tvCategoryName  = (TextView)findViewById(R.id.textViewCategoryName);
        tvCategoryName.setText(titlename);
        buttonSubscribe= (TextView)findViewById(R.id.buttonSubscribe);
        tvSortIndicator = (TextView)findViewById(R.id.textViewSortIndicator);
        sortBy="newest";
        tvSortIndicator.setText("Newest");

        fabSortBy = (FloatingActionButton)findViewById(R.id.fabSortBy);

        fabSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SubCategoryActivity.this, fabSortBy);
                popup.getMenuInflater().inflate(R.menu.sortby, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.mostPopular:
                                sortBy= "mostPopular";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());

                                break;
                            case R.id.mostViewed:
                                sortBy= "mostViewed";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());
                                break;
                            case R.id.newest:
                                sortBy= "newest";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());

                                break;
                            case R.id.lastPost:
                                sortBy= "lastPost";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());

                                break;

                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        rvHorizontal = (RecyclerView)findViewById(R.id.recycler_view_horizontal);
        rvVertical = (RecyclerView)findViewById(R.id.recycler_view_vertical);
        rvVertical.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                //toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                //tabLayout.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                linlin.animate().translationY(-(linlin.getHeight())).setInterpolator(new AccelerateInterpolator(2));
                rvVertical.setPadding(0,0,0,0);

            }

            @Override
            public void onShow() {
                //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                //tabLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                linlin.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                rvVertical.setPadding(0,linlin.getHeight(),0,0);

            }
        });

        rvVertical.setPadding(0,linlin.getHeight(),0,0);

        etSearch = (EditText)findViewById(R.id.editTextSearch);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    threadListGetterList.clear();

                    String text;
                    text=etSearch.getText().toString();
                    text=text.replace(" ","&");
                    adapter.notifyDataSetChanged();

                    GetThread(0,text,sortBy);
                    return true;
                }

                return false;
            }
        });
        ivSearch = (ImageView)findViewById(R.id.imageViewSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threadListGetterList.clear();

                String text;
                text=etSearch.getText().toString();
                text=text.replace(" ","&");
                adapter.notifyDataSetChanged();

                GetThread(0,text,sortBy);

            }
        });
        subCategoryAdapter = new SubCategoryAdapter(subCategoryGetterList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rvHorizontal.setLayoutManager(layoutManager);
        rvHorizontal.setAdapter(subCategoryAdapter);
        rvHorizontal.setItemAnimator(new DefaultItemAnimator());
        SetRvHorizontal();
        SetupPagination();

    }

    void refreshItems() {
        // Load items
        // ...
        linlin.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        rvVertical.setPadding(0,linlin.getHeight(),0,0);

        threadListGetterList.clear();
        SetupPagination();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void SetButtonSubscribe()
    {
        if(subscribeList.size()>0) {
            for (int i = 0; i < subscribeList.size(); i++) {
                if (categoryname.equals(subscribeList.get(i))) {
                    buttonSubscribe.setText("subscribed");
                    buttonSubscribe.setTextColor(Color.parseColor("#607d8b"));
                    break;
                } else {
                    buttonSubscribe.setText("subscribe");
                    buttonSubscribe.setTextColor(Color.WHITE);
                }

            }
        }
        else
        {
            buttonSubscribe.setText("subscribe");
            buttonSubscribe.setTextColor(Color.WHITE);
        }

        buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonSubscribe.getText().equals("subscribe")) {
                    SubscribeCategory();
                }else
                {
                    RemoveSubscribeCategory();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        subscribeList.clear();
        GetTotalSubscribeList();
    }
    private void GetTotalSubscribeList()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.categoryActivityUrl2+"?totalSubscribe="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int jumlah=0;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                       jumlah= Integer.parseInt(jsonObject.getString("jumlah"));
                    }
                    if(jumlah==0)
                    {
                        SetButtonSubscribe();
                        rvVertical.setPadding(0,linlin.getHeight(),0,0);
                    }else
                    {
                        GetSubscribeList();
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


    private void GetSubscribeList()
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.categoryActivityUrl2+"?getSubscribe="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        subscribeList.add(jsonObject.getString("subscategory"));
                    }
                    SetButtonSubscribe();
                    rvVertical.setPadding(0,linlin.getHeight(),0,0);

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

    private void SubscribeCategory()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.categoryActivityUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        buttonSubscribe.setText("subscribed");
                        buttonSubscribe.setTextColor(Color.parseColor("#607d8b"));
                        FirebaseMessaging.getInstance().subscribeToTopic(categoryname);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                FirebaseMessaging.getInstance().subscribeToTopic(categoryname);

                            }
                        },200);
                        FirebaseMessaging.getInstance().subscribeToTopic(categoryname);

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
                HashMap<String,String> map = new HashMap<>();
                map.put("username", User.username);
                map.put("subscategory",categoryname);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
    private void RemoveSubscribeCategory()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.categoryActivityUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        buttonSubscribe.setText("subscribe");
                        buttonSubscribe.setTextColor(Color.WHITE);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname);

                            }
                        },200);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname);


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
                HashMap<String,String> map = new HashMap<>();
                map.put("username", User.username);
                map.put("removesubscategory",categoryname);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void SetRvHorizontal()
    {

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.selectCategoryActivityUrl+"?GetSubCategoryById="+categoryid, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    String id,name,picurl,subsname,row,type;
                    try {
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id= jsonObject.getString("id");
                            name=jsonObject.getString("name");
                            picurl=jsonObject.getString("picurl");
                            subsname=jsonObject.getString("subsname");
                            row=jsonObject.getString("rows");
                            type=jsonObject.getString("type");

                            SubCategoryGetter categoryGetter = new SubCategoryGetter();
                            categoryGetter.setId(id);
                            categoryGetter.setName(name);
                            categoryGetter.setPicurl(picurl);
                            categoryGetter.setSubsname(subsname);
                            categoryGetter.setType(type);
                            categoryGetter.setRow(row);
                            subCategoryGetterList.add(categoryGetter);
                        }

                        subCategoryAdapter.notifyDataSetChanged();
                        rvVertical.setPadding(0,linlin.getHeight(),0,0);

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

    protected void SetupPagination(){

        adapter = new ThreadListAdapter(threadListGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvVertical.setLayoutManager(layoutManager);
        rvVertical.setItemAnimator(new DefaultItemAnimator());
        rvVertical.setAdapter(adapter);
        rvVertical.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                String text;
                text=etSearch.getText().toString();
                text=text.replace(" ","&");
                GetThread(totalItemsCount,text,sortBy);
            }
        });
        String text;
        text=etSearch.getText().toString();
        text=text.replace(" ","&");
        GetThread(0,text,sortBy);
    }



    private void GetThread(int c,String text,String sort){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?poi="+categoryname+"&rrr="+c+"&search="+text+"&sortBy="+sort, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String id,title, createby, createdate,category,profilepic,content,totalReply,totalViews,imageContentUrl;

                // String jsonResponse = "";
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = response.getJSONObject(i);
                        ThreadListGetter threadGetter = new ThreadListGetter();
                        id = person.getString("id");
                        title = person.getString("title");
                        createby = person.getString("createBy");
                        createdate = person.getString("theDate");
                        category = person.getString("category");
                        profilepic = person.getString("profilepic");
                        content = person.getString("content");
                        totalReply = person.getString("totalReply");
                        totalViews= person.getString("views");
                        imageContentUrl = person.getString("imageUrl");
                        threadGetter.setImageContentUrl(imageContentUrl);
                        threadGetter.setTotalReply(totalReply);
                        threadGetter.setTotalViews(totalViews);
                        threadGetter.setId(id);
                        threadGetter.setTitle(title);
                        threadGetter.setCreateBy(createby);
                        threadGetter.setDate(createdate);
                        threadGetter.setCategory(category);
                        threadGetter.setContent(content);
                        threadGetter.setPhoto(profilepic);
                        threadListGetterList.add(threadGetter);
                    }
                    adapter.notifyDataSetChanged();
                    rvVertical.setPadding(0,linlin.getHeight(),0,0);

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
