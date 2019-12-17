package com.kapalert.kadunastategovernment.utils;


import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.response.ActionListRespons;
import com.kapalert.kadunastategovernment.response.AssignRespons;
import com.kapalert.kadunastategovernment.response.DisputeRespons;
import com.kapalert.kadunastategovernment.response.MdaInvokedRespons;
import com.kapalert.kadunastategovernment.templates.AnnouncementListPOJO;
import com.kapalert.kadunastategovernment.templates.BeanUploadImage;
import com.kapalert.kadunastategovernment.templates.CaseDetailPOJO;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.ChatListPOJO;
import com.kapalert.kadunastategovernment.templates.CommonListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselAccusedListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselCommentsPOJO;
import com.kapalert.kadunastategovernment.templates.CourtListPOJO;
import com.kapalert.kadunastategovernment.templates.EventListPOJO;
import com.kapalert.kadunastategovernment.templates.FileListPOJO;
import com.kapalert.kadunastategovernment.templates.InboxListPOJO;
import com.kapalert.kadunastategovernment.templates.JudgeListPOJO;
import com.kapalert.kadunastategovernment.templates.NoteJSON;
import com.kapalert.kadunastategovernment.templates.OffencePOJO;
import com.kapalert.kadunastategovernment.templates.PoliceFileListPOJO;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.templates.UserListPOJO;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by Dawinder on 10/02/2016.
 */

//Here save all general values that are constant
public class Constants {

    public static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static String FONT_FILE = "font_regular.ttf";
    public static String MAIN_BASE_URL = "http://kapalert.net/KadunaStateMOJ/";
    public static String BASE_URL = MAIN_BASE_URL + "api/";
    public static String FILES_BASE_URL = MAIN_BASE_URL + "uploads/case_files/";
    public static String USER_IMAGE_BASE_URL = MAIN_BASE_URL + "uploads/";
    public static String TAG = "Someone2";
    public static String APPODEAL_KEY = "9d0c6d8eb37ac206ef42efb3cc408c13b3366be349cba658";
    public static String TO_MAIL = "naveen.orem@gmail.com";
    public static final int REQUEST_CAMERA_CAPTURE = 21;
    public static String FILE_PROVIDER = "com.kapalert.kadunastategovernment.android.fileprovider";


    //    2017-09-07 08:09:20
    public static SimpleDateFormat SET_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String API_EXCHANGE_TYPE_ROLE = "Role";
    public static final String API_EXCHANGE_TYPE_USER = "User";
    public static final String API_FORWARD_TYPE_SEND = "Send";
    public static final String API_FORWARD_TYPE_ACCEPT = "Accept";

    public static String RESPONSE_TRUE = "1";

    public static final String FB_USER_ONLINE = "online";
    public static final String FB_USER_OFFLINE = "offline";

    /*
    * user role list
===============
1  = Admin
22 = Counsel(Lawyer)
23 = civil litigation
24 = criminal Litigation
25 = DPP
    * */
    public static final String USER_ROLE_COUNSEL = "22";
    public static final String USER_ROLE_LITIGATION_CRIMINAL = "24";
    public static final String USER_ROLE_LITIGATION_CIVIL = "23";
    public static final String USER_ROLE_DPP = "25";

    /*
    -----------------------------
    SHARED PREFERENCES (TINYDB) KEYS
    -----------------------------
    */
    private static final String DB_USER_DATA = "user_data_sdlkfsjtoweit";
    public static final String DB_SELECTED_CATEGORIES = "selectedCategories_seklrewj593285";
    public static final String DB_SELECTED_RADIUS = "selected_radius_sdlkrjew95832";
    public static final String DB_SELECTED_GROUP_CHAT = "selected_group_chat_5r93wksdfsdjkfre";
    public static final String DB_SELECTED_PRIVATE_CHAT = "selected_private_chat_dslfjt2390523";
    public static final String DB_REMEMBER_ME = "remember_me_dsklrjewiro";
    public static final String USER_ID = "user_id";
    public static final String NOTICE_RELATED_CASE_ID = "id";
    public static final String APPEAL_RELATED_CASE_ID = "id";
    public static final String MOTION_RELATED_CASE_ID = "id";


