package com.dennistjahyadigotama.soaya.Menu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.ThreadOpenActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Denn on 6/29/2016.
 */
public class ThreadListAdapter extends RecyclerView.Adapter<ThreadListAdapter.MyViewHolder> {

    List<ThreadListGetter> threadListGetterList;
    Context context;
    RequestQueue requestQueue;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView createBy, title, content, category, date, tvReply, tvViews;
        ImageView imageView, ivImageContent;
        LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();
            requestQueue = Volley.newRequestQueue(context);
            lin = (LinearLayout) itemView.findViewById(R.id.theLayout);
            imageView = (ImageView) itemView.findViewById(R.id.imageViewProfile);
            ivImageContent = (ImageView) itemView.findViewById(R.id.imageViewImage);
            createBy = (TextView) itemView.findViewById(R.id.textViewCreateBy);
            title = (TextView) itemView.findViewById(R.id.textViewTitleThread);
            content = (TextView) itemView.findViewById(R.id.textViewContent);
            category = (TextView) itemView.findViewById(R.id.textViewCategory);
            date = (TextView) itemView.findViewById(R.id.textViewDate);
            tvReply = (TextView) itemView.findViewById(R.id.textViewReply);
            tvViews = (TextView) itemView.findViewById(R.id.textViewViews);
        }
    }


    public ThreadListAdapter(List<ThreadListGetter> list) {

        this.threadListGetterList = list;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thread_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ThreadListGetter threadListGetter = threadListGetterList.get(position);
        if (threadListGetter.getPhoto() == null || threadListGetter.getPhoto() == "" || threadListGetter.getPhoto().isEmpty()) {

            Picasso.with(context).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(holder.imageView);

        } else {
            // Picasso.with(context).invalidate(threadListGetter.getPhoto());
            Picasso.with(context).load(threadListGetter.getPhoto()).transform(new CircleTransform()).into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PeopleProfileActivity.class);
                i.putExtra("username", threadListGetter.getCreateBy());
                context.startActivity(i);
            }
        });

        holder.createBy.setText(threadListGetter.getCreateBy());
        holder.createBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PeopleProfileActivity.class);
                i.putExtra("username", threadListGetter.getCreateBy());
                context.startActivity(i);
            }
        });
        String totalReply = "0";
        if (!threadListGetter.getTotalReply().equals("null")) {
            totalReply = threadListGetter.getTotalReply();
        }
        holder.tvReply.setText(totalReply);
        holder.tvViews.setText(threadListGetter.getTotalViews());
        holder.title.setText(threadListGetter.getTitle().trim());

        if (!threadListGetter.getImageContentUrl().equals("null")) {
            holder.ivImageContent.setVisibility(View.VISIBLE);
            Picasso.with(context).load(threadListGetter.getImageContentUrl()).error(R.drawable.ic_error_outline_black).placeholder(R.drawable.progress_animation).resize(getScreenWidth(), getScreenWidth()).centerInside().into(holder.ivImageContent);

        } else {
            holder.ivImageContent.setVisibility(View.GONE);

        }

        holder.content.setText(threadListGetter.getContent().trim());
        holder.category.setText(threadListGetter.getCategory());
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"thread id: "+threadListGetter.getId(),Toast.LENGTH_SHORT).show();

                Intent innn = new Intent("mainactivity");
                sendLocationBroadcast(innn);


                Intent intent = new Intent(context, ThreadOpenActivity.class);
                intent.putExtra("ThreadId", threadListGetter.getId());
                context.startActivity(intent);
            }
        });
        holder.lin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (context.getSharedPreferences("prefs", MODE_PRIVATE).getString(QuickstartPreferences.USERNAME, "").equals("dennistgt")) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Push?");
                    alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            SendPushNotif(threadListGetter.getCategory(), threadListGetter.getTitle(), threadListGetter.getId());

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

                return false;
            }
        });

        //   holder.date.setText(threadListGetter.getDate());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date mDate = sdf.parse(threadListGetter.getDate());
            long your_time_in_milliseconds = mDate.getTime();
            long current_time_in_millisecinds = System.currentTimeMillis();

            CharSequence thedate = DateUtils.getRelativeTimeSpanString(your_time_in_milliseconds, current_time_in_millisecinds, DateUtils.MINUTE_IN_MILLIS);
            holder.date.setText(thedate);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void SendPushNotif(final String category, final String title, final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, User.pushNotifUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
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
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("category", category);
                hashMap.put("title", title);
                hashMap.put("threadId", id);

                return hashMap;
            }
        };

        requestQueue.add(stringRequest);

    }

    private int getScreenWidth() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    private void sendLocationBroadcast(Intent intent) {

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return threadListGetterList.size();
    }


}
