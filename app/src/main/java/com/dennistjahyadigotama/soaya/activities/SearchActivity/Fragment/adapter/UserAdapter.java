package com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Denn on 7/13/2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    List<UserGetter> userGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linLayout;
        ImageView imageView;
        TextView textViewUsername,textViewName;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            linLayout = (LinearLayout)itemView.findViewById(R.id.linLayout);
            imageView = (ImageView)itemView.findViewById(R.id.imageViewPhoto);
            textViewUsername = (TextView)itemView.findViewById(R.id.textViewUsername);
            textViewName = (TextView)itemView.findViewById(R.id.textViewName);

        }
    }


    public UserAdapter(List<UserGetter> userGetterList){

        this.userGetterList = userGetterList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserGetter userGetter = userGetterList.get(position);
        if(userGetter.getProfileUrl()==null|| userGetter.getProfileUrl()==""||userGetter.getProfileUrl().isEmpty())
        {

            Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(holder.imageView);

        }else
        {
            Picasso.with(context).invalidate(userGetter.getProfileUrl());
            Picasso.with(context).load(userGetter.getProfileUrl()).transform(new CircleTransform()).into(holder.imageView);
        }
        holder.textViewUsername.setText(userGetter.getUsername());
        holder.textViewName.setText(userGetter.getName());
        holder.linLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PeopleProfileActivity.class);
                i.putExtra("username",userGetter.getUsername());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userGetterList.size();
    }
}