    /*
    -----------------------------
    FIREBASE DATABASE KEYS
    -----------------------------
    */

    public static final String FB_GROUP_CHATS = "groupChats";
    public static final String FB_PRIVATE_CHATS = "privateChats";
    public static final String FB_PRIVATE_CHAT = "chat";
    public static final String FB_PRIVATE_CHAT_LAST_MESSAGE = "last_message";
    public static final String FB_PRIVATE_CHAT_LAST_MESSAGE_TIME = "last_message_time";
    public static final String FB_PRIVATE_CHAT_DATA = "chatData";
    public static final String FB_PRIVATE_CHAT_DATA_USER_1 = "user_1";
    public static final String FB_PRIVATE_CHAT_DATA_USER_2 = "user_2";
    public static final String FB_PRIVATE_USERS_STATUS = "chatStatus";
    public static final String FB_PRIVATE_USERS_1_STATUS = "user1_status";
    public static final String FB_PRIVATE_USERS_2_STATUS = "user2_status";
    public static final String FB_PRIVATE_CHAT_DATA_USER_1_TOKEN = "user1_token";
    public static final String FB_PRIVATE_CHAT_DATA_USER_2_TOKEN = "user2_token";
    public static final String FB_GROUP_DATA = "group_data";
    public static final String FB_USER_ONLINE_STATUS_KEY = "online_status";
    public static final String FB_GROUP_MEMBERS = "users";

    public static final String CHAT_SENDERID = "senderID";
    public static final String CHAT_SENDERNAME = "senderName";
    public static final String CHAT_RECIPIENTID = "recipientID";
    public static final String CHAT_RECIPIENTNAME = "recipientName";
    public static final String CHAT_MESSAGE = "message";
    public static final String CHAT_TIME = "time";
    public static String DB_SELECTED_FILE_CASE = "select_file_case_sdflkwejt5o4325";
    public static String DB_SELECTED_EVENT = "selected_event_dskflejtew9t08325";
    public static String DB_SELECTED_CASE = "selectec_case_sflksrj29385";
    public static String DB_SELECTED_CHAT = "selected_chat_sdlfkjwert985342";
    public static String DB_SELECTED_CHAT_NEW = "selected_chat_new_dkltwejtoiwe325";

    /*
    -----------------------------
    API CALL METHODS
    -----------------------------
    */



