package com.dennistjahyadigotama.soaya.activities.CategoryActivity;

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
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryAdapter;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 6/20/2016.
 */
public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerViewCategory;
    List<CategoryGetter> categoryListGetterList = new ArrayList<>();
    CategoryAdapter adapter;
    Toolbar toolbar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_activity);
        requestQueue = Volley.newRequestQueue(this);
        recyclerViewCategory = (RecyclerView)findViewById(R.id.recycler_view_category);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new CategoryAdapter(categoryListGetterList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(adapter);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());

        LoadCategories();
    }



    private void LoadCategories()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.selectCategoryActivityUrl+"?GetJustCategory=category", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                String id,name,picurl,subsname,row,type,desc;
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id= jsonObject.getString("id");
                        name=jsonObject.getString("name");
                        picurl=jsonObject.getString("picurl");
                        subsname=jsonObject.getString("subsname");
                        row=jsonObject.getString("rows");
                        type=jsonObject.getString("type");
                        desc=jsonObject.getString("description");

                        CategoryGetter categoryGetter = new CategoryGetter();
                        categoryGetter.setId(id);
                        categoryGetter.setName(name);
                        categoryGetter.setPicurl(picurl);
                        categoryGetter.setSubsname(subsname);
                        categoryGetter.setType(type);
                        categoryGetter.setRow(row);
                        categoryGetter.setDesc(desc);

                        categoryListGetterList.add(categoryGetter);
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