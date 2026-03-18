package com.tiktak24.user.parking.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fragments.BaseFragment;
import com.general.files.GeneralFunctions;
import com.general.files.MyApp;
import com.tiktak24.user.R;
import com.tiktak24.user.databinding.ParkingInformationFragmentLayoutBinding;
import com.tiktak24.user.parking.ParkingDetailsActivity;
import com.tiktak24.user.parking.adapter.ParkingImagesAdapterNew;

import java.util.HashMap;

public class ParkingInformationFragment extends BaseFragment {

    public ParkingInformationFragmentLayoutBinding binding;
    private ParkingDetailsActivity mActivity;

    private GeneralFunctions generalFunc;
    private ParkingImagesAdapterNew imageAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.parking_information_fragment_layout, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        imageAdapter = new ParkingImagesAdapterNew(mActivity, generalFunc, mActivity.photosData, 5, false, new ParkingImagesAdapterNew.OnItemClickListener() {
            @Override
            public void onItemClickList(int position, HashMap<String, String> mapData) {

            }

            @Override
            public void onDeleteClick(int position, HashMap<String, String> mapData) {

            }
        }, true);
        binding.photosRv.setAdapter(imageAdapter);
        binding.mediaHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_RENT_ITEM_PHOTOS"));
        binding.AdditionalNotesHTxt.setText(generalFunc.retrieveLangLBl("", "LBL_ADDITIONAL_NOTES"));
//        MyApp.executeWV(msgTxt, generalFunctions, Msg);
        binding.WebView.getSettings().setUseWideViewPort(true);
        binding.WebView.getSettings().setLoadWithOverviewMode(true);
        binding.WebView.getSettings().setTextZoom(350);
        MyApp.executeWV(binding.WebView, generalFunc, mActivity.instructions);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.getInstance().getCurrentAct() instanceof ParkingDetailsActivity) {
            mActivity = (ParkingDetailsActivity) requireActivity();
            generalFunc = mActivity.generalFunc;
        }
    }
}
