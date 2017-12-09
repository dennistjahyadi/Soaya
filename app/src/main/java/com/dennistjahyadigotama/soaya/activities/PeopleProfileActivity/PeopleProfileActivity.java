package com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.CropImage.ViewImageActivity;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;
import com.dennistjahyadigotama.soaya.activities.FriendListActivity.FriendListActivity;
import com.dennistjahyadigotama.soaya.activities.HistoryActivity.CommentHistoryActivity;
import com.dennistjahyadigotama.soaya.activities.HistoryActivity.ThreadHistoryActivity;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.adapter.PeopleProfileAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Denn on 7/15/2016.
 */
public class PeopleProfileActivity extends AppCompatActivity {
    String url= User.peopleProfileActivityUrl;
    String url2 = User.peopleProfileActivityUrl2;
    String url3 = User.peopleProfileActivityUrl3;
    String urlNotif = User.peopleProfileActivityUrlNotif;
    RequestQueue requestQueue;
    Toolbar toolbar;
    ImageView imageViewProfilePic;
    TextView tvName,tvUserName,tvFakultas,tvPosts,tvThread,tvFriend,tvRelation,tvAAA;
    int totalPost,totalThread,totalFriend;
    LinearLayout linPost,linThread,linFriend;
    String user1 = User.username;
    String user2;
    RecyclerView recyclerView;
    List<CategoryGetter> categoryGetterList = new ArrayList<>();
    PeopleProfileAdapter adapter;


    int checkPeopleExist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_profile_activity);
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        user1 = User.username;
        user2 = getIntent().getStringExtra("username");
        toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageViewProfilePic = (ImageView)findViewById(R.id.imageViewProfilePic);
        tvAAA = (TextView)findViewById(R.id.textViewAAA);
        tvAAA.setText(user2+" subscribe to:");

        adapter = new PeopleProfileAdapter(categoryGetterList);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        GetSubscribeList();

        tvName = (TextView)findViewById(R.id.textViewName);
        tvUserName = (TextView)findViewById(R.id.textViewUsername);
        tvFakultas = (TextView)findViewById(R.id.textViewFakultas);
        tvPosts = (TextView)findViewById(R.id.textViewPosts);
        tvThread = (TextView)findViewById(R.id.textViewThreads);
        tvFriend=(TextView)findViewById(R.id.textViewFriends);

        tvRelation = (TextView)findViewById(R.id.textViewRelation);
        linPost=(LinearLayout) findViewById(R.id.linPost);
        linPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), CommentHistoryActivity.class);
                    i.putExtra("username",user2);
                    startActivity(i);

            }
        });
        linThread=(LinearLayout)findViewById(R.id.linThread);
        linThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), ThreadHistoryActivity.class);
                    i.putExtra("username",user2);
                    startActivity(i);
            }
        });
        linFriend = (LinearLayout)findViewById(R.id.linFriend);
        linFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent i = new Intent(getApplicationContext(), FriendListActivity.class);
                    i.putExtra("username",user2);
                    startActivity(i);


            }
        });

        CheckPeopleExist();

    }

    private void GetSubscribeList()
    {
        categoryGetterList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.categoryActivityUrl2+"?getSubscribe="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    String name,picurl,id;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        id = jsonObject.getString("id");
                        name = jsonObject.getString("name");
                        picurl = jsonObject.getString("picurl");

                        CategoryGetter categoryGetter = new CategoryGetter();
                        categoryGetter.setId(id);
                        categoryGetter.setName(name);
                        categoryGetter.setPicurl(picurl);
                        categoryGetterList.add(categoryGetter);
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

    private void CheckPeopleExist(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?checkPeopleExist="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        checkPeopleExist = Integer.parseInt(jsonObject.getString("jumlah"));

                    }
                    if(checkPeopleExist==1)
                    {
                        GetData();
                    }
                    else
                    {
                        tvUserName.setText("User not found");
                        imageViewProfilePic.setVisibility(View.GONE);
                        tvName.setVisibility(View.GONE);
                        tvFakultas.setVisibility(View.GONE);
                        tvPosts.setVisibility(View.GONE);
                        tvThread.setVisibility(View.GONE);
                        tvFriend.setVisibility(View.GONE);
                        tvRelation.setVisibility(View.GONE);
                        linPost.setVisibility(View.GONE);
                        linThread.setVisibility(View.GONE);
                        linFriend.setVisibility(View.GONE);
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

    private void SetPicOnClick(final String picurl)
    {
        imageViewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ViewImageActivity.class);
                i.putExtra("picUrl",picurl);
                startActivity(i);
            }
        });

    }

    private void SetButton(){
       final String words = tvRelation.getText().toString();

            tvRelation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(words.equals("Friend"))
                    {

                    }
                    else if(words.equals("Waiting"))
                    {

                    }
                    else if(words.equals("Confirm"))
                    {
                        ConfirmFriend();
                    }
                    else if(words.equals("Add Friend"))
                    {
                        AddFriend();
                    }
                }
            });

    }

    private void ConfirmFriend()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        tvRelation.setText("Friend");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map =new HashMap<>();
                map.put("ConfirmUser1",user1);
                map.put("ConfirmUser2",user2);

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void AddFriend()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        tvRelation.setText("Waiting");

                        SendFriendRequestNotification();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map =new HashMap<>();
                map.put("AddFriendUser1",user1);
                map.put("AddFriendUser2",user2);

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void SendFriendRequestNotification()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlNotif, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("notifFriendRequestTo",user2);
                map.put("notifFriendRequestFrom",user1);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void CheckRelation()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("friend"))
                    {
                        tvRelation.setText("Friend");
                    }
                    else if(jsonObject.names().get(0).equals("request0"))
                    {
                        tvRelation.setText("Waiting");
                    }
                    else if(jsonObject.names().get(0).equals("request1"))
                    {
                        tvRelation.setText("Confirm");

                    }
                    else if(jsonObject.names().get(0).equals("request2"))
                    {
                        tvRelation.setText("Add Friend");

                    }

                    SetButton();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("user1",user1);
                    hashMap.put("user2",user2);
                    return hashMap;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void GetData()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?poi="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String name,fakultas,profilePicUrl = null;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        name = jsonObject.getString("name");
                        user2 = jsonObject.getString("username");
                        fakultas = jsonObject.getString("fakultas");
                        profilePicUrl = jsonObject.getString("profilepic");
                        tvName.setText(name);
                        tvUserName.setText(user2);
                        tvFakultas.setText(fakultas);
                    }
                    if(profilePicUrl==null|| profilePicUrl==""||profilePicUrl.isEmpty())
                    {

                        Picasso.with(getApplicationContext()).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(imageViewProfilePic);

                    }else
                    {
                        Picasso.with(getApplicationContext()).load(profilePicUrl).transform(new CircleTransform()).into(imageViewProfilePic);
                        SetPicOnClick(profilePicUrl);
                    }

                    GetTotalPost();
                    GetTotalThread();
                    GetTotalFriend();
                    if(user1==user2 || user1.equals(user2)) {

                    }else
                    {
                        CheckRelation();
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

    public void GetTotalPost(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalPost="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        tvPosts.setText(jsonObject.getString("jumlah"));
                        totalPost = Integer.parseInt(tvPosts.getText().toString());

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

    public void GetTotalThread(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalThread="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        tvThread.setText(jsonObject.getString("jumlah"));
                        totalThread = Integer.parseInt(tvThread.getText().toString());


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
    public void GetTotalFriend(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalFriend="+user2, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        tvFriend.setText(jsonObject.getString("jumlah"));
                        totalFriend= Integer.parseInt(tvThread.getText().toString());


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
