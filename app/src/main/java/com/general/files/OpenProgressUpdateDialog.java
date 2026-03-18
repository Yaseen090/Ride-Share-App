package com.general.files;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tiktak24.user.BuildConfig;
import com.tiktak24.user.R;
import com.utils.Logger;
import com.utils.Utils;
import com.view.GenerateAlertBox;
import com.view.MTextView;

public class OpenProgressUpdateDialog implements Runnable {

    private final Context mContext;
    private final GeneralFunctions generalFunc;
    private ProgressBar mProgress, simpleProgressbar;
    private MTextView progressTxt;
    private ImageView cancelUpload;
    private final String txtMsg;

    public Dialog dialog_img_update;
    UploadProfileImage uploadProfileImage;

    public OpenProgressUpdateDialog(Context mContext, GeneralFunctions generalFunc, UploadProfileImage uploadProfileImage, String txtMsg) {
        this.mContext = mContext;
        this.generalFunc = generalFunc;
        this.uploadProfileImage = uploadProfileImage;
        this.txtMsg = txtMsg;
    }

    @Override
    public void run() {
        if (!(mContext instanceof Activity)) {
            Logger.e(BuildConfig.APPLICATION_ID, "Context must be instance of Activity OR Fragment");
            return;
        }

        dialog_img_update = new Dialog(mContext, R.style.ImageSourceDialogStyle);

        dialog_img_update.setContentView(R.layout.dialog_progress_update);

        MTextView pleasewaitTxt = dialog_img_update.findViewById(R.id.pleasewaitTxt);
        MTextView uploadingTxt = dialog_img_update.findViewById(R.id.uploadingTxt);
        progressTxt = dialog_img_update.findViewById(R.id.progressTxt);
        progressTxt.setText("0%");
        pleasewaitTxt.setText(generalFunc.retrieveLangLBl("Please Wait", "LBL_PLEASE_WAIT"));

        if (Utils.checkText(txtMsg)) {
            uploadingTxt.setText(txtMsg);
        }

        cancelUpload = dialog_img_update.findViewById(R.id.cancelUpload);
        cancelUpload.setOnClickListener(v -> {

            final GenerateAlertBox generateAlert = new GenerateAlertBox(mContext);
            generateAlert.setCancelable(false);
            generateAlert.setBtnClickList(btn_id -> {
                if (btn_id == 1) {
                    uploadProfileImage.cancel(true);
                } else if (btn_id == 0) {
                    generateAlert.closeAlertBox();
                }
            });
            generateAlert.setContentMessage("", generalFunc.retrieveLangLBl("Are you sure you want to cancel upload?", "LBL_SURE_CANCEL_UPLOAD"));
            generateAlert.setPositiveBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_OK_TXT"));
            generateAlert.setNegativeBtn(generalFunc.retrieveLangLBl("", "LBL_BTN_CANCEL_TRIP_TXT"));
            generateAlert.showAlertBox();
        });

        mProgress = dialog_img_update.findViewById(R.id.circularProgressbar);
        simpleProgressbar = dialog_img_update.findViewById(R.id.simpleProgressbar);

        dialog_img_update.setCanceledOnTouchOutside(false);
        dialog_img_update.setCancelable(false);

        Window window = dialog_img_update.getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog_img_update.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (generalFunc.isRTLmode()) {
            dialog_img_update.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        dialog_img_update.show();
    }

    @SuppressLint("SetTextI18n")
    public void updateProgress(int progress) {
        progressTxt.setText("" + progress + "%");
        mProgress.setProgress(progress);
        if (progress == 100) {
            mProgress.setVisibility(View.GONE);
            simpleProgressbar.setVisibility(View.VISIBLE);
            cancelUpload.setVisibility(View.GONE);
        }
    }
}