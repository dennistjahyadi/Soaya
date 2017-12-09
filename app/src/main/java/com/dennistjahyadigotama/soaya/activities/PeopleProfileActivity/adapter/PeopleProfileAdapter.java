package com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.adapter;

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
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.SubCategoryActivity;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Denn on 8/6/2016.
 */
public class PeopleProfileAdapter extends RecyclerView.Adapter<PeopleProfileAdapter.MyViewHolder> {

    List<CategoryGetter> categoryGetterList;
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

    public PeopleProfileAdapter(List<CategoryGetter> pCategoryGetterList)
    {
        this.categoryGetterList=pCategoryGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscribed_category_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryGetter categoryGetter = categoryGetterList.get(position);

        if(categoryGetter.getPicurl()==null || categoryGetter.getPicurl().equals("") || categoryGetter.getPicurl().isEmpty())
        {
            holder.ivImage.setImageResource(R.drawable.border);
        }else
        {
            Picasso.with(context).load(categoryGetter.getPicurl()).into(holder.ivImage);

        }
        holder.tvName.setText(categoryGetter.getName());
        holder.tvUnsubscribe.setVisibility(View.GONE);
        holder.tvUnsubscribe.setEnabled(false);
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


    @Override
    public int getItemCount() {
        return categoryGetterList.size();
    }
}