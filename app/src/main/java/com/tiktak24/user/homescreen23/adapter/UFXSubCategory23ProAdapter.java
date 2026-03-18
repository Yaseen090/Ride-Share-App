package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23UfxSubCategoryBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class UFXSubCategory23ProAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final UberXHomeActivity mActivity;
    private final GeneralFunctions generalFunc;
    private final OnItemClickList onItemClickList;

    private JSONArray mServiceArray;

    private CompoundButton lastCheckedRB = null;

    private boolean isMultiSelect = false, isRadioSelection = false;
    int grid;

    public UFXSubCategory23ProAdapter(UberXHomeActivity activity, GeneralFunctions generalFunc, JSONArray serviceArray, OnItemClickList onItemClickList) {
        this.mActivity = activity;
        this.mServiceArray = serviceArray;
        this.generalFunc = generalFunc;
        this.onItemClickList = onItemClickList;

        updateData(isMultiSelect, isRadioSelection);
        this.grid = mActivity.getResources().getDimensionPixelSize(R.dimen.category_grid_size);
    }

    public void updateData(boolean isMultiSelect, boolean isRadioSelection) {
        this.isMultiSelect = isMultiSelect;
        this.isRadioSelection = isRadioSelection;
        if (lastCheckedRB != null) {
            if (lastCheckedRB.isChecked()) {
                lastCheckedRB.setChecked(false);
                lastCheckedRB = null;
            }
        }
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(Item23UfxSubCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder parentViewHolder, final int position) {

        JSONObject mServiceObject = generalFunc.getJsonObject(mServiceArray, position);

        String vCategory = generalFunc.getJsonValueStr("vCategory", mServiceObject);
        String vLogo_image = generalFunc.getJsonValueStr("vLogo_image", mServiceObject);

        if (parentViewHolder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) parentViewHolder;

            if (generalFunc.isRTLmode()) {
                viewHolder.binding.arrowImageView.setRotation(180);
            }
            if (vCategory != null) {
                if (vCategory.matches("\\w*")) {
                    viewHolder.binding.uberXCatNameTxtView.setMaxLines(1);

                    viewHolder.binding.uberXCatNameTxtView.setText(vCategory);
                } else {
                    viewHolder.binding.uberXCatNameTxtView.setMaxLines(2);

                    viewHolder.binding.uberXCatNameTxtView.setText(vCategory);
                }
            }

            String imageURL = Utils.getResizeImgURL(mActivity, vLogo_image, grid, grid);
            new LoadImage.builder(LoadImage.bind(imageURL), viewHolder.binding.catImgView).setErrorImagePath(R.mipmap.ic_no_icon).setPlaceholderImagePath(R.mipmap.ic_no_icon).build();

            viewHolder.binding.uberXCatNameTxtView.setTextColor(mActivity.getResources().getColor(R.color.text23Pro_Dark));
            viewHolder.binding.serviceCheckbox.setChecked(false);

            viewHolder.binding.contentArea.setOnClickListener(view -> {

                if (isMultiSelect) {
                    viewHolder.binding.serviceCheckbox.setChecked(!viewHolder.binding.serviceCheckbox.isChecked());
                } else {
                    if (isRadioSelection) {
                        viewHolder.binding.serviceradioBtn.performClick();
                    } else {
                        if (onItemClickList != null) {
                            onItemClickList.onItemClick(position);
                        }
                    }
                }
            });

            viewHolder.binding.arrowImageView.setVisibility(View.GONE);
            if (isMultiSelect) {
                viewHolder.binding.serviceradioBtn.setVisibility(View.GONE);
                viewHolder.binding.serviceCheckbox.setVisibility(View.VISIBLE);
                viewHolder.binding.serviceCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
                            if (b) {
                                viewHolder.binding.uberXCatNameTxtView.setTextColor(mActivity.getResources().getColor(R.color.appThemeColor_1));
                            } else {
                                viewHolder.binding.uberXCatNameTxtView.setTextColor(mActivity.getResources().getColor(R.color.text23Pro_Dark));
                            }
                            String iVehicleCategoryId = generalFunc.getJsonValueStr("iVehicleCategoryId", mServiceObject);
                            String iBiddingId = generalFunc.getJsonValueStr("iBiddingId", mServiceObject);
                            onItemClickList.onMultiItem(Utils.checkText(iVehicleCategoryId) ? iVehicleCategoryId : iBiddingId, vCategory, b);
                        }
                );

                String isCheck = generalFunc.getJsonValueStr("isCheck", mServiceObject);
                if (isCheck != null && isCheck.equals("Yes")) {
                    viewHolder.binding.serviceCheckbox.setChecked(true);
                } else if (isCheck != null && isCheck.equals("No")) {
                    viewHolder.binding.serviceCheckbox.setChecked(false);
                }
            } else if (isRadioSelection) {
                viewHolder.binding.serviceradioBtn.setVisibility(View.VISIBLE);
                viewHolder.binding.serviceCheckbox.setVisibility(View.GONE);
                viewHolder.binding.serviceradioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
                            if (lastCheckedRB != null) {
                                lastCheckedRB.setChecked(false);
                            }
                            lastCheckedRB = viewHolder.binding.serviceradioBtn;
                            if (b) {
                                viewHolder.binding.uberXCatNameTxtView.setTextColor(mActivity.getResources().getColor(R.color.appThemeColor_1));
                            } else {
                                viewHolder.binding.uberXCatNameTxtView.setTextColor(mActivity.getResources().getColor(R.color.text23Pro_Dark));
                            }
                            String iVehicleCategoryId = generalFunc.getJsonValueStr("iVehicleCategoryId", mServiceObject);
                            String iBiddingId = generalFunc.getJsonValueStr("iBiddingId", mServiceObject);
                            onItemClickList.onMultiItem(Utils.checkText(iVehicleCategoryId) ? iVehicleCategoryId : iBiddingId, vCategory, b);
                        }
                );
                String isCheck = generalFunc.getJsonValueStr("isCheck", mServiceObject);
                if (isCheck != null && isCheck.equals("Yes")) {
                    viewHolder.binding.serviceradioBtn.setChecked(true);
                } else if (isCheck != null && isCheck.equals("No")) {
                    viewHolder.binding.serviceradioBtn.setChecked(false);
                }
            } else {
                viewHolder.binding.serviceradioBtn.setVisibility(View.GONE);
                viewHolder.binding.serviceCheckbox.setVisibility(View.GONE);
                viewHolder.binding.arrowImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mServiceArray == null) {
            return 0;
        }
        return mServiceArray.length();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(JSONArray serviceArray, boolean isMultiSelect, boolean isRadioSelection) {
        this.mServiceArray = serviceArray;
        updateData(isMultiSelect, isRadioSelection);
        notifyDataSetChanged();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final Item23UfxSubCategoryBinding binding;

        private ViewHolder(Item23UfxSubCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickList {
        void onItemClick(int position);

        void onMultiItem(String id, String category, boolean b);
    }
}