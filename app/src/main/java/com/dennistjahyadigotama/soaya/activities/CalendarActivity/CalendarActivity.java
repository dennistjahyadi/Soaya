package com.dennistjahyadigotama.soaya.activities.CalendarActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CalendarActivity.adapter.CalendarListAdapter;
import com.dennistjahyadigotama.soaya.activities.CalendarActivity.adapter.CalenderAdapter;
import com.dennistjahyadigotama.soaya.activities.CalendarActivity.adapter.CalenderGetter;
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
 * Created by Denn on 8/30/2016.
 */
public class CalendarActivity extends AppCompatActivity {

    TextView tvTanggal, tvCategory;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    public CaldroidFragment caldroidFragment;
    Toolbar toolbar;
    ImageView ivAddEvent, ivShowAll;
    RecyclerView recyclerView;
    CalenderAdapter adapter;
    CalendarListAdapter adapter2;
    List<CalenderGetter> calenderGetterList = new ArrayList<>();
    String color = " ";
    String categoryyy = "Akademik";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kalender Ubaya");
        requestQueue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        User.username = sharedPreferences.getString(QuickstartPreferences.USERNAME, null);
        progressBar = (ProgressBar) findViewById(R.id.loadingPanel);
        progressBar.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        tvCategory = (TextView) findViewById(R.id.textViewCategory);
        tvCategory.setText(categoryyy);
        tvCategory.setVisibility(View.VISIBLE);

        ivShowAll = (ImageView) findViewById(R.id.imageViewShowAllEvent);
        ivShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                // SetAllCalendarListEvent();
                PopupMenu popup = new PopupMenu(CalendarActivity.this, ivShowAll);
                popup.getMenuInflater().inflate(R.menu.popupcalendarcategory, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.akademik:
                                tvCategory.setText(item.getTitle());
                                categoryyy = item.getTitle().toString();
                                GetAllEvent();
                                SetAllCalendarListEvent();
                                tvCategory.setVisibility(View.VISIBLE);
                                break;
                            case R.id.nonAkademik:
                                tvCategory.setText(item.getTitle());
                                categoryyy = item.getTitle().toString();
                                GetAllEvent();
                                SetAllCalendarListEvent();
                                tvCategory.setVisibility(View.VISIBLE);

                                break;
                            case R.id.event:
                                tvCategory.setText(item.getTitle());
                                categoryyy = item.getTitle().toString();
                                GetAllEvent();
                                SetAllCalendarListEvent();
                                tvCategory.setVisibility(View.VISIBLE);

                                break;
                            case R.id.showAll:
                                tvCategory.setText(" ");
                                categoryyy = " ";
                                GetAllEvent();
                                SetAllCalendarListEvent();
                                tvCategory.setVisibility(View.GONE);
                                break;

                        }

