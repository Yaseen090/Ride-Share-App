package com.tiktak24.user.homescreen23.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tiktak24.user.UberXHomeActivity;
import com.tiktak24.user.databinding.Item23GridItemBinding;
import com.tiktak24.user.databinding.Item23GridItemProDeliverAllBinding;
import com.tiktak24.user.databinding.Item23GridItemProDeliveryOnlyBinding;
import com.tiktak24.user.databinding.Item23GridItemProSpBinding;
import com.model.ServiceModule;
import com.utils.LoadImage;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class ServiceGrid23Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final UberXHomeActivity mActivity;
    private JSONArray mServicesArr;
    private final OnClickListener listener;

    public ServiceGrid23Adapter(@NonNull UberXHomeActivity activity, @NonNull JSONObject itemObject, @NonNull OnClickListener listener) {
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
            return new ViewHolderDeliveryOnly(Item23GridItemProDeliveryOnlyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (ServiceModule.isServiceProviderOnly()) {
            return new ViewHolderSP(Item23GridItemProSpBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (mActivity.generalFunc.isDeliverOnlyEnabled() && ServiceModule.DeliverAll) {
            return new ViewHolderDeliverAll(Item23GridItemProDeliverAllBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ViewHolder(Item23GridItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder bHolder = (ViewHolder) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.imgCategory).build();

            ///////////////////////////////////////////////////////////////-----------------------------
            bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceItemClick(position, mServiceObject));
        } else if (holder instanceof ViewHolderSP) {
            ViewHolderSP bHolder = (ViewHolderSP) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategoryName", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.imgCategory).build();

            ///////////////////////////////////////////////////////////////-----------------------------
            bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceItemClick(position, mServiceObject));

        } else if (holder instanceof ViewHolderDeliverAll) {
            ViewHolderDeliverAll bHolder = (ViewHolderDeliverAll) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("vCategory", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vLogo_image", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.imgCategory).build();

            ///////////////////////////////////////////////////////////////-----------------------------
            bHolder.binding.mainArea.setOnClickListener(v -> listener.onServiceItemClick(position, mServiceObject));
        } else if (holder instanceof ViewHolderDeliveryOnly) {
            ViewHolderDeliveryOnly bHolder = (ViewHolderDeliveryOnly) holder;
            JSONObject mServiceObject = mActivity.generalFunc.getJsonObject(mServicesArr, position);

            bHolder.binding.txtCategoryName.setText(mActivity.generalFunc.getJsonValueStr("itemName", mServiceObject));

            String Url = mActivity.generalFunc.getJsonValueStr("vImage", mServiceObject);
            if (!Utils.checkText(Url)) {
                Url = "Temp";
            }
            new LoadImage.builder(LoadImage.bind(Url), bHolder.binding.imgCategory).build();

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

        private final Item23GridItemBinding binding;

        private ViewHolder(Item23GridItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ViewHolderSP extends RecyclerView.ViewHolder {

        private final Item23GridItemProSpBinding binding;

        private ViewHolderSP(Item23GridItemProSpBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ViewHolderDeliverAll extends RecyclerView.ViewHolder {

        private final Item23GridItemProDeliverAllBinding binding;

        private ViewHolderDeliverAll(Item23GridItemProDeliverAllBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private static class ViewHolderDeliveryOnly extends RecyclerView.ViewHolder {

        private final Item23GridItemProDeliveryOnlyBinding binding;

        private ViewHolderDeliveryOnly(Item23GridItemProDeliveryOnlyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnClickListener {
        void onServiceItemClick(int position, JSONObject jsonObject);
    }
}