package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiktak24.user.R;
import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23GridBinding;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class Grid23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final UberXHomeActivity mActivity;
    private JSONArray mServicesArr;
    private final OnClickListener listener;
    private int sWidth;

    public Grid23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        if (itemObject.has("servicesArr")) {
            this.mServicesArr = mActivity.generalFunc.getJsonArray("servicesArr", itemObject);
        } else if (itemObject.has("SubCategories")) {
            this.mServicesArr = mActivity.generalFunc.getJsonArray("SubCategories", itemObject);
        }
        this.listener = listener;

        this.sWidth = (int) Utils.getScreenPixelWidth(mActivity);
        if (mServicesArr != null && mServicesArr.length() > 0) {
            sWidth = (sWidth - (mActivity.getResources().getDimensionPixelSize(R.dimen._15sdp) * 4)) / 3;
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(Item23GridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder bHolder = (ViewHolder) holder;

            LinearLayout.LayoutParams mParams = (LinearLayout.LayoutParams) bHolder.binding.mainArea.getLayoutParams();
            //mParams.width = sWidth;
            mParams.height = sWidth;
            bHolder.binding.mainArea.setLayoutParams(mParams);

            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            } else {
                Url = Utils.getResizeImgURL(mActivity, Url, sWidth, sWidth);
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.gridImage).build();

            ///////////////////////////////////////////////////////////////-----------------------------
            bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceItemClick(position, mServiceObject));
        }
    }

    @Override
    public int getItemCount() {
        if (mServicesArr == null) {
            return 0;
        }
        return mServicesArr.length();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private final Item23GridBinding binding;

        private ViewHolder(Item23GridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onServiceItemClick(int position, JSONObject jsonObject);
    }
}