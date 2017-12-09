package com.dennistjahyadigotama.soaya.activities.input_penjadwalan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denn on 10/2/2016.
 */
public class AddJadwalActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    static EditText etTanggalAwal, etTanggalAkhir, etKegiatan, etOrganisasi;
    TextView tvSave;
    Toolbar toolbar;
    RelativeLayout rev;
    private String fk_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_jadwal_activity);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Event");
        tvSave = (TextView) findViewById(R.id.textViewCreate);
        rev = (RelativeLayout) findViewById(R.id.loadingPanel);
        etTanggalAwal = (EditText) findViewById(R.id.etTanggalAwal);
        etTanggalAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerAwal");

            }
        });
        etTanggalAkhir = (EditText) findViewById(R.id.etTanggalAkhir);
        etTanggalAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerAkhir");

            }
        });
        etKegiatan = (EditText) findViewById(R.id.etKegiatan);
        etOrganisasi = (EditText) findViewById(R.id.etOrganisasi);

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSave.setEnabled(false);
                rev.setVisibility(View.VISIBLE);


                if (!etTanggalAwal.getText().toString().equals("") && !etTanggalAkhir.getText().toString().equals("") && !etKegiatan.getText().toString().equals("")
                        && !etOrganisasi.equals("")) {
                    rev.setVisibility(View.VISIBLE);
                    tvSave.setEnabled(false);
                    etTanggalAwal.setEnabled(false);
                    etTanggalAkhir.setEnabled(false);
                    etKegiatan.setEnabled(false);
                    etOrganisasi.setEnabled(false);
                    Process();

                } else {
                    tvSave.setEnabled(true);
                    rev.setVisibility(View.GONE);

                    Toast.makeText(AddJadwalActivity.this, "form harus diisi semua", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Process() {
        Date date1, date2;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date1 = simpleDateFormat.parse(etTanggalAwal.getText().toString());
            date2 = simpleDateFormat.parse(etTanggalAkhir.getText().toString());

            if (date1.after(date2) || date1.equals(date2)) {
                Toast.makeText(getApplicationContext(), "tanggal akhir harus setelah tanggal awal", Toast.LENGTH_SHORT).show();
                tvSave.setEnabled(true);
                etTanggalAwal.setEnabled(true);
                etTanggalAkhir.setEnabled(true);
                etKegiatan.setEnabled(true);
                rev.setVisibility(View.GONE);
                etOrganisasi.setEnabled(true);

                return;
            }
            SendToServer2(etTanggalAwal.getText().toString(), etTanggalAkhir.getText().toString(), etKegiatan.getText().toString(), etOrganisasi.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void Process2() {
        Date date1, date2;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = simpleDateFormat.parse(etTanggalAwal.getText().toString());
            date2 = simpleDateFormat.parse(etTanggalAkhir.getText().toString());
            for (Date date = date1; date.before(date2); ) {
                String aaa = simpleDateFormat.format(date);
                SendToServer(aaa, etKegiatan.getText().toString(), etOrganisasi.getText().toString());

                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
                Thread.sleep(300);
            }
            String bbb = simpleDateFormat.format(date2);
            SendToServer(bbb, etKegiatan.getText().toString(), etOrganisasi.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void test() {
        Date date1, date2;
        Calendar c = Calendar.getInstance();

        String tanggal1 = "2016-09-01";
        String tanggal2 = "2016-09-05";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = simpleDateFormat.parse(tanggal1);
            date2 = simpleDateFormat.parse(tanggal2);
            for (Date date = date1; date.before(date2); ) {
                Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_SHORT).show();
                c.setTime(date);
                c.add(Calendar.DATE, 1);
                date = c.getTime();
            }
            Toast.makeText(getApplicationContext(), date2.toString(), Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String mm;
            String dd;
            if ((month + 1) < 10) {
                mm = "0" + (month + 1);
            } else {
                mm = (month + 1) + "";
            }
            if (day < 10) {
                dd = "0" + day;
            } else {
                dd = day + "";
            }
            if (getTag().equals("datePickerAwal")) {
                etTanggalAwal.setText(year + "-" + mm + "-" + dd);
            } else {
                etTanggalAkhir.setText(year + "-" + mm + "-" + dd);
            }
        }
    }

    private void SendToServer(final String tanggal, final String event, final String organisasi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.penjadwalanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {

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
                map.put("tanggal", tanggal);
                map.put("event", event);
                map.put("organisasi", organisasi);
                map.put("id", fk_id);

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void SendToServer2(final String tanggal1, final String tanggal2, final String event, final String organisasi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.penjadwalanUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Date date;

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {

                        fk_id = jsonObject.getString("success");
                        Process2();
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
