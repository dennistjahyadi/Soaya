package com.dennistjahyadigotama.soaya;

/**
 * Created by Denn on 8/10/2016.
 */
public class backup {

    public static String myFirebaseInstanceIDServiceUrl ="http://103.31.251.226/lul/ubaya/update_tokenId.php";
    public static String createThreadActivityUrl = "http://103.31.251.226/lul/ubaya/create_thread_and_edit.php";
    public static String threadEditActivityUrl = "http://103.31.251.226/lul/ubaya/create_thread_and_edit.php";
    public static String mainActivityUrl = "http://103.31.251.226/lul/ubaya/getId_profileUrl.php";
    public static String loginActivityUrl = "http://103.31.251.226/lul/ubaya/user_login.php";
    public static String registerActivityUrl = "http://103.31.251.226/lul/ubaya/user_register.php";
    public static String profileUrl = "http://103.31.251.226/lul/ubaya/getId_profileUrl.php?userPic=";
    public static String profileUrl2 = "http://103.31.251.226/lul/ubaya/getTotalPost_thread_friends.php";
    public static String profileUrl3 = "http://103.31.251.226/lul/ubaya/getPeopleProfile.php";
    public static String cropActivityUrl = "http://103.31.251.226/lul/ubaya/upload_profile_pic.php";
    public static String homeUrl = "http://103.31.251.226/lul/ubaya/forum_list.php?poi=";
    public static String threadOpenActivityUrl = "http://103.31.251.226/lul/ubaya/forum_content.php";
    public static String categoryActivityUrl = "http://103.31.251.226/lul/ubaya/forum_list_by_category.php";
    public static String categoryActivityUrl2 = "http://103.31.251.226/lul/ubaya/subscribe_category.php";
    public static String searchFragmentThreadUrl = "http://103.31.251.226/lul/ubaya/search.php";
    public static String searchFragmentPeopleUrl = "http://103.31.251.226/lul/ubaya/search.php";
    public static String peopleProfileActivityUrl = "http://103.31.251.226/lul/ubaya/getPeopleProfile.php";
    public static String peopleProfileActivityUrl2 = "http://103.31.251.226/lul/ubaya/getTotalPost_thread_friends.php";
    public static String peopleProfileActivityUrl3 = "http://103.31.251.226/lul/ubaya/people_relation.php";
    public static String peopleProfileActivityUrlNotif = "http://103.31.251.226/lul/ubaya/friend_request_notification.php";
    public static String threadHistoryActivityUrl = "http://103.31.251.226/lul/ubaya/forum_list_by_history.php";
    public static String postHistoryActivityUrl = "http://103.31.251.226/lul/ubaya/comment_list_by_history.php";
    public static String friendListActivityUrl = "http://103.31.251.226/lul/ubaya/friend_list.php";
    public static String friendRequestUrl = "http://103.31.251.226/lul/ubaya/friend_request.php";
    public static String threadOpenActivityNoticeUrl = "http://103.31.251.226/lul/ubaya/notification.php";
    public static String notificationInfoUrl = "http://103.31.251.226/lul/ubaya/notice_list.php";
    public static String messageActivityUrl = "http://103.31.251.226/lul/ubaya/room_chat_notification.php";
    public static String chatActivityUrl = "http://103.31.251.226/lul/ubaya/room_chat_notification.php";
    public static String messageUrl = "http://103.31.251.226/lul/ubaya/message_list.php";
    public static String profileOptionsActivityUrl ="http://103.31.251.226/lul/ubaya/update_profile.php";
    public static String selectCategoryActivityUrl = "http://103.31.251.226/lul/ubaya/select_category.php";


