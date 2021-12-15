package com.application.a4_school.RestAPI;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;

import com.application.a4_school.R;
import com.application.a4_school.ui.classroom.DetailClassRoomActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadFromUrl {
    private static final String TAG = "DownloadTask";
    private Context context;

    private String downloadUrl = "", downloadFileName = "", extension="", mime="";
    private ProgressDialog progressDialog;

    public DownloadFromUrl(Context context, String downloadUrl, String downloadFileName, String extension, String mime) {
        this.context = context;
        this.downloadUrl = downloadUrl;
        this.downloadFileName = downloadFileName;
        this.extension = extension;
        this.mime = mime;
    }

    public void downloadFile(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://192.168.43.3/api/");
        Retrofit retrofit = builder.build();
        APIService api = retrofit.create(APIService.class);
        Call<ResponseBody> download = api.downloadFile(downloadUrl);
        download.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean success = writeResponseBodyToDisk(response.body());
                            if (success == true){
                                progressDialog.dismiss();
                            }else {
                                progressDialog.dismiss();
                            }
                            Log.d(TAG, "file download was a success? " + success);
                            Log.d(TAG, "file download was a success? " + response.body());
                            return null;
                        }
                    }.execute();
                }else{
                    Log.d(TAG, "server contact failed");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "error "+t.getMessage());
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File files = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator, downloadFileName+"."+extension);
            Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", files);
            Log.d(TAG, "path: "+path);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(files);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                Uri openPath = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", files);
                Intent openFile = new Intent(Intent.ACTION_VIEW);
                openFile.setDataAndType(openPath, mime);
                openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(openFile);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
