package com.kapalert.kadunastategovernment.utils;

import com.kapalert.kadunastategovernment.request.mdainvokedrequest;
import com.kapalert.kadunastategovernment.response.ActionList;
import com.kapalert.kadunastategovernment.response.ActionListRespons;
import com.kapalert.kadunastategovernment.response.AssignRespons;
import com.kapalert.kadunastategovernment.response.DisputeRespons;
import com.kapalert.kadunastategovernment.response.MdaInvokedRespons;
import com.kapalert.kadunastategovernment.templates.AnnouncementListPOJO;
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

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Dawinder on 16/02/2016.
 */
public interface Apis {

    @GET
    Call<ResponseBody> requestJson(@Url String url);

    @GET
    Call<UserInfoJson> requestUserInfo(@Url String url);

    @GET
    Call<OffencePOJO> requestOffences(@Url String url);

    @GET
    Call<CaseListPOJO> requestCaseList(@Url String url);

    @GET
    Call<FileListPOJO> requestFileList(@Url String url);

    @GET
    Call<EventListPOJO> requestEventList(@Url String url);

    @GET
    Call<UserListPOJO> requestUserList(@Url String url);

    @GET
    Call<CourtListPOJO> requestCourtList(@Url String url);

    @GET
    Call<JudgeListPOJO> requestJudgeList(@Url String url);

    @GET
    Call<CommonListPOJO> requestCommonList(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> requestFile(@Url String url);

    @Multipart
    @POST
    Call<UserInfoJson> requestCommonJson(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<CaseDetailPOJO> requestCaseDetail(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<PoliceFileListPOJO> requestPoliceFileList(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<CounselCommentsPOJO> requestCounselComments(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<InboxListPOJO> requestInboxList(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<NoteJSON> requestSendNote(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<ChatListPOJO> requestChatList(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<AnnouncementListPOJO> requestAnnouncementList(@Url String url, @PartMap HashMap<String, RequestBody> map);

    @Multipart
    @POST
    Call<CounselAccusedListPOJO> requestCounselAccuseds(@Url String url, @PartMap HashMap<String, RequestBody> map);


    @GET
    Call<MdaInvokedRespons> MDA_INVOKED_RESPONS_CALL(@Url String url);

    @GET
    Call<DisputeRespons> DISPUTE_RESPONS_CALL(@Url String url);

    @GET
    Call<ActionListRespons> ACTION_LIST_RESPONS_CALL(@Url String url);
    @GET
    Call<AssignRespons> ASSIGN_RESPONS_CALL(@Url String url);
}