    public static Call<UserInfoJson> getLoginUrl(String email, String password) {
        String deviceToken = "";
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken == null || deviceToken.isEmpty()) {
            deviceToken = "noDeviceToken";
        }
        return Utils.requestApiDefault().requestUserInfo("login/pro?email=" + email + "&pass=" + password + "&deviceToken=" + deviceToken);
    }

    public static Call<UserInfoJson> getForgotPassUrl(String email) {
        return Utils.requestApiDefault().requestUserInfo("login/forgotpw_pro?email=" + email);
    }

    public static Call<UserInfoJson> getUpdatePassUrl(Context context, String newPassword) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("password", getRequestBodyParam(newPassword));

        return Utils.requestApiDefault().requestCommonJson("login/update_password", map);
    }

    public static Call<CaseDetailPOJO> getCaseDetailUrl(Context context, String caseID) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("case_id", getRequestBodyParam(caseID));

        return Utils.requestApiDefault().requestCaseDetail("caseblock/trackcasemob", map);
    }

    public static Call<AnnouncementListPOJO> getAnnouncementListUrl(Context context) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));

        return Utils.requestApiDefault().requestAnnouncementList("caseblock/get_announcement_list", map);
    }

    public static Call<OffencePOJO> getNatureOffenceUrl(Context context) {
        return Utils.requestApiDefault().requestOffences("caseblock/offenceList?token=" + getUser(context).token);
    }

    public static Call<OffencePOJO> getInvestigationOffenceUrl(Context context) {
        return Utils.requestApiDefault().requestOffences("caseblock/investigate_unit?token=" + getUser(context).token);
    }

    public static Call<OffencePOJO> getLocOffenceUrl(Context context) {
        return Utils.requestApiDefault().requestOffences("caseblock/locationOfOffence?token=" + getUser(context).token);
    }

    public static Call<CaseListPOJO> getCaseListUrl(Context context, String casetype,String tabselect) {
        UserInfoJson.UserData user = getUser(context);
        String baseUrl = "case_list";
        if (user.userRole.equalsIgnoreCase(Constants.USER_ROLE_COUNSEL)) {
            baseUrl = "counsel_case_list";
        } else if (user.userRole.equalsIgnoreCase(Constants.USER_ROLE_DPP)) {
            baseUrl = "dpp_case_list";

        }
        return Utils.requestApiDefault().requestCaseList("caseblock/" + baseUrl + "?token=" + user.token + "&user_id=" + user.id+"&casetype="+casetype+"&type="+tabselect);
    }

    public static Call<FileListPOJO> getFileListUrl(Context context, String caseID) {
        return Utils.requestApiDefault().requestFileList("caseblock/case_files_by_id?token=" + getUser(context).token + "&case_id=" + caseID);
    }

    public static Call<UserListPOJO> getUserListUrl(Context context) {
        return Utils.requestApiDefault().requestUserList("caseblock/allUsersList?token=" + getUser(context).token);
    }

    public static Call<CounselCommentsPOJO> getCounselCommentListUrl(Context context, String caseID) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));

        return Utils.requestApiDefault().requestCounselComments("caseblock/commentsList", map);
    }

    public static Call<ChatListPOJO> getChatMessageListUrl(Context context, String chatID, String message, String isUserNew, String toUsername, String loggedUsername, String loggedUserID) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("chat_id", getRequestBodyParam(chatID));
        map.put("message", getRequestBodyParam(message));
        map.put("isUserNew", getRequestBodyParam(isUserNew));
        map.put("username", getRequestBodyParam(toUsername));
        map.put("login_username", getRequestBodyParam(loggedUsername));
        map.put("login_userID", getRequestBodyParam(loggedUserID));

        return Utils.requestApiDefault().requestChatList("caseblock/sendMessages", map);
    }

    public static Call<InboxListPOJO> getInboxListUrl(Context context) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));

        return Utils.requestApiDefault().requestInboxList("caseblock/get_active_chats", map);
    }

 public static Call<NoteJSON> requestSendNote(Context context, String caseID, String note) {
        HashMap<String, RequestBody> map = new HashMap<>();
     map.put("token", getRequestBodyParam(getUser(context).token));
     map.put("user_id", getRequestBodyParam(getUser(context).id));
     map.put("case_id", getRequestBodyParam(caseID));
     map.put("case_note", getRequestBodyParam(note));

        return Utils.requestApiDefault().requestSendNote("caseblock/casenotesubmit", map);
    }



    public static Call<ChatListPOJO> getChatListUrl(Context context, String chatID) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("chat_id", getRequestBodyParam(chatID));

        return Utils.requestApiDefault().requestChatList("caseblock/get_single_user_chat_messages", map);
    }

    public static Call<CounselAccusedListPOJO> getCounselAccusedListUrl(Context context, String caseID) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));

        return Utils.requestApiDefault().requestCounselAccuseds("caseblock/accused_list", map);
    }

    public static Call<UserInfoJson> getAccusedRecommendationUrl(Context context, String caseID, String accusedJson, String stepNum) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("step_no", getRequestBodyParam(stepNum));
        map.put("accused_data", getRequestBodyParam(accusedJson));

        return Utils.requestApiDefault().requestCommonJson("caseblock/accused_recommendations", map);
    }

    public static Call<UserInfoJson> getCreateCounselCommentUrl(Context context, String caseID, String comment, File file) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("comments_on_case", getRequestBodyParam(comment));

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);

        return Utils.requestApiDefault().requestCommonJson("caseblock/commentAndForward", map);
    }

    public static Call<UserInfoJson> getCreateLitigationCommentUrl(Context context, String caseID, String type, File file) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("get_type", getRequestBodyParam(type));

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);

        return Utils.requestApiDefault().requestCommonJson("caseblock/commentAndForwardByLititgation", map);
    }

    public static Call<PoliceFileListPOJO> getPoliceFileListUrl(Context context, String caseID) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("step_no", getRequestBodyParam("10"));

        return Utils.requestApiDefault().requestPoliceFileList("caseblock/get_Upload_Data_By_Litigation_list", map);
    }


    public static Call<UserInfoJson> getPoliceDateUrl(Context context, String caseID, String date) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam("25"));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("step_no", getRequestBodyParam("9"));
        map.put("send_time", getRequestBodyParam(date));

        return Utils.requestApiDefault().requestCommonJson("caseblock/forward_to_police", map);
    }


    public static Call<UserListPOJO> getCounselListUrl(Context context) {
        return Utils.requestApiDefault().requestUserList("caseblock/lawyerList?token=" + getUser(context).token + "&username=" + getUser(context).first_name);
    }



    public static Call<CourtListPOJO> getCourtListUrl(Context context) {
        return Utils.requestApiDefault().requestCourtList("caseblock/courtList?token=" + getUser(context).token);
    }

    public static Call<CommonListPOJO> getAllListUrl(Context context) {
        return Utils.requestApiDefault().requestCommonList("caseblock/collaborationList?token=" + getUser(context).token);
    }

    public static Call<JudgeListPOJO> getJudgesListUrl(Context context) {
        return Utils.requestApiDefault().requestJudgeList("caseblock/judgeList?token=" + getUser(context).token);
    }

    public static Call<EventListPOJO> getEventListUrl(Context context) {
        return Utils.requestApiDefault().requestEventList("caseblock/listEvent?token=" + getUser(context).token + "&userid=" + getUser(context).id);
    }

    public static Call<UserInfoJson> getEventDeleteUrl(Context context, String eventID) {
        return Utils.requestApiDefault().requestUserInfo("caseblock/deleteEvent?token=" + getUser(context).token + "&eventId=" + eventID);
    }

    public static Call<UserInfoJson> getEventCreateUrl(Context context, String name, String description, String startTime, String endTime, String selectedUserStr) {
        return Utils.requestApiDefault().requestUserInfo("caseblock/createEvent?token=" + getUser(context).token + "&name=" + name + "&description=" + description + "&start_time=" + startTime + "&end_time=" + endTime + "&userid=" + getUser(context).id + "&to_users=" + selectedUserStr);
    }

    public static Call<UserInfoJson> getCaseProgressUrl(Context context, String caseID, String exchangeType, String forwardType, String toUserRole, String stageNum, String stepNum) {
        return Utils.requestApiDefault().requestUserInfo("caseblock/case_progress?token=" + getUser(context).token + "&userid=" + getUser(context).id + "&case_id=" + caseID + "&exchange_type=" + exchangeType + "&forward_type=" + forwardType + "&to_user_or_role_id=" + toUserRole + "&stage_no=" + stageNum + "&step_no=" + stepNum);
    }

    public static Call<UserInfoJson> getCaseProgress(Context context, String caseID, String exchangeType, String forwardType, String toUserRole, String stageNum, String stepNum) {
        return Utils.requestApiDefault().requestUserInfo("caseblock/case_progress?token=" + getUser(context).token + "&userid=" + getUser(context).id + "&case_id=" + caseID + "&exchange_type=" + exchangeType + "&forward_type=" + forwardType + "&to_user_or_role_id=" + getUser(context).id+ "&stage_no=" + stageNum + "&step_no=" + stepNum);
    }

    public static Call<MdaInvokedRespons> mdaInvokedResponsCall(Context context) {
        return Utils.requestApiDefault().MDA_INVOKED_RESPONS_CALL("caseblock/mdalist?token=" + getUser(context).token);
    }
    public static Call<DisputeRespons> disputeResponsCall(Context context) {
        return Utils.requestApiDefault().DISPUTE_RESPONS_CALL("caseblock/locationOfOffence?token=" + getUser(context).token);
    }
    public static Call<ActionListRespons> actionListResponsCall(Context context) {
        return Utils.requestApiDefault().ACTION_LIST_RESPONS_CALL("caseblock/actionlist?token=" + getUser(context).token);
    }
    public static Call<AssignRespons> assignResponsCall (Context context) {
        return Utils.requestApiDefault().ASSIGN_RESPONS_CALL("caseblock/groupList?token=" + getUser(context).token);
    }
    public static Call<UserInfoJson> getForm1SubmitUrl(Context context, String caseID, String dateCPETyping, String dateCPECopies, String dateCPESent, String dateLitigationSent, String courtID) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("date_getfrom_litigation", getRequestBodyParam(""));
        map.put("date_cpe_sent_for_typing", getRequestBodyParam(dateCPETyping));
        map.put("date_cpe_copies_cpe_ready", getRequestBodyParam(dateCPECopies));
        map.put("date_cpe_sent_litigation_dispatch", getRequestBodyParam(dateLitigationSent));
        map.put("date_cpe_sent_tocourt", getRequestBodyParam(dateCPESent));
        map.put("court_id", getRequestBodyParam(courtID));

        return Utils.requestApiDefault().requestCommonJson("caseblock/form1Submit", map);

    }

    public static Call<UserInfoJson> getUpdateProfileUrl(Context context, String firstName, String lastName, String phone, File file) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("first_name", getRequestBodyParam(firstName));
        map.put("last_name", getRequestBodyParam(lastName));
        map.put("phone", getRequestBodyParam(phone));

        if (file != null) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);
        }

        return Utils.requestApiDefault().requestCommonJson("login/updateProfile", map);

    }

    public static Call<UserInfoJson> getForm2SubmitUrl(Context context, String caseID, String dateNoticeReceived, String dateFirstHearing, String courtID, String judgeID, String refNum) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("date_hearing_notice_received", getRequestBodyParam(dateNoticeReceived));
        map.put("date_of_first_hearing", getRequestBodyParam(dateFirstHearing));
        map.put("name_of_trial_court", getRequestBodyParam(courtID));
        map.put("name_of_trial_judge", getRequestBodyParam(judgeID));
        map.put("court_case_file_reference_number", getRequestBodyParam(refNum));

        return Utils.requestApiDefault().requestCommonJson("caseblock/form2Submit", map);
    }

    public static Call<UserInfoJson> getForm3SubmitUrl(Context context, String caseID, String adjournmentJson, String prosecutionCommences, String prosecutionCompleted, String defenseCommence, String defenseCompleted, String addressComplete, String judgementDate, String sentencingDate, String finalCaseToDPP, String closeDate, String accusedJudgementJson, String dateOfFirstMention, String judgementNarrative) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("step_no", getRequestBodyParam("15"));
        map.put("adjournment_data", getRequestBodyParam(adjournmentJson));
        map.put("date_of_first_mention", getRequestBodyParam(dateOfFirstMention));
        map.put("judgement_narrative", getRequestBodyParam(judgementNarrative));
        map.put("date_prosecution_case_commences", getRequestBodyParam(prosecutionCommences));
        map.put("date_prosecution_case_completed", getRequestBodyParam(prosecutionCompleted));
        map.put("date_defense_case_commenced", getRequestBodyParam(defenseCommence));
        map.put("date_defense_case_completed", getRequestBodyParam(defenseCompleted));
        map.put("date_address_completed", getRequestBodyParam(addressComplete));
        map.put("date_of_judgment", getRequestBodyParam(judgementDate));
        map.put("date_of_sentencing", getRequestBodyParam(sentencingDate));
        map.put("date_of_final_case_report_to_dpp", getRequestBodyParam(finalCaseToDPP));
        map.put("case_closed_date", getRequestBodyParam(closeDate));
        map.put("judgement_for_each_accused", getRequestBodyParam(accusedJudgementJson));

        return Utils.requestApiDefault().requestCommonJson("caseblock/form3Submit", map);
    }

    public static Call<UserInfoJson> getEventEditUrl(Context context, String eventID, String name, String description, String startTime, String endTime, String selectedUserStr) {
        return Utils.requestApiDefault().requestUserInfo("caseblock/updateEvent?token=" + getUser(context).token + "&eventId=" + eventID + "&name=" + name + "&description=" + description + "&start_time=" + startTime + "&end_time=" + endTime + "&userid=" + getUser(context).id + "&to_users=" + selectedUserStr);
    }

    public static Call<ResponseBody> getFileDownloadUrl(String fileUrl) {
        return Utils.requestApi().requestFile(fileUrl);
    }

    public static String getCaseFileUrlStr(String caseID, String fileName) {
        return FILES_BASE_URL + caseID + "/" + fileName;
    }

    public static Call<UserInfoJson> getCreateCaseUrl(Context context, File file, String caseName, String offenceDate, String locationID, String refNum, String investigateID, String fileReceived, String daysBet, String arrestDate, String jsonData) {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("file_name", getRequestBodyParam(caseName));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("date_of_offence", getRequestBodyParam(offenceDate));
        map.put("location_of_offence", getRequestBodyParam(locationID));
        map.put("police_case_file_ref", getRequestBodyParam(refNum));
        map.put("investigate_unit", getRequestBodyParam(investigateID));
        map.put("date_p_file_received", getRequestBodyParam(fileReceived));
        map.put("days_from_offence_to_arrest", getRequestBodyParam(daysBet));
        map.put("date_of_arrest", getRequestBodyParam(arrestDate));
        map.put("case_data", getRequestBodyParam(jsonData));

        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);

        return Utils.requestApiDefault().requestCommonJson("caseblock/createCase", map);
    }
    public static Call<UserInfoJson> getCreateCaseUrl1(Context context,String user_id, List<BeanUploadImage> uploadImages, String caseName, String plaintiffs, String defendants, String mdavalue, String jsonData, String location_of_dispute, String datereqmda, String datebrimda, String assignvalue) {
        Log.e("plaintage",plaintiffs);
        Log.e("defendants",defendants);
        Log.e("mdavalue",mdavalue);
        Log.e("jsonData",jsonData);
        Log.e("location_of_dispute",location_of_dispute);
        Log.e("datereqmda",datereqmda);
        Log.e("datebrimda",datebrimda);
        Log.e("=========token=======",getUser(context).token);

        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("case",getRequestBodyParam("civil"));
        map.put("file_name", getRequestBodyParam(caseName));
        map.put("no_of_plaintiffs", getRequestBodyParam(plaintiffs));
        map.put("no_of_defendants", getRequestBodyParam(defendants));
        map.put("user_id", getRequestBodyParam(user_id));
        map.put("mda_involved", getRequestBodyParam(mdavalue));
        map.put("location_of_dispute", getRequestBodyParam(location_of_dispute));
        map.put("date_requested_by_mda", getRequestBodyParam(datereqmda));
        map.put("date_briefing_mda", getRequestBodyParam(datebrimda));
        map.put("casedata", getRequestBodyParam(jsonData));
        map.put("group[]", getRequestBodyParam(assignvalue));
        RequestBody requestBody=null;
        for(int i=0;i<uploadImages.size();i++) {
           /// RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), sourceFile);
            String filePaths = String.valueOf(uploadImages.get(i).getUri());
            File sourceFile = new File(filePaths);
            System.out.println("======sourceFile============="+sourceFile);
           requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),sourceFile);
         ///   System.out.println("======requestBody============="+requestBody.toString());

        ///  map.put("casefile["+i+"]", MultipartBody.create(MediaType.parse("multipart/form-data"), sourceFile));

        }
        map.put("casefile[]", requestBody);
        return Utils.requestApiDefault().requestCommonJson("caseblock/createCase?token="+getUser(context).token, map);
    }

    public static Call<UserInfoJson> forwarddata(Context context,File file, String caseID, String type,String name ) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("file_type", getRequestBodyParam(type));
        map.put("casefilename", getRequestBodyParam(name));
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);

        return Utils.requestApiDefault().requestCommonJson("caseblock/upoadFiles", map);
    }


    public static Call<UserInfoJson> forwardcunseldata(Context context,File file, String caseID, String type,String name ) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("file_type", getRequestBodyParam(type));
        map.put("casefilename", getRequestBodyParam(name));
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        map.put("casefile\"; filename=\"" + "file_" + System.currentTimeMillis() + "\" ", body);

        return Utils.requestApiDefault().requestCommonJson("caseblock/upoadFiles", map);
    }


    public static Call<UserInfoJson> cunseldata(Context context,String caseID,String cunselid ) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", getRequestBodyParam(getUser(context).token));
        map.put("user_id", getRequestBodyParam(getUser(context).id));
        map.put("case_id", getRequestBodyParam(caseID));
        map.put("counsel", getRequestBodyParam(cunselid));

        return Utils.requestApiDefault().requestCommonJson("caseblock/assignupoadFiles", map);
    }

    /*
    -----------------------------
    OTHER PROJECT USEFUL METHODS
    -----------------------------
    */

    public static RequestBody getRequestBodyParam(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    public static boolean isUserLoggedIn(Context context) {
        TinyDB db = new TinyDB(context);
        UserInfoJson.UserData userPOJO = new Gson().fromJson(db.getString(DB_USER_DATA), UserInfoJson.UserData.class);
        return (userPOJO != null && userPOJO.id != null && !userPOJO.id.isEmpty());
    }

    public static void setUser(Context mContext, UserInfoJson.UserData userPOJO) {
        TinyDB db = new TinyDB(mContext);

        String userStr = new Gson().toJson(userPOJO);
        if (userPOJO == null) {
            db.remove(DB_USER_DATA);
            return;
        }

        db.putString(DB_USER_DATA, userStr);

        AppBaseActivity baseActivity = ((AppBaseActivity) mContext);
        if (baseActivity != null) {
            baseActivity.userPOJO = userPOJO;
        }
    }

    public static UserInfoJson.UserData getUser(Context mContext) {
        TinyDB db = new TinyDB(mContext);
        String userStr = db.getString(DB_USER_DATA);

        UserInfoJson.UserData user = new Gson().fromJson(userStr, UserInfoJson.UserData.class);

        return user;
    }

    public static String getDateStr(String unFormattedDate) {
        try {
            return new SimpleDateFormat("dd MMM yyyy 'at' hh:mm a").format(SET_DATE_FORMAT.parse(unFormattedDate));
        } catch (Exception e) {
            e.printStackTrace();
            return unFormattedDate;
        }
    }


/*
    public static void hideViews(View... views) {
        for (View view:views){
            view.setVisibility(View.GONE);
        }
    }
    public static void unHideViews(View... views) {
        for (View view:views){
            view.setVisibility(View.VISIBLE);
        }
    }
*/
}
