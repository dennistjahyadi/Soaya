package com.dennistjahyadigotama.soaya.Menu.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.dennistjahyadigotama.soaya.Menu.adapter.NoticeAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.NoticeGetter;
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
public class NotificationInfo extends Fragment {

    List<NoticeGetter> noticeGetterList = new ArrayList<>();
    NoticeAdapter adapter;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String url= User.notificationInfoUrl;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout loadingPanel;
    SharedPreferences sharedPreferences;
    String TAG_LIST="tag_list";

    public static NotificationInfo newInstance(int page, String title) {
        NotificationInfo fragmentFirst = new NotificationInfo();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_info, container, false);
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

        loadingPanel.setVisibility(View.VISIBLE);
        noticeGetterList.clear();
        Setup();
        return view;
    }

    private void sendLocationBroadcast(Intent intent){

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
    void refreshItems() {
        // Load items
        // ...
        noticeGetterList.clear();
        Setup();
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

    }

    protected void Setup(){

        adapter = new NoticeAdapter(noticeGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

            GetData();

    }

    private void GetData()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?noticeList="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    noticeGetterList.clear();

                    String id,profileurl,text,fromusername,type,date;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getString("keyId");
                        profileurl= jsonObject.getString("profilepic");
                        text= jsonObject.getString("messages");
                        fromusername= jsonObject.getString("namefrom");
                        type= jsonObject.getString("type");
                        date = jsonObject.getString("notdate");
                        NoticeGetter noticeGetter = new NoticeGetter();
                        noticeGetter.setId(id);
                        noticeGetter.setProfileUrl(profileurl);
                        noticeGetter.setText(text);
                        noticeGetter.setFromUsername(fromusername);
                        noticeGetter.setType(type);
                        noticeGetter.setDate(date);
                        noticeGetterList.add(noticeGetter);

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
