package com.kapalert.kadunastategovernment.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.activities.CreateCaseActivity;
import com.kapalert.kadunastategovernment.templates.CaseListPOJO;
import com.kapalert.kadunastategovernment.templates.CounselCommentsPOJO;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.CustomProgressDialog;
import com.kapalert.kadunastategovernment.utils.TinyDB;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by win10 on 6/22/2017.
 */

public class AdapterCounselCommentList extends RecyclerView.Adapter<AdapterCounselCommentList.CounselCommentHolder> {

    private Context mContext;
    private ArrayList<CounselCommentsPOJO.Comment> mList;
    private String mSelectedFileUrl;
    private ProgressDialog downlaodProgress;

    public AdapterCounselCommentList(Context mContext, ArrayList<CounselCommentsPOJO.Comment> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CounselCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_comment_counsel, parent, false);
        return new CounselCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(final CounselCommentHolder holder, int position) {
        final CounselCommentsPOJO.Comment data = mList.get(position);

        holder.comment.setText(data.comment);
        holder.fileName.setText(String.format(mContext.getString(R.string.file_name_format), data.fileName));

        holder.fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDialog(mContext, mContext.getString(R.string.are_you_sure), mContext.getString(R.string.download_file), mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadFile(data.fileName);
                    }
                }, null, mContext.getString(R.string.no), null, null, true);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CounselCommentHolder extends RecyclerView.ViewHolder {
        TextView comment, fileName;

        public CounselCommentHolder(View itemView) {
            super(itemView);

            comment = (TextView) itemView.findViewById(R.id.comment);
            fileName = (TextView) itemView.findViewById(R.id.comment_file);
        }
    }

    private void downloadFile(String url) {

        CaseListPOJO.CaseList caseObj = new Gson().fromJson(new TinyDB(mContext).getString(Constants.DB_SELECTED_CASE), CaseListPOJO.CaseList.class);
        mSelectedFileUrl = Constants.getCaseFileUrlStr(caseObj.id, url);
        final CustomProgressDialog pd = new CustomProgressDialog(mContext);
        pd.show();
        Call<ResponseBody> call = Constants.getFileDownloadUrl(mSelectedFileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (pd.isShowing())
                    pd.dismiss();
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();

                    downlaodProgress = new ProgressDialog(mContext);
                    downlaodProgress.setMax(100);
                    downlaodProgress.setTitle(mContext.getString(R.string.file_downloading));
                    downlaodProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    downlaodProgress.setProgress(0);
                    downlaodProgress.setProgressNumberFormat(null);
                    downlaodProgress.setCancelable(false);
                    downlaodProgress.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final File file = writeResponseBodyToDisk(responseBody); //writeResponseBodyToDisk() is returning path where file is downloaded.

                            downlaodProgress.dismiss();
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (file == null) {
                                        Utils.showLog(mContext.getString(R.string.file_download_failed));
                                        return;
                                    }
                                    Utils.showLog("file download was a success? " + file.getAbsolutePath());
                                    Utils.showToast(mContext, mContext.getString(R.string.file_downloaded_successfully));

                                }
                            });

                        }
                    }).start();

                } else
                    Utils.showToast(mContext, mContext.getString(R.string.invalid_data_found));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (pd.isShowing())
                    pd.dismiss();
                Utils.showToast(mContext, mContext.getString(R.string.something_went_wrong));
            }
        });

    }

    private File writeResponseBodyToDisk(ResponseBody body) {
        String[] urlSplitted = mSelectedFileUrl.split("/");
        String fileName = urlSplitted[urlSplitted.length - 1];
        fileName = fileName.replace("(", "_").replace(")", "_").replace(" ", "_");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        try {
            Utils.showLog(file.getAbsolutePath());
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    final long finalFileSizeDownloaded = fileSizeDownloaded;
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                double filedown = ((double) fileSize / finalFileSizeDownloaded);
                                double downvalue = 100 / filedown;
                                downlaodProgress.setProgress(Double.valueOf(downvalue).intValue());
                            } catch (Exception e) {

                            }
                        }
                    });
                    Utils.showLog("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
