package com.dennistjahyadigotama.soaya.Menu.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.Menu.adapter.FriendRequestAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.FriendRequestGetter;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/20/2016.
 */
public class FriendRequest extends Fragment {

    List<FriendRequestGetter> friendRequestGetterList = new ArrayList<>();
    FriendRequestAdapter adapter;
    RecyclerView recyclerView;
    String url = User.friendRequestUrl;
    RequestQueue requestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout loadingPanel;
    SharedPreferences sharedPreferences;
    String TAG_LIST="friendRequestList";


    public static FriendRequest newInstance(int page, String title) {
        FriendRequest fragmentFirst = new FriendRequest();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_friend_request, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        loadingPanel = (RelativeLayout)view.findViewById(R.id.loadingPanel);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        sharedPreferences = getActivity().getSharedPreferences("prefs",getActivity().MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedPreferences.edit().putInt(QuickstartPreferences.MARKNOTICE,0).apply();
                Intent intents = new Intent("mainactivity");
                sendLocationBroadcast(intents);
                refreshItems();
            }
        });


        return view;
    }



    private void sendLocationBroadcast(Intent intent){

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
    void refreshItems() {
        // Load items
        // ...
        friendRequestGetterList.clear();
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

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.edit().putInt(QuickstartPreferences.MARKNOTICE,0).apply();
        Intent intents = new Intent("mainactivity");
        sendLocationBroadcast(intents);
        loadingPanel.setVisibility(View.VISIBLE);

        friendRequestGetterList.clear();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetupPagination();

            }
        }, 100);

    }
    protected void SetupPagination(){

        adapter = new FriendRequestAdapter(friendRequestGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                GetData(totalItemsCount);
            }
        });

        GetData(0);


    }
    private void GetData(int c)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?friendRequest="+ User.username+"&rrr="+c, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    String friendRequestId,userId,username,profileUrl,name;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        friendRequestId = jsonObject.getString("friendRequestId");
                        userId = jsonObject.getString("userId");
                        username = jsonObject.getString("username");
                        name = jsonObject.getString("name");
                        profileUrl = jsonObject.getString("profilepic");
                        FriendRequestGetter userGetter = new FriendRequestGetter();
                        userGetter.setFriendRequestId(friendRequestId);
                        userGetter.setUserId(userId);
                        userGetter.setName(name);
                        userGetter.setUsername(username);
                        userGetter.setProfileUrl(profileUrl);
                        friendRequestGetterList.add(userGetter);
                    }
                    adapter.notifyDataSetChanged();

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
        loadingPanel.setVisibility(View.GONE);

    }
}
