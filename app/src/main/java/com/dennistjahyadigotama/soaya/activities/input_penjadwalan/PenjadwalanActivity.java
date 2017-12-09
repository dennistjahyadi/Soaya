package com.dennistjahyadigotama.soaya.activities.input_penjadwalan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.TypeFaceManager;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter.PenjadwalanAdapter;
import com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter.PenjadwalanGetter;
import com.dennistjahyadigotama.soaya.activities.input_penjadwalan.adapter.PenjadwalanListAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Denn on 10/1/2016.
 */
public class PenjadwalanActivity extends AppCompatActivity {

    TextView tvTanggal, tvAdd, tvRefresh;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    public CaldroidFragment caldroidFragment;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<PenjadwalanGetter> penjadwalanGetterList = new ArrayList<>();
    PenjadwalanAdapter adapter;
    PenjadwalanListAdapter adapter2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penjadwalan_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Jadwal Kegiatan");
        requestQueue = Volley.newRequestQueue(this);
        progressBar = (ProgressBar) findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvRefresh = (TextView) findViewById(R.id.tvRefresh);
        tvAdd.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        tvRefresh.setTypeface(TypeFaceManager.getFontAwesomeTypeface(this));
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetCaldroid();
                SetAllCalendarListEvent();
                GetAllEvent();
            }
        });
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddJadwalActivity.class);
                startActivity(i);
            }
        });

        tvTanggal = (TextView) findViewById(R.id.textViewTanggal);
        SetCaldroid();

    }

    private void SetCaldroid() {
        caldroidFragment = new CaldroidFragment();

        Bundle args = new Bundle();
        final Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        CaldroidListener caldroidListener = new CaldroidListener() {

            @Override
            public void onChangeMonth(int month, int year) {
                super.onChangeMonth(month, year);
                SetAllCalendarListEvent();

            }

            @Override
            public void onSelectDate(Date date, View view) {

                progressBar.setVisibility(View.VISIBLE);
                String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
                String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
                String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
                String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
                String day = (String) android.text.format.DateFormat.format("dd", date); //20
                tvTanggal.setText(dayOfTheWeek + ", " + day + " " + stringMonth + " " + year);

                GetEvent(year + "-" + intMonth + "-" + day);
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                super.onLongClickDate(date, view);


                final String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
                final String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
                final String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
                final String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
                final String day = (String) android.text.format.DateFormat.format("dd", date); //20

                AlertDialog.Builder builder = new AlertDialog.Builder(PenjadwalanActivity.this);
                builder.setTitle("Add Event to " + dayOfTheWeek + ", " + day + " " + stringMonth + " " + year);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.penjadwalan_register_dialog, null);
                builder.setView(dialogView);
                final EditText organisasi = (EditText) dialogView.findViewById(R.id.etOrganisasi);
                final EditText etEvent = (EditText) dialogView.findViewById(R.id.etEvent);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (organisasi.getText().toString().equals("") || organisasi.getText().toString().equals(" ") || organisasi.getText() == null
                                || etEvent.getText().toString().equals("") || etEvent.getText().toString().equals(" ") || etEvent.getText() == null) {
                            Toast.makeText(getApplicationContext(), "form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            String tanggal = year + "-" + intMonth + "-" + day;
                            //SendToServer(tanggal, organisasi.getText().toString(), dayOfTheWeek + ", " + day + " " + stringMonth + " " + year, etEvent.getText().toString());
                            SendToServer2(tanggal, tanggal, dayOfTheWeek + ", " + day + " " + stringMonth + " " + year, etEvent.getText().toString(), organisasi.getText().toString());

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }
        };

        caldroidFragment.setCaldroidListener(caldroidListener);


        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        SetAllCalendarListEvent();
    }

    private void SetAllCalendarListEvent() {
        adapter2 = new PenjadwalanListAdapter(penjadwalanGetterList, this);


        recyclerView.setAdapter(adapter2);

        int mon = caldroidFragment.getMonth();
        String mmm = null;
        if (mon < 10) {
            mmm = "0" + mon;
        } else {
            mmm = mon + "";
        }
        String yyyy = caldroidFragment.getYear() + "";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        try {
            Date date = simpleDateFormat.parse(mmm + "-" + yyyy);
            String stringMonth = (String) android.text.format.DateFormat.format("MMMM", date); //Jun
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            if (stringMonth.length() == 1) {
                if (mmm.equals("01")) {
                    stringMonth = "JANUARY";
                } else if (mmm.equals("02")) {
                    stringMonth = "FEBRUARY";

                } else if (mmm.equals("03")) {
                    stringMonth = "MARCH";

                } else if (mmm.equals("04")) {
                    stringMonth = "APRIL";

                } else if (mmm.equals("05")) {
                    stringMonth = "MAY";

                } else if (mmm.equals("06")) {
                    stringMonth = "JUNE";

                } else if (mmm.equals("07")) {
                    stringMonth = "JULY";

                } else if (mmm.equals("08")) {
                    stringMonth = "AUGUST";

                } else if (mmm.equals("09")) {
                    stringMonth = "SEPTEMBER";

                } else if (mmm.equals("10")) {
                    stringMonth = "OCTOBER";

                } else if (mmm.equals("11")) {
                    stringMonth = "NOVEMBER";

                } else if (mmm.equals("12")) {
                    stringMonth = "DECEMBER";

                }

            }

            tvTanggal.setText(stringMonth + " " + year);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        GetAllListEvent(mmm, yyyy);

    }

    private void GetAllListEvent(final String month, final String year) {
        progressBar.setVisibility(View.VISIBLE);

        penjadwalanGetterList.clear();

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.penjadwalanUrl + "?GetAllEvent2=aaa", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        date = simpleDateFormat.parse(jsonObject.getString("tanggal1"));
                        String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
                        String year2 = (String) android.text.format.DateFormat.format("yyyy", date); //2013

                        if (month.equals(intMonth) && year.equals(year2)) {
                            PenjadwalanGetter penjadwalanGetter = new PenjadwalanGetter();
                            penjadwalanGetter.setId(jsonObject.getString("id"));
                            penjadwalanGetter.setTanggal1(jsonObject.getString("tanggal1"));
                            penjadwalanGetter.setTanggal2(jsonObject.getString("tanggal2"));
                            penjadwalanGetter.setEvent(jsonObject.getString("event"));
                            penjadwalanGetter.setOrganisasi(jsonObject.getString("organisasi"));

                            penjadwalanGetterList.add(penjadwalanGetter);
                        }
                    }

                    Collections.sort(penjadwalanGetterList, new Comparator<PenjadwalanGetter>() {
                        @Override
                        public int compare(PenjadwalanGetter penjadwalanGetter, PenjadwalanGetter t1) {
                            return penjadwalanGetter.getTanggal1().compareTo(t1.getTanggal1());
                        }
                    });

                    adapter2.notifyDataSetChanged();


                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }

        );


        requestQueue.add(jsonArrayRequest);

    }

    private void SendToServer(final String tanggal, final String organisasi, final String textDateIndicator, final String event, final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.penjadwalanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Date date;

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        date = simpleDateFormat.parse(tanggal);
                        caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(Color.YELLOW), date);
                        tvTanggal.setText(textDateIndicator);
                        caldroidFragment.refreshView();
                        penjadwalanGetterList.clear();
                        GetEvent(tanggal);
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
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
                map.put("tanggal", tanggal);
                map.put("organisasi", organisasi);
                map.put("event", event);
                map.put("id", id);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void SendToServer2(final String tanggal1, final String tanggal2, final String textDateIndicator, final String event, final String organisasi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.penjadwalanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {

                        SendToServer(tanggal1, organisasi, textDateIndicator, event, jsonObject.getString("success"));
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();

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
                map.put("tanggal1", tanggal1);
                map.put("tanggal2", tanggal2);
                map.put("event", event);
                map.put("organisasi", organisasi);
                map.put("user", User.username);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void GetEvent(String date) {
        penjadwalanGetterList.clear();
        adapter = new PenjadwalanAdapter(penjadwalanGetterList);
        recyclerView.setAdapter(adapter);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.penjadwalanUrl + "?GetEvent=" + date, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    penjadwalanGetterList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        PenjadwalanGetter penjadwalanGetter = new PenjadwalanGetter();
                        penjadwalanGetter.setEvent(jsonObject.getString("event"));
                        penjadwalanGetter.setId(jsonObject.getString("id"));
                        penjadwalanGetter.setOrganisasi(jsonObject.getString("organisasi"));

                        penjadwalanGetterList.add(penjadwalanGetter);
                    }

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });


        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetCaldroid();
        try {
            Thread.sleep(500);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SetAllCalendarListEvent();
        GetAllEvent();
    }

    private void GetAllEvent() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.penjadwalanUrl + "?GetAllEvent=aaa", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    Map<Date, Drawable> map = new HashMap<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        date = simpleDateFormat.parse(jsonObject.getString("tanggal"));
                        map.put(date, new ColorDrawable(Color.YELLOW));
                    }
                    caldroidFragment.setBackgroundDrawableForDates(map);
                    caldroidFragment.refreshView();
                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (ParseException e) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
