package com.dennistjahyadigotama.soaya.Menu.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CalendarActivity.CalendarActivity;
import com.dennistjahyadigotama.soaya.activities.KuponActivity.KuponListActivity;
import com.dennistjahyadigotama.soaya.activities.WebsiteActivity.WebsiteActivity;
import com.dennistjahyadigotama.soaya.activities.input_penjadwalan.PenjadwalanActivity;
import com.dennistjahyadigotama.soaya.activities.warta_ubaya_activity.WartaUbayaListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Denn on 8/25/2016.
 */
public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.MyViewHolder> {

    List<FeatureGetter> featureGetterList;
    Context context;
    RequestQueue requestQueue;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvNama;
        LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            requestQueue = Volley.newRequestQueue(context);
            ivPhoto = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
            tvNama = (TextView) itemView.findViewById(R.id.textViewNama);
            lin = (LinearLayout) itemView.findViewById(R.id.linLayout);
        }


    }

    public FeatureAdapter(List<FeatureGetter> featureGetterList) {
        this.featureGetterList = featureGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeatureGetter featureGetter = featureGetterList.get(position);
        holder.ivPhoto.setImageResource(featureGetter.getPhoto());
        holder.tvNama.setText(featureGetter.getNama());
        SetOnClick(holder.lin, featureGetter);

    }

    @Override
    public int getItemCount() {
        return featureGetterList.size();
    }

    private void SetOnClick(LinearLayout lin, final FeatureGetter featureGetter) {

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                int click = sharedPreferences.getInt(QuickstartPreferences.Click, 0);
                click += 1;
                sharedPreferences.edit().putInt(QuickstartPreferences.Click, click).apply();

                if (featureGetter.getNama().equals("My Ubaya")) {
                    Intent i = new Intent(context, WebsiteActivity.class);
                    i.putExtra("url", featureGetter.getUrl());
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Kupon")) {
                    Intent i = new Intent(context, KuponListActivity.class);
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("ULS Ubaya")) {
                    Intent i = new Intent(context, WebsiteActivity.class);
                    i.putExtra("url", featureGetter.getUrl());
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Elib (Perpustakaan)")) {
                    Intent i = new Intent(context, WebsiteActivity.class);
                    i.putExtra("url", featureGetter.getUrl());
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Gooaya")) {
                    Intent i = new Intent(context, WebsiteActivity.class);
                    i.putExtra("url", featureGetter.getUrl());
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Lowongan Pekerjaan (CAC)")) {
                    Intent i = new Intent(context, WebsiteActivity.class);
                    i.putExtra("url", featureGetter.getUrl());
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Kalender Ubaya")) {
                    Intent i = new Intent(context, CalendarActivity.class);
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Warta Ubaya")) {
                    Intent i = new Intent(context, WartaUbayaListActivity.class);
                    context.startActivity(i);
                } else if (featureGetter.getNama().equals("Input Penjadwalan FT")) {
                    //   Intent i = new Intent(context, WartaUbayaListActivity.class);
                    //  context.startActivity(i);
                    showDialog();
                }


            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_login_penjadwalan);
        // Set dialog title
        dialog.setTitle("Select Date");
        final EditText etPass = (EditText) dialog.findViewById(R.id.etPass);

        final Button bOk = (Button) dialog.findViewById(R.id.bOk);
        final Button bCancel = (Button) dialog.findViewById(R.id.bCancel);


        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penjadwalanPermission(dialog, etPass.getText().toString());

            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    private void penjadwalanPermission(final Dialog dialog, final String pass) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.penjadwalanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        if (jsonObject.getString("success").equals("1")) {

                            Intent i = new Intent(context, PenjadwalanActivity.class);
                            context.startActivity(i);
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("pass", pass);

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

}
