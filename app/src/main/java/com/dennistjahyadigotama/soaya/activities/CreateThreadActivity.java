package com.dennistjahyadigotama.soaya.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.CreateThreadAdapter.ImagesGetter;
import com.dennistjahyadigotama.soaya.activities.CreateThreadAdapter.ImagesThreadAdapter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Denn on 6/30/2016.
 */
public class CreateThreadActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText etTitle,etCategory,etContent;
    RecyclerView recyclerView;
    TextView buttonCreate,insertImage;
    String category="";
    RequestQueue requestQueue;
    static List<ImagesGetter> imagesGetterList = new ArrayList<>();
    ImagesThreadAdapter adapter;
    private Uri mCropImageUri;
    RelativeLayout loadingPanel;
    String url = User.createThreadActivityUrl;
    int orderPicForUpload = 0;
    String threadId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_thread_activity);
        requestQueue = Volley.newRequestQueue(this);
        imagesGetterList.clear();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Thread");
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        etTitle = (EditText)findViewById(R.id.editTextTitle);
        etContent = (EditText)findViewById(R.id.editTextContent);
        etCategory = (EditText)findViewById(R.id.editTextCategory);
        etCategory.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        buttonCreate = (TextView) findViewById(R.id.buttonCreate);

        adapter = new ImagesThreadAdapter(imagesGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        insertImage = (TextView)findViewById(R.id.insertImage);
        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    CropImage.startPickImageActivity(CreateThreadActivity.this);

            }
        });

        etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),SelectCategoryActivity.class);
                startActivityForResult(i,121);
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPanel.setVisibility(View.VISIBLE);
                buttonCreate.setEnabled(false);
                etCategory.setEnabled(false);
                etContent.setEnabled(false);
                etTitle.setEnabled(false);
                insertImage.setEnabled(false);
                UploadContent();

            }
        });
    }


    private void UploadContent(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        if (imagesGetterList.size() > 0) {
                            threadId = jsonObject.getString("success");

                            new UploadToServer().execute();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"create success", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    else
                    {
                    Toast.makeText(getApplicationContext(),jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        loadingPanel.setVisibility(View.GONE);
                        buttonCreate.setEnabled(true);
                        etCategory.setEnabled(true);
                        etContent.setEnabled(true);
                        etTitle.setEnabled(true);
                        insertImage.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingPanel.setVisibility(View.GONE);
                    buttonCreate.setEnabled(true);
                    etCategory.setEnabled(true);
                    etContent.setEnabled(true);
                    etTitle.setEnabled(true);
                    insertImage.setEnabled(true);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPanel.setVisibility(View.GONE);
                buttonCreate.setEnabled(true);
                etCategory.setEnabled(true);
                etContent.setEnabled(true);
                etTitle.setEnabled(true);
                insertImage.setEnabled(true);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("username", User.username);
                hashMap.put("title",etTitle.getText().toString());
                hashMap.put("category",category);
                hashMap.put("content",etContent.getText().toString());

                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }


    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {


                SetImages(imageUri);
            }
        }else if(requestCode==121)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                etCategory.setText(data.getStringExtra("category"));
                category=data.getStringExtra("category");
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                SetImages(mCropImageUri);

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SetImages(Uri uri){
        ImagesGetter imagesGetter = new ImagesGetter();
        imagesGetter.setCaption("");
        imagesGetter.setUri(uri);
        imagesGetterList.add(imagesGetter);
       adapter.notifyDataSetChanged();

    }




    private class UploadToServer extends AsyncTask<Void,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {


        }

        @Override
        protected String doInBackground(Void... params) {
            return UploadFile();
        }

        private String UploadFile(){
            final String[] responseString = {null};

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        responseString[0] = jsonObject.getString("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    responseString[0] =error.getMessage();
                }
            }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("username", User.username);
                    hashMap.put("threadId",threadId);
                    hashMap.put("encoded",imagesGetterList.get(orderPicForUpload).getEncoded());
                    String caption;
                    if(imagesGetterList.get(orderPicForUpload).getCaption()==null)
                    {
                         caption = "";
                    }else
                    {
                        caption = imagesGetterList.get(orderPicForUpload).getCaption();
                    }
                    hashMap.put("caption",caption);

                    return hashMap;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    (int) TimeUnit.SECONDS.toMillis(20),
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);


            return responseString[0];
        }

        @Override
        protected void onPostExecute(String s)
        {

            //Toast.makeText(getApplicationContext(),"upload images "+orderPicForUpload+" Success",Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    orderPicForUpload +=1;
                    if(orderPicForUpload==imagesGetterList.size())
                    {
                        Toast.makeText(getApplicationContext(),"create success", Toast.LENGTH_SHORT).show();
                        orderPicForUpload = 0;
                        finish();

                    }else
                    {
                        new UploadToServer().execute();
                    }
                }
            }, 1000);

            super.onPostExecute(s);
        }
    }
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

