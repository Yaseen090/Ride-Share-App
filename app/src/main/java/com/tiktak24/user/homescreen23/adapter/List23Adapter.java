package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23ListBinding;
import com.tiktak24.user.databinding.Item23ListProDeliveryOnlyBinding;
import com.model.ServiceModule;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class List23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final UberXHomeActivity mActivity;
    private JSONArray mServicesArr;
    private final OnClickListener listener;

    public List23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
        this.mActivity = activity;
        if (itemObject.has("servicesArr")) {
            this.mServicesArr = mActivity.generalFunc.getJsonArray("servicesArr", itemObject);
        } else if (itemObject.has("SubCategories")) {
            this.mServicesArr = mActivity.generalFunc.getJsonArray("SubCategories", itemObject);
        }
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (ServiceModule.isDeliveronly()) {
            return new ListViewHolderDeliveryOnly(Item23ListProDeliveryOnlyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ListViewHolder(Item23ListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ListViewHolder) {
            ListViewHolder bHolder = (ListViewHolder) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.titleTxt.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));
            bHolder.binding.descriptionTxt.setText(mActivity.generalFunc.getJsonValueStr("tListDescription", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.listImage).build();

            ///////////////////////////////////////////////////////////////-----------------------------
            bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceItemClick(position, mServiceObject));
        } else if (holder instanceof ListViewHolderDeliveryOnly) {
            ListViewHolderDeliveryOnly bHolder = (ListViewHolderDeliveryOnly) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.titleTxt.setText(mActivity.generalFunc.getJsonValueStr("vCategory", mServiceObject));
            bHolder.binding.descriptionTxt.setText(mActivity.generalFunc.getJsonValueStr("tListDescription", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.listImage).build();

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

    private static class ListViewHolder extends RecyclerView.ViewHolder {

        private final Item23ListBinding binding;

        private ListViewHolder(Item23ListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ListViewHolderDeliveryOnly extends RecyclerView.ViewHolder {

        private final Item23ListProDeliveryOnlyBinding binding;

        private ListViewHolderDeliveryOnly(Item23ListProDeliveryOnlyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onServiceItemClick(int position, JSONObject jsonObject);
    }
}