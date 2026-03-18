package com.tiktak24.user.giftcard;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.general.files.GeneralFunctions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tiktak24.user.R;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;

public class GiftRedeemDialog {

    private final Context actContext;
    private final GeneralFunctions generalFunc;
    private BottomSheetDialog hotoUseDialog;

    @Nullable
    private OnTouchListener mListener;

    public GiftRedeemDialog(Context actContext, GeneralFunctions generalFunc) {
        this.actContext = actContext;
        this.generalFunc = generalFunc;
    }

    public void setListener(OnTouchListener listener) {
        this.mListener = listener;
    }

    public void showPreferenceDialog(String title, String posBtn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(actContext);

        View dialogView = View.inflate(actContext, R.layout.dialog_gift_redeem_details, null);
        builder.setView(dialogView);
        MButton redeemOkBtn = ((MaterialRippleLayout) dialogView.findViewById(R.id.redeemOkBtn)).getChildView();
        MTextView titileTxt = dialogView.findViewById(R.id.titileTxt);
        if (!title.equalsIgnoreCase("")) {
            titileTxt.setText(title);
            titileTxt.setVisibility(View.VISIBLE);
        } else {
            titileTxt.setVisibility(View.GONE);
        }

        if (!posBtn.equals("")) {
            redeemOkBtn.setText(posBtn);
            redeemOkBtn.setVisibility(View.VISIBLE);
        } else {
            redeemOkBtn.setVisibility(View.GONE);
        }
        redeemOkBtn.setOnClickListener(view -> {
            hotoUseDialog.dismiss();
            if (mListener != null) {
                mListener.onTouch();
            }
        });

        hotoUseDialog = new BottomSheetDialog(actContext);
        hotoUseDialog.setContentView(dialogView);
        View bottomSheetView = hotoUseDialog.getWindow().getDecorView().findViewById(R.id.design_bottom_sheet);
        bottomSheetView.setBackgroundColor(actContext.getResources().getColor(android.R.color.transparent));
        BottomSheetBehavior<View> mBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
        mBehavior.setPeekHeight(Utils.dpToPx(600, actContext));

        hotoUseDialog.setCancelable(false);
        if (generalFunc.isRTLmode()) {
            generalFunc.forceRTLIfSupported(hotoUseDialog);
        }
        Animation a = AnimationUtils.loadAnimation(actContext, R.anim.bottom_up);
        a.reset();
        bottomSheetView.clearAnimation();
        bottomSheetView.startAnimation(a);
        hotoUseDialog.show();
    }

    public interface OnTouchListener {
        void onTouch();
    }
}