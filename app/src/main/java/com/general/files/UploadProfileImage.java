package com.general.files;

import static com.general.files.GeneralFunctions.rotateBitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.tiktak24.user.BuildConfig;
import com.tiktak24.user.MyProfileActivity;
import com.tiktak24.user.PrescriptionActivity;

import com.tiktak24.user.parking.ParkingPublish;

import com.tiktak24.user.rideSharingPro.RideSharingProHomeActivity;
import com.service.server.AppClient;
import com.service.server.DataReqBody;
import com.service.server.ServerTask;
import com.utils.CommonUtilities;
import com.utils.Logger;
import com.utils.ScalingUtilities;
import com.utils.Utils;
import com.view.MyProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 08-07-2016.
 */
public class UploadProfileImage implements DataReqBody.UploadCallbacks {

    private final GeneralFunctions generalFunc;
    private final Activity act;
    private MyProgressDialog myPDialog;

    private final String temp_File_Name, selectedImagePath;
    private String responseString = "", txtMsg = "";
    private final ArrayList<String[]> paramsList;
    private boolean isAudio = false, isProgressUpdateDialog = false, isCropIgnore = false;

    private OpenProgressUpdateDialog openProgressDialog;
    Call<Object> call_current;
    boolean isTaskKilled = false;
    private ProgressBar loading;

    public UploadProfileImage(Activity myProfileAct, String selectedImagePath, String temp_File_Name, ArrayList<String[]> paramsList) {
        this.selectedImagePath = selectedImagePath;
        this.temp_File_Name = temp_File_Name;
        this.paramsList = paramsList;
        this.act = myProfileAct;
        this.generalFunc = MyApp.getInstance().getGeneralFun(act);
    }

    public UploadProfileImage(Activity myProfileAct, String selectedImagePath, String temp_File_Name, ArrayList<String[]> paramsList, boolean isaudio) {
        this.selectedImagePath = selectedImagePath;
        this.temp_File_Name = temp_File_Name;
        this.paramsList = paramsList;
        this.act = myProfileAct;
        this.generalFunc = MyApp.getInstance().getGeneralFun(act);
        this.isAudio = isaudio;
    }

    public UploadProfileImage(boolean isCropIgnore, Activity myProfileAct, String selectedImagePath, String temp_File_Name, ArrayList<String[]> paramsList) {
        this.selectedImagePath = selectedImagePath;
        this.temp_File_Name = temp_File_Name;
        this.paramsList = paramsList;
        this.act = myProfileAct;
        this.generalFunc = MyApp.getInstance().getGeneralFun(act);
        this.isCropIgnore = isCropIgnore;
    }

    public void execute(boolean isProgressUpdateDialog, String txtMsg) {
        this.isProgressUpdateDialog = isProgressUpdateDialog;
        this.txtMsg = txtMsg;
        execute();
    }

    public void execute(ProgressBar loading) {
        this.loading = loading;
        execute();
    }

    public void execute() {
        if (loading != null) {
            loading.setVisibility(View.VISIBLE);
        } else if (isProgressUpdateDialog) {
            openProgressDialog = new OpenProgressUpdateDialog(act, generalFunc, this, txtMsg);
            try {
                openProgressDialog.run();
            } catch (Exception e) {
                Logger.e("Exception", "::" + e.getMessage());
            }

        } else {
            myPDialog = new MyProgressDialog(act, false, generalFunc.retrieveLangLBl("Loading", "LBL_LOADING_TXT"));
            try {
                myPDialog.show();
            } catch (Exception e) {
                Logger.e("Exception", "::" + e.getMessage());
            }
        }

        String filePath = "";
        if (isCropIgnore) {
            filePath = selectedImagePath;
        } else if (!selectedImagePath.equalsIgnoreCase("")) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String var5 = null;
                Bitmap var6;

                String path = selectedImagePath;
                int DESIREDWIDTH = Utils.ImageUpload_DESIREDWIDTH;
                int DESIREDHEIGHT = Utils.ImageUpload_DESIREDHEIGHT;

                try {
                    int var7 = Utils.getExifRotation(path);
                    Bitmap var8 = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);
                    if (var8.getWidth() <= DESIREDWIDTH && var8.getHeight() <= DESIREDHEIGHT) {
                        if (var8.getWidth() > var8.getHeight()) {
                            var6 = ScalingUtilities.createScaledBitmap(var8, var8.getHeight(), var8.getHeight(), ScalingUtilities.ScalingLogic.CROP);
                        } else {
                            var6 = ScalingUtilities.createScaledBitmap(var8, var8.getWidth(), var8.getWidth(), ScalingUtilities.ScalingLogic.CROP);
                        }
                    } else {
                        var6 = ScalingUtilities.createScaledBitmap(var8, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.CROP);
                    }

                    var6 = rotateBitmap(var6, var7);
                    String var9 = act.getExternalCacheDir().toString(); //NOSONAR
                    File var10 = new File(var9 + "/" + "TempImages");
                    if (!var10.exists()) {
                        var10.mkdir();
                    }

                    File var11 = new File(var10.getAbsolutePath(), temp_File_Name);
                    var5 = var11.getAbsolutePath();
                    FileOutputStream var12;

                    try {
                        var12 = new FileOutputStream(var11); //NOSONAR
                        var6.compress(Bitmap.CompressFormat.JPEG, 60, var12);
                        var12.flush();
                        var12.close();
                    } catch (Exception var14) {
                        Logger.e("Exception", "::" + var14.getMessage());
                    }

                    var6.recycle();
                } catch (Throwable var16) {
                    Logger.e("", "" + var16.getMessage());
                }

