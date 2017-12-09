package com.dennistjahyadigotama.soaya.Menu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Denn on 7/21/2016.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder> {

    List<FriendRequestGetter> friendRequestGetterList;
    Context context;
    String url = User.friendRequestUrl;
    RequestQueue requestQueue;

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout revLayout;
        ImageView imageView;
        TextView textViewUsername,textViewName,accept,decline;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            requestQueue = Volley.newRequestQueue(context);
            revLayout = (RelativeLayout)itemView.findViewById(R.id.revLayout);
            imageView = (ImageView)itemView.findViewById(R.id.imageViewPhoto);
            textViewUsername = (TextView)itemView.findViewById(R.id.textViewUsername);
            textViewName = (TextView)itemView.findViewById(R.id.textViewName);
            accept = (TextView)itemView.findViewById(R.id.textViewAccept);
            decline = (TextView)itemView.findViewById(R.id.textViewDecline);
        }
    }

    public FriendRequestAdapter(List<FriendRequestGetter> friendRequestGetterList)
    {
        this.friendRequestGetterList = friendRequestGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final FriendRequestGetter friendRequestGetter = friendRequestGetterList.get(position);
        if(friendRequestGetter.getProfileUrl()==null|| friendRequestGetter.getProfileUrl()==""||friendRequestGetter.getProfileUrl().isEmpty())
        {

            Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(holder.imageView);

        }else
        {
            Picasso.with(context).load(friendRequestGetter.getProfileUrl()).transform(new CircleTransform()).into(holder.imageView);
        }
        holder.textViewUsername.setText(friendRequestGetter.getUsername());
        holder.textViewName.setText(friendRequestGetter.getName());
        holder.revLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PeopleProfileActivity.class);
                i.putExtra("username",friendRequestGetter.getUsername());
                context.startActivity(i);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptFriendRequest(friendRequestGetter.getFriendRequestId(),position);
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFriendRequest(friendRequestGetter.getFriendRequestId(),position);
            }
        });

    }

    private void AcceptFriendRequest(final String id, final int position)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        friendRequestGetterList.remove(position);
                        notifyDataSetChanged();
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
                HashMap<String,String> map = new HashMap<>();
                map.put("acceptFriendRequest",id);
                return map;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void DeleteFriendRequest(final String id, final int position)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        friendRequestGetterList.remove(position);
                        notifyDataSetChanged();
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
                HashMap<String,String> map = new HashMap<>();
                map.put("deleteFriendRequest",id);
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return friendRequestGetterList.size();
    }
}
