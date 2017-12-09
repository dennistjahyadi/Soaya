package com.dennistjahyadigotama.soaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denn on 4/10/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText etName,etUsername,etEmail,etFakultas;
    Button buttonRegister;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String url = User.registerActivityUrl;
    String email;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        requestQueue = Volley.newRequestQueue(this);
        email = getIntent().getStringExtra("email");
        etName = (EditText)findViewById(R.id.editTextName);
        etUsername = (EditText)findViewById(R.id.editTextUserName);
        etFakultas = (EditText)findViewById(R.id.editTextFakultas);
        etFakultas.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etEmail.setText(email);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);

        etFakultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                PopupMenu popup = new PopupMenu(RegisterActivity.this, etFakultas);
                popup.getMenuInflater().inflate(R.menu.popupjurusan, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        etFakultas.setText(item.getTitle());
                        return false;
                    }
                });

                popup.show();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().length()>28)
                {
                    Toast.makeText(getApplicationContext(),"username maximum 28 character", Toast.LENGTH_SHORT).show();
                }
                else {
                    stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
                                    sharedPreferences.edit().putString(QuickstartPreferences.EMAIL,email).apply();
                                    sharedPreferences.edit().putBoolean(QuickstartPreferences.ALLOW_LOGIN,true).apply();

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    finish();


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
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("name", etName.getText().toString());
                            hashMap.put("username", etUsername.getText().toString().toLowerCase());
                            hashMap.put("fakultas", etFakultas.getText().toString());
                            hashMap.put("email", etEmail.getText().toString().toLowerCase());

                            return hashMap;
                        }
                    };


                    requestQueue.add(stringRequest);
                }



            }
        });
    }
}

