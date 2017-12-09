package com.dennistjahyadigotama.soaya.activities.HistoryActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/16/2016.
 */
public class ThreadHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    List<ThreadListGetter> threadListGetters = new ArrayList<>();
    ThreadListAdapter adapter;
    RequestQueue requestQueue;
    String url = User.threadHistoryActivityUrl;
    String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity_friendlist);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Threads");



        username = getIntent().getStringExtra("username");

        SetupPagination();

    }


    protected void SetupPagination(){

        adapter = new ThreadListAdapter(threadListGetters);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // GetData(totalItemsCount);
                GetData(totalItemsCount);
            }
        });
        GetData(0);

    }


    private void GetData(int c){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?username="+username+"&poi="+c, new Response.Listener<JSONArray>() {
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
                        threadListGetters.add(threadGetter);

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
