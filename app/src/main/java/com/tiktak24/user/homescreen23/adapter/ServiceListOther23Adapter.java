package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerFamily;
import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23ServiceListOtherItemBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceListOther23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final UberXHomeActivity mActivity;
    private final JSONArray mServicesArr;
    private final OnClickListener listener;

    private int sWidth, itemMargin;
    private final boolean isRTL;

    public ServiceListOther23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        this.mServicesArr = mActivity.generalFunc.getJsonArray("servicesArr", itemObject);
        this.listener = listener;

        this.isRTL = activity.generalFunc.isRTLmode();

        this.sWidth = (int) Utils.getScreenPixelWidth(mActivity);
        if (mServicesArr != null && mServicesArr.length() > 0) {
            sWidth = (sWidth - mActivity.getResources().getDimensionPixelSize(R.dimen._30sdp)) / mServicesArr.length();
            itemMargin = mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp);

          //  sWidth = (sWidth - (itemMargin / (mServicesArr.length() - 1))) - mActivity.getResources().getDimensionPixelSize(R.dimen._2sdp);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(Item23ServiceListOtherItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder bHolder = (ViewHolder) holder;

        JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

        bHolder.binding.serviceTitleTxt.setText(mActivity.generalFunc.getJsonValueStr("vTitle", mServiceObject));
        bHolder.binding.serviceSubTitleTxt.setText(mActivity.generalFunc.getJsonValueStr("vSubTitle", mServiceObject));

       /* LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) bHolder.binding.serviceImg.getLayoutParams();
        imageParams.width = sWidth;
        imageParams.height = sWidth;
        bHolder.binding.serviceImg.setLayoutParams(imageParams);*/

        String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.serviceImg).build();

        //--------------
        String showBackgroundShadow = mActivity.generalFunc.getJsonValueStr("showBackgroundShadow", mServiceObject);
        if (showBackgroundShadow.equalsIgnoreCase("Yes")) {
            bHolder.binding.imgBgArea.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.card_view_23_white_shadow));
        } else {
            bHolder.binding.imgBgArea.setBackground(null);
        }

        int allCorners;
        String ImageRadius = mActivity.generalFunc.getJsonValueStr("ImageRadius", mServiceObject);
        if (ImageRadius.equalsIgnoreCase("Yes")) {
            allCorners = mActivity.getResources().getDimensionPixelSize(R.dimen._6sdp);
        } else {
            allCorners = 0;
        }
        bHolder.binding.serviceImg.setShapeAppearanceModel(bHolder.binding.serviceImg.getShapeAppearanceModel()
                .toBuilder().setAllCorners(CornerFamily.ROUNDED, allCorners).build());


        int leftP = 0, rightP = 0;
        if (position == 0) {
            leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
        }
        if (position != (mServicesArr.length() - 1)) {
            rightP = itemMargin;
        }
        bHolder.binding.mainArea.setPadding(isRTL ? rightP : leftP, 0, isRTL ? leftP : rightP, 0);

        ///////////////////////////////////////////////////////////////-----------------------------
        bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceListItemClick(position, mServiceObject));
    }

    @Override
    public int getItemCount() {
        if (mServicesArr == null) {
            return 0;
        }
        return mServicesArr.length();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceListOtherItemBinding binding;

        private ViewHolder(Item23ServiceListOtherItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onServiceListItemClick(int position, JSONObject jsonObject);
    }
}