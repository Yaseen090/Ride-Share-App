package com.tiktak24.user;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.activity.ParentActivity;
import com.general.files.ActUtils;
import com.utils.Utils;
import com.view.MButton;
import com.view.MTextView;
import com.view.MaterialRippleLayout;
import com.view.editBox.MaterialEditText;

public class BusinessSetupActivity extends ParentActivity {

    MTextView titleTxt, emailNoteTxt;

    MButton skipbtn, nextbtn;
    MaterialEditText emailBox;
    MTextView emailLabelTxt;

    boolean emailEntered;
    String required_str = "", error_email_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_setup);

        required_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_REQUIRD");
        error_email_str = generalFunc.retrieveLangLBl("", "LBL_FEILD_EMAIL_ERROR_TXT");
        titleTxt = findViewById(R.id.titleTxt);
        emailNoteTxt = findViewById(R.id.emailNoteTxt);
        emailBox = findViewById(R.id.emailBox);
        skipbtn = ((MaterialRippleLayout)findViewById(R.id.skipbtn)).getChildView();
        nextbtn =  ((MaterialRippleLayout)findViewById(R.id.nextbtn)).getChildView();

        skipbtn.setId(Utils.generateViewId());
        nextbtn.setId(Utils.generateViewId());

        emailLabelTxt = findViewById(R.id.emailLabelTxt);
        emailBox.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        ImageView backImgView = findViewById(R.id.backImgView);
        addToClickHandler(backImgView);
        if (generalFunc.isRTLmode()) {
            backImgView.setRotation(180);
        }
        titleTxt.setText(generalFunc.retrieveLangLBl("", "LBL_TRACK_SERVICE_SETUP_PROFILE_TXT"));
        emailLabelTxt.setText(generalFunc.retrieveLangLBl("","LBL_EMAIL"));
        emailNoteTxt.setText(generalFunc.retrieveLangLBl("", "LBL_BUSINESS_EMAIL_FOR_BILL"));
        emailBox.setHint(generalFunc.retrieveLangLBl("", "LBL_ENTER_EMAIL_HINT"));
        skipbtn.setText(generalFunc.retrieveLangLBl("", "LBL_SKIP"));
        nextbtn.setText(generalFunc.retrieveLangLBl("", "LBL_BTN_NEXT_TXT"));
        addToClickHandler(skipbtn);
        addToClickHandler(nextbtn);
        emailBox.setText(generalFunc.getJsonValueStr("vEmail", obj_userProfile));

    }

    private Context getActContext() {
        return BusinessSetupActivity.this;
    }


    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.backImgView) {
            onBackPressed();
        } else if (i == skipbtn.getId()) {
            Bundle bn = new Bundle();
            bn.putString("iUserProfileMasterId", getIntent().getStringExtra("iUserProfileMasterId"));
            bn.putString("email", generalFunc.getJsonValueStr("vEmail", obj_userProfile));
            new ActUtils(getActContext()).startActWithData(SelectOrganizationActivity.class, bn);
        } else if (i == nextbtn.getId()) {
            emailEntered = Utils.checkText(emailBox) ?
                    (generalFunc.isEmailValid(Utils.getText(emailBox)) || Utils.setErrorFields(emailBox, error_email_str))
                    : Utils.setErrorFields(emailBox, required_str);

            if (!emailEntered) {
                return;
            }
            Bundle bn = new Bundle();
            bn.putString("iUserProfileMasterId", getIntent().getStringExtra("iUserProfileMasterId"));
            bn.putString("email", emailBox.getText().toString().trim());
            new ActUtils(getActContext()).startActWithData(SelectOrganizationActivity.class, bn);
        }
    }

}