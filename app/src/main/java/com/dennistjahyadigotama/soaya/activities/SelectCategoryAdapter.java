package com.dennistjahyadigotama.soaya.activities;

import android.app.Activity;
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
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter.CategoryGetter;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Denn on 8/5/2016.
 */
public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.MyViewHolder> {

    List<CategoryGetter> categoryGetterList;
    String url = User.selectCategoryActivityUrl;
    Context context;
    SelectCategoryActivity selectCategoryActivity;
    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin,lin2;
        ImageView ivPic;
        TextView tvName;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            lin = (LinearLayout)itemView.findViewById(R.id.lin);
            lin2 = (LinearLayout)itemView.findViewById(R.id.lin2);

            ivPic = (ImageView)itemView.findViewById(R.id.imageViewImage);
            tvName = (TextView)itemView.findViewById(R.id.textViewName);
        }
    }

    public SelectCategoryAdapter(List<CategoryGetter> categoryGetterList, SelectCategoryActivity selectCategoryActivity)
    {
        this.categoryGetterList=categoryGetterList;
        this.selectCategoryActivity = selectCategoryActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_category_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {   final CategoryGetter categoryGetter = categoryGetterList.get(position);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(categoryGetter.getPicurl()==null || categoryGetter.getPicurl().equals("") || categoryGetter.getPicurl().isEmpty())
        {
            holder.ivPic.setImageResource(R.drawable.border);
        }else
        {
            Picasso.with(context).load(categoryGetter.getPicurl()).into(holder.ivPic);

        }
        if(categoryGetter.getType().equals("subcategory"))
        {
            params.setMargins(50, 0, 0, 0);
            holder.lin2.setLayoutParams(params);
        }else
        {
            params.setMargins(0, 0, 0, 0);
            holder.lin2.setLayoutParams(params);
        }
        holder.tvName.setText(categoryGetter.getName());
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("category",categoryGetter.getName());
                selectCategoryActivity.setResult(Activity.RESULT_OK,returnIntent);
                selectCategoryActivity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryGetterList.size();
    }


}
