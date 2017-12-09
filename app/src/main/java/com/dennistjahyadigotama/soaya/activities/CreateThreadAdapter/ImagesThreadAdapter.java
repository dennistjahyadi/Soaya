package com.dennistjahyadigotama.soaya.activities.CreateThreadAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.List;


/**
 * Created by Denn on 6/30/2016.
 */
public class ImagesThreadAdapter extends RecyclerView.Adapter<ImagesThreadAdapter.MyViewHolder>{

    List<ImagesGetter> imagesGetterList;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView buttonCancel;
        EditText etCaption;
        TextView saveCaption;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            imageView = (ImageView)itemView.findViewById(R.id.imageViewImage);
            buttonCancel = (TextView)itemView.findViewById(R.id.buttonCancel);
            buttonCancel.setTypeface(TypeFaceManager.getFontAwesomeTypeface(context));
            etCaption = (EditText)itemView.findViewById(R.id.editTextCaption);
            saveCaption = (TextView)itemView.findViewById(R.id.textViewSaveCaption);
        }
    }

    public ImagesThreadAdapter(List<ImagesGetter> param){
        this.imagesGetterList = param;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_view,parent,false);
        return new MyViewHolder(view);
    }


    private Bitmap getScaledBitmap(Bitmap bitmap)
    {
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int newWidth = getScreenWidth(); //this method should return the width of device screen.
        float scaleFactor = (float)newWidth/(float)imageWidth;
        int newHeight = (int)(imageHeight * scaleFactor);

        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return bitmap;
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

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
      final ImagesGetter imagesGetter = imagesGetterList.get(position);

        Bitmap bitmap=null;
        try {
            // bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imagesGetter.uri);
            bitmap = decodeUri(context,imagesGetter.getUri(),1000);
            imagesGetter.setEncoded(ImageBase64.encodeTobase64(bitmap));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //bitmap = getScaledBitmap(bitmap);

        Picasso.with(context).load(imagesGetter.getUri()).resize(getScreenWidth(),getScreenWidth()).centerInside().into(holder.imageView);
        //holder.imageView.setImageBitmap(bitmap);

        holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesGetterList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.etCaption.setText(imagesGetter.getCaption());
        holder.saveCaption.setVisibility(View.GONE);
        holder.saveCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesGetter.setCaption(holder.etCaption.getText().toString());
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


       /* holder.etCaption.setText(imagesGetter.getCaption());
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imagesGetter.setCaption(holder.etCaption.getText().toString());
                Toast.makeText(context,"done",Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/


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



    private int getScreenWidth(){

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    @Override
    public int getItemCount() {
        return imagesGetterList.size();
    }
}
