package com.adapter.files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ItemCommon23BannerBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommonBanner23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final GeneralFunctions generalFunc;
    private JSONArray mBannerArray;
    boolean isAlreadyMarginGiven = false;
    int bannerWidth, bannerHeight, itemSpacing, itemLRMargin;

    public CommonBanner23Adapter(@NonNull Context context, @NonNull GeneralFunctions generalFunc, @Nullable JSONArray mBannerArray) {
        this.mContext = context;
        this.generalFunc = generalFunc;
        this.mBannerArray = mBannerArray;

        int sWidth = (int) Utils.getScreenPixelWidth(mContext);
        this.itemLRMargin = mContext.getResources().getDimensionPixelSize(R.dimen._15sdp);
        this.bannerWidth = sWidth - (itemLRMargin * 2);
        this.itemSpacing = mContext.getResources().getDimensionPixelSize(R.dimen._11sdp);

        setImageRatio();
    }

    private void setImageRatio() {
        if (mBannerArray != null && mBannerArray.length() > 1) {
            if (!isAlreadyMarginGiven) {
                isAlreadyMarginGiven = true;
                this.bannerWidth = (bannerWidth - itemLRMargin);
            }
        }
        this.bannerHeight = (int) (bannerWidth / 2.33);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new NormalViewHolder(ItemCommon23BannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommonBanner23Adapter.NormalViewHolder bHolder = (CommonBanner23Adapter.NormalViewHolder) holder;

        JSONObject mBannerObject = generalFunc.getJsonObject(mBannerArray, position);

        CardView.LayoutParams bannerLayoutParams = (CardView.LayoutParams) bHolder.binding.bannerImgView.getLayoutParams();
        bannerLayoutParams.width = bannerWidth;
        bannerLayoutParams.height = bannerHeight;
        bHolder.binding.bannerImgView.setLayoutParams(bannerLayoutParams);

        String Url = generalFunc.getJsonValueStr("vImage", mBannerObject);
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        Url = Utils.getResizeImgURL(mContext, Url, bannerWidth, bannerHeight);
        new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.bannerImgView).build();
        bHolder.binding.cardViewBanner.setRadius(mContext.getResources().getDimensionPixelSize(R.dimen._6sdp));

        int leftP = 0, centerP = 0;
        if (position == 0) {
            leftP = itemLRMargin;
        }
        if (mBannerArray != null && mBannerArray.length() > 1) {
            if (position != (mBannerArray.length() - 1)) {
                centerP = itemSpacing;
            } else if (position == (mBannerArray.length() - 1)) {
                centerP = itemLRMargin;
            }
        }
        bHolder.binding.mainArea.setPadding(generalFunc.isRTLmode() ? centerP : leftP, 0, generalFunc.isRTLmode() ? leftP : centerP, 0);
    }

    @Override
    public int getItemCount() {
        if (mBannerArray == null) {
            return 0;
        }
        return mBannerArray.length();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONArray mBannerArray) {
        this.mBannerArray = mBannerArray;
        setImageRatio();
        notifyDataSetChanged();
    }

    /////////////////////////////-----------------//////////////////////////////////////////////
    private static class NormalViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommon23BannerBinding binding;

        private NormalViewHolder(ItemCommon23BannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}