package com.dennistjahyadigotama.soaya.activities.KuponActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.ConvertImage.ImageBase64;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Denn on 8/28/2016.
 */
public class AddKuponActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    ImageView ivPhoto;
    Toolbar toolbar;
    static  EditText etTanggalAwal,etTanggalAkhir;
    static EditText etTimeAwal,etTimeAkhir;
    RelativeLayout rev;
    EditText etTitle,etDesc;
    private Uri mCropImageUri;
    TextView tvCreate;
    String encoded;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_kupon_activity);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Kupon");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);

        rev = (RelativeLayout)findViewById(R.id.loadingPanel);
        rev.setVisibility(View.GONE);
        ivPhoto = (ImageView)findViewById(R.id.imageViewPhoto);
        etTitle = (EditText)findViewById(R.id.editTextJudulKupon);
        etDesc = (EditText)findViewById(R.id.editTextDescription);
        etTanggalAwal = (EditText)findViewById(R.id.etTanggalAwal);
        etTanggalAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerAwal");

            }
        });
        etTanggalAkhir = (EditText)findViewById(R.id.etTanggalAkhir);
        etTanggalAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerAkhir");

            }
        });
        etTimeAwal = (EditText)findViewById(R.id.etTimeAwal);
        etTimeAwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePickerAwal");

            }
        });
        etTimeAkhir = (EditText)findViewById(R.id.etTimeAkhir);
        etTimeAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePickerAkhir");

            }
        });

        Picasso.with(getApplicationContext()).load(R.drawable.add_photo).resize(getScreenWidth()/2,getScreenWidth()/2).centerInside().into(ivPhoto);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(AddKuponActivity.this);

            }
        });
        tvCreate = (TextView)findViewById(R.id.textViewCreate);
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(encoded!=null && !encoded.equals("")) {
                    tvCreate.setEnabled(false);
                    rev.setVisibility(View.VISIBLE);
                    SendToServer();
                }else
                {
                    Toast.makeText(getApplicationContext(),"please insert an image",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int getScreenWidth(){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
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

            if(getTag().equals("datePickerAwal"))
            {
                etTanggalAwal.setText(year+"-"+(month+1)+"-"+day);
            }else
            {
                etTanggalAkhir.setText(year+"-"+(month+1)+"-"+day);
            }
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

            // Create a new instance of TimePickerDialog and return it
            return timePickerDialog;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String hh;
            String mm;
            if(hourOfDay<10)
            {
                hh = "0"+hourOfDay;
            }else
            {
                hh= hourOfDay+"";
            }
            if(minute<10)
            {
                mm = "0"+minute;
            }else
            {
                mm=minute+"";
            }
            if(getTag().equals("timePickerAwal"))
            {
                etTimeAwal.setText(hh+":"+mm);
            }else
            {
                etTimeAkhir.setText(hh+":"+mm);

            }
        }
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
        try {
            Bitmap bitmap = decodeUri(getApplicationContext(),uri,1000);
            encoded = ImageBase64.encodeTobase64(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(uri).resize(getScreenWidth(),getScreenWidth()).centerInside().into(ivPhoto);

    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize )
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp  < requiredSize || height_tmp  < requiredSize)
            {
                break;
            }else
            {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;}
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        o2.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        Bitmap decoded=null;

        try {
            System.gc();

            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {


            try {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));

            }catch (OutOfMemoryError ex)
            {
                try {
                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
                }catch (OutOfMemoryError ez)
                {
                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
                }


            }
            Log.e("EWN", "Out of memory error catched");


        }


        return decoded;
    }

    private void SendToServer()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.kuponListActivityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        Toast.makeText(getApplicationContext(),"Coupon created",Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                        tvCreate.setEnabled(true);
                        rev.setVisibility(View.GONE);


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
                map.put("title",etTitle.getText().toString());
                map.put("startDate",etTanggalAwal.getText()+" "+etTimeAwal.getText()+":00");
                map.put("expDate",etTanggalAkhir.getText()+" "+etTimeAkhir.getText()+":00");
                map.put("encodedImage",encoded);
                map.put("description",etDesc.getText().toString());
                map.put("username",User.username);
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

}
