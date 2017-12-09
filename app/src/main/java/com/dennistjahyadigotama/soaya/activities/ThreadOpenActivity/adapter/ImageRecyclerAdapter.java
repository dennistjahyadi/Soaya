package com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.CropImage.ViewImageActivity;
import com.dennistjahyadigotama.soaya.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Denn on 7/3/2016.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.MyViewHolder> {

    List<ImageGetter> imageGetterList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView caption;
        public MyViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            imageView= (ImageView)itemView.findViewById(R.id.imageViewImage);
            caption= (TextView)itemView.findViewById(R.id.textViewCaption);

        }
    }

    public ImageRecyclerAdapter(List<ImageGetter> imageGetterList)
    {
    this.imageGetterList=imageGetterList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_open_image_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ImageGetter imageGetter = imageGetterList.get(position);


        Picasso.with(context).load(imageGetter.getUrl() + "pic_" + imageGetter.getImageId() + ".jpg").error(R.drawable.ic_error_outline_black).placeholder(R.drawable.progress_animation).resize(getScreenWidth(),getScreenWidth()).centerInside().into(holder.imageView);
                holder.caption.setText(imageGetter.getCaption());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewImageActivity.class);
                i.putExtra("picUrl",imageGetter.getUrl() + "pic_" + imageGetter.getImageId() + ".jpg");
                context.startActivity(i);
            }
        });


    }


    private int getScreenWidth(){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    @Override
    public int getItemCount() {
        return imageGetterList.size();
    }
}
