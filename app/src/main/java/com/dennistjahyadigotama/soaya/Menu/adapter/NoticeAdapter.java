package com.dennistjahyadigotama.soaya.Menu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.ThreadOpenActivity;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Denn on 7/23/2016.
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    List<NoticeGetter> noticeGetterList;
    Context context;


    public NoticeAdapter(List<NoticeGetter> noticeGetterList)
    {
        this.noticeGetterList = noticeGetterList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin;
        ImageView imageViewPhoto;
        TextView textViewUsername,textViewText,textViewDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            lin = (LinearLayout)itemView.findViewById(R.id.lin);
            imageViewPhoto = (ImageView)itemView.findViewById(R.id.imageViewPhoto);
            textViewUsername = (TextView) itemView.findViewById(R.id.textViewUsername);
            textViewText = (TextView)itemView.findViewById(R.id.textViewNotice);
            textViewDate= (TextView)itemView.findViewById(R.id.textViewDate);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_recycler_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NoticeGetter noticeGetter = noticeGetterList.get(position);

        if(noticeGetter.getProfileUrl()==null|| noticeGetter.getProfileUrl()==""||noticeGetter.getProfileUrl().isEmpty())
        {

            Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(holder.imageViewPhoto);

        }else
        {
            Picasso.with(context).load(noticeGetter.getProfileUrl()).transform(new CircleTransform()).into(holder.imageViewPhoto);
        }
        holder.textViewUsername.setText(noticeGetter.getFromUsername());
        holder.textViewText.setText(noticeGetter.getText());

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
        LinkBuilder.on(holder.textViewText)
                .addLink(link)
                .build();



        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(noticeGetter.getType().equals("someonecommentyourthread") || noticeGetter.getType().equals("commentnotice")||noticeGetter.getType().equals("newthread"))
                {
                    Intent i = new Intent(context, ThreadOpenActivity.class);
                    i.putExtra("ThreadId",noticeGetter.getId());
                    context.startActivity(i);
                }

            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date mDate = sdf.parse(noticeGetter.getDate());
            long your_time_in_milliseconds = mDate.getTime();
            long current_time_in_millisecinds = System.currentTimeMillis();

            CharSequence thedate= DateUtils.getRelativeTimeSpanString(your_time_in_milliseconds, current_time_in_millisecinds, DateUtils.MINUTE_IN_MILLIS);
            holder.textViewDate.setText(thedate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return noticeGetterList.size();
    }

}
