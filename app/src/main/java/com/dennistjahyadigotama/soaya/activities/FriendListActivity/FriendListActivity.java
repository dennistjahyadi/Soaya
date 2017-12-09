package com.dennistjahyadigotama.soaya.activities.FriendListActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 7/16/2016.
 */
public class FriendListActivity extends AppCompatActivity {
    Toolbar toolbar;
    List<UserGetter> userGetterList = new ArrayList<>();
    RecyclerView recyclerView;
    UserAdapter adapter;
    RequestQueue requestQueue;
    String url = User.friendListActivityUrl;
    String username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity_friendlist);
        requestQueue = Volley.newRequestQueue(this);
        username = getIntent().getStringExtra("username");
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friends");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        SetupPagination();
    }

    protected void SetupPagination(){

        adapter = new UserAdapter(userGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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

    private void GetData(int c){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?friendList="+username+"&rrr="+c, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    String id,username1,profileUrl,name;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if(jsonObject.getString("user1").toLowerCase().equals(username.toLowerCase()))
                        {
                            id = jsonObject.getString("id2");
                            username1 = jsonObject.getString("user2");
                            profileUrl = jsonObject.getString("profilepic2");
                            name = jsonObject.getString("name2");
                        }else
                        {
                            id = jsonObject.getString("id1");
                            username1 = jsonObject.getString("user1");
                            profileUrl = jsonObject.getString("profilepic1");
                            name = jsonObject.getString("name1");
                        }


                        UserGetter userGetter = new UserGetter();
                        userGetter.setId(id);
                        userGetter.setUsername(username1);
                        userGetter.setName(name);
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
