package com.adapter.files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ItemDonationBannerDesignBinding;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.CreateRoundedView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class DonationBannerAdapter extends RecyclerView.Adapter<DonationBannerAdapter.ViewHolder> {

    @NonNull
    private final Context context;
    private final GeneralFunctions generalFunc;
    @NonNull
    private final OnBannerItemClickList onItemClickList;
    @Nullable
    private JSONArray mDonationArr;
    private final String LBL_DONATE_NOW;
    private final int sWidth, sHeight;

    public DonationBannerAdapter(@NonNull Context context, @NonNull GeneralFunctions generalFunc, @Nullable JSONArray donationArr, @NonNull OnBannerItemClickList onItemClickList) {
        this.context = context;
        this.generalFunc = generalFunc;
        this.mDonationArr = donationArr;
        this.onItemClickList = onItemClickList;

        this.LBL_DONATE_NOW = generalFunc.retrieveLangLBl("", "LBL_DONATE_NOW");

        this.sWidth = (int) (Utils.getScreenPixelWidth(context) - context.getResources().getDimensionPixelSize(R.dimen._10sdp));
        this.sHeight = (int) (sWidth / 1.77777778);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDonationBannerDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        JSONObject mItemObj = generalFunc.getJsonObject(mDonationArr, position);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.binding.bannerImgView.getLayoutParams();
        layoutParams.width = sWidth;
        layoutParams.height = sHeight;
        holder.binding.bannerImgView.setLayoutParams(layoutParams);

        String Url = generalFunc.getJsonValueStr("vImage", mItemObj);
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        } else {
            Url = Utils.getResizeImgURL(context, Url, sWidth, sHeight);
        }
        new LoadImage.builder(LoadImage.bind(Url), holder.binding.bannerImgView).setErrorImagePath(R.mipmap.ic_no_icon).setPlaceholderImagePath(R.mipmap.ic_no_icon).build();

        holder.binding.noteTxt.setText(generalFunc.getJsonValueStr("tDescription", mItemObj));
        holder.binding.titleTxt.setText(generalFunc.getJsonValueStr("tTitle", mItemObj));
        holder.binding.bookNowTxt.setText(LBL_DONATE_NOW);

        new CreateRoundedView(context.getResources().getColor(R.color.donateBtn), Utils.dipToPixels(context, 8), Utils.dipToPixels(context, 0), context.getResources().getColor(R.color.donateBtn), holder.binding.bookNowTxt);

        holder.binding.bookNowTxt.setOnClickListener(v -> onItemClickList.onBannerItemClick(position, mItemObj));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONArray array) {
        this.mDonationArr = array;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDonationArr != null ? mDonationArr.length() : 0;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemDonationBannerDesignBinding binding;

        private ViewHolder(ItemDonationBannerDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnBannerItemClickList {
        void onBannerItemClick(int position, JSONObject mItemObj);
    }
}