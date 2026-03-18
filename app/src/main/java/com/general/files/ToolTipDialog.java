package com.general.files;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tiktak24.user.R;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

public class ToolTipDialog {

    BottomSheetDialog tipDialog;
    Context context;
    GeneralFunctions generalFunctions;
    String Title;
    String Msg;

    public ToolTipDialog(Context context, GeneralFunctions generalFunctions, String Title, String Msg) {
        this.context = context;
        this.generalFunctions = generalFunctions;
        this.Title = Title;
        this.Msg = Msg;
        createView();
    }

    public void createView() {
        if (tipDialog != null && tipDialog.isShowing()) {
            return;
        }
        tipDialog = new BottomSheetDialog(context);

        View contentView = View.inflate(context, R.layout.desgin_tooltip, null);
        if (generalFunctions.isRTLmode()) {
            contentView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        tipDialog.setContentView(contentView);

        tipDialog.setCancelable(true);

        View bottomSheetView = tipDialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);
        bottomSheetView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        mBehavior.setPeekHeight(Utils.dipToPixels(context, 450));
        mBehavior.setHideable(true);
        MTextView titleTxt = contentView.findViewById(R.id.titleTxt);
        MButton okTxt = ((MaterialRippleLayout) contentView.findViewById(R.id.okTxt)).getChildView();
        okTxt.setId(Utils.generateViewId());
        WebView msgTxt = contentView.findViewById(R.id.msgTxt);
        msgTxt.setBackgroundColor(context.getResources().getColor(R.color.cardView23ProBG));

        msgTxt.getSettings().setUseWideViewPort(true);
        msgTxt.getSettings().setLoadWithOverviewMode(true);
        msgTxt.getSettings().setTextZoom(350);

        titleTxt.setText(Title);
        okTxt.setText(generalFunctions.retrieveLangLBl("", "LBL_OK_THANKS"));
        okTxt.setOnClickListener(v -> tipDialog.dismiss());

        MyApp.executeWV(msgTxt, generalFunctions, Msg);

        tipDialog.show();
    }
}