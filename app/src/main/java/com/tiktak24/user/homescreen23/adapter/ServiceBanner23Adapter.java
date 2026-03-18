package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23ServiceBannerViewItemBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceBanner23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TRACKING = 0, TYPE_MEDICAL = 1, TYPE_MEDICAL_OTHER = 2;

    private final UberXHomeActivity mActivity;
    private final JSONArray mImagesArr;
    private final OnClickListener listener;

    private final boolean isRTL, isScroll, isFullView, isClickable, AddTopPadding, AddBottomPadding;
    int displayCount, sWidth, minHeight;

    public ServiceBanner23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        this.mImagesArr = mActivity.generalFunc.getJsonArray("imagesArr", itemObject);
        this.listener = listener;

        isRTL = activity.generalFunc.isRTLmode();

        this.isScroll = mActivity.generalFunc.getJsonValueStr("isScroll", itemObject).equalsIgnoreCase("Yes");
        this.isFullView = mActivity.generalFunc.getJsonValueStr("isFullView", itemObject).equalsIgnoreCase("Yes");
        this.isClickable = mActivity.generalFunc.getJsonValueStr("isClickable", itemObject).equalsIgnoreCase("Yes");
        this.displayCount = GeneralFunctions.parseIntegerValue(1, mActivity.generalFunc.getJsonValueStr("displayCount", itemObject));

        this.AddTopPadding = mActivity.generalFunc.getJsonValueStr("AddTopPadding", itemObject).equalsIgnoreCase("Yes");
        this.AddBottomPadding = mActivity.generalFunc.getJsonValueStr("AddBottomPadding", itemObject).equalsIgnoreCase("Yes");

        this.sWidth = (int) Utils.getScreenPixelWidth(mActivity);
        this.minHeight = (sWidth / 2) - mActivity.getResources().getDimensionPixelSize(R.dimen._40sdp);

        sWidth = sWidth - mActivity.getResources().getDimensionPixelSize(R.dimen._30sdp);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MEDICAL) {
            return new MedicalViewHolder(Item23ServiceBannerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_MEDICAL_OTHER) {
            return new MedicalOtherViewHolder(Item23ServiceBannerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new TrackViewHolder(Item23ServiceBannerViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mImagesArr, position);

        if (holder instanceof MedicalViewHolder) {

            MedicalViewHolder mHolder = (MedicalViewHolder) holder;

            mHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));
            mHolder.binding.txtCategoryOtherDesc.setVisibility(View.GONE);
            mHolder.binding.txtCategoryDesc.setText(mActivity.generalFunc.getJsonValueStr("vCategoryDesc", mServiceObject));

            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mServiceObject);
            if (Utils.checkText(vBgColor)) {
                mHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) mHolder.binding.cardViewBanner.getLayoutParams();
            if (!isFullView && displayCount <= 2 || isScroll) {
                bannerLayoutParams.width = (sWidth - mActivity.getResources().getDimensionPixelSize(R.dimen._7sdp)) / displayCount;
            } else {
                bannerLayoutParams.width = sWidth / displayCount;
            }
            mHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            mHolder.binding.cardViewBanner.setMinimumHeight(minHeight);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            commonCode(mHolder.binding.bannerImgView, Url, mHolder.binding.cardViewBanner);

            ///////////////////////////////////////////////////////////////-----------------------------
            if (isClickable) {
                mHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onServiceBannerItemClick(position, mServiceObject));
            }

            int leftP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = isRTL ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                } else if (position >= 1) {
                    leftP = isRTL ? 0 : mActivity.getResources().getDimensionPixelSize(R.dimen._8sdp);
                }
            }
            mHolder.binding.mainArea.setPadding(isRTL ? 0 : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0, isRTL ? leftP : 0, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0);

            ServiceList23Adapter mAdapter = new ServiceList23Adapter(mActivity, mServiceObject, 3, Color.parseColor(vBgColor), displayCount, listener::onServiceBannerItemClick);
            mHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof MedicalOtherViewHolder) {

            MedicalOtherViewHolder moHolder = (MedicalOtherViewHolder) holder;

            moHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));
            moHolder.binding.txtCategoryOtherDesc.setText(mActivity.generalFunc.getJsonValueStr("vCategoryDesc", mServiceObject));
            moHolder.binding.txtCategoryDesc.setVisibility(View.GONE);

            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mServiceObject);
            if (Utils.checkText(vBgColor)) {
                moHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) moHolder.binding.cardViewBanner.getLayoutParams();
            bannerLayoutParams.width = sWidth / displayCount;
            moHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            moHolder.binding.cardViewBanner.setMinimumHeight(minHeight);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            commonCode(moHolder.binding.bannerImgView, Url, moHolder.binding.cardViewBanner);

            ///////////////////////////////////////////////////////////////-----------------------------
            if (isClickable) {
                moHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onServiceBannerItemClick(position, mServiceObject));
            }

            int leftP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                }
            }
            moHolder.binding.mainArea.setPadding(isRTL ? 0 : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0, isRTL ? leftP : 0, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0);
            ServiceList23Adapter mAdapter = new ServiceList23Adapter(mActivity, mServiceObject, 3, Color.parseColor(vBgColor), displayCount, listener::onServiceBannerItemClick);
            moHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof TrackViewHolder) {

            TrackViewHolder bHolder = (TrackViewHolder) holder;

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));
            bHolder.binding.txtCategoryDesc.setText(mActivity.generalFunc.getJsonValueStr("vCategoryDesc", mServiceObject));

            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mServiceObject);
            if (Utils.checkText(vBgColor)) {
                bHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) bHolder.binding.cardViewBanner.getLayoutParams();
            bannerLayoutParams.width = sWidth / displayCount;
            bHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            bHolder.binding.cardViewBanner.setMinimumHeight(minHeight);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            commonCode(bHolder.binding.bannerImgView, Url, bHolder.binding.cardViewBanner);

            ///////////////////////////////////////////////////////////////-----------------------------
            if (isClickable) {
                bHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onServiceBannerItemClick(position, mServiceObject));
            }

            int leftP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                }
            }
            bHolder.binding.mainArea.setPadding(isRTL ? 0 : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0, isRTL ? leftP : 0, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._5sdp) : 0);

        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mImagesArr, position);

        if (itemObject.has("MedicalServiceSection")) {
            if (displayCount == 1) {
                return TYPE_MEDICAL_OTHER;
            } else if (displayCount == 2) {
                return TYPE_MEDICAL;
            }
        } else if (itemObject.has("TrackServiceSection")) {
            return TYPE_TRACKING;
        }
        return position;
    }

    @Override
    public int getItemCount() {
        if (mImagesArr == null) {
            return 0;
        }
        return mImagesArr.length();
    }

    /////////////////////////////-----------------//////////////////////////////////////////////
    private static class MedicalViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceBannerViewItemBinding binding;

        private MedicalViewHolder(Item23ServiceBannerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class MedicalOtherViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceBannerViewItemBinding binding;

        private MedicalOtherViewHolder(Item23ServiceBannerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class TrackViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceBannerViewItemBinding binding;

        private TrackViewHolder(Item23ServiceBannerViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //..............................
    private void commonCode(AppCompatImageView holder, String Url, LinearLayout cardViewBanner) {
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        new LoadImage.builder(LoadImage.bind(Url), holder).build();

        ///////////////////////////////////////////////////////////////-----------------------------
        if (isFullView) {
            cardViewBanner.setBackground(null);
        } else {
            cardViewBanner.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.card_view_23_gray_flat));
        }
    }

    public interface OnClickListener {
        void onServiceBannerItemClick(int position, JSONObject jsonObject);
    }
}