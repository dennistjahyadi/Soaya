package com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.R;

import java.util.List;

/**
 * Created by Denn on 10/2/2016.
 */
public class PenjadwalanAdapter extends RecyclerView.Adapter<PenjadwalanAdapter.MyViewHolder> {
    List<PenjadwalanGetter> penjadwalanGetterList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEvent, tvTanggal, tvOrganisasi;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvEvent = (TextView) itemView.findViewById(R.id.textViewEvent);
            tvTanggal = (TextView) itemView.findViewById(R.id.textViewTanggal);
            tvOrganisasi = (TextView) itemView.findViewById(R.id.textViewOrganisasi);
        }
    }

    public PenjadwalanAdapter(List<PenjadwalanGetter> penjadwalanGetterList) {
        this.penjadwalanGetterList = penjadwalanGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.penjadwalanlist_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PenjadwalanGetter penjadwalanGetter = penjadwalanGetterList.get(position);
        holder.tvTanggal.setVisibility(View.GONE);
        holder.tvEvent.setText(penjadwalanGetter.getEvent());
        holder.tvOrganisasi.setText(penjadwalanGetter.getOrganisasi());

    }

    @Override
    public int getItemCount() {
        return penjadwalanGetterList.size();
    }
}