                        return false;
                    }
                });

                popup.show();

            }
        });
        ivAddEvent = (ImageView) findViewById(R.id.imageViewAddEvent);
        ivAddEvent.setVisibility(View.GONE);

       /* if(!User.username.equals("dailyubaya")&&!User.username.equals("dennistgt"))
        {
            ivAddEvent.setVisibility(View.GONE);

        }else
        {            ivAddEvent.setVisibility(View.VISIBLE);
        }*/
        if (!User.username.equals("dennistgt")) {
            ivAddEvent.setVisibility(View.GONE);

        } else {
            ivAddEvent.setVisibility(View.VISIBLE);
        }

        ivAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddEventActivity.class);
                startActivity(i);
            }
        });
        tvTanggal = (TextView) findViewById(R.id.textViewTanggal);
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
                tvCategory.setText(categoryyy);
                if (categoryyy.equals(" ")) {
                    tvCategory.setVisibility(View.GONE);

                } else {
                    tvCategory.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onSelectDate(Date date, View view) {

                tvCategory.setText("");
                tvCategory.setVisibility(View.GONE);

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

               /* if (!User.username.equals("dailyubaya") && !User.username.equals("dennistgt")) {
                    return;
                }*/
                if (!User.username.equals("dennistgt")) {
                    return;
                }

                final String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", date);//Thursday
                final String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
                final String intMonth = (String) android.text.format.DateFormat.format("MM", date); //06
                final String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
                final String day = (String) android.text.format.DateFormat.format("dd", date); //20

                color = " ";
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                builder.setTitle("Add Event to " + dayOfTheWeek + ", " + day + " " + stringMonth + " " + year);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.calendar_custom_dialog, null);
                builder.setView(dialogView);
                final EditText input = (EditText) dialogView.findViewById(R.id.etDescription);
                final EditText etCategory = (EditText) dialogView.findViewById(R.id.etCategory);
                etCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(CalendarActivity.this, etCategory);
                        popup.getMenuInflater().inflate(R.menu.popupcalendarcategory, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.akademik:
                                        etCategory.setText(item.getTitle());
                                        break;
                                    case R.id.nonAkademik:
                                        etCategory.setText(item.getTitle());
                                        break;
                                    case R.id.event:
                                        etCategory.setText(item.getTitle());
                                        break;

                                }

                                return false;
                            }
                        });

                        popup.show();
                    }
                });
                final EditText etColor = (EditText) dialogView.findViewById(R.id.etColor);
                etColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(CalendarActivity.this, etColor);
                        popup.getMenuInflater().inflate(R.menu.popupcalendarcolor, popup.getMenu());

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.merah:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.merah);
                                        break;
                                    case R.id.jingga:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.jingga);

                                        break;
                                    case R.id.kuning:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.kuning);

                                        break;
                                    case R.id.hijau:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.hijau);

                                        break;
                                    case R.id.biru:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.biru);

                                        break;
                                    case R.id.ungu:
                                        etColor.setText(item.getTitle());
                                        color = getResources().getString(R.string.ungu);

                                        break;

                                }

                                return false;
                            }
                        });

                        popup.show();
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (input.getText().toString().equals("") || input.getText().toString().equals(" ") || input.getText() == null || etCategory.getText().toString().equals("")
                                || color.equals(" ")) {
                            Toast.makeText(getApplicationContext(), "form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            String tanggal = year + "-" + intMonth + "-" + day;
                            SendToServer(tanggal, input.getText().toString(), dayOfTheWeek + ", " + day + " " + stringMonth + " " + year, etCategory.getText().toString(), color);

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
        adapter2 = new CalendarListAdapter(calenderGetterList, this);
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

        GetAllListEvent(mmm, yyyy, categoryyy);

    }

    private void GetAllListEvent(final String month, final String year, final String categoryyy) {
        calenderGetterList.clear();

        progressBar.setVisibility(View.VISIBLE);

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.calendarActivityUrl + "?GetAllEvent2=aaa", new Response.Listener<JSONArray>() {
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
                        String category = jsonObject.getString("category");

                        if (month.equals(intMonth) && year.equals(year2) && categoryyy.equals(" ")) {
                            CalenderGetter calenderGetter = new CalenderGetter();
                            calenderGetter.setId(jsonObject.getString("id"));
                            calenderGetter.setTanggal1(jsonObject.getString("tanggal1"));
                            calenderGetter.setTanggal2(jsonObject.getString("tanggal2"));
                            calenderGetter.setEvent(jsonObject.getString("event"));
                            calenderGetter.setCategory(jsonObject.getString("category"));
                            calenderGetter.setColor(jsonObject.getString("color"));
                            calenderGetterList.add(calenderGetter);
                        } else {
                            if (month.equals(intMonth) && year.equals(year2) && category.equals(categoryyy)) {
                                CalenderGetter calenderGetter = new CalenderGetter();
                                calenderGetter.setId(jsonObject.getString("id"));
                                calenderGetter.setTanggal1(jsonObject.getString("tanggal1"));
                                calenderGetter.setTanggal2(jsonObject.getString("tanggal2"));
                                calenderGetter.setEvent(jsonObject.getString("event"));
                                calenderGetter.setCategory(jsonObject.getString("category"));
                                calenderGetter.setColor(jsonObject.getString("color"));
                                calenderGetterList.add(calenderGetter);
                            }
                        }
                    }

                    Collections.sort(calenderGetterList, new Comparator<CalenderGetter>() {
                        @Override
                        public int compare(CalenderGetter calenderGetter, CalenderGetter t1) {
                            return calenderGetter.getTanggal1().compareTo(t1.getTanggal1());
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });


        requestQueue.add(jsonArrayRequest);

    }

    private void SendToServer(final String tanggal, final String event, final String textDateIndicator, final String category, final String color) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.calendarActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Date date;

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        date = simpleDateFormat.parse(tanggal);
                        caldroidFragment.setBackgroundDrawableForDate(new ColorDrawable(Color.parseColor(color)), date);
                        tvTanggal.setText(textDateIndicator);
                        caldroidFragment.refreshView();
                        SendToServer2(tanggal, tanggal, event, category);
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
                map.put("event", event);
                map.put("category", category);
                map.put("color", color);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void SendToServer2(final String tanggal1, final String tanggal2, final String event, final String category) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.calendarActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        calenderGetterList.clear();
                        GetEvent(tanggal2);
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
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
                map.put("category", category);
                map.put("color", color);
                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void GetEvent(String date) {
        tvCategory.setText("");
        tvCategory.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        calenderGetterList.clear();
        adapter = new CalenderAdapter(calenderGetterList);
        recyclerView.setAdapter(adapter);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.calendarActivityUrl + "?GetEvent=" + date, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        CalenderGetter calenderGetter = new CalenderGetter();
                        calenderGetter.setEvent(jsonObject.getString("event"));
                        calenderGetter.setId(jsonObject.getString("id"));
                        calenderGetterList.add(calenderGetter);
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
        GetAllEvent();
    }

    private void GetAllEvent() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.calendarActivityUrl + "?GetAllEvent=aaa", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date;
                    String color, category;
                    Map<Date, Drawable> map = new HashMap<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        date = simpleDateFormat.parse(jsonObject.getString("tanggal"));
                        color = jsonObject.getString("color");
                        category = jsonObject.getString("category");
                        if (categoryyy.equals(" ")) {
                            map.put(date, new ColorDrawable(Color.parseColor(color)));

                        } else {
                            if (category.equals(categoryyy)) {
                                map.put(date, new ColorDrawable(Color.parseColor(color)));
                            } else {
                                map.put(date, new ColorDrawable(Color.parseColor("#ffffff")));
                            }
                        }
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
