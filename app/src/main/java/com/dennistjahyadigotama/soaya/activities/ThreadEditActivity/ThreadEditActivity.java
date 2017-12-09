package com.dennistjahyadigotama.soaya.activities.ThreadEditActivity;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.dennistjahyadigotama.soaya.activities.SelectCategoryActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadEditActivity.adapter.ImageGetter;
import com.dennistjahyadigotama.soaya.activities.ThreadEditActivity.adapter.RecyclerImageAdapter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Denn on 7/4/2016.
 */
public class ThreadEditActivity extends AppCompatActivity {

    TextView buttonSave,insertImage;
    EditText etTitle,etCategory,etContent;
    RecyclerView recyclerViewImages;
    Toolbar toolbar;
    RequestQueue requestQueue;
    RecyclerImageAdapter adapter;
    List<ImageGetter> imagesGetterList = new ArrayList<>();
    String category;
    String url= User.threadEditActivityUrl;
    String threadId;
    private ProgressBar progressBar;
    private TextView txtPercentage;
    private Uri mCropImageUri;
    int orderPicForUpload = 0;
    RelativeLayout loadingPanel;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_edit_activity);
        threadId=getIntent().getStringExtra("threadId");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit");
        SetAllView();
        etTitle.setText(getIntent().getStringExtra("title"));
        etCategory.setText(getIntent().getStringExtra("category"));
        category = etCategory.getText().toString();
        etContent.setText(getIntent().getStringExtra("content"));
        GetThreadImagesForUpdate();
    }



    private void SetAllView()
    {
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        txtPercentage = (TextView)findViewById(R.id.txtPercentage);
        buttonSave = (TextView)findViewById(R.id.buttonSave);
        loadingPanel = (RelativeLayout)findViewById(R.id.loadingPanel);
        loadingPanel.setVisibility(View.GONE);

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
        recyclerViewImages = (RecyclerView)findViewById(R.id.recycler_view);
        adapter = new RecyclerImageAdapter(imagesGetterList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setAdapter(adapter);
        recyclerViewImages.setNestedScrollingEnabled(false);
        insertImage = (TextView)findViewById(R.id.insertImage);
        insertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.startPickImageActivity(ThreadEditActivity.this);


            }
        });

        etCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),SelectCategoryActivity.class);
                startActivityForResult(i,121);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPanel.setVisibility(View.VISIBLE);
                buttonSave.setEnabled(false);
                etCategory.setEnabled(false);
                etContent.setEnabled(false);
                etTitle.setEnabled(false);
                insertImage.setEnabled(false);
                UploadContent();
            }
        });
    }

    private void GetThreadImagesForUpdate()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?GetImagesForUpdate="+threadId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String id,url;

                try {
                for(int i=0;i<response.length();i++){
                    final JSONObject jsonObject = response.getJSONObject(i);
                    final ImageGetter imageGetter = new ImageGetter();
                    id=jsonObject.getString("id");
                    url=jsonObject.getString("url");
                    imageGetter.setId(id);
                    imageGetter.setImageUrl(url);
                    imageGetter.setCaption(jsonObject.getString("caption"));
                    imagesGetterList.add(imageGetter);
                    adapter.notifyDataSetChanged();

                }

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
                            new UploadToServer().execute();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                        loadingPanel.setVisibility(View.GONE);
                        buttonSave.setEnabled(true);
                        etCategory.setEnabled(true);
                        etContent.setEnabled(true);
                        etTitle.setEnabled(true);
                        insertImage.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingPanel.setVisibility(View.GONE);
                    buttonSave.setEnabled(true);
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
                buttonSave.setEnabled(true);
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
                hashMap.put("threadId",threadId);
                hashMap.put("edittitle",etTitle.getText().toString());
                hashMap.put("editcategory",category);
                hashMap.put("editcontent",etContent.getText().toString());

                return hashMap;
            }
        };

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

               /* Intent i = new Intent(this, CropActivity.class);
                i.putExtra("imageUri",imageUri.toString());
                startActivity(i);*/
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


               /* Intent i = new Intent(this, CropActivity.class);
                i.putExtra("imageUri",mCropImageUri.toString());
                startActivity(i);*/
                SetImages(mCropImageUri);

            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void SetImages(Uri uri){


        ImageGetter imagesGetter = new ImageGetter();
        imagesGetter.setUri(uri);
        imagesGetter.setCaption("");
        imagesGetterList.add(imagesGetter);
        adapter.notifyDataSetChanged();

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

    private class UploadToServer extends AsyncTask<Void,Integer,String> {
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);

            txtPercentage.setText(String.valueOf(values[0])+"%");

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
                        Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
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
}
