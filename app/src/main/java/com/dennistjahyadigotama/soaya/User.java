package com.dennistjahyadigotama.soaya;

/**
 * Created by Denn on 6/24/2016.
 */
public class User {


    public static String email;
    public static String id;
    public static String username;

    public static String sortBy;
    public static String sortByOfficial;

    public static String webUrl = "http://localhost/lul/ubaya/";

    public static String myFirebaseInstanceIDServiceUrl = webUrl + "update_tokenId.php";
    public static String createThreadActivityUrl = webUrl + "create_thread_and_edit.php";
    public static String threadEditActivityUrl = webUrl + "create_thread_and_edit.php";
    public static String mainActivityUrl = webUrl + "getId_profileUrl.php";
    public static String loginActivityUrl = webUrl + "user_login.php";
    public static String registerActivityUrl = webUrl + "user_register.php";
    public static String profileUrl = webUrl + "getId_profileUrl.php?userPic=";
    public static String profileUrl2 = webUrl + "getTotalPost_thread_friends.php";
    public static String profileUrl3 = webUrl + "getPeopleProfile.php";
    public static String cropActivityUrl = webUrl + "upload_profile_pic.php";
    public static String homeUrl = webUrl + "forum_list.php?poi=";
    public static String officialUrl = webUrl + "forum_official_list.php?poi=";
    public static String checkOfficialUsers = webUrl + "check_official_users.php";
    public static String threadOpenActivityUrl = webUrl + "forum_content.php";
    public static String categoryActivityUrl = webUrl + "forum_list_by_category.php";
    public static String categoryActivityUrl2 = webUrl + "subscribe_category.php";
    public static String searchFragmentThreadUrl = webUrl + "search.php";
    public static String searchFragmentPeopleUrl = webUrl + "search.php";
    public static String peopleProfileActivityUrl = webUrl + "getPeopleProfile.php";
    public static String peopleProfileActivityUrl2 = webUrl + "getTotalPost_thread_friends.php";
    public static String peopleProfileActivityUrl3 = webUrl + "people_relation.php";
    public static String peopleProfileActivityUrlNotif = webUrl + "friend_request_notification.php";
    public static String threadHistoryActivityUrl = webUrl + "forum_list_by_history.php";
    public static String postHistoryActivityUrl = webUrl + "comment_list_by_history.php";
    public static String friendListActivityUrl = webUrl + "friend_list.php";
    public static String friendRequestUrl = webUrl + "friend_request.php";
    public static String threadOpenActivityNoticeUrl = webUrl + "notification.php";
    public static String notificationInfoUrl = webUrl + "notice_list.php";
    public static String messageActivityUrl = webUrl + "room_chat_notification.php";
    public static String chatActivityUrl = webUrl + "room_chat_notification.php";
    public static String messageUrl = webUrl + "message_list.php";
    public static String profileOptionsActivityUrl = webUrl + "update_profile.php";
    public static String selectCategoryActivityUrl = webUrl + "select_category.php";
    public static String kuponListActivityUrl = webUrl + "coupon.php";
    public static String calendarActivityUrl = webUrl + "calendar.php";
    public static String popUpAds = webUrl + "popupads.php";
    public static String wartaUbayaUrl = webUrl + "wartaubaya.php";
    public static String penjadwalanUrl = webUrl + "penjadwalan.php";
    public static String pushNotifUrl = webUrl + "push.php";
}
