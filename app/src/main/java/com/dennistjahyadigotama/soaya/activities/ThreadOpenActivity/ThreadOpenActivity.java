package com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CircleTransform;
import com.dennistjahyadigotama.soaya.MyNestedScrollView;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.dennistjahyadigotama.soaya.activities.SearchActivity.Fragment.adapter.UserGetter;
import com.dennistjahyadigotama.soaya.activities.ThreadEditActivity.ThreadEditActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter.CommentGetter;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter.CommentRecyclerAdapter;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter.FriendListAdapter;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter.ImageGetter;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.adapter.ImageRecyclerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Denn on 7/1/2016.
 */
public class ThreadOpenActivity extends AppCompatActivity {
    String threadID,createBy,title,content,category;
    RequestQueue requestQueue;
    RecyclerView recyclerViewImages,recyclerViewComment;
    List<ImageGetter> imageGetterList = new ArrayList<>();
    List<CommentGetter> commentGetterList = new ArrayList<>();
    ImageRecyclerAdapter imageRecyclerAdapter;
    CommentRecyclerAdapter commentRecyclerAdapter;
    ImageView imageViewCreateBy,buttonSendComment;
    TextView textViewCreateBy,textViewCreateDate,textViewCategory,textViewTitle,textViewContent,textViewEdit,loadMoreComment;
    MultiAutoCompleteTextView editTextComment;
    String url = User.threadOpenActivityUrl;
    String urlFriendList = User.friendListActivityUrl;
    String urlMention = User.threadOpenActivityNoticeUrl;
    List<UserGetter> friendList = new ArrayList<>();
    NestedScrollView nestedScrollView;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coor;
    int totalComments;
    int totalImages;
    ArrayAdapter<UserGetter> adapter;
    List<String> tags = new ArrayList<String>();
    JSONArray mentionArray;
    boolean increaseView= true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.thread_open_activity);

        threadID=getIntent().getStringExtra("ThreadId");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        int click = sharedPreferences.getInt(QuickstartPreferences.Click,0);
        click +=1;
        sharedPreferences.edit().putInt(QuickstartPreferences.Click,click).apply();


        SetViewClassId();
        GetThreadContent();
        recyclerViewImages.setNestedScrollingEnabled(false);
        recyclerViewComment.setNestedScrollingEnabled(false);
       // int scrollTo = ((View) childView.getParent().getParent()).getTop() + childView.getTop();
       // nestedScrollView.smoothScrollTo(0, scrollTo);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(commentGetterList.size()<totalComments)
                {
                    loadMoreComment.setVisibility(View.VISIBLE);
                }else if(commentGetterList.size()>=totalComments)
                {
                    loadMoreComment.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    public void SetViewClassId(){
        imageViewCreateBy = (ImageView)findViewById(R.id.imageViewProfile);

        textViewCreateBy = (TextView)findViewById(R.id.textViewCreateBy);

        textViewEdit = (TextView)findViewById(R.id.textViewEdit);
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ThreadEditActivity.class);
                intent.putExtra("threadId",threadID);
                intent.putExtra("category",category);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });
        nestedScrollView = (MyNestedScrollView)findViewById(R.id.nested_scroll);
        coor = (CoordinatorLayout)findViewById(R.id.coor_layout);
        textViewCreateDate = (TextView)findViewById(R.id.textViewDate);
        textViewCategory = (TextView)findViewById(R.id.textViewCategory);
        textViewTitle = (TextView)findViewById(R.id.textViewTitleThread);
        textViewContent = (TextView)findViewById(R.id.textViewContent);
        loadMoreComment = (TextView)findViewById(R.id.loadMoreComment);
        loadMoreComment.setVisibility(View.GONE);
        loadMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetTotalComments();

            }
        });
        recyclerViewImages = (RecyclerView)findViewById(R.id.recycler_viewImages);
        recyclerViewComment = (RecyclerView)findViewById(R.id.recycler_viewComments);

        buttonSendComment = (ImageView)findViewById(R.id.imageViewSend);
        editTextComment = (MultiAutoCompleteTextView)findViewById(R.id.editTextComment);

        adapter = new FriendListAdapter(this, R.layout.user_recycler_view,friendList);
       // GetFriendList();
        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    String last = editTextComment.getText().toString();
                    last = last.substring(last.length() - 1);
                    if(last.equals("@"))
                    {
                        GetFriendList();
                    }
                }catch (Exception ex)
                {

                }

            }
        });


       // ArrayAdapter<String> adapter =
           //     new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendList);



        imageRecyclerAdapter = new ImageRecyclerAdapter(imageGetterList);
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentGetterList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        recyclerViewImages.setLayoutManager(layoutManager);
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setAdapter(imageRecyclerAdapter);
        recyclerViewComment.setLayoutManager(layoutManager1);
        recyclerViewComment.setItemAnimator(new DefaultItemAnimator());
        recyclerViewComment.setAdapter(commentRecyclerAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReadTextMentionComment();

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                if(increaseView) {
                    IncreaseViews();
                }
            }
        }, 3500);

    }

    @Override
    protected void onStop() {
        super.onStop();
        increaseView=false;

    }

    private void ReadTextMentionComment()
    {
        if(editTextComment.getText().toString()!=null||editTextComment.getText().toString()!="") {

            String aaa = editTextComment.getText().toString();
            if(!aaa.matches(""))
            {
                String text = editTextComment.getText().toString();

                String regexPattern = "(@\\w+)";

                Pattern p = Pattern.compile(regexPattern);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    String hashtag = m.group(1);
                    // Add hashtag to ArrayList
                    String asd = hashtag.substring(1);
                    tags.add(asd);

                }


                mentionArray = new JSONArray();
                for (int i=0;i<tags.size();i++)
                {
                    mentionArray.put(tags.get(i));

                }
                SendingComment(editTextComment.getText().toString());
                editTextComment.setText("");


            }

        }
    }


    void refreshItems() {
        // Load items
        // ...
        commentGetterList.clear();
        imageGetterList.clear();
        GetThreadContent();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void SendingComment(final String messages)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        commentGetterList.clear();
                        GetThreadComments2();

                        if(tags.size()>0)
                        {
                            SendingMentionNotificationAndSaveToDb(messages);

                        }
                        if(!User.username.equals(createBy))
                        {
                            SendNotificationToThreadCreator(messages);
                        }



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
                map.put("username", User.username);
                map.put("threadId",threadID);
                map.put("comment",messages);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


    private void SendingMentionNotificationAndSaveToDb(final String messages)
    {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlMention, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    tags.clear();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.names().get(0).equals("success")) {

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
                    map.put("from", User.username);
                    map.put("jsonArrayUsername", mentionArray.toString());
                    map.put("messages", messages);
                    map.put("createBy", createBy);
                    map.put("threadId", threadID);

                    return map;
                }
            };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

    }


    private void SendNotificationToThreadCreator(final String messages)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlMention, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("from", User.username);
                map.put("to",createBy);
                map.put("messages",messages);
                map.put("threadId",threadID);


                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void GetTotalComments(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?totalcomments="+threadID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        totalComments = Integer.parseInt(jsonObject.getString("jumlah"));
                        if(totalComments>0)
                        {
                            //Toast.makeText(getApplicationContext(),"There are a Comments",Toast.LENGTH_SHORT).show();
                            GetThreadComments();
                        }

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
    private void GetTotalImages(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?totalimages="+threadID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        totalImages = Integer.parseInt(jsonObject.getString("jumlah"));

                        if(totalImages>0)
                        {
                            //Toast.makeText(getApplicationContext(),"There are an Images",Toast.LENGTH_SHORT).show();
                            GetThreadImages();
                        }
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

    private void GetThreadContent(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?poi="+threadID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                    try {
                        for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                            String profilePicUrl = jsonObject.getString("profilepic");
                            if(profilePicUrl==null|| profilePicUrl==""||profilePicUrl.isEmpty())
                            {

                                Picasso.with(getApplicationContext()).load(R.drawable.default_user_icon_profile).transform(new CircleTransform()).into(imageViewCreateBy);

                            }else
                            {
                                Picasso.with(getApplicationContext()).load(profilePicUrl).transform(new CircleTransform()).into(imageViewCreateBy);
                            }


                            textViewCreateBy.setText(jsonObject.getString("createBy"));
                            createBy = jsonObject.getString("createBy");
                            textViewCreateBy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), PeopleProfileActivity.class);
                                    i.putExtra("username", createBy);
                                    startActivity(i);
                                }
                            });
                            imageViewCreateBy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getApplicationContext(), PeopleProfileActivity.class);
                                    i.putExtra("username", createBy);
                                    startActivity(i);
                                }
                            });
                            if(createBy.equals(User.username))
                            {
                                textViewEdit.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                textViewEdit.setVisibility(View.GONE);

                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            try {
                                Date mDate = sdf.parse(jsonObject.getString("theDate"));
                                long your_time_in_milliseconds = mDate.getTime();
                                long current_time_in_millisecinds = System.currentTimeMillis();

                                CharSequence thedate= DateUtils.getRelativeTimeSpanString(your_time_in_milliseconds, current_time_in_millisecinds, DateUtils.MINUTE_IN_MILLIS);
                                textViewCreateDate.setText(thedate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            textViewCategory.setText(jsonObject.getString("category"));
                            category = jsonObject.getString("category");
                            textViewTitle.setText(jsonObject.getString("title"));
                            title = jsonObject.getString("title");
                            textViewContent.setText(jsonObject.getString("content"));
                            content = jsonObject.getString("content");

                            GetTotalImages();
                            GetTotalComments();



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

    private void IncreaseViews()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {


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
                map.put("increaseViews",threadID);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void GetThreadImages(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?getimages="+threadID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                    try {
                        for(int i=0;i<response.length();i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ImageGetter imageGetter = new ImageGetter();
                            imageGetter.setImageId(jsonObject.getString("id"));
                            imageGetter.setCaption(jsonObject.getString("caption"));
                            imageGetter.setUrl(jsonObject.getString("url"));
                            imageGetterList.add(imageGetter);
                        }
                        imageRecyclerAdapter.notifyDataSetChanged();

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
    private void GetThreadComments(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?getcomments="+threadID+"&offset="+commentGetterList.size(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        CommentGetter commentGetter = new CommentGetter();
                        commentGetter.setName(jsonObject.getString("postBy"));
                        commentGetter.setComment(jsonObject.getString("text"));
                        commentGetterList.add(commentGetter);
                    }
                    commentRecyclerAdapter.notifyDataSetChanged();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            if(commentGetterList.size()<totalComments)
                            {
                                loadMoreComment.setVisibility(View.VISIBLE);
                            }else if(commentGetterList.size()>=totalComments)
                            {
                                loadMoreComment.setVisibility(View.GONE);
                            }
                        }
                    }, 200);
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

    private void GetFriendList(){
        friendList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlFriendList+"?friendList1="+ User.username, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try
                {
                    String id,username1,profileUrl,name;
                    for(int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if(jsonObject.getString("user1").equals(User.username))
                        {
                            id = jsonObject.getString("id2");
                            username1 = jsonObject.getString("user2");
                            profileUrl = jsonObject.getString("profilepic2");
                            name = jsonObject.getString("name2");
                        }else
                        {
                            id = jsonObject.getString("id1");
                            username1 = jsonObject.getString("user1");
                            profileUrl = jsonObject.getString("profilepic1");
                            name = jsonObject.getString("name1");
                        }


                        UserGetter userGetter = new UserGetter();
                        userGetter.setId(id);
                        userGetter.setUsername("@"+username1);
                        userGetter.setName(name);
                        userGetter.setProfileUrl(profileUrl);
                        friendList.add(userGetter);
                    }
                    adapter.notifyDataSetChanged();

                    editTextComment.setThreshold(1);
                    editTextComment.setAdapter(adapter);
                    editTextComment.setTokenizer(new SpaceTokenizer());
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

    private void GetThreadComments2(){

        GetThreadComments();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                nestedScrollView.scrollTo(0,coor.getBottom());

            }
        }, 400);
    }

    class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer{


        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }

}
