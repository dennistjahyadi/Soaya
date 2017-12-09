package com.dennistjahyadigotama.soaya.activities.KuponActivity.adapter;

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
import com.dennistjahyadigotama.soaya.activities.KuponActivity.KuponActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Denn on 8/28/2016.
 */
public class KuponListAdapter extends RecyclerView.Adapter<KuponListAdapter.MyViewHolder> {

    List<KuponListGetter> kuponListGetterList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvTitleCoupon,tvExpDate;
        LinearLayout lin;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ivPhoto = (ImageView)itemView.findViewById(R.id.imageViewPhoto);
            tvTitleCoupon = (TextView)itemView.findViewById(R.id.textViewTitleCoupon);
            tvExpDate = (TextView)itemView.findViewById(R.id.textViewExpDate);
            lin = (LinearLayout)itemView.findViewById(R.id.linLayout);
        }
    }

    public KuponListAdapter(List<KuponListGetter> pKupon)
    {
        this.kuponListGetterList = pKupon;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kupon_list_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final KuponListGetter kuponListGetter = kuponListGetterList.get(position);

        Picasso.with(context).load(kuponListGetter.getPhotoUrl()).into(holder.ivPhoto);
        holder.tvTitleCoupon.setText(kuponListGetter.getTitle());
        holder.tvExpDate.setText("Expired: "+kuponListGetter.getExpDate());
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, KuponActivity.class);
                i.putExtra("id",kuponListGetter.getId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return kuponListGetterList.size();
    }
}
