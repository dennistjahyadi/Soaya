package com.dennistjahyadigotama.soaya.activities.CategoryActivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.CategoryActivity.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Denn on 7/9/2016.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    List<SubCategoryGetter> subCategoryGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rev;
        ImageView imageView;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            rev = (RelativeLayout)itemView.findViewById(R.id.rev);
            imageView= (ImageView)itemView.findViewById(R.id.imageViewImage);
            textView =(TextView)itemView.findViewById(R.id.textViewName);
        }
    }

    public SubCategoryAdapter(List<SubCategoryGetter> list){
        this.subCategoryGetterList = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SubCategoryGetter subCategoryGetter = subCategoryGetterList.get(position);

        if(subCategoryGetter.getPicurl()==null || subCategoryGetter.getPicurl().equals("") || subCategoryGetter.getPicurl().isEmpty())
        {
            holder.imageView.setImageResource(R.drawable.border);
        }else
        {
            Picasso.with(context).load(subCategoryGetter.getPicurl()).into(holder.imageView);
        }

        holder.textView.setText(subCategoryGetter.getName());
        holder.rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SubCategoryActivity.class);
                i.putExtra("categoryname",subCategoryGetter.getName().replace(" ", ""));//volley can't pass white space ex: get post
                i.putExtra("categoryid",subCategoryGetter.getId());
                i.putExtra("titlename",subCategoryGetter.getName());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryGetterList.size();
    }
}
