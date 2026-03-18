package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23BannerListBinding;
import com.tiktak24.user.databinding.Item23GridListDeliverAllBinding;
import com.tiktak24.user.databinding.Item23TitleViewBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main23DeliverAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_BANNER = 0;
    private final int TYPE_TITLE_VIEW = 1;
    private final int TYPE_GRID = 2;

    private final UberXHomeActivity mActivity;
    private JSONArray mainArray;
    private final OnClickListener listener;
    SnapHelper mSnapHelper;

    public Main23DeliverAllAdapter(@NonNull UberXHomeActivity activity, @NonNull JSONArray list, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        this.mainArray = list;
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE_VIEW) {
            return new TitleViewHolder(Item23TitleViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_GRID) {
            return new GridViewHolder(Item23GridListDeliverAllBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new BannerViewHolder(Item23BannerListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mainArray, position);

        if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleHolder = (TitleViewHolder) holder;
            titleHolder.binding.titleTxtView.setText(mActivity.generalFunc.getJsonValueStr("vTitle", itemObject));
            titleHolder.binding.subTitleTxtView.setVisibility(View.GONE);
            titleHolder.binding.seeAllTxt.setVisibility(View.GONE);

        } else if (holder instanceof GridViewHolder) {

            GridViewHolder gridHolder = (GridViewHolder) holder;
            ServiceGrid23Adapter mAdapter = new ServiceGrid23Adapter(mActivity, itemObject, listener::onGridItemClick);
            gridHolder.binding.rvGridView.setAdapter(mAdapter);

        } else if (holder instanceof BannerViewHolder) {

            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            Banner23Adapter mAdapter = new Banner23Adapter(mActivity, true, itemObject, listener::onBannerItemClick);
            if (mSnapHelper == null) {
                mSnapHelper = new PagerSnapHelper();
                mSnapHelper.attachToRecyclerView(bannerHolder.binding.rvBanner);
            }
            bannerHolder.binding.rvBanner.setAdapter(mAdapter);

        }
    }

    @Override
    public int getItemViewType(int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mainArray, position);
        String eShowType = mActivity.generalFunc.getJsonValueStr("eShowType", itemObject);
        if (eShowType != null && eShowType.equalsIgnoreCase("Header")) {
            return TYPE_TITLE_VIEW;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("Service_Icon")) {
            return TYPE_GRID;
        } else {
            return TYPE_BANNER;
        }
    }

    @Override
    public int getItemCount() {
        if (mainArray == null) {
            return 0;
        }
        return mainArray.length();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONArray homeScreenDataArray) {
        this.mainArray = homeScreenDataArray;
        notifyDataSetChanged();
    }

    /////////////////////////////-----------------//////////////////////////////////////////////
    private static class BannerViewHolder extends RecyclerView.ViewHolder {

        private final Item23BannerListBinding binding;

        private BannerViewHolder(Item23BannerListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class GridViewHolder extends RecyclerView.ViewHolder {

        private final Item23GridListDeliverAllBinding binding;

        private GridViewHolder(Item23GridListDeliverAllBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        private final Item23TitleViewBinding binding;

        private TitleViewHolder(Item23TitleViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onBannerItemClick(int position, JSONObject jsonObject);

        void onGridItemClick(int position, JSONObject jsonObject);
    }
}