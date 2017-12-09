package com.dennistjahyadigotama.soaya.activities.KuponActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Denn on 8/28/2016.
 */
public class KuponActivity extends AppCompatActivity {

    RelativeLayout rev;
    TextView tvTitle,tvExp,tvDesc,tvGetCoupon;
    RequestQueue requestQueue;
    ImageView ivPhoto;
    Toolbar toolbar;
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kupon_activity);
        id = getIntent().getStringExtra("id");
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kupon");
        rev=(RelativeLayout)findViewById(R.id.loadingPanel);
        tvTitle = (TextView)findViewById(R.id.textViewTitleCoupon);
        tvExp = (TextView)findViewById(R.id.textViewExpDate);
        tvDesc = (TextView)findViewById(R.id.textViewDescription);
        tvGetCoupon = (TextView)findViewById(R.id.textViewGetCoupon);
        tvGetCoupon.setEnabled(false);
        ivPhoto = (ImageView)findViewById(R.id.imageViewPhoto);
        SetGone();
        GetData();
    }
    private void SetGone(){

        tvTitle.setVisibility(View.GONE);
        tvExp.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        tvGetCoupon.setVisibility(View.GONE);
        ivPhoto.setVisibility(View.GONE);
    }


    private void SetVisible()
    {
        tvTitle.setVisibility(View.VISIBLE);
        tvExp.setVisibility(View.VISIBLE);
        tvDesc.setVisibility(View.VISIBLE);
        tvGetCoupon.setVisibility(View.VISIBLE);
        ivPhoto.setVisibility(View.VISIBLE);
    }
    private void GetData()
    {
         JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.kuponListActivityUrl+"?GetTheCoupon="+id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        tvTitle.setText(jsonObject.getString("title"));
                        String expDate = jsonObject.getString("expDate");
                        tvExp.setText("Expired: "+expDate.substring(0,expDate.length()-3));
                        Picasso.with(getApplicationContext()).load(jsonObject.getString("photoUrl")).into(ivPhoto);
                        tvDesc.setText(jsonObject.getString("description"));

                    }
                    CheckSudahDigunakan();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void CheckSudahDigunakan()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.kuponListActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        int jumlah = Integer.parseInt(jsonObject.getString("success"));
                        if(jumlah>0)
                        {
                            tvGetCoupon.setEnabled(false);
                            tvGetCoupon.setText("Kupon Sudah Digunakan");
                            tvGetCoupon.setBackgroundColor(Color.GRAY);
                            rev.setVisibility(View.GONE);
                            SetVisible();
                        }else
                        {
                            CheckExpiredCoupon();
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("CheckSudahDigunakan",id);
                map.put("username",User.username);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CheckExpiredCoupon()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.kuponListActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        tvGetCoupon.setEnabled(true);
                        tvGetCoupon.setBackgroundColor(Color.parseColor("#4CAF50"));
                        tvGetCoupon.setText("Dapatkan Kupon");
                        tvGetCoupon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(KuponActivity.this);
                                alertDialog.setMessage("Jika anda mengklik tombol ini kupon akan kadaluarsa, anda yakin?");
                                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        rev.setVisibility(View.VISIBLE);
                                        UseTheCoupon();
                                    }
                                });

                                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog alertDialog2 = alertDialog.create();
                                alertDialog2.show();

                            }
                        });
                    }else
                    {
                        tvGetCoupon.setEnabled(false);
                        tvGetCoupon.setBackgroundColor(Color.GRAY);
                        tvGetCoupon.setText("Expired");
                    }
                    rev.setVisibility(View.GONE);
                    SetVisible();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void UseTheCoupon()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.kuponListActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(KuponActivity.this);
                        alertDialog.setMessage("Kupon berhasil digunakan");
                        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                        AlertDialog alertDialog2 = alertDialog.create();
                        alertDialog2.show();
                        tvGetCoupon.setEnabled(false);
                        tvGetCoupon.setText("Kupon Sudah Digunakan");
                        tvGetCoupon.setBackgroundColor(Color.GRAY);
                    }else
                    {
                        tvGetCoupon.setEnabled(false);
                        tvGetCoupon.setBackgroundColor(Color.GRAY);
                        tvGetCoupon.setText("Expired");
                        rev.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"maaf kupon sudah kadaluarsa",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("UseCoupon",id);
                map.put("username",User.username);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
