package com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter;

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
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Denn on 7/7/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    List<CategoryGetter> categoryListGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lin,lin2;
        ImageView ivPic;
        TextView tvName,tvDesc;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            lin = (LinearLayout)itemView.findViewById(R.id.lin);
            lin2 = (LinearLayout)itemView.findViewById(R.id.lin2);
            ivPic = (ImageView)itemView.findViewById(R.id.imageViewImage);
            tvName = (TextView)itemView.findViewById(R.id.textViewName);
            tvDesc = (TextView)itemView.findViewById(R.id.textViewDescription);
        }
    }

    public CategoryAdapter(List<CategoryGetter> categoryListGetterList){

        this.categoryListGetterList=categoryListGetterList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_forum_category_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CategoryGetter categoryListGetter = categoryListGetterList.get(position);

        if(categoryListGetter.getPicurl()==null || categoryListGetter.getPicurl().equals("") || categoryListGetter.getPicurl().isEmpty())
        {
            holder.ivPic.setImageResource(R.drawable.border);
        }else
        {
            Picasso.with(context).load(categoryListGetter.getPicurl()).into(holder.ivPic);

        }


        holder.tvName.setText(categoryListGetter.getName());
        holder.tvDesc.setText(categoryListGetter.getDesc());

        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(context,categoryListGetter.getName(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("categoryid",categoryListGetter.getId());
                i.putExtra("categoryname",categoryListGetter.getName().replace(" ", "")); //volley can't pass white space ex: get post
                i.putExtra("titlename",categoryListGetter.getName());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryListGetterList.size();
    }
}
