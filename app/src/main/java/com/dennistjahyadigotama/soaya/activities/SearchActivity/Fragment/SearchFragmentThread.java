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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.ThreadListGetter;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/12/2016.
 */
public class SearchFragmentThread extends Fragment {

    List<ThreadListGetter> threadListGetterList = new ArrayList<>();
    ThreadListAdapter adapter;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    String url = User.searchFragmentThreadUrl;

    public static SearchFragmentThread newInstance(int page, String title) {
        SearchFragmentThread fragmentFirst = new SearchFragmentThread();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_thread, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
       threadListGetterList.clear();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SetupPagination(SearchActivity.text);

            }
        }, 100);
    }

    protected void SetupPagination(final String text){

        adapter = new ThreadListAdapter(threadListGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // GetData(totalItemsCount);
                GetData(totalItemsCount,text);
            }
        });
        GetData(0,text);

    }

    private void GetData(int c, String text){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?searchForum="+text+"&rrr="+c, new Response.Listener<JSONArray>() {
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

                        threadGetter.setTotalReply(totalReply);
                        threadGetter.setTotalViews(totalViews);
                        threadGetter.setId(id);
                        threadGetter.setTitle(title);
                        threadGetter.setCreateBy(createby);
                        threadGetter.setDate(createdate);
                        threadGetter.setCategory(category);
                        threadGetter.setContent(content);
                        threadGetter.setPhoto(profilepic);
                        threadGetter.setImageContentUrl(imageContentUrl);
                        threadListGetterList.add(threadGetter);

                    }
                    adapter.notifyDataSetChanged();

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
