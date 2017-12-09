package com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity;

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
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.adapter.WartaUbayaGetter;
import com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.adapter.WartaUbayaListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denn on 9/21/2016.
 */
public class WartaUbayaListActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    RecyclerView recyclerView;
    WartaUbayaListAdapter adapter;
    List<WartaUbayaGetter> wartaUbayaGetterList = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warta_ubaya_list_activity);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Warta Ubaya");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        adapter = new WartaUbayaListAdapter(wartaUbayaGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetData();
    }

    private void GetData()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.wartaUbayaUrl+"?getAllWarta=as", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        WartaUbayaGetter wartaUbayaGetter = new WartaUbayaGetter();
                        wartaUbayaGetter.setTitle(jsonObject.getString("title"));
                        wartaUbayaGetter.setEdisi(jsonObject.getString("version"));
                        wartaUbayaGetter.setPhotoUrl(jsonObject.getString("location"));
                        wartaUbayaGetter.setTotalPage(Integer.parseInt(jsonObject.getString("totalpage")));

                        wartaUbayaGetterList.add(wartaUbayaGetter);

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
