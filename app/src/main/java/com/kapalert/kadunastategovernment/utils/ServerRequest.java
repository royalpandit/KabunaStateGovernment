package com.kapalert.kadunastategovernment.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;


import androidx.appcompat.app.AppCompatActivity;

import com.kapalert.kadunastategovernment.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 11/22/2016.
 */

public abstract class ServerRequest<T> {

    private Context mContext;
    private Call<T> call;
    private ProgressDialog downlaodProgress;
    CustomProgressDialog pd;

    /**
     * @param mContext     Context of the activity.
     * @param call         api call that needs to be executed.
     * @param showProgress show progress or not.
     */
    public ServerRequest(final Context mContext, Call<T> call, boolean showProgress) {
        this.mContext = mContext;
        this.call = call;

        if (Utils.isNetworkConnected(mContext)) {
            if (showProgress) {
                pd = new CustomProgressDialog(mContext);
                pd.show();
            }

            Utils.showLog(call.request().url().toString());
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    try {
                        if (pd.isShowing())
                            pd.dismiss();
                    } catch (Exception e) {

                    }
                    if (response.isSuccessful())
                        afterResponse(mContext, call, response);
                    else
                        Utils.showToast(mContext, mContext.getString(R.string.invalid_response_error));
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    try {
                        if (pd.isShowing())
                            pd.dismiss();
                    } catch (Exception e) {

                    }
                    Utils.showLog("Exp : " + t.getMessage());
                    Utils.showMessageDialog(mContext, mContext.getString(R.string.app_says), mContext.getString(R.string.something_went_wrong));
                }
            });
        } else {
            Utils.showMessageDialog(mContext, mContext.getString(R.string.app_says), mContext.getString(R.string.no_internet_message));
        }
    }

    private void afterResponse(Context mContext, Call<T> call, Response<T> response) {
        onCompletion(call, response);
    }

    public abstract void onCompletion(Call<T> call, Response<T> response);

    public ServerRequest(final Context mContext, Call<ResponseBody> call) {
        this.mContext = mContext;

        final CustomProgressDialog pd = new CustomProgressDialog(mContext);
        pd.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, Response<ResponseBody> response) {
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
                            final File file = writeResponseBodyToDisk(responseBody, call.request().url().toString()); //writeResponseBodyToDisk() is returning path where file is downloaded.

                            downlaodProgress.dismiss();
                            ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
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

    private File writeResponseBodyToDisk(ResponseBody body, String selectedFileUrl) {

        String[] urlSplitted = selectedFileUrl.split("/");
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