                filePath = var5 == null ? path : var5;
            } else {
                filePath = generalFunc.decodeFile(selectedImagePath, Utils.ImageUpload_DESIREDWIDTH, Utils.ImageUpload_DESIREDHEIGHT, temp_File_Name);
            }
        }


        if (filePath.equals("")) {
            HashMap<String, String> dataParams = new HashMap<>();
            for (int i = 0; i < paramsList.size(); i++) {
                String[] arrData = paramsList.get(i);

                dataParams.put(arrData[0], arrData[1]);
            }

            dataParams.put("tSessionId", generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY));
            dataParams.put("deviceHeight", Utils.getScreenPixelHeight(act) + "");
            dataParams.put("deviceWidth", Utils.getScreenPixelWidth(act) + "");
            dataParams.put("GeneralUserType", Utils.app_type);
            dataParams.put("GeneralMemberId", generalFunc.getMemberId());
            dataParams.put("GeneralDeviceType", "" + Utils.deviceType);
            dataParams.put("GeneralAppVersion", BuildConfig.VERSION_NAME);
            dataParams.put("vTimeZone", generalFunc.getTimezone());
            dataParams.put("vUserDeviceCountry", Utils.getUserDeviceCountryCode(act));
            dataParams.put("APP_TYPE", ServerTask.CUSTOM_APP_TYPE);

            if (call_current != null) {
                call_current.cancel();
                call_current = null;
            }

            Call<Object> call = AppClient.getClient("POST", CommonUtilities.SERVER).getResponse(CommonUtilities.SERVER_WEBSERVICE_PATH, dataParams);
            call_current = call;
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (isTaskKilled) {
                        return;
                    }
                    if (response.isSuccessful()) {
                        responseString = AppClient.getGSONBuilder().toJson(response.body());
                        fireResponse();
                    } else {
                        responseString = "";
                        fireResponse();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    if (isTaskKilled) {
                        return;
                    }
                    Logger.d("DataError", "::" + t.getMessage());
                    responseString = "";
                    fireResponse();
                }
            });
            return;
        }

        File file = new File(filePath);

        MultipartBody.Part filePart = null;
        if (!file.getAbsolutePath().equals("")) {
            if (isProgressUpdateDialog) {
                filePart = MultipartBody.Part.createFormData("vImage", temp_File_Name, new DataReqBody(MediaType.parse("multipart/form-data"), file, this));
            } else if (isAudio) {
                filePart = MultipartBody.Part.createFormData("voiceDirectionFile", temp_File_Name, RequestBody.create(MediaType.parse("multipart/form-data"), file));
            } else {
                filePart = MultipartBody.Part.createFormData("vImage", temp_File_Name, RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }

        HashMap<String, RequestBody> dataParams = new HashMap<>();

        for (int i = 0; i < paramsList.size(); i++) {
            String[] arrData = paramsList.get(i);
            dataParams.put(arrData[0], RequestBody.create(MediaType.parse("text/plain"), arrData[1]));
        }

        dataParams.put("tSessionId", RequestBody.create(MediaType.parse("text/plain"), generalFunc.getMemberId().equals("") ? "" : generalFunc.retrieveValue(Utils.SESSION_ID_KEY)));
        dataParams.put("deviceHeight", RequestBody.create(MediaType.parse("text/plain"), Utils.getScreenPixelHeight(act) + ""));
        dataParams.put("deviceWidth", RequestBody.create(MediaType.parse("text/plain"), Utils.getScreenPixelWidth(act) + ""));
        dataParams.put("GeneralUserType", RequestBody.create(MediaType.parse("text/plain"), Utils.app_type));
        dataParams.put("GeneralMemberId", RequestBody.create(MediaType.parse("text/plain"), generalFunc.getMemberId()));
        dataParams.put("GeneralDeviceType", RequestBody.create(MediaType.parse("text/plain"), "" + Utils.deviceType));
        dataParams.put("GeneralAppVersion", RequestBody.create(MediaType.parse("text/plain"), BuildConfig.VERSION_NAME));
        dataParams.put("vTimeZone", RequestBody.create(MediaType.parse("text/plain"), generalFunc.getTimezone()));
        dataParams.put("vUserDeviceCountry", RequestBody.create(MediaType.parse("text/plain"), Utils.getUserDeviceCountryCode(act)));
        dataParams.put("APP_TYPE", RequestBody.create(MediaType.parse("text/plain"), ServerTask.CUSTOM_APP_TYPE));
        dataParams.put("UBERX_PARENT_CAT_ID", RequestBody.create(MediaType.parse("text/plain"), ServerTask.CUSTOM_UBERX_PARENT_CAT_ID));
        dataParams.put("vCurrentTime", RequestBody.create(MediaType.parse("text/plain"), generalFunc.getCurrentGregorianDateHourMin()));

        Call<Object> call = AppClient.getClient("POST", CommonUtilities.SERVER).uploadData(CommonUtilities.SERVER_WEBSERVICE_PATH, filePart, dataParams);
        call_current = call;

        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (isTaskKilled) {
                    return;
                }
                if (response.isSuccessful()) {
                    responseString = AppClient.getGSONBuilder().toJson(response.body());
                    fireResponse();
                } else {
                    responseString = "";
                    fireResponse();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (isTaskKilled) {
                    return;
                }
                Logger.d("DataError", "::" + t.getMessage());
                responseString = "";
                fireResponse();
            }
        });

    }

    private void fireResponse() {
        try {
            if (myPDialog != null) {
                myPDialog.close();
            }
            if (openProgressDialog != null) {
                openProgressDialog.dialog_img_update.cancel();
            }
            openProgressDialog = null;
            if (loading != null) {
                loading.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
        if (act instanceof MyProfileActivity activity) {
            activity.handleImgUploadResponse(responseString);
        } else if (act instanceof PrescriptionActivity activity) {
            activity.handleImgUploadResponse(responseString);


        } else if (act instanceof RideSharingProHomeActivity activity) {
            activity.handleImgUploadResponse(responseString);

        } else if (act instanceof ParkingPublish activity) {
            activity.handleImgUploadResponse(responseString);
        }
    }

    @Override
    public void onProgressUpdate(int i, DataReqBody DataReqBody) {
        if (openProgressDialog != null) {
            openProgressDialog.updateProgress(i);
        }
    }

    @Override
    public void onError(DataReqBody DataReqBody) {

    }

    @Override
    public void onFinish(DataReqBody DataReqBody) {

    }

    @Override
    public void uploadStart(DataReqBody DataReqBody) {

    }

    public void cancel(boolean value) {

        this.isTaskKilled = value;
        if (call_current != null) {
            call_current.cancel();
        }
        try {
            if (myPDialog != null) {
                myPDialog.close();
            }
            if (openProgressDialog != null) {
                openProgressDialog.dialog_img_update.cancel();
            }
            openProgressDialog = null;
            if (loading != null) {
                loading.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
    }
}