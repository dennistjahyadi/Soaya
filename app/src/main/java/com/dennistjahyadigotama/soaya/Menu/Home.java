package com.dennistjahyadigotama.soaya.Menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.MainActivity;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListGetter;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.CategoryActivity;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 6/20/2016.
 */
public class Home extends Fragment {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    List<ThreadListGetter> threadListGetterList = new ArrayList<>();
    ThreadListAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String url= User.homeUrl;
    FloatingActionButton fabSortBy;
    RelativeLayout revAllCategories;
    TextView tvSortIndicator;
    RelativeLayout loadingPanel;
    Toolbar toolbar;
    TabLayout tabLayout;
    LinearLayout lin;
    String TAG_LIST="threadList";

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
            } else if (scrolledDistance < (-HIDE_THRESHOLD-600) && !controlsVisible) {
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        User.sortBy = "lastPost";
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        toolbar =((MainActivity) getActivity()).toolbar;
        tabLayout = ((MainActivity) getActivity()).tabLayout;
        //recyclerView.setNestedScrollingEnabled(false);
        requestQueue = Volley.newRequestQueue(getActivity());
        lin = (LinearLayout) view.findViewById(R.id.lin);
        loadingPanel = (RelativeLayout)view.findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.VISIBLE);
        revAllCategories = (RelativeLayout)view.findViewById(R.id.revAllCategories);
        revAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),CategoryActivity.class);
                startActivity(i);
            }
        });

        tvSortIndicator = (TextView)view.findViewById(R.id.textViewSortIndicator);
        tvSortIndicator.setText("Last Post");
        fabSortBy = (FloatingActionButton)view.findViewById(R.id.fabSortBy);

        fabSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), fabSortBy);
                popup.getMenuInflater().inflate(R.menu.sortby, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.mostPopular:
                                User.sortBy= "mostPopular";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());

                                break;
                            case R.id.mostViewed:
                                User.sortBy= "mostViewed";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());
                                break;
                            case R.id.newest:
                                User.sortBy= "newest";
                                refreshItems();
                                tvSortIndicator.setText(item.getTitle());

                                break;
                            case R.id.lastPost:
                                User.sortBy= "lastPost";
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
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        threadListGetterList.clear();


        SetupPagination();

        return view;
    }
    void refreshItems() {
        // Load items
        // ...
        lin.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        recyclerView.setPadding(0,revAllCategories.getHeight(),0,0);

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


    protected void SetupPagination(){

        adapter = new ThreadListAdapter(threadListGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
               // GetData(totalItemsCount);
                GetData(totalItemsCount, User.sortBy);
            }
        });
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                //toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                //tabLayout.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
                lin.animate().translationY(-(revAllCategories.getHeight())).setInterpolator(new AccelerateInterpolator(2));
                recyclerView.setPadding(0,0,0,0);
            }

            @Override
            public void onShow() {
                //toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                //tabLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                lin.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                recyclerView.setPadding(0,revAllCategories.getHeight(),0,0);
            }
        });
        threadListGetterList.clear();

        GetData(0, User.sortBy);

    }


    private void GetData(int c,String sort){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+c+"&sortBy="+sort, new Response.Listener<JSONArray>() {
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


                        threadGetter.setId(id);
                        threadGetter.setTitle(title);
                        threadGetter.setCreateBy(createby);
                        threadGetter.setDate(createdate);
                        threadGetter.setCategory(category);
                        threadGetter.setContent(content);
                        threadGetter.setPhoto(profilepic);
                        threadGetter.setTotalReply(totalReply);
                        threadGetter.setTotalViews(totalViews);
                        threadGetter.setImageContentUrl(imageContentUrl);
                        threadListGetterList.add(threadGetter);

                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setPadding(0,revAllCategories.getHeight(),0,0);

                    loadingPanel.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);

    }


}
