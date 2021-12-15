package com.application.a4_school.ui.classroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.a4_school.Auth.Login;
import com.application.a4_school.Models.ClassRoom;
import com.application.a4_school.Models.FilesUpload;
import com.application.a4_school.Models.Members;
import com.application.a4_school.R;
import com.application.a4_school.RestAPI.APIClient;
import com.application.a4_school.RestAPI.APIService;
import com.application.a4_school.RestAPI.DownloadFromUrl;
import com.application.a4_school.RestAPI.ResponseData;
import com.application.a4_school.UserInteraction.BottomSheet;
import com.application.a4_school.UserInteraction.BottomSheetFile;
import com.application.a4_school.adapter.ClassFilesAdapter;
import com.application.a4_school.adapter.MemberClassListAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClassRoomActivity extends AppCompatActivity {
    private TextView shDeadline, shTitle, shDetail, shPoint, shAttachment, shStatAssign;
    private ClassFilesAdapter adapter;
    private RecyclerView rv_files, rv_files_assignment, rv_completeduser;
    public static final int REQUEST_FILE = 9542;
    private String type;
    private static String file_url;
    private String id_taskclass;
    MemberClassListAdapter adapterMembers;
    private LinearLayout btmSheetAsiignment, containerCompletedUser;
    private List<FilesUpload> listFilesGuru = new ArrayList<>();
    private List<FilesUpload> listFilesSiswa = new ArrayList<>();
    private ArrayList<Members> listMembers = new ArrayList<>();
    private Button btnAttach, btnAssign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_class);
        initialize();
        String condition = getIntent().getStringExtra("EXTRA_CONDITION");
        ClassRoom classdata = getIntent().getParcelableExtra("EXTRA_PARCEL_CLASS");
        String id_task = getIntent().getStringExtra("EXTRA_ID_TASK");
        if (condition.equals("checkCompletedStudent")) {
            getItemFileGuru();
            id_taskclass = id_task;
        } else {
            id_taskclass = classdata.getId_taskclass();
            type = classdata.getType();
            String role = getSharedPreferences("session", 0).getString("role", "");
            switch (role) {
                case "siswa":
                    switch (type) {
                        case "Task":
                            shDeadline.setText("deadline: " + reformatdate(classdata.getDeadline()));
                            getItemFilesiswa();
                            break;
                        case "Theory":
                            shPoint.setVisibility(View.GONE);
                            shDeadline.setVisibility(View.GONE);
                            btmSheetAsiignment.setVisibility(View.GONE);
                            break;
                    }
                    break;

                case "guru":
                    shPoint.setVisibility(View.GONE);
                    btmSheetAsiignment.setVisibility(View.GONE);
                    switch (type) {
                        case "Task":
                            btmSheetAsiignment.setVisibility(View.GONE);
                            shDeadline.setText("deadline: " + reformatdate(classdata.getDeadline()));
                            containerCompletedUser.setVisibility(View.VISIBLE);
                            getListCompletedStudent(id_taskclass);
                            break;
                        case "Theory":
                            shDeadline.setVisibility(View.GONE);
                            break;
                    }
                    break;
            }
            if (classdata.getFile_url() == null) {
                shAttachment.setVisibility(View.GONE);
            } else {
                getItemFileGuru();
            }
            shTitle.setText(classdata.getTitle());
            shDetail.setText(classdata.getDescription());

            btnAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openMedia = new Intent(Intent.ACTION_GET_CONTENT);
                    openMedia.setType("*/*");
                    startActivityForResult(openMedia, REQUEST_FILE);
                }
            });
            btnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assign();
                }
            });
        }
    }

    private void initialize() {
        shDeadline = findViewById(R.id.tv_deadline_detail_class);
        shPoint = findViewById(R.id.tv_point_detail_class);
        shTitle = findViewById(R.id.tv_title_detail_class);
        shDetail = findViewById(R.id.tv_detail_detail_class);
        shAttachment = findViewById(R.id.txt_attach);
        rv_files = findViewById(R.id.rv_files_detail);
        btmSheetAsiignment = findViewById(R.id.btm_assignment);
        rv_files_assignment = findViewById(R.id.rv_files_assignment);
        btnAttach = findViewById(R.id.btn_attach);
        btnAssign = findViewById(R.id.btn_assign);
        shStatAssign = findViewById(R.id.status_assign);
        rv_completeduser = findViewById(R.id.rv_completedtask);
        containerCompletedUser = findViewById(R.id.container_completedtask);
    }

    private void getItemFileGuru() {
        APIService api = APIClient.getClient().create(APIService.class);
        Call<ResponseData> loadFile = api.getListFiles(id_taskclass, 0, "file_guru");
        loadFile.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getFilesDetail() != null) {
                        listFilesGuru.addAll(response.body().getFilesDetail());
                        adapter = new ClassFilesAdapter(listFilesGuru, DetailClassRoomActivity.this, "detail");
                        adapter.notifyDataSetChanged();
                        rv_files.setAdapter(adapter);
                        adapter.setOnItemClickCallback(new ClassFilesAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(FilesUpload filesUpload, int index) {
                                addListFiles(filesUpload.getFile_url(), filesUpload.getNamefile(), filesUpload.getTypefile(), index);
                            }
                        });

                    }
                } else {
                    Log.d("DetailLoadfile", "not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("DetailLoadfile", "failure: " + t.getMessage());
            }
        });
    }

    private void getItemFilesiswa() {
        APIService api = APIClient.getClient().create(APIService.class);
        int id = getSharedPreferences("userInfo", 0).getInt("id", 0);
        Call<ResponseData> loadFile = api.getListFiles(id_taskclass, id, "file_siswa");
        loadFile.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getFilesDetail() != null) {
                        Log.d("DetailLoadfilesiswa", "success");
                        listFilesSiswa.addAll(response.body().getFilesDetail());
                        adapter = new ClassFilesAdapter(listFilesSiswa, DetailClassRoomActivity.this, "detail");
                        adapter.notifyDataSetChanged();
                        rv_files_assignment.setAdapter(adapter);
                        adapter.setOnItemClickCallback(new ClassFilesAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(FilesUpload filesUpload, int index) {
                                addListFiles(filesUpload.getFile_url(), filesUpload.getNamefile(), filesUpload.getTypefile(), index);
                            }
                        });
                        if (listFilesSiswa.size() != 0) {
                            btnAssign.setVisibility(View.GONE);
                            btnAttach.setVisibility(View.GONE);
                            shStatAssign.setText("Assigned");
                        }
                        Log.d("arrayvalue", "value: " + listFilesSiswa);
                    }
                } else {
                    Log.d("DetailLoadfilesiswa", "not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("DetailLoadfilesiswa", "failure: " + t.getMessage());
            }
        });
    }

    private void assign() {
        String token = getSharedPreferences("session", 0).getString("token", "");
        final ProgressDialog progressDialog = new ProgressDialog(DetailClassRoomActivity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        RequestBody status = createPartFromString("Assigned");
        int id = getSharedPreferences("userInfo", 0).getInt("id", 0);
        RequestBody id_siswa = createPartFromString(String.valueOf(id));
        MultipartBody.Part[] document;
        if (listFilesSiswa == null) {
            Toast.makeText(this, "Please add attachment", Toast.LENGTH_SHORT).show();
        } else {
            document = prepareDocument(listFilesSiswa);
            APIService api = APIClient.getClient().create(APIService.class);
            Call<ResponseBody> assignTask = api.assignTask("Bearer " + token, id_taskclass, id_siswa, status, document);
            assignTask.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            String jsonObject = response.body().string();
                            Log.d("AssignTask", "success: " + jsonObject);
                            btnAssign.setVisibility(View.GONE);
                            btnAttach.setVisibility(View.GONE);
                            shStatAssign.setText("Assigned");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String jObjError = response.errorBody().string();
                            Log.d("AssignTask", "not success: " + jObjError);
                            Toast.makeText(DetailClassRoomActivity.this, jObjError, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(DetailClassRoomActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("AssignTask", "failure: " + t.getMessage());
                }
            });
        }

    }

    private RequestBody createPartFromString(String param) {
        return RequestBody.create(MediaType.parse("*/*"), param);
    }

    private MultipartBody.Part[] prepareDocument(List<FilesUpload> fileupload) {
        MultipartBody.Part[] multipleFile = new MultipartBody.Part[fileupload.size()];
        for (int i = 0; i < fileupload.size(); i++) {
            File file = new File(fileupload.get(i).getPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            multipleFile[i] = MultipartBody.Part.createFormData("file[]", file.getName(), requestBody);
        }
        return multipleFile;
    }

    private String reformatdate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM HH.mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void addListFiles(String url, String filename, String extension, int position) {
        Log.d("downloadfile", "" + filename);
        File fileDownloaded = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + "." + extension);
        Uri openPath = FileProvider.getUriForFile(DetailClassRoomActivity.this, getApplicationContext().getPackageName() + ".provider", fileDownloaded);
        ContentResolver cR = this.getContentResolver();
        String type = cR.getType(openPath);
        Log.d("downloadfile", "mime: " + type);
        Log.d("downloadfile", "" + openPath);
        if (fileDownloaded.exists()) {
            Intent openFile = new Intent(Intent.ACTION_VIEW);
            openFile.setDataAndType(openPath, type);
            openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Log.d("realmime", "" + type);
            startActivity(openFile);
        } else {
            Log.d("downloadfile", "" + url);
            new DownloadFromUrl(DetailClassRoomActivity.this, url, filename, extension, type).downloadFile();
        }
    }


    private void getListCompletedStudent(final String id_task) {
        APIService api = APIClient.getClient().create(APIService.class);
        String token = getSharedPreferences("session", 0).getString("token", "");
        Call<ResponseData> loadCompletedUser = api.checkTask("Bearer " + token, id_task);
        Log.d("openlistcompleted", "value: " + id_task);
        loadCompletedUser.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCompleted_user() != null) {
                        listMembers.addAll(response.body().getCompleted_user());
                    }
                    rv_completeduser.setLayoutManager(new LinearLayoutManager(DetailClassRoomActivity.this));
                    adapterMembers = new MemberClassListAdapter(listMembers, DetailClassRoomActivity.this);
                    adapterMembers.setOnItemClickCallback(new MemberClassListAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(Members memberCompletedTask) {
                            BottomSheetFile bottomSheetFile = new BottomSheetFile(id_taskclass, memberCompletedTask.getId_user());
                            bottomSheetFile.show(getSupportFragmentManager(), bottomSheetFile.getTag());
                        }
                    });
                    rv_completeduser.setAdapter(adapterMembers);

                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FILE:
                if (resultCode == RESULT_OK) {
                    final Uri path = data.getData();
                    Log.d("activityResultValue", "value path: " + path);
                    Log.d("activityResultValue", "value name: " + getFileName(path));
                    Log.d("activityResultValue", "value realpath: " + path.getPath());
                    Log.d("activityResultValue", "value realpath: " + getMimeType(this, path));
                    ContentResolver cR = this.getContentResolver();
                    String type = cR.getType(path);
                    FilesUpload filesUpload = new FilesUpload();
                    filesUpload.setUri(path);
                    filesUpload.setFile(new File(getRealPath(getApplicationContext(), path)));
                    filesUpload.setNamefile(getFileName(path));
                    filesUpload.setTypefile(getMimeType(DetailClassRoomActivity.this, path));
                    filesUpload.setPath(getRealPathFromURI(DetailClassRoomActivity.this, path));
                    filesUpload.setRealMime(type);
                    listFilesSiswa.add(filesUpload);
                    adapter = new ClassFilesAdapter(listFilesSiswa, this, "form");
                    rv_files_assignment.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickCallback(new ClassFilesAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(FilesUpload filesUpload, int index) {
                            Intent openFile = new Intent(Intent.ACTION_VIEW);
                            Uri openPath = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", filesUpload.getFile());
                            openFile.setDataAndType(openPath, filesUpload.getRealMime());
                            openFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Log.d("realmime", "" + openPath);
                            startActivity(openFile);
                        }
                    });

                    try {
                        InputStream inputStream = DetailClassRoomActivity.this.getContentResolver().openInputStream(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    public static String getMimeType(Context context, Uri uri) {
        String extension;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getRealPath(Context context, Uri fileUri) {
        String realPath;
        // SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            realPath = getRealPathFromURI_BelowAPI11(context, fileUri);
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19) {
            realPath = getRealPathFromURI_API11to18(context, fileUri);
        }
        // SDK > 19 (Android 4.4) and up
        else {
            realPath = getRealPathFromURI_API19(context, fileUri);
        }
        return realPath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = 0;
        String result = "";
        if (cursor != null) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return result;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}