package com.dennistjahyadigotama.soaya.activities.CalendarActivity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;

import java.util.List;

/**
 * Created by Denn on 9/1/2016.
 */
public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.MyViewHolder> {
    List<CalenderGetter> calenderGetterList;
    Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEvent;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvEvent = (TextView)itemView.findViewById(R.id.textViewEvent);
        }
    }

    public CalenderAdapter(List<CalenderGetter> calenderGetters)
    {
        this.calenderGetterList = calenderGetters;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CalenderGetter calenderGetter = calenderGetterList.get(position);
        holder.tvEvent.setText(calenderGetter.getEvent());

    }

    @Override
    public int getItemCount() {
        return calenderGetterList.size();
    }
}
