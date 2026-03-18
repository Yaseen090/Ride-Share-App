package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23BannerListBinding;
import com.tiktak24.user.databinding.Item23GridListBinding;
import com.tiktak24.user.databinding.Item23GridListProdeliveryonlyBinding;
import com.tiktak24.user.databinding.Item23IntroViewBinding;

import com.tiktak24.user.databinding.Item23ListVerticalBinding;
import com.tiktak24.user.databinding.Item23RecentLocationViewBinding;
import com.tiktak24.user.databinding.Item23ServiceBannerListBinding;
import com.tiktak24.user.databinding.Item23ServiceListBinding;
import com.tiktak24.user.databinding.Item23TitleViewBinding;
import com.model.ServiceModule;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TITLE = 0;
    private final int TYPE_INTRO = 1;
    private final int TYPE_BANNER = 2;
    private final int TYPE_GRID = 3;
    private final int TYPE_LIST = 4;
    private final int TYPE_SERVICE_BANNER = 5;
    private final int TYPE_SERVICE_LIST = 6;
    private final int TYPE_SERVICE_LIST_OTHER = 7;
    private final int TYPE_RECENT_LOCATION = 8;
    private final int TYPE_GRID_ICON_VIEW = 9;
    private final int TYPE_NEW_LISTING_VIEW = 10;

    private final UberXHomeActivity mActivity;
    private JSONArray mainArray;
    private final OnClickListener listener;
    private SnapHelper mSnapHelper;
    private String mResponseString;

    public Main23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONArray list, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        this.mainArray = list;
        this.listener = listener;
    }

    public void setResponseString(String responseString) {
        this.mResponseString = responseString;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_INTRO) {
            return new IntroViewHolder(Item23IntroViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_BANNER) {
            return new BannerViewHolder(Item23BannerListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_GRID) {
            return new GridViewHolder(Item23GridListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_GRID_ICON_VIEW) {
            return new GridIconViewHolder(Item23GridListProdeliveryonlyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_LIST) {
            return new ListViewHolder(Item23ListVerticalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_SERVICE_BANNER) {
            return new ServiceBannerViewHolder(Item23ServiceBannerListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_SERVICE_LIST) {
            return new ServiceListViewHolder(Item23ServiceListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_SERVICE_LIST_OTHER) {
            return new ServiceListOtherViewHolder(Item23ServiceListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_RECENT_LOCATION) {
            return new RecentLocationViewHolder(Item23RecentLocationViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        } else {
            return new TitleViewHolder(Item23TitleViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mainArray, position);

        if (holder instanceof IntroViewHolder) {

            IntroViewHolder introHolder = (IntroViewHolder) holder;
            introHolder.binding.welcomeHTxt.setText(mActivity.generalFunc.getJsonValueStr("vTitle", itemObject));
            introHolder.binding.welcomeVTxt.setText(mActivity.generalFunc.getJsonValueStr("vSubtitle", itemObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", itemObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), introHolder.binding.emojiImage).build();

        } else if (holder instanceof BannerViewHolder) {

            BannerViewHolder bannerHolder = (BannerViewHolder) holder;
            Banner23Adapter mAdapter = new Banner23Adapter(mActivity, false, itemObject, listener::onBannerItemClick);
            if (mActivity.generalFunc.getJsonValueStr("isScroll", itemObject).equalsIgnoreCase("Yes")) {
                if (mSnapHelper == null) {
                    mSnapHelper = new PagerSnapHelper();
                    mSnapHelper.attachToRecyclerView(bannerHolder.binding.rvBanner);
                }
            }
            bannerHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof GridViewHolder) {

            GridViewHolder gridHolder = (GridViewHolder) holder;
            if (ServiceModule.isDeliveryKingApp) {
                Grid23Adapter mAdapter = new Grid23Adapter(mActivity, itemObject, listener::onGridItemClick);
                gridHolder.binding.rvGridView.setAdapter(mAdapter);
            } else {
                ServiceGrid23Adapter mAdapter = new ServiceGrid23Adapter(mActivity, itemObject, listener::onGridItemClick);
                gridHolder.binding.rvGridView.setAdapter(mAdapter);
            }

        } else if (holder instanceof GridIconViewHolder) {

            GridIconViewHolder gridHolder = (GridIconViewHolder) holder;
            ((GridIconViewHolder) holder).binding.sendParcelBtn.setText(mActivity.generalFunc.retrieveLangLBl("", "LBL_DELIVERY_SEND_PARCEL_BTN_TXT"));
            ServiceGrid23Adapter mAdapter = new ServiceGrid23Adapter(mActivity, itemObject, listener::onGridItemClick);
            gridHolder.binding.rvGridView.setAdapter(mAdapter);

            gridHolder.binding.sendParcelBtn.setOnClickListener(view -> listener.onGridItemClick(position, mActivity.generalFunc.getJsonObject(mResponseString)));

        } else if (holder instanceof ListViewHolder) {

            ListViewHolder listHolder = (ListViewHolder) holder;
            List23Adapter mAdapter = new List23Adapter(mActivity, itemObject, listener::onGridItemClick);
            listHolder.binding.rvListView.setAdapter(mAdapter);

        } else if (holder instanceof ServiceBannerViewHolder) {

            ServiceBannerViewHolder sbHolder = (ServiceBannerViewHolder) holder;
            ServiceBanner23Adapter mAdapter = new ServiceBanner23Adapter(mActivity, itemObject, listener::onServiceBannerItemClick);
            sbHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof ServiceListViewHolder) {

            ServiceListViewHolder slHolder = (ServiceListViewHolder) holder;
            ServiceList23Adapter mAdapter = new ServiceList23Adapter(mActivity, itemObject, 4, 0, 1, listener::onServiceListItemClick);
            slHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof ServiceListOtherViewHolder) {

            ServiceListOtherViewHolder sloHolder = (ServiceListOtherViewHolder) holder;
            ServiceListOther23Adapter mAdapter = new ServiceListOther23Adapter(mActivity, itemObject, listener::onServiceOtherListItemClick);
            sloHolder.binding.rvBanner.setAdapter(mAdapter);

        } else if (holder instanceof RecentLocationViewHolder) {

            RecentLocationViewHolder rlHolder = (RecentLocationViewHolder) holder;

            // where to area
            String whereToArea = mActivity.generalFunc.getJsonValueStr("isShowEnterLocation", itemObject);
            if (Utils.checkText(whereToArea) && whereToArea.equalsIgnoreCase("Yes")) {
                rlHolder.binding.whereToArea.setVisibility(View.VISIBLE);
                rlHolder.binding.whereToTxt.setText(mActivity.generalFunc.retrieveLangLBl("", "LBL_WHERE_TO"));
                rlHolder.binding.nowBtnArea.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity, R.color.appThemeColor_1)));
                rlHolder.binding.nowTxt.setText(mActivity.generalFunc.retrieveLangLBl("", "LBL_NOW"));

                rlHolder.binding.whereToTxt.setOnClickListener(v -> listener.onWhereToClick(position, itemObject));
                rlHolder.binding.nowBtnArea.setOnClickListener(v -> listener.onNowClick(position, itemObject));
                if (mActivity.generalFunc.getJsonValueStr("RIDE_LATER_BOOKING_ENABLED", mActivity.obj_userProfile).equalsIgnoreCase("Yes")) {
                    rlHolder.binding.nowBtnArea.setVisibility(View.VISIBLE);
                } else {
                    rlHolder.binding.nowBtnArea.setVisibility(View.GONE);
                }

            } else {
                rlHolder.binding.whereToArea.setVisibility(View.GONE);
            }

            /// recent list data
            RecentLocationList23Adapter mAdapter = new RecentLocationList23Adapter(mActivity, mActivity.generalFunc, itemObject, listener::onServiceListItemClick);
            rlHolder.binding.rvRecentLocationList.setAdapter(mAdapter);

        } else if (holder instanceof TitleViewHolder) {

            TitleViewHolder titleHolder = (TitleViewHolder) holder;
            titleHolder.binding.titleTxtView.setText(mActivity.generalFunc.getJsonValueStr("vTitle", itemObject));

            String vSubtitle = mActivity.generalFunc.getJsonValueStr("vSubtitle", itemObject);
            if (ServiceModule.isRideOnly() || ServiceModule.RideDeliveryProduct || ServiceModule.isCubeXApp || ServiceModule.isServiceProviderOnly()) {
                titleHolder.binding.titleTxtView.setTextColor(Color.BLACK);
            }
            if (Utils.checkText(vSubtitle)) {
                titleHolder.binding.subTitleTxtView.setVisibility(View.VISIBLE);
                titleHolder.binding.subTitleTxtView.setText(vSubtitle);
            } else {
                titleHolder.binding.subTitleTxtView.setVisibility(View.GONE);
            }

            String isShowAll = mActivity.generalFunc.getJsonValueStr("isShowAll", itemObject);
            if (isShowAll.equalsIgnoreCase("Yes")) {
                titleHolder.binding.seeAllTxt.setVisibility(View.VISIBLE);
                titleHolder.binding.seeAllTxt.setText(mActivity.generalFunc.retrieveLangLBl("", "LBL_SEE_ALL"));
                titleHolder.binding.seeAllTxt.setOnClickListener(v -> listener.onSeeAllClick(position, itemObject));
            } else {
                titleHolder.binding.seeAllTxt.setVisibility(View.GONE);
            }
            if (ServiceModule.isDeliveronly()) {
                titleHolder.binding.titleTxtView.setTextColor(ContextCompat.getColor(mActivity, R.color.appThemeColor_1));
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mainArray, position);
        String eShowType = mActivity.generalFunc.getJsonValueStr("eViewType", itemObject);
        if (eShowType != null && eShowType.equalsIgnoreCase("NewListingView")) {
            return TYPE_NEW_LISTING_VIEW;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("IntroView")) {
            return TYPE_INTRO;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("BannerView")) {
            return TYPE_BANNER;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("GridView")) {
            return TYPE_GRID;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("GridIconView")) {
            return TYPE_GRID_ICON_VIEW;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("ListView")) {
            return TYPE_LIST;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("ServiceBannerView")) {
            return TYPE_SERVICE_BANNER;
        } else if (eShowType != null && eShowType.equalsIgnoreCase("ServiceListView")) {
            String eServiceType = mActivity.generalFunc.getJsonValueStr("eServiceType", itemObject);
            if (eServiceType != null && eServiceType.equalsIgnoreCase("Other")) {
                return TYPE_SERVICE_LIST_OTHER;
            } else {
                return TYPE_SERVICE_LIST;
            }
        } else if (eShowType != null && eShowType.equalsIgnoreCase("DestinationDetailView")) {
            return TYPE_RECENT_LOCATION;
        } else {
            return TYPE_TITLE;
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
        if (mSnapHelper != null) {
            mSnapHelper.attachToRecyclerView(null);
            mSnapHelper = null;
        }
        this.mainArray = homeScreenDataArray;
        notifyDataSetChanged();
    }

    /////////////////////////////-----------------//////////////////////////////////////////////

    private static class IntroViewHolder extends RecyclerView.ViewHolder {

        private final Item23IntroViewBinding binding;

        private IntroViewHolder(Item23IntroViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



    private static class BannerViewHolder extends RecyclerView.ViewHolder {

        private final Item23BannerListBinding binding;

        private BannerViewHolder(Item23BannerListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class GridViewHolder extends RecyclerView.ViewHolder {

        private final Item23GridListBinding binding;

        private GridViewHolder(Item23GridListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class GridIconViewHolder extends RecyclerView.ViewHolder {

        private final Item23GridListProdeliveryonlyBinding binding;

        private GridIconViewHolder(Item23GridListProdeliveryonlyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ListViewHolder extends RecyclerView.ViewHolder {

        private final Item23ListVerticalBinding binding;

        private ListViewHolder(Item23ListVerticalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ServiceBannerViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceBannerListBinding binding;

        private ServiceBannerViewHolder(Item23ServiceBannerListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ServiceListViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceListBinding binding;

        private ServiceListViewHolder(Item23ServiceListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ServiceListOtherViewHolder extends RecyclerView.ViewHolder {

        private final Item23ServiceListBinding binding;

        private ServiceListOtherViewHolder(Item23ServiceListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class RecentLocationViewHolder extends RecyclerView.ViewHolder {

        private final Item23RecentLocationViewBinding binding;

        private RecentLocationViewHolder(Item23RecentLocationViewBinding binding) {
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

        void onServiceBannerItemClick(int position, JSONObject jsonObject);

        void onGridItemClick(int position, JSONObject jsonObject);

        void onServiceListItemClick(int position, JSONObject jsonObject);

        void onServiceOtherListItemClick(int position, JSONObject jsonObject);

        void onWhereToClick(int position, JSONObject jsonObject);

        void onNowClick(int position, JSONObject jsonObject);

        void onSeeAllClick(int position, JSONObject itemObject);

        void onNewListingClick(int position, JSONObject itemObject);
    }
}