package com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.WartaUbayaActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Denn on 9/21/2016.
 */
public class WartaUbayaListAdapter extends RecyclerView.Adapter<WartaUbayaListAdapter.MyViewHolder> {

    List<WartaUbayaGetter> wartaUbayaGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;
        TextView tvTitle,tvEdisi;
        LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ivPhoto = (ImageView)itemView.findViewById(R.id.imageViewPhoto);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvEdisi = (TextView)itemView.findViewById(R.id.tvEdisi);
            lin = (LinearLayout)itemView.findViewById(R.id.linLayout);
        }
    }

    public WartaUbayaListAdapter(List<WartaUbayaGetter> wartaUbayaGetterList)
    {
        this.wartaUbayaGetterList = wartaUbayaGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.warta_ubaya_list_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final WartaUbayaGetter wartaUbayaGetter = wartaUbayaGetterList.get(position);
        holder.tvTitle.setText(wartaUbayaGetter.getTitle());
        holder.tvEdisi.setText("Edisi "+wartaUbayaGetter.getEdisi());
        Picasso.with(context).load(wartaUbayaGetter.getPhotoUrl()+"1.jpg").into(holder.ivPhoto);
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WartaUbayaActivity.class);
                i.putExtra("totalpage",wartaUbayaGetter.getTotalPage());
                i.putExtra("location",wartaUbayaGetter.getPhotoUrl());
                i.putExtra("edisi",wartaUbayaGetter.getEdisi());
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return this.wartaUbayaGetterList.size();
    }
}
