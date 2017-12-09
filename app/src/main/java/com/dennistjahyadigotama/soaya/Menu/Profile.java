package com.dennistjahyadigotama.soaya.Menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.CropImage.ViewImageActivity;
import com.dennistjahyadigotama.soaya.Menu.adapter.ProfileAdapter;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;
import com.dennistjahyadigotama.soaya.activities.FriendListActivity.FriendListActivity;
import com.dennistjahyadigotama.soaya.activities.HistoryActivity.CommentHistoryActivity;
import com.dennistjahyadigotama.soaya.activities.HistoryActivity.ThreadHistoryActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 6/20/2016.
 */
public class Profile extends Fragment {

    Context context;
    ImageView imageView;
    String url2 = User.profileUrl2;
    String url3 = User.profileUrl3;
    LinearLayout linPost,linThread,linFriend;
    RequestQueue requestQueue;
    TextView tvName,tvFakultas,tvPosts,tvThread,tvFriend,tvAAA,tvUsername;
    String profilePicUrl=null;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    int totalThread,totalPost,totalFriend;
    SharedPreferences sharedPreferences;
    List<CategoryGetter> categoryGetterList = new ArrayList<>();
    ProfileAdapter adapter;
    String TAG_LIST="subscribedList";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context=view.getContext();
        requestQueue = Volley.newRequestQueue(context);
        sharedPreferences = getActivity().getSharedPreferences("prefs",getActivity().MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        tvAAA = (TextView)view.findViewById(R.id.textViewAAA);
        tvAAA.setText("subscribe to:");

        tvName = (TextView)view.findViewById(R.id.textViewName);
        tvUsername = (TextView)view.findViewById(R.id.textViewUsername);
        tvFakultas = (TextView)view.findViewById(R.id.textViewFakultas);
        tvPosts=(TextView)view.findViewById(R.id.textViewPosts);
        tvThread=(TextView)view.findViewById(R.id.textViewThreads);
        tvFriend=(TextView)view.findViewById(R.id.textViewFriends);
        linPost=(LinearLayout) view.findViewById(R.id.linPost);
        linThread=(LinearLayout)view.findViewById(R.id.linThread);
        linFriend = (LinearLayout)view.findViewById(R.id.linFriend);
        imageView = (ImageView)view.findViewById(R.id.imageViewProfilePic);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        adapter = new ProfileAdapter(categoryGetterList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        refreshItems();

        return view;

    }
    void refreshItems() {
        // Load items
        // ...
        GetSubscribeList();

        String name,fakultas,username;
        username = sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        name = sharedPreferences.getString(QuickstartPreferences.NAME,null);
        fakultas=sharedPreferences.getString(QuickstartPreferences.FAKULTAS,null);
        tvUsername.setText("@"+username);

        if(name==null ||fakultas==null) {
            GetProfileData();
        }else
        {
            tvName.setText(name);
            tvFakultas.setText(fakultas);
            GetProfilePicUrl();
        }

            GetTotalFriend();
            GetTotalPost();
            GetTotalThread();

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void GetSubscribeList()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.categoryActivityUrl2+"?getSubscribe="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    categoryGetterList.clear();

                    String id,name,picurl;
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




    public void SetIvProfileOnClick(final String ppUrl)
    {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getContext(),imageView);
                popup.getMenuInflater().inflate(R.menu.popupchangeprofilepic, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.viewPhoto:
                                Intent i = new Intent(getActivity(), ViewImageActivity.class);
                                i.putExtra("picUrl",ppUrl);
                                startActivity(i);
                                break;
                            case R.id.changePhoto:
                                if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);

                                } else {
                                    CropImage.startPickImageActivity(getActivity());

                                }
                                break;

                        }
                        return false;
                    }
                });

                popup.show();


            }
        });
    }


    public void GetTotalPost(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalPost="+ User.username, new Response.Listener<JSONArray>() {
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
                    linPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(getActivity(), CommentHistoryActivity.class);
                                i.putExtra("username", User.username);
                                startActivity(i);


                        }
                    });
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalThread="+ User.username, new Response.Listener<JSONArray>() {
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
                    linThread.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(getActivity(), ThreadHistoryActivity.class);
                                i.putExtra("username", User.username);

                                startActivity(i);


                        }
                    });
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url2+"?totalFriend="+ User.username, new Response.Listener<JSONArray>() {
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

                    linFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent i = new Intent(getActivity(), FriendListActivity.class);
                                i.putExtra("username", User.username);
                                startActivity(i);

                        }
                    });
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

    private void GetProfileData()
    {
        String username = sharedPreferences.getString(QuickstartPreferences.USERNAME,null);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url3+"?poi="+ username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String name=null,fakultas = null;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        name = jsonObject.getString("name");
                        fakultas = jsonObject.getString("fakultas");
                        profilePicUrl = jsonObject.getString("profilepic");
                        tvName.setText(name);
                        tvFakultas.setText(fakultas);

                    }
                    sharedPreferences.edit().putString(QuickstartPreferences.NAME,name).apply();
                    sharedPreferences.edit().putString(QuickstartPreferences.FAKULTAS,fakultas).apply();
                    if(profilePicUrl==null|| profilePicUrl.equals("")||profilePicUrl.isEmpty())
                    {

                        Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(imageView);

                    }else
                    {
                        Picasso.with(context).load(profilePicUrl).transform(new CircleTransform()).into(imageView);
                    }
                    SetIvProfileOnClick(profilePicUrl);

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
    private void GetProfilePicUrl()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url3+"?poi="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String profilePicUrl=null;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);

                        profilePicUrl = jsonObject.getString("profilepic");

                    }

                    if(profilePicUrl==null|| profilePicUrl.equals("")||profilePicUrl.isEmpty())
                    {

                        Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(imageView);

                    }else
                    {
                        Picasso.with(context).load(profilePicUrl).transform(new CircleTransform()).into(imageView);
                    }
                    SetIvProfileOnClick(profilePicUrl);

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