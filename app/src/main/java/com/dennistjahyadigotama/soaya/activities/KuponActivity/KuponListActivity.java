package com.dennistjahyadigotama.soaya.activities.KuponActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Paginate.EndlessRecyclerViewScrollListener;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.KuponActivity.adapter.KuponListAdapter;
import com.dennistjahyadigotama.soaya.activities.KuponActivity.adapter.KuponListGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denn on 8/27/2016.
 */
public class KuponListActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    ImageView ivAddCoupon;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    KuponListAdapter adapter;
    List<KuponListGetter> kuponListGetterList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kupon_list_activity);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivAddCoupon = (ImageView) findViewById(R.id.imageViewAddKupon);
        ivAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddKuponActivity.class);
                startActivity(intent);
            }
        });
        CheckUser();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        SetupPagination();
    }

    void refreshItems() {
        // Load items
        // ...
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

    private void CheckUser() {
        String email1 = sharedPreferences.getString(QuickstartPreferences.EMAIL, null);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.mainActivityUrl + "?poi=" + email1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = response.getJSONObject(i);

                      /*  if(person.getString("username").equals("dennistgt")||person.getString("username").equals("dailyubaya"))
                        {
                            ivAddCoupon.setVisibility(View.VISIBLE);
                        }else
                        {
                            ivAddCoupon.setVisibility(View.GONE);
                        }*/
                        if (person.getString("username").equals("dennistgt")) {
                            ivAddCoupon.setVisibility(View.VISIBLE);
                        } else {
                            ivAddCoupon.setVisibility(View.GONE);
                        }

                    }

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

    protected void SetupPagination() {

        adapter = new KuponListAdapter(kuponListGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // GetData(totalItemsCount);
                GetCouponList(totalItemsCount);
            }
        });
        kuponListGetterList.clear();

        GetCouponList(0);

    }

    private void GetCouponList(int rrr) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.kuponListActivityUrl + "?GetAllCoupon=aaa&rrr=" + rrr, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        KuponListGetter kuponListGetter = new KuponListGetter();
                        kuponListGetter.setId(jsonObject.getString("id"));
                        kuponListGetter.setTitle(jsonObject.getString("title"));
                        kuponListGetter.setExpDate(jsonObject.getString("expDate"));
                        kuponListGetter.setPhotoUrl(jsonObject.getString("photoUrl"));
                        kuponListGetterList.add(kuponListGetter);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
