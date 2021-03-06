package com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.input_penjadwalan.PenjadwalanActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Denn on 10/2/2016.
 */
public class PenjadwalanListAdapter extends RecyclerView.Adapter<PenjadwalanListAdapter.MyViewHolder> {
    List<PenjadwalanGetter> penjadwalanGetterList;
    PenjadwalanActivity penjadwalanActivity;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEvent, tvTanggal, tvOrganisasi;
        LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvEvent = (TextView) itemView.findViewById(R.id.textViewEvent);
            tvTanggal = (TextView) itemView.findViewById(R.id.textViewTanggal);
            tvOrganisasi = (TextView) itemView.findViewById(R.id.textViewOrganisasi);
            lin = (LinearLayout) itemView.findViewById(R.id.lin);
        }
    }

    public PenjadwalanListAdapter(List<PenjadwalanGetter> penjadwalanGetterList, PenjadwalanActivity penjadwalanActivity) {
        this.penjadwalanGetterList = penjadwalanGetterList;
        this.penjadwalanActivity = penjadwalanActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.penjadwalanlist_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final PenjadwalanGetter penjadwalanGetter = penjadwalanGetterList.get(position);
        holder.tvEvent.setText(penjadwalanGetter.getEvent());
        holder.tvOrganisasi.setText(penjadwalanGetter.getOrganisasi());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
        try {
            Date date1 = simpleDateFormat.parse(penjadwalanGetter.getTanggal1());
            Date date2 = simpleDateFormat.parse(penjadwalanGetter.getTanggal2());
            final String dayOfTheWeek1 = (String) android.text.format.DateFormat.format("EEEE", date1);//Thursday
            final String stringMonth1 = (String) android.text.format.DateFormat.format("MMM", date1); //Jun
            final String intMonth1 = (String) android.text.format.DateFormat.format("MM", date1); //06
            final String year1 = (String) android.text.format.DateFormat.format("yyyy", date1); //2013
            final String day1 = (String) android.text.format.DateFormat.format("dd", date1);
            final String dayOfTheWeek2 = (String) android.text.format.DateFormat.format("EEEE", date2);//Thursday
            final String stringMonth2 = (String) android.text.format.DateFormat.format("MMM", date2); //Jun
            final String intMonth2 = (String) android.text.format.DateFormat.format("MM", date2); //06
            final String year2 = (String) android.text.format.DateFormat.format("yyyy", date2); //2013
            final String day2 = (String) android.text.format.DateFormat.format("dd", date2);
            if (penjadwalanGetter.getTanggal1().equals(penjadwalanGetter.getTanggal2())) {
                holder.tvTanggal.setText(day1 + " " + stringMonth1 + " " + year1);
            } else {
                holder.tvTanggal.setText(day1 + " " + stringMonth1 + " " + year1 + " s.d. " + day2 + " " + stringMonth2 + " " + year2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date1;
                try {
                    date1 = simpleDateFormat.parse(penjadwalanGetter.getTanggal1());
                    penjadwalanActivity.caldroidFragment.moveToDate(date1);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return penjadwalanGetterList.size();
    }
}
