package com.dennistjahyadigotama.soaya.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Denn on 8/5/2016.
 */
public class SelectCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestQueue requestQueue;
    List<CategoryGetter> categoryGetterList = new ArrayList<>();
    String url= User.selectCategoryActivityUrl;
    SelectCategoryAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_activity);
        requestQueue = Volley.newRequestQueue(this);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        adapter = new SelectCategoryAdapter(categoryGetterList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GetData();
    }

    private void GetData()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?GetCategory=a", new Response.Listener<JSONArray>() {
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

                        CategoryGetter categoryGetter = new CategoryGetter();
                        categoryGetter.setId(id);
                        categoryGetter.setName(name);
                        categoryGetter.setPicurl(picurl);
                        categoryGetter.setSubsname(subsname);
                        categoryGetter.setType(type);
                        categoryGetter.setRow(row);
                        categoryGetterList.add(categoryGetter);
                    }
                    Collections.sort(categoryGetterList, new Comparator<CategoryGetter>() {
                        @Override
                        public int compare(CategoryGetter lhs, CategoryGetter rhs) {

                            return lhs.getRow().compareTo(rhs.getRow());
                        }
                    });
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }
}