    /*
     public static String myFirebaseInstanceIDServiceUrl ="http://192.168.1.100:81/ubaya/update_tokenId.php";//
    public static String createThreadActivityUrl = "http://192.168.1.100:81/ubaya/create_thread_and_edit.php";//
    public static String threadEditActivityUrl = "http://192.168.1.100:81/ubaya/create_thread_and_edit.php";//
    public static String mainActivityUrl = "http://192.168.1.100:81/ubaya/getId_profileUrl.php";//
    public static String loginActivityUrl = "http://192.168.1.100:81/ubaya/user_login.php";//
    public static String registerActivityUrl = "http://192.168.1.100:81/ubaya/user_register.php";//
    public static String profileUrl = "http://192.168.1.100:81/ubaya/getId_profileUrl.php?userPic=";//
    public static String profileUrl2 = "http://192.168.1.100:81/ubaya/getTotalPost_thread_friends.php";//
    public static String profileUrl3 = "http://192.168.1.100:81/ubaya/getPeopleProfile.php";//
    public static String cropActivityUrl = "http://192.168.1.100:81/ubaya/upload_profile_pic.php";//
    public static String homeUrl = "http://192.168.1.100:81/ubaya/forum_list.php?poi=";//
    public static String threadOpenActivityUrl = "http://192.168.1.100:81/ubaya/forum_content.php";//
    public static String categoryActivityUrl = "http://192.168.1.100:81/ubaya/forum_list_by_category.php";//
    public static String categoryActivityUrl2 = "http://192.168.1.100:81/ubaya/subscribe_category.php";//
    public static String searchFragmentThreadUrl = "http://192.168.1.100:81/ubaya/search.php";//
    public static String searchFragmentPeopleUrl = "http://192.168.1.100:81/ubaya/search.php";//
    public static String peopleProfileActivityUrl = "http://192.168.1.100:81/ubaya/getPeopleProfile.php";//
    public static String peopleProfileActivityUrl2 = "http://192.168.1.100:81/ubaya/getTotalPost_thread_friends.php";//
    public static String peopleProfileActivityUrl3 = "http://192.168.1.100:81/ubaya/people_relation.php";//
    public static String peopleProfileActivityUrlNotif = "http://192.168.1.100:81/ubaya/friend_request_notification.php";//
    public static String threadHistoryActivityUrl = "http://192.168.1.100:81/ubaya/forum_list_by_history.php";//
    public static String postHistoryActivityUrl = "http://192.168.1.100:81/ubaya/comment_list_by_history.php";//
    public static String friendListActivityUrl = "http://192.168.1.100:81/ubaya/friend_list.php";//
    public static String friendRequestUrl = "http://192.168.1.100:81/ubaya/friend_request.php";//
    public static String threadOpenActivityNoticeUrl = "http://192.168.1.100:81/ubaya/notification.php";//
    public static String notificationInfoUrl = "http://192.168.1.100:81/ubaya/notice_list.php";//
    public static String messageActivityUrl = "http://192.168.1.100:81/ubaya/room_chat_notification.php";//
    public static String chatActivityUrl = "http://192.168.1.100:81/ubaya/room_chat_notification.php";//
    public static String messageUrl = "http://192.168.1.100:81/ubaya/message_list.php";
    public static String profileOptionsActivityUrl ="http://192.168.1.100:81/ubaya/update_profile.php";
    public static String selectCategoryActivityUrl = "http://192.168.1.100:81/ubaya/select_category.php";

*/


/*



package com.dennistjahyadigotama.soaya.activities.MessageActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dennistjahyadigotama.soaya.CropImage.CropActivityForChat;
import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.SQLite.DBHelper;
import com.dennistjahyadigotama.soaya.SQLite.DBInfo;
import com.dennistjahyadigotama.soaya.User;
import com.dennistjahyadigotama.soaya.activities.MessageActivity.adapter.ChatActivityAdapter;
import com.dennistjahyadigotama.soaya.activities.MessageActivity.adapter.ChatActivityGetter;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/*

public class ChatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout rev;
    TextView tvUsername;
    ImageView imageViewAddPic,imageViewSend,imageViewInfo;
    EditText etText;
    public static String roomid;
    RequestQueue requestQueue;
    String url= User.chatActivityUrl;
    List<String> users = new ArrayList<>();
    List<ChatActivityGetter> chatActivityGetterList = new ArrayList<>();
    RecyclerView recyclerView;
    ChatActivityAdapter adapter;
    private Uri mCropImageUri;
    boolean check=true;
    public static boolean active = false;
    DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        requestQueue = Volley.newRequestQueue(this);
        dbHelper = new DBHelper(getApplicationContext());
        roomid = getIntent().getStringExtra("roomId");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        DBHelper db = new DBHelper(this);
        db.UpdateRoomNewMessage(roomid);
        db.close();
        rev = (RelativeLayout)findViewById(R.id.rev);
        tvUsername = (TextView)findViewById(R.id.textViewUsername);
        imageViewInfo = (ImageView)findViewById(R.id.imageViewInfo);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setScrollContainer(true);
        imageViewAddPic = (ImageView)findViewById(R.id.imageViewAddPic);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imageViewSend = (ImageView)findViewById(R.id.imageViewSend);
        etText = (EditText)findViewById(R.id.editTextComment);
        etText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(chatActivityGetterList.size()-1);
                    }
                }, 200);
            }
        });
        GetRoomUsers();



    }

    @Override
    public void onStart() {
        super.onStart();
        active=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active=false;

    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            //  ... react to local broadcast message
            try
            {
                String theroomid = intent.getStringExtra("roomid");
                String id = intent.getStringExtra("id");
                if(roomid.equals(theroomid)) {
                    if (!check && active) {
                        Toast.makeText(getApplicationContext(), "New Feature..", Toast.LENGTH_SHORT).show();
                    }
                    GetNextMessageNotif(id);
                }
            }catch (Exception e)
            {

            }

        }

    };



    protected void SetupPagination(){
        adapter = new ChatActivityAdapter(chatActivityGetterList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(layoutManager.findFirstCompletelyVisibleItemPosition()>(chatActivityGetterList.size()-15))
                {

                    check = true;
                }else
                {
                    check =false;
                }

            }
        });
        GetTheData();

    }


    private void GetTheData()
    {
        Cursor cursor = dbHelper.GetChatData(roomid);
        if(cursor.getCount()==0) {

            GetMessageList();
        }
        else
        {
            CheckTotalChat();
        }



    }


    private void Setup()
    {
        SetupPagination();
        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ChatInfoActivity.class);
                i.putExtra("roomid",roomid);
                startActivity(i);

            }
        });

        imageViewAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(ChatActivity.this);

            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etText.getText().toString();
                text = text.trim();
                if(!text.isEmpty()||!text.equals("")) {

                    SendMessage(text,roomid);
                }
                etText.setText("");
            }
        });



        etText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check=true;
            }


        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("chatactivity"));

    }

    private void UpdateRoomInfo(String idMessage, final String roomid)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?idMessages="+idMessage, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String date,messages,type,sender;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        date= jsonObject.getString("thedate");
                        messages=jsonObject.getString("messages");
                        type= jsonObject.getString("type");
                        sender=jsonObject.getString("sender");

                        GetRoomUsers(roomid,date,messages,type,sender,0);

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

    private void CheckTotalChat()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.chatActivityUrl+"?getTotalChat="+roomid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    int jumlah=0;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        jumlah = Integer.parseInt(jsonObject.getString("jumlah"));

                    }

                    Cursor cursor = dbHelper.GetChatData(roomid);
                    if(jumlah!=cursor.getCount())
                    {
                        GetMessageList();
                    }else
                    {
                        GetDataFromSqlite(cursor);
                    }
                    cursor.close();



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

    private void GetDataFromSqlite(Cursor cursor){

        if (cursor.moveToFirst()) {
            String id,thetime,profilepic,messages,type,thedate,sender;

            if (cursor.moveToFirst()) {
                do {
                    ChatActivityGetter chatActivityGetter = new ChatActivityGetter();
                    id = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_ID));
                    messages = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_MESSAGES));
                    type = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_TYPE));
                    thedate = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_THEDATE));
                    sender = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_SENDER));
                    thetime = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_THETIME));
                    profilepic = cursor.getString(cursor.getColumnIndex(DBInfo.CHAT_COLUMN_PROFILEPIC));
                    chatActivityGetter.setId(id);
                    chatActivityGetter.setType(type);
                    chatActivityGetter.setDate(thedate);
                    chatActivityGetter.setTime(thetime);
                    chatActivityGetter.setMessages(messages);
                    chatActivityGetter.setSender(sender);
                    chatActivityGetter.setProfilepic(profilepic);

                    chatActivityGetterList.add(chatActivityGetter);


                } while (cursor.moveToNext());

            }
            adapter.notifyDataSetChanged();

            if(check) {
                recyclerView.scrollToPosition(chatActivityGetterList.size()-1);
            }

            cursor.close();
        }

    }




    private void GetRoomUsers(final String roomid, final String date, final String messages,
                              final String type, final String sender,
                              final int con)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(User.chatActivityUrl+"?getRoomUsers="+roomid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    String users,roomname2;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        users = jsonObject.getString("users");
                        roomname2 = jsonObject.getString("roomname");

                        dbHelper.UpdateRoomInfo(roomid,date,messages,type,sender,users,con,roomname2);

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

    private void SendMessage(final String text, final String idroom){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success"))
                    {
                        GetNextMessageNotif(jsonObject.getString("success"));
                        UpdateRoomInfo(jsonObject.getString("success"),idroom);

                        recyclerView.scrollToPosition(chatActivityGetterList.size()-1);


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
                map.put("room",idroom);
                map.put("messages",text);
                map.put("type","text");
                map.put("sender", User.username);

                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void AddToDb(ChatActivityGetter chatActivityGetter)
    {
        String id,messages,type,thedate,sender,thetime,profilepic;
        id = chatActivityGetter.getId();
        messages = chatActivityGetter.getMessages();
        type =chatActivityGetter.getType();
        thedate = chatActivityGetter.getDate();
        sender = chatActivityGetter.getSender();
        thetime = chatActivityGetter.getTime();
        profilepic = chatActivityGetter.getProfilepic();

        dbHelper.InsertChat(id,roomid,messages,type,thedate,sender,thetime,profilepic);
        chatActivityGetterList.add(chatActivityGetter);

    }


    private void GetMessageList()
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?getMessageList="+roomid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        ChatActivityGetter chatActivityGetter = new ChatActivityGetter();
                        chatActivityGetter.setId(jsonObject.getString("id"));
                        chatActivityGetter.setType(jsonObject.getString("type"));
                        chatActivityGetter.setTime(jsonObject.getString("thetime"));
                        chatActivityGetter.setDate(jsonObject.getString("thedate"));

                        chatActivityGetter.setMessages(jsonObject.getString("messages"));
                        chatActivityGetter.setSender(jsonObject.getString("sender"));
                        chatActivityGetter.setProfilepic(jsonObject.getString("profilepic"));
                        AddToDb(chatActivityGetter);

                    }
                    adapter.notifyDataSetChanged();
                    if(check) {
                        recyclerView.scrollToPosition(chatActivityGetterList.size()-1);
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


    private void GetNextMessageNotif(String idMessage)
    {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?idMessages="+idMessage, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        ChatActivityGetter chatActivityGetter = new ChatActivityGetter();
                        chatActivityGetter.setId(jsonObject.getString("id"));
                        chatActivityGetter.setType(jsonObject.getString("type"));
                        chatActivityGetter.setTime(jsonObject.getString("thetime"));
                        chatActivityGetter.setDate(jsonObject.getString("thedate"));
                        chatActivityGetter.setMessages(jsonObject.getString("messages"));
                        chatActivityGetter.setSender(jsonObject.getString("sender"));
                        chatActivityGetter.setProfilepic(jsonObject.getString("profilepic"));
                        AddToDb(chatActivityGetter);
                    }
                    adapter.notifyDataSetChanged();

                    if(check) {
                        recyclerView.scrollToPosition(chatActivityGetterList.size()-1);
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

    private void GetRoomUsers()
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url+"?getRoomUsers="+roomid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String userInRoom=null;
                    for(int i=0;i<response.length();i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        userInRoom=jsonObject.getString("users");
                    }

                    String asd="";

                    JSONArray json = new JSONArray(userInRoom);
                    for(int i=0;i<json.length();i++)
                    {
                        users.add(json.getString(i));
                        if(i==0) {
                            asd = json.getString(i);
                        }else
                        {
                            asd = asd+", "+json.getString(i);
                        }
                    }

                    if(users.size()==2)
                    {
                        if(users.get(0).equals(User.username))
                        {
                            asd= users.get(1);
                        }else
                        {
                            asd= users.get(0);

                        }
                    }
                    tvUsername.setText(asd);



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




    @Override
    protected void onResume() {
        super.onResume();
        chatActivityGetterList.clear();
        Setup();

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
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {

                Intent i = new Intent(this, CropActivityForChat.class);
                i.putExtra("imageUri",imageUri.toString());
                i.putExtra("roomid",roomid);
                startActivity(i);
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


                Intent i = new Intent(this, CropActivityForChat.class);
                i.putExtra("imageUri",mCropImageUri.toString());
                i.putExtra("roomid",roomid);
                startActivity(i);


            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
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






    */




}
