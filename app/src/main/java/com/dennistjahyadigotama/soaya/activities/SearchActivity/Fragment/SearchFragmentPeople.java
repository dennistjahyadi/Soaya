package com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.adapter.UserAdapter;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.adapter.UserGetter;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/12/2016.
 */
public class SearchFragmentPeople extends Fragment {

    RequestQueue requestQueue;
    List<UserGetter> userGetterList = new ArrayList<>();
    RecyclerView recyclerView;
    UserAdapter adapter;
    String url = User.searchFragmentPeopleUrl;


    public static SearchFragmentPeople newInstance(int page, String title) {
        SearchFragmentPeople fragmentFirst = new SearchFragmentPeople();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_people, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userGetterList.clear();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetupPagination(SearchActivity.text);

            }
        }, 100);

    }

    protected void SetupPagination(final String text){

        adapter = new UserAdapter(userGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                GetData(totalItemsCount,text);
            }
        });
        GetData(0,text);

    }

    private void GetData(int c,String text){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?searchPeople="+text+"&rrr="+c, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    String id,username,profileUrl,name;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getString("id");
                        username = jsonObject.getString("username");
                        name = jsonObject.getString("name");
                        profileUrl = jsonObject.getString("profilepic");
                        UserGetter userGetter = new UserGetter();
                        userGetter.setId(id);
                        userGetter.setName(name);
                        userGetter.setUsername(username);
                        userGetter.setProfileUrl(profileUrl);
                        userGetterList.add(userGetter);
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

    }

}
