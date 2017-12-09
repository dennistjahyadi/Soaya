package com.dennistjahyadigotama.soaya.activities.ThreadEditActivity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dennistjahyadigotama.soaya.ConvertImage.ImageBase64;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.TypeFaceManager;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by Denn on 7/4/2016.
 */
public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.MyViewHolder> {

    List<ImageGetter> imageGetterList;
    Context context;

    protected class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView buttonCancel;
        EditText etCaption;
        TextView saveCaption;

        public MyViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            imageView = (ImageView)itemView.findViewById(R.id.imageViewImage);
            buttonCancel = (TextView)itemView.findViewById(R.id.buttonCancel);
            buttonCancel.setTypeface(TypeFaceManager.getFontAwesomeTypeface(context));
            etCaption = (EditText)itemView.findViewById(R.id.editTextCaption);
            saveCaption = (TextView)itemView.findViewById(R.id.textViewSaveCaption);
        }
    }


    public RecyclerImageAdapter (List<ImageGetter> imageGetterList){
        this.imageGetterList = imageGetterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                SetViewHolder(holder,position);

            }
        }, 200);

    }

    public int getWidth(Bitmap bitmap){
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int newWidth = getScreenWidth(); //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);

        return newWidth;
    }
    public int getHeight(Bitmap bitmap){
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int newWidth = getScreenWidth(); //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);

        return newHeight;
    }



    private void SetViewHolder(final MyViewHolder holder,final int position){

       final ImageGetter imageGetter = imageGetterList.get(position);
       // bitmap = getScaledBitmap(bitmap);

        //holder.imageView.setImageBitmap(bitmap);



        if(imageGetter.getImageUrl()!=null)
        {
          Picasso.with(context).load(imageGetter.getImageUrl()+"pic_"+imageGetter.getId()+".jpg").resize(getScreenWidth(),getScreenWidth()).centerInside().into(holder.imageView);
           // Picasso.with(context).load(imageGetter.getImageUrl()+"pic_"+imageGetter.getId()+".jpg").into(target);

            new SaveEncodeToList(imageGetter).execute();

        }
        else if(imageGetter.getUri()!=null)
        {
            Picasso.with(context).load(imageGetter.getUri()).resize(getScreenWidth(),getScreenWidth()).centerInside().into(holder.imageView);
            try {
                Bitmap bmp = decodeUri(context,imageGetter.getUri(),1000);
                imageGetter.setEncoded(ImageBase64.encodeTobase64(bmp));
               // notifyDataSetChanged();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }



        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGetterList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.etCaption.setText(imageGetter.getCaption());

        holder.saveCaption.setVisibility(View.GONE);
        holder.saveCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGetter.setCaption(holder.etCaption.getText().toString());
                holder.saveCaption.setVisibility(View.GONE);
            }
        });

        holder.etCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.saveCaption.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    class SaveEncodeToList extends AsyncTask<String, Void, Bitmap> {
        ImageGetter imageGetter;
        public SaveEncodeToList(ImageGetter img){
            this.imageGetter = img;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(imageGetter.getImageUrl()+"pic_"+imageGetter.getId()+".jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageGetter.setEncoded(ImageBase64.encodeTobase64(bitmap));

            super.onPostExecute(bitmap);
        }
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
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

        try {
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            Log.e("EWN", "Out of memory error catched");
        }

        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));

        return decoded;
    }




    private Bitmap getScaledBitmap(Bitmap bitmap)
    {
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int newWidth = getScreenWidth(); //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        try {
            System.gc();
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            Log.e("EWN", "Out of memory error catched");
        }
        return bitmap;
    }

    private int getScreenWidth(){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    @Override
    public int getItemCount() {
        return imageGetterList.size();
    }
}
