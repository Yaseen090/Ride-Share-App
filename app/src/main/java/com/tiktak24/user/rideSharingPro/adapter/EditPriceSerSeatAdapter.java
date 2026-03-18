package com.tiktak24.user.rideSharingPro.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.autofit.et.lib.AutoFitEditText;
import com.general.files.DecimalDigitsInputFilter;
import com.general.files.GeneralFunctions;
import com.tiktak24.user.databinding.ItemRideProEditPriceBinding;
import com.utils.Logger;
import com.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class EditPriceSerSeatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final GeneralFunctions generalFunc;
    private final String currency;
    @Nullable
    private JSONArray mEditPriceArr;
    private final double finalFareBidTaxi = 0.0;

    public EditPriceSerSeatAdapter(@NonNull GeneralFunctions generalFunctions, @NonNull String currency, @NonNull JSONArray jsonArray) {
        this.generalFunc = generalFunctions;
        this.currency = currency;
        this.mEditPriceArr = jsonArray;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRideProEditPriceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder bHolder = (ViewHolder) holder;

        JSONObject mItemObj = generalFunc.getJsonObject(mEditPriceArr, position);

        bHolder.binding.startAddressTxt.setText(generalFunc.getJsonValue("add", generalFunc.getJsonValueStr("startPoint", mItemObj)));
        bHolder.binding.endAddressTxt.setText(generalFunc.getJsonValue("add", generalFunc.getJsonValueStr("endPoint", mItemObj)));
        bHolder.binding.currencyTxt.setText(currency);

        bHolder.binding.priceEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        bHolder.binding.priceEdit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        bHolder.binding.priceEdit.setHint("0.0");
        bHolder.binding.priceEdit.setText(generalFunc.getJsonValueStr("recommended_price", mItemObj));

        bHolder.binding.priceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                savePrice(position, mItemObj, bHolder.binding.priceEdit);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bHolder.binding.priceEdit.getText().length() == 1) {
                    if (bHolder.binding.priceEdit.getText().toString().contains(".")) {
                        bHolder.binding.priceEdit.setText("0.");
                        bHolder.binding.priceEdit.setSelection(bHolder.binding.priceEdit.length());
                    }
                }
            }
        });

        bHolder.binding.minusBtn.setOnClickListener(view -> {
            if (Utils.checkText(bHolder.binding.priceEdit) && GeneralFunctions.parseDoubleValue(0, Utils.getText(bHolder.binding.priceEdit)) >= finalFareBidTaxi) {
                bHolder.binding.priceEdit.setText(String.format(Locale.ENGLISH, "%.2f", (double) (GeneralFunctions.parseDoubleValue(0.0, convertCommaToDecimal(Utils.getText(bHolder.binding.priceEdit))) - 1)));
                if (!(GeneralFunctions.parseDoubleValue(0, Utils.getText(bHolder.binding.priceEdit)) >= finalFareBidTaxi)) {
                    bHolder.binding.priceEdit.setText("" + finalFareBidTaxi);
                }
                savePrice(position, mItemObj, bHolder.binding.priceEdit);
            }
        });
        bHolder.binding.plusBtn.setOnClickListener(view -> {
            if (Utils.checkText(bHolder.binding.priceEdit)) {
                bHolder.binding.priceEdit.setText(String.format(Locale.ENGLISH, "%.2f", (double) (GeneralFunctions.parseDoubleValue(0.0, convertCommaToDecimal(Utils.getText(bHolder.binding.priceEdit))) + 1)));
            } else {
                bHolder.binding.priceEdit.setText("1.00");
            }
            savePrice(position, mItemObj, bHolder.binding.priceEdit);
        });
    }

    private void savePrice(int position, JSONObject mItemObj, AutoFitEditText priceEdit) {
        try {
            mItemObj.put("recommended_price", Utils.getText(priceEdit));
            if (mEditPriceArr != null) {
                mEditPriceArr.put(position, mItemObj);
            }
        } catch (JSONException e) {
            Logger.e("Exception", "::" + e.getMessage());
        }
    }

    private String convertCommaToDecimal(String amount) {
        return amount.contains(",") ? amount.replace(",", ".") : amount;
    }

    @Override
    public int getItemCount() {
        return mEditPriceArr != null ? mEditPriceArr.length() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(JSONArray servicesArr) {
        this.mEditPriceArr = servicesArr;
        notifyDataSetChanged();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemRideProEditPriceBinding binding;

        private ViewHolder(ItemRideProEditPriceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}