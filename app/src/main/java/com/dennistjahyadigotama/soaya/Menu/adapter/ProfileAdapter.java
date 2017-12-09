package com.dennistjahyadigotama.soaya.Menu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.Menu.Profile;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.SubCategoryActivity;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Denn on 8/6/2016.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    List<CategoryGetter> categoryGetterList;
    Profile profile;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvName,tvUnsubscribe;
        LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ivImage = (ImageView)itemView.findViewById(R.id.imageViewImage);
            tvName = (TextView)itemView.findViewById(R.id.textViewName);
            tvUnsubscribe = (TextView)itemView.findViewById(R.id.textViewUnsubscribe);
            lin = (LinearLayout)itemView.findViewById(R.id.lin);
        }
    }

    public ProfileAdapter(List<CategoryGetter> pCategoryGetterList, Profile profile)
    {
        this.categoryGetterList=pCategoryGetterList;
        this.profile=profile;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_category_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        final CategoryGetter categoryGetter = categoryGetterList.get(position);

        if(categoryGetter.getPicurl()==null || categoryGetter.getPicurl().equals("") || categoryGetter.getPicurl().isEmpty())
        {
            holder.ivImage.setImageResource(R.drawable.border);
        }else
        {
            Picasso.with(context).load(categoryGetter.getPicurl()).into(holder.ivImage);

        }
        holder.tvName.setText(categoryGetter.getName());
        holder.tvUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(profile.getActivity());
                alertDialog.setMessage("Unsubscribe "+categoryGetter.getName()+"?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RemoveSubscribeCategory(categoryGetter.getName(),position);
                    }
                });
                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog2 = alertDialog.create();
                alertDialog2.show();
            }
        });
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("categoryid",categoryGetter.getId());
                i.putExtra("categoryname",categoryGetter.getName().replace(" ", ""));
                i.putExtra("titlename",categoryGetter.getName());
                context.startActivity(i);
            }
        });

    }

    private void RemoveSubscribeCategory(final String categoryname, final int position)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.categoryActivityUrl2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname.replace(" ",""));
                        categoryGetterList.remove(position);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname.replace(" ",""));

                            }
                        },200);
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(categoryname.replace(" ",""));

                    }
                    notifyDataSetChanged();
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
                map.put("username", User.username);
                map.put("removesubscategory",categoryname.replace(" ",""));
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return categoryGetterList.size();
    }
}
