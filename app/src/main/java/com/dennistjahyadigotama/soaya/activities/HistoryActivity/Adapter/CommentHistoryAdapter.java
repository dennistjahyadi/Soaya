package com.dennistjahyadigotama.soaya.activities.HistoryActivity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.ThreadOpenActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Denn on 7/16/2016.
 */
public class CommentHistoryAdapter extends RecyclerView.Adapter<CommentHistoryAdapter.MyViewHolder> {

    List<CommentHistoryGetter> commentHistoryGetterList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin;
        TextView tvTitle,tvDate,tvComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            lin = (LinearLayout)itemView.findViewById(R.id.lin);
            tvDate = (TextView)itemView.findViewById(R.id.textViewDate);
            tvTitle = (TextView)itemView.findViewById(R.id.textViewTitle);
            tvComment = (TextView)itemView.findViewById(R.id.textViewComment);
        }
    }


    public CommentHistoryAdapter(List<CommentHistoryGetter> param){

        this.commentHistoryGetterList = param;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_history_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CommentHistoryGetter commentHistoryGetter = commentHistoryGetterList.get(position);
        holder.tvTitle.setText(commentHistoryGetter.getTitle());
        holder.tvComment.setText(commentHistoryGetter.getComment());
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
        LinkBuilder.on(holder.tvComment)
                .addLink(link)
                .build();





        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ThreadOpenActivity.class);
                i.putExtra("ThreadId",commentHistoryGetter.getThreadId());
                context.startActivity(i);
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date mDate = sdf.parse(commentHistoryGetter.getDate());
            long your_time_in_milliseconds = mDate.getTime();
            long current_time_in_millisecinds = System.currentTimeMillis();

            CharSequence thedate= DateUtils.getRelativeTimeSpanString(your_time_in_milliseconds, current_time_in_millisecinds, DateUtils.MINUTE_IN_MILLIS);
            holder.tvDate.setText(thedate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return commentHistoryGetterList.size();
    }
}
