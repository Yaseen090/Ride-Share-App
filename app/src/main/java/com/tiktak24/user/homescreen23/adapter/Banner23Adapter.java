package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fontanalyzer.SystemFont;
import com.general.files.GeneralFunctions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23BannerItemBinding;
import com.tiktak24.user.databinding.Item23RentEstateCarBinding;
import com.tiktak24.user.databinding.Item23RentItemBinding;
import com.tiktak24.user.databinding.Item23RideShareBinding;
import com.tiktak24.user.databinding.ItemDataBannerBinding;
import com.model.ServiceModule;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class Banner23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_NORMAL = 0;
    private final int TYPE_RENT_ESTATE_CAR = 1;
    private final int TYPE_RENT_ITEM = 2;
    private final int TYPE_RIDE_SHARE = 3;
    private final int TYPE_DATA_ARRAY = 4;

    private final UberXHomeActivity mActivity;
    private final JSONObject mItemObject;
    private JSONArray mBannerArray;
    private final OnClickListener listener;

    private boolean isScroll, isOnlyImage, isClickable;
    private final boolean isRTL, isFullView, AddTopPadding, AddBottomPadding;

    int displayCount, itemLRMargin, itemSpacing, sWidth, minHeight;
    int bannerWidth, bannerHeight;
    int vImageWidth, vImageHeight;
    private final int v7sdp, v10sdp;

    public Banner23Adapter(@NonNull UberXHomeActivity activity, boolean isDeliverAll, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        this.mItemObject = itemObject;
        if (itemObject.has("DataArr")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("DataArr", itemObject);
        } else if (itemObject.has("imagesArr")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("imagesArr", itemObject);
        } else if (itemObject.has("BANNER_DATA")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("BANNER_DATA", itemObject);
        }
        this.listener = listener;

        isRTL = activity.generalFunc.isRTLmode();

        this.v7sdp = mActivity.getResources().getDimensionPixelSize(R.dimen._7sdp);
        this.v10sdp = mActivity.getResources().getDimensionPixelSize(R.dimen._10sdp);

        this.itemLRMargin = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
        this.itemSpacing = mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp);

        this.isScroll = mActivity.generalFunc.getJsonValueStr("isScroll", itemObject).equalsIgnoreCase("Yes");
        this.isFullView = mActivity.generalFunc.getJsonValueStr("isFullView", itemObject).equalsIgnoreCase("Yes");
        this.isOnlyImage = mActivity.generalFunc.getJsonValueStr("isOnlyImage", itemObject).equalsIgnoreCase("Yes");
        this.isClickable = mActivity.generalFunc.getJsonValueStr("isClickable", itemObject).equalsIgnoreCase("Yes");
        this.displayCount = GeneralFunctions.parseIntegerValue(1, mActivity.generalFunc.getJsonValueStr("displayCount", itemObject));

        this.AddTopPadding = mActivity.generalFunc.getJsonValueStr("AddTopPadding", itemObject).equalsIgnoreCase("Yes");
        this.AddBottomPadding = mActivity.generalFunc.getJsonValueStr("AddBottomPadding", itemObject).equalsIgnoreCase("Yes");

        this.vImageWidth = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("vImageWidth", itemObject));
        this.vImageHeight = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("vImageHeight", itemObject));

        double ratio = GeneralFunctions.parseDoubleValue(0.0, String.valueOf(vImageWidth)) / GeneralFunctions.parseDoubleValue(0.0, String.valueOf(vImageHeight));

        this.sWidth = (int) Utils.getScreenPixelWidth(mActivity);
        this.minHeight = (sWidth / 2) - mActivity.getResources().getDimensionPixelSize(R.dimen._40sdp);

        if (isDeliverAll) {
            isScroll = true;
            isOnlyImage = true;
            isClickable = true;
            ratio = 2.33;
        }

        setImageRatio(ratio);
    }

    private void setImageRatio(double ratio) {
        if (isFullView) {
            bannerWidth = sWidth;
        } else {
            if (isScroll) {
                if (mBannerArray != null && mBannerArray.length() == 1) {
                    bannerWidth = sWidth - (itemLRMargin * 2);
                } else {
                    bannerWidth = sWidth - mActivity.getResources().getDimensionPixelSize(R.dimen._50sdp);
                }
            } else {
                if (displayCount == 1) {
                    bannerWidth = (sWidth / displayCount) - (itemLRMargin * 2);
                } else if (displayCount == 2) {
                    bannerWidth = (sWidth - (itemLRMargin * 2) - itemSpacing) / displayCount;
                }
            }
        }
        bannerHeight = (int) (bannerWidth / ratio);
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_RENT_ESTATE_CAR) {
            return new EStateCarViewHolder(Item23RentEstateCarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_RENT_ITEM) {
            return new RentItemViewHolder(Item23RentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_RIDE_SHARE) {
            return new RideShareViewHolder(Item23RideShareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == TYPE_DATA_ARRAY) {
            return new DataArrHolder(ItemDataBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new NormalViewHolder(Item23BannerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n", "RtlHardcoded"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        JSONObject mBannerObject = mActivity.generalFunc.getJsonObject(mBannerArray, position);

        if (holder instanceof NormalViewHolder) {
            NormalViewHolder bHolder = (NormalViewHolder) holder;

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) bHolder.binding.bannerImgView.getLayoutParams();
            bannerLayoutParams.width = bannerWidth;
            bannerLayoutParams.height = bannerHeight;
            bHolder.binding.bannerImgView.setLayoutParams(bannerLayoutParams);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mBannerObject);
            commonCode_2(bHolder.binding.bannerImgView, Url, true, mActivity.getResources().getDimensionPixelSize(R.dimen._6sdp));

            if (mActivity.generalFunc.getJsonValueStr("isClickable", mBannerObject).equalsIgnoreCase("Yes")) {
                bHolder.binding.mainArea.setOnClickListener(v -> listener.onBannerItemClick(position, mBannerObject));
            }

            int leftP = 0, centerP = 0;
            if (!isFullView && isOnlyImage && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                    centerP = isRTL ? mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp) : 0;
                }
                if (position != (mBannerArray.length() - 1)) {
                    centerP = itemSpacing;
                } else if (position == (mBannerArray.length() - 1)) {
                    centerP = isRTL ? 0 : itemLRMargin;
                }
            }
            bHolder.binding.mainArea.setPadding(isRTL ? centerP : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0, isRTL ? leftP : centerP, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0);

        } else if (holder instanceof EStateCarViewHolder) {
            EStateCarViewHolder estateHolder = (EStateCarViewHolder) holder;

            estateHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mBannerObject));
            String vTextColor = mActivity.generalFunc.getJsonValueStr("vTextColor", mBannerObject);
            if (Utils.checkText(vTextColor)) {
                estateHolder.binding.txtCategoryName.setTextColor(Color.parseColor(vTextColor));
            }
            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mBannerObject);
            if (Utils.checkText(vBgColor)) {
                estateHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }

            String promotionalTagText = mActivity.generalFunc.getJsonValueStr("PromotionalTagText", mBannerObject);
            if (Utils.checkText(promotionalTagText)) {
                estateHolder.binding.promoTagView.setVisibility(View.VISIBLE);
                estateHolder.binding.txtPromotionalTag.setText(promotionalTagText);
            } else {
                estateHolder.binding.promoTagView.setVisibility(View.GONE);
            }

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) estateHolder.binding.cardViewBanner.getLayoutParams();
            bannerLayoutParams.width = bannerWidth;
            bannerLayoutParams.height = minHeight;
            estateHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mBannerObject);
            commonCode_1(estateHolder.binding.bannerImgView, Url, estateHolder.binding.cardViewBanner, false);
            if (isRTL) {
                estateHolder.binding.bannerImgView.setRotationY(180);
            }

            int leftP = 0, centerP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                    centerP = isRTL ? mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp) : 0;
                }
                if (position != (mBannerArray.length() - 1)) {
                    centerP = itemSpacing;
                } else if (position == (mBannerArray.length() - 1)) {
                    centerP = isRTL ? 0 : itemLRMargin;
                }
            }
            estateHolder.binding.mainArea.setPadding(isRTL ? centerP : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0, isRTL ? leftP : centerP, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0);
            if (isClickable) {
                estateHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onBannerItemClick(position, mBannerObject));
            }

        } else if (holder instanceof RentItemViewHolder) {
            RentItemViewHolder rentItemHolder = (RentItemViewHolder) holder;

            rentItemHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mBannerObject));
            String vTextColor = mActivity.generalFunc.getJsonValueStr("vTextColor", mBannerObject);
            if (Utils.checkText(vTextColor)) {
                rentItemHolder.binding.txtCategoryName.setTextColor(Color.parseColor(vTextColor));
            }
            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mBannerObject);
            if (Utils.checkText(vBgColor)) {
                rentItemHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }
            if (ServiceModule.isOnlyBuySellRentEnable()) {
                rentItemHolder.binding.txtCategoryName.setTypeface(SystemFont.FontStyle.BOLD.font);
            }
            String promotionalTagText = mActivity.generalFunc.getJsonValueStr("PromotionalTagText", mBannerObject);
            if (Utils.checkText(promotionalTagText)) {
                rentItemHolder.binding.promoTagView.setVisibility(View.VISIBLE);
                rentItemHolder.binding.txtPromotionalTag.setText(promotionalTagText);
            } else {
                rentItemHolder.binding.promoTagView.setVisibility(View.GONE);
            }

            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) rentItemHolder.binding.cardViewBanner.getLayoutParams();
            bannerLayoutParams.width = bannerWidth;
            bannerLayoutParams.height = minHeight;
            rentItemHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mBannerObject);
            commonCode_1(rentItemHolder.binding.bannerImgView, Url, rentItemHolder.binding.cardViewBanner, false);
            if (isRTL) {
                rentItemHolder.binding.bannerImgView.setRotationY(180);
            }

            int leftP = 0, centerP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                }
                if (position != (mBannerArray.length() - 1)) {
                    centerP = itemSpacing;
                } else if (position == (mBannerArray.length() - 1)) {
                    centerP = itemLRMargin;
                }
            }
            rentItemHolder.binding.mainArea.setPadding(isRTL ? centerP : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0, isRTL ? leftP : centerP, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0);
            if (isClickable) {
                rentItemHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onBannerItemClick(position, mBannerObject));
            }

        } else if (holder instanceof DataArrHolder) {
            DataArrHolder dataItemHolder = (DataArrHolder) holder;

            String vTitle = mActivity.generalFunc.getJsonValueStr("vTitle", mBannerObject);
            String vTextColor = mActivity.generalFunc.getJsonValueStr("vTextColor", mBannerObject);
            float vTextFontSize = Float.parseFloat(mActivity.generalFunc.getJsonValueStr("vTextFontSize", mBannerObject));
            float vDecFontSize = Float.parseFloat(mActivity.generalFunc.getJsonValueStr("vDecFontSize", mBannerObject));
            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mBannerObject);
            String vDescription = mActivity.generalFunc.getJsonValueStr("vDescription", mBannerObject);

            setFieldData(dataItemHolder, vTitle, vTextColor, vTextFontSize, vDecFontSize, vBgColor, vDescription);


            RelativeLayout.LayoutParams bannerLayoutParams = (RelativeLayout.LayoutParams) dataItemHolder.binding.cardViewBanner.getLayoutParams();
            bannerLayoutParams.width = bannerWidth;
            bannerLayoutParams.height = bannerHeight;
            dataItemHolder.binding.cardViewBanner.setLayoutParams(bannerLayoutParams);

            String Url = mActivity.generalFunc.getJsonValueStr("vIconImage", mBannerObject);

            float vWidth = GeneralFunctions.parseFloatValue(0, mActivity.generalFunc.getJsonValueStr("vIconImageWidth", mBannerObject));
            float vHeight = GeneralFunctions.parseFloatValue(0, mActivity.generalFunc.getJsonValueStr("vIconImageHeight", mBannerObject));
            double ratio = vWidth / vHeight;

            vWidth = Utils.dpToPx(mActivity, vWidth);
            vHeight = Utils.dpToPx(mActivity, vHeight);

            if (isTablet(mActivity)) {
                if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Left-Full")
                        || mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Right-Full")) {

                    vHeight = bannerHeight;
                    if (!mActivity.generalFunc.getJsonValueStr("eServiceType", mItemObject).equalsIgnoreCase("TrackAnyService")) {
                        vHeight = bannerHeight - mActivity.getResources().getDimensionPixelSize(R.dimen._20sdp);
                    }
                    vWidth = (float) (vHeight * ratio);
                }
            }

            int TopPadding = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("ImageMargin-Top", mBannerObject));
            int LeftPadding = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("ImageMargin-Left", mBannerObject));
            int RightPadding = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("ImageMargin-Right", mBannerObject));
            int BottomPadding = GeneralFunctions.parseIntegerValue(0, mActivity.generalFunc.getJsonValueStr("ImageMargin-Bottom", mBannerObject));

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Left-Full")) {
                commonCode_3(dataItemHolder.binding.bannerImgViewLeft, Url, dataItemHolder.binding.cardViewBanner, true, (int) vHeight, (int) vWidth);
                dataItemHolder.binding.bannerImgViewLeft.setLayoutParams(params1);
                dataItemHolder.binding.bannerImgViewLeft.setPadding(isRTL ? RightPadding : LeftPadding, TopPadding, isRTL ? LeftPadding : RightPadding, BottomPadding);

                dataItemHolder.binding.txtSubTitle.setPadding(isRTL ? v10sdp : 0, 0, isRTL ? 0 : v10sdp, 0);

                if (isRTL) {
                    dataItemHolder.binding.bannerImgViewLeft.setRotationY(180);
                }
            } else if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Right-Bottom")) {
                commonCode_3(dataItemHolder.binding.bannerImgViewRight, Url, dataItemHolder.binding.cardViewBanner, true, (int) vHeight, (int) vWidth);
                params1.gravity = Gravity.BOTTOM;
                dataItemHolder.binding.txtArea.setPadding(isRTL ? 0 : v10sdp, v7sdp, isRTL ? v10sdp : 0, 0);
                LinearLayout.LayoutParams subTitleLayoutParams = (LinearLayout.LayoutParams) dataItemHolder.binding.txtSubTitle.getLayoutParams();
                subTitleLayoutParams.bottomMargin = v7sdp;
                dataItemHolder.binding.txtSubTitle.setLayoutParams(subTitleLayoutParams);

                dataItemHolder.binding.bannerImgViewRight.setVisibility(View.VISIBLE);
                dataItemHolder.binding.bannerImgViewRight.setLayoutParams(params1);
                dataItemHolder.binding.bannerImgViewLeft.setVisibility(View.GONE);


                if (isRTL) {
                    dataItemHolder.binding.bannerImgViewRight.setRotationY(180);
                    dataItemHolder.binding.bannerImgViewRight.setPadding(RightPadding, TopPadding, LeftPadding, BottomPadding);
                }
            } else if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Right-Full")) {
                commonCode_3(dataItemHolder.binding.bannerImgViewRight, Url, dataItemHolder.binding.cardViewBanner, true, (int) vHeight, (int) vWidth);
                dataItemHolder.binding.txtArea.setPadding(v10sdp, 0, v10sdp, 0);
                params1.gravity = Gravity.BOTTOM;
                dataItemHolder.binding.bannerImgViewRight.setPadding(isRTL ? RightPadding : LeftPadding, TopPadding, isRTL ? LeftPadding : RightPadding, BottomPadding);
                dataItemHolder.binding.bannerImgViewRight.setVisibility(View.VISIBLE);
                dataItemHolder.binding.bannerImgViewRight.setLayoutParams(params1);
                dataItemHolder.binding.txtTitle.setVisibility(View.GONE);
                dataItemHolder.binding.txtTitleRight.setVisibility(View.VISIBLE);
                dataItemHolder.binding.bannerImgViewLeft.setVisibility(View.GONE);


                if (isRTL) {
                    dataItemHolder.binding.bannerImgViewRight.setRotationY(180);
                }
            } else if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Center-Full")) {
                commonCode_3(dataItemHolder.binding.bannerImgViewLeft, Url, dataItemHolder.binding.cardViewBanner, true, (int) vHeight, (int) vWidth);
                dataItemHolder.binding.txtArea.setVisibility(View.GONE);
                dataItemHolder.binding.txtTitle.setVisibility(View.GONE);
                dataItemHolder.binding.bannerImgViewLeft.setVisibility(View.VISIBLE);

            } else if (mActivity.generalFunc.getJsonValueStr("ImagePosition", mBannerObject).equalsIgnoreCase("Center-Bottom")) {
                commonCode_3(dataItemHolder.binding.bannerImgViewLeft, Url, dataItemHolder.binding.cardViewBanner, true, (int) vHeight, (int) vWidth);
                dataItemHolder.binding.txtTitleLeft.setVisibility(View.VISIBLE);
                dataItemHolder.binding.bannerImgViewLeft.setVisibility(View.VISIBLE);
                dataItemHolder.binding.txtArea.setVisibility(View.GONE);
                dataItemHolder.binding.txtTitle.setVisibility(View.GONE);

                dataItemHolder.binding.leftArea.setPadding(v7sdp, v7sdp, v7sdp, v7sdp);

            }


            int leftP = 0, centerP = 0;
            if (!isFullView && displayCount <= 2 || isScroll) {
                if (position == 0) {
                    leftP = mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp);
                }
                if (position != (mBannerArray.length() - 1)) {
                    centerP = itemSpacing;
                } else if (position == (mBannerArray.length() - 1)) {
                    centerP = itemLRMargin;
                }
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(isRTL ? centerP : leftP, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0, isRTL ? leftP : centerP, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0);
            dataItemHolder.binding.mainArea.setLayoutParams(layoutParams);

            dataItemHolder.binding.txtSubTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dataItemHolder.binding.txtSubTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    try {
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            int height = dataItemHolder.binding.txtSubTitle.getMeasuredHeight();
                            int height2 = dataItemHolder.binding.cardViewBanner.getMeasuredHeight();
                            int height3 = dataItemHolder.binding.txtTitleRight.getMeasuredHeight();
                            int height4 = dataItemHolder.binding.txtTitle.getMeasuredHeight();
                            int totalHeight = height + height3 + height4;
                            if (displayCount == 1) {
                                totalHeight = totalHeight + mActivity.getResources().getDimensionPixelSize(R.dimen._8sdp);
                            } else {
                                totalHeight = totalHeight + mActivity.getResources().getDimensionPixelSize(R.dimen._14sdp);
                            }
                            if (height2 <= totalHeight) {
                                int lineCount = dataItemHolder.binding.txtSubTitle.getLineCount() - 1;
                                dataItemHolder.binding.txtSubTitle.setMaxLines(lineCount);
                                dataItemHolder.binding.txtSubTitle.setEllipsize(TextUtils.TruncateAt.END);
                            }

                        }, 50);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (isClickable) {
                dataItemHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onBannerItemClick(position, mBannerObject));
            }

        } else if (holder instanceof RideShareViewHolder) {
            RideShareViewHolder rsHolder = (RideShareViewHolder) holder;

            rsHolder.binding.txtTitle.setText(mActivity.generalFunc.getJsonValueStr("vCategoryTitle", mBannerObject));
            rsHolder.binding.txtSubTitle.setText(mActivity.generalFunc.getJsonValueStr("vCategoryDesc", mBannerObject));
            rsHolder.binding.txtBookNow.setText(mActivity.generalFunc.getJsonValueStr("BookBtnText", mItemObject));

            String vBgColor = mActivity.generalFunc.getJsonValueStr("vBgColor", mBannerObject);
            if (Utils.checkText(vBgColor)) {
                rsHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
            }

            LinearLayout.LayoutParams bannerLayoutParams = (LinearLayout.LayoutParams) rsHolder.binding.bannerImgView.getLayoutParams();
            bannerLayoutParams.width = bannerWidth;
            bannerLayoutParams.height = bannerHeight;
            rsHolder.binding.bannerImgView.setLayoutParams(bannerLayoutParams);

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mBannerObject);
            commonCode_1(rsHolder.binding.bannerImgView, Url, rsHolder.binding.cardViewBanner, false);

            if (isClickable) {
                rsHolder.binding.cardViewBanner.setOnClickListener(v -> listener.onBannerItemClick(position, mBannerObject));
            }
            rsHolder.binding.mainArea.setPadding(0, AddTopPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0, 0, AddBottomPadding ? mActivity.getResources().getDimensionPixelSize(R.dimen._11sdp) : 0);
        }
    }

    private void setFieldData(DataArrHolder dataItemHolder, String vTitle, String vTextColor, float vTextFontSize, float vDecFontSize, String vBgColor, String vDescription) {
        dataItemHolder.binding.txtTitleLeft.setText(vTitle);
        dataItemHolder.binding.txtTitle.setText(vTitle);
        dataItemHolder.binding.txtTitleRight.setText(vTitle);

        if (Utils.checkText(vTextColor)) {
            dataItemHolder.binding.txtTitleLeft.setTextColor(Color.parseColor(vTextColor));
            dataItemHolder.binding.txtTitle.setTextColor(Color.parseColor(vTextColor));
            dataItemHolder.binding.txtTitleRight.setTextColor(Color.parseColor(vTextColor));
            dataItemHolder.binding.txtSubTitle.setTextColor(Color.parseColor(vTextColor));
        }
        /*if (Utils.checkText(String.valueOf(vTextFontSize))) {
            if (displayCount == 1) {
                dataItemHolder.binding.txtTitle.setTextSize(vTextFontSize);
                dataItemHolder.binding.txtTitleRight.setTextSize(vTextFontSize);
            } else {
                dataItemHolder.binding.txtTitle.setTextSize((float) (vTextFontSize / 1.1));
                dataItemHolder.binding.txtTitleRight.setTextSize((float) (vTextFontSize / 1.1));
            }
        }*/


        dataItemHolder.binding.txtSubTitle.setText(vDescription);
        /*if (Utils.checkText(String.valueOf(vDecFontSize))) {
            dataItemHolder.binding.txtSubTitle.setTextSize(vDecFontSize);
        }*/
        if (Utils.checkText(vBgColor)) {
            dataItemHolder.binding.cardViewBanner.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(vBgColor)));
        }

    }

    @Override
    public int getItemViewType(int position) {
        JSONObject itemObject = mActivity.generalFunc.getJsonObject(mBannerArray, position);
        if (itemObject == null) {
            return 0;
        }

        if (mItemObject.has("DataArr")) {
            return TYPE_DATA_ARRAY;
        } else if (itemObject.has("rentitemSection") || itemObject.has("rentestateSection") || itemObject.has("rentcarSection")) {

            if (displayCount == 2) {
                return TYPE_RENT_ESTATE_CAR;
            } else {
                return TYPE_RENT_ITEM;
            }

        } else if (itemObject.has("RideShareSection") && !isOnlyImage) {
            return TYPE_RIDE_SHARE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        if (mBannerArray == null) {
            return 0;
        }
        return mBannerArray.length();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONObject mResponseObject) {
        if (mResponseObject.has("BANNER_DATA")) {
            this.mBannerArray = mActivity.generalFunc.getJsonArray("BANNER_DATA", mResponseObject);
            this.bannerWidth = sWidth - itemLRMargin;
            this.bannerHeight = (int) ((int) sWidth / 2.33);
        }
        notifyDataSetChanged();
    }
    /////////////////////////////-----------------//////////////////////////////////////////////

    private static class EStateCarViewHolder extends RecyclerView.ViewHolder {

        private final Item23RentEstateCarBinding binding;

        private EStateCarViewHolder(Item23RentEstateCarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class RentItemViewHolder extends RecyclerView.ViewHolder {

        private final Item23RentItemBinding binding;

        private RentItemViewHolder(Item23RentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class DataArrHolder extends RecyclerView.ViewHolder {

        private final ItemDataBannerBinding binding;

        private DataArrHolder(ItemDataBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class RideShareViewHolder extends RecyclerView.ViewHolder {

        private final Item23RideShareBinding binding;

        private RideShareViewHolder(Item23RideShareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class NormalViewHolder extends RecyclerView.ViewHolder {

        private final Item23BannerItemBinding binding;

        private NormalViewHolder(Item23BannerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //..............................
    private void commonCode_1(AppCompatImageView holder, String Url, LinearLayout cardViewBanner, boolean isResize) {
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        if (isResize) {
            Url = Utils.getResizeImgURL(mActivity, Url, bannerWidth, bannerHeight);
        }
        new LoadImage.builder(LoadImage.bind(Url), holder).build();

        ///////////////////////////////////////////////////////////////-----------------------------
        if (isFullView) {
            cardViewBanner.setBackground(null);
            cardViewBanner.setClipToOutline(false);
        } else {
            cardViewBanner.setClipToOutline(true);
            cardViewBanner.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.card_view_23_gray_flat));
        }
    }

    private void commonCode_3(AppCompatImageView holder, String Url, LinearLayout cardViewBanner, boolean isResize, int height, int width) {
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        if (isResize) {
            Url = Utils.getResizeImgURL(mActivity, Url, width, height);
        }
        new LoadImage.builder(LoadImage.bind(Url), holder).build();

        ///////////////////////////////////////////////////////////////-----------------------------
        if (isFullView) {
            cardViewBanner.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.cardView23ProBG));
            cardViewBanner.setClipToOutline(false);
        } else {
            cardViewBanner.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.card_view_23_gray_flat));
            cardViewBanner.setClipToOutline(true);
        }
    }

    private void commonCode_2(ShapeableImageView mImgView, String Url, boolean isResize, int allCorners) {
        if (!Utils.checkText(Url)) {
            Url = "Temp";
        }
        if (isResize) {
            Url = Utils.getResizeImgURL(mActivity, Url, bannerWidth, bannerHeight);
        }
        if (isScroll) {
            new LoadImage.builder(LoadImage.bind(Url), mImgView).setPlaceholderImagePath(R.color.imageBg).setErrorImagePath(R.color.imageBg).build();
        } else {
            new LoadImage.builder(LoadImage.bind(Url), mImgView).build();
        }

        ///////////////////////////////////////////////////////////////-----------------------------
        if (isFullView) {
            allCorners = 0;
        }
        mImgView.setShapeAppearanceModel(mImgView.getShapeAppearanceModel()
                .toBuilder().setAllCorners(CornerFamily.ROUNDED, allCorners).build());
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public interface OnClickListener {
        void onBannerItemClick(int position, JSONObject jsonObject);
    }
}