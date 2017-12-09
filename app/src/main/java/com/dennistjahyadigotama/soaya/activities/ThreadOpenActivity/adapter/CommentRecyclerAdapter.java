package com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Denn on 7/3/2016.
 */
public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.MyViewHolder> {

    List<CommentGetter> commentGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName,textViewComment;
        public MyViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            textViewName = (TextView)itemView.findViewById(R.id.textViewName);
            textViewComment = (TextView)itemView.findViewById(R.id.textViewComment);

        }
    }

    public CommentRecyclerAdapter(List<CommentGetter> commentGetterList){

        this.commentGetterList=commentGetterList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_open_comment_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CommentGetter commentGetter = commentGetterList.get(position);
        holder.textViewName.setText(commentGetter.getName());
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,PeopleProfileActivity.class);
                i.putExtra("username",commentGetter.getName());
                context.startActivity(i);
            }
        });
        holder.textViewComment.setText(commentGetter.getComment());

        Link link = new Link(Pattern.compile("(@\\w+)"))
                .setTextColor(Color.parseColor("#3366BB"))    // optional, defaults to holo blue
                .setHighlightAlpha(.4f)                       // optional, defaults to .15f
                .setUnderlined(false)                         // optional, defaults to true
                .setOnLongClickListener(new Link.OnLongClickListener() {
                    @Override
                    public void onLongClick(String clickedText) {
                        // long clicked
                    }
                })
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        // single clicked
                       clickedText= clickedText.substring(1);
                        Intent i = new Intent(context, PeopleProfileActivity.class);
                        i.putExtra("username",clickedText);
                        context.startActivity(i);
                        //Toast.makeText(context,clickedText,Toast.LENGTH_SHORT).show();
                    }
                });
        LinkBuilder.on(holder.textViewComment)
                .addLink(link)
                .build();

    }

    @Override
    public int getItemCount() {
        return commentGetterList.size();
    }
}
