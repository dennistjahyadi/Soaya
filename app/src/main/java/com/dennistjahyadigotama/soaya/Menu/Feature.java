package com.dennistjahyadigotama.soaya.Menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dennistjahyadigotama.soaya.Menu.adapter.FeatureAdapter;
import com.dennistjahyadigotama.soaya.Menu.adapter.FeatureGetter;
import com.dennistjahyadigotama.soaya.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Denn on 6/20/2016.
 */
public class Feature extends Fragment {

    RecyclerView recyclerView;
    FeatureAdapter adapter;
    List<FeatureGetter> featureGetterList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new FeatureAdapter(featureGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        SetFeature();
        return view;
    }

    private void SetFeature() {

        FeatureGetter featureGetter = new FeatureGetter();
        featureGetter.setNama("My Ubaya");
        featureGetter.setPhoto(R.drawable.ubaya);
        featureGetter.setUrl("https://my.ubaya.ac.id/");
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Kalender Ubaya");
        featureGetter.setPhoto(R.drawable.calendar);
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Warta Ubaya");
        featureGetter.setPhoto(R.drawable.warta);
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("ULS Ubaya");
        featureGetter.setPhoto(R.drawable.uls);
        featureGetter.setUrl("http://uls.ubaya.ac.id/uls/");
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Elib (Perpustakaan)");
        featureGetter.setPhoto(R.drawable.library);
        featureGetter.setUrl("http://elib.ubaya.ac.id/");
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Gooaya");
        featureGetter.setPhoto(R.drawable.gooaya);
        featureGetter.setUrl("https://gooaya.ubaya.ac.id/index.php");
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Lowongan Pekerjaan (CAC)");
        featureGetter.setPhoto(R.drawable.career);
        featureGetter.setUrl("http://career.ubaya.ac.id/index.php");
        featureGetterList.add(featureGetter);

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Kupon");
        featureGetter.setPhoto(R.drawable.coupon);
        featureGetterList.add(featureGetter);
        adapter.notifyDataSetChanged();

        featureGetter = new FeatureGetter();
        featureGetter.setNama("Input Penjadwalan FT");
        featureGetter.setPhoto(R.drawable.ic_add_black_24dp);
        featureGetterList.add(featureGetter);
        adapter.notifyDataSetChanged();
    }


}