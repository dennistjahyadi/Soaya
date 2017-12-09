package com.dennistjahyadigotama.soaya.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.LoginActivity;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.SQLite.DBHelper;
import com.dennistjahyadigotama.soaya.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Denn on 8/1/2016.
 */
public class ProfileOptionsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    RequestQueue requestQueue;
    Toolbar toolbar;
    EditText etUsername,etName,etEmail,etFakultas;
    TextView tvLogout,tvSave;
    SharedPreferences sharedPreferences;
    String url= User.profileOptionsActivityUrl;
    Switch aSwitch,couponSwitch;
    String name,email,fakultas,username;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_option_activity);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        name = sharedPreferences.getString(QuickstartPreferences.NAME,null);
        email = sharedPreferences.getString(QuickstartPreferences.EMAIL,null);
        fakultas = sharedPreferences.getString(QuickstartPreferences.FAKULTAS,null);
        username = sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        boolean a = sharedPreferences.getBoolean(QuickstartPreferences.Notification,true);
        boolean a1 = sharedPreferences.getBoolean(QuickstartPreferences.CouponNotification,true);

        aSwitch = (Switch)findViewById(R.id.switchNotification);
        couponSwitch = (Switch)findViewById(R.id.switchCouponNotification);
        aSwitch.setChecked(a);
        couponSwitch.setChecked(a1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    sharedPreferences.edit().putBoolean(QuickstartPreferences.Notification,true).apply();
                    Toast.makeText(getApplicationContext(),"notification on",Toast.LENGTH_SHORT).show();
                }else
                {
                    sharedPreferences.edit().putBoolean(QuickstartPreferences.Notification,false).apply();
                    Toast.makeText(getApplicationContext(),"notification off",Toast.LENGTH_SHORT).show();
                }
            }
        });
        couponSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    sharedPreferences.edit().putBoolean(QuickstartPreferences.CouponNotification,true).apply();
                    Toast.makeText(getApplicationContext(),"coupon notification on",Toast.LENGTH_SHORT).show();
                }else
                {
                    sharedPreferences.edit().putBoolean(QuickstartPreferences.CouponNotification,false).apply();
                    Toast.makeText(getApplicationContext(),"coupon notification off",Toast.LENGTH_SHORT).show();
                }
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");
        tvSave = (TextView)findViewById(R.id.textViewSave);
        tvSave.setVisibility(View.GONE);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setEnabled(false);
                etEmail.setEnabled(false);
                UpdateProfile();

            }
        });
        etUsername = (EditText)findViewById(R.id.editTextUsername);
        etName = (EditText)findViewById(R.id.editTextName);
        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etFakultas = (EditText)findViewById(R.id.editTextFakultas);
        SetUserData();

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSave.setVisibility(View.VISIBLE);
            }
        });

        tvLogout = (TextView)findViewById(R.id.textViewLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileOptionsActivity.this);
                alertDialog.setMessage("Are you sure, you want to logout?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();

                        DBHelper db = new DBHelper(getApplicationContext());
                        db.DeleteAllRoom();
                        db.DeleteAllText();

                        sharedPreferences.edit().clear().apply();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

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
    }

    private void SetUserData()
    {
        etName.setText(name);
        etUsername.setText(username);
        etEmail.setText(email);
        etFakultas.setText(fakultas);
    }

    private void UpdateProfile()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        sharedPreferences.edit().putString(QuickstartPreferences.NAME,etName.getText().toString()).apply();
                        sharedPreferences.edit().putString(QuickstartPreferences.EMAIL,etEmail.getText().toString()).apply();
                        tvSave.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                        etName.setEnabled(true);

                    }else
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error"), Toast.LENGTH_SHORT).show();

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
                map.put("username",username);
                map.put("name",etName.getText().toString());
                map.put("email",etEmail.getText().toString());
                return map;
            }
        };
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
