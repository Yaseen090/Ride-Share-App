package com.adapter.files;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.general.files.GeneralFunctions;
import com.tiktak24.user.R;
import com.utils.CommonUtilities;
import com.utils.LoadImage;
import com.utils.Utils;
import com.view.MTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 09-07-2016.
 */
public class ChatMessagesRecycleAdapter extends RecyclerView.Adapter<ChatMessagesRecycleAdapter.ViewHolder> {

    public GeneralFunctions generalFunc;
    ArrayList<HashMap<String, String>> list_item;
    Context mContext;
    OnItemClickList onItemClickList;

    String memberId;

    public ChatMessagesRecycleAdapter(Context mContext, ArrayList<HashMap<String, String>> list_item, GeneralFunctions generalFunc) {
        this.mContext = mContext;
        this.list_item = list_item;
        this.generalFunc = generalFunc;

        memberId = generalFunc.getMemberId();
    }

    @Override
    public ChatMessagesRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        HashMap<String, String> item = list_item.get(position);

        String eUserType = item.get("iFromMemberType").toString();

        if (eUserType.equalsIgnoreCase(Utils.app_type)) {
            viewHolder.activity_main.setVisibility(View.VISIBLE);

            viewHolder.messageUser.setText("You");
            viewHolder.rightuserImageview.setVisibility(View.VISIBLE);
            viewHolder.lefttuserImageview.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            viewHolder.activity_main.setLayoutParams(params);

            viewHolder.activity_main.getBackground().setColorFilter(mContext.getResources().getColor(R.color.text23Pro_Dark), PorterDuff.Mode.SRC_ATOP);
            viewHolder.leftshap.setColorFilter(mContext.getResources().getColor(R.color.text23Pro_Dark));
            viewHolder.mainlayout.setVisibility(View.VISIBLE);
            viewHolder.rightshape.setVisibility(View.GONE);
            viewHolder.leftshap.setVisibility(View.VISIBLE);
            viewHolder.messageText.setTextColor(mContext.getResources().getColor(R.color.white));

            if (item.get("tTime") != null && !item.get("tTime").equals("")) {
                viewHolder.right_message_time.setVisibility(View.VISIBLE);
                viewHolder.right_message_time.setText(generalFunc.convertNumberWithRTL(item.get("tTime")));

            }
            viewHolder.left_message_time.setVisibility(View.GONE);

            String image_url = CommonUtilities.USER_PHOTO_PATH + item.get("iFromMemberId") + "/" + item.get("iFromMemberImage");

            new LoadImage.builder(LoadImage.bind(image_url), viewHolder.rightuserImageview).setErrorImagePath(R.mipmap.ic_no_pic_user).setPlaceholderImagePath(R.mipmap.ic_no_pic_user).build();
        } else {
            viewHolder.activity_main.setVisibility(View.VISIBLE);

            viewHolder.lefttuserImageview.setVisibility(View.VISIBLE);
            viewHolder.rightuserImageview.setVisibility(View.GONE);
            viewHolder.messageText.setTextColor(mContext.getResources().getColor(R.color.white));

            if (item.get("tTime") != null && !item.get("tTime").equals("")) {
                viewHolder.left_message_time.setVisibility(View.VISIBLE);
                viewHolder.left_message_time.setText(generalFunc.convertNumberWithRTL(item.get("tTime")));
            }
            viewHolder.right_message_time.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            viewHolder.activity_main.setLayoutParams(params);
            viewHolder.activity_main.getBackground().setColorFilter(mContext.getResources().getColor(R.color.appThemeColor_1), PorterDuff.Mode.SRC_ATOP);
            viewHolder.rightshape.setVisibility(View.VISIBLE);
            viewHolder.leftshap.setVisibility(View.GONE);

            String image_url = CommonUtilities.PROVIDER_PHOTO_PATH + item.get("iFromMemberId") + "/" + item.get("iFromMemberImage");


            new LoadImage.builder(LoadImage.bind(image_url), viewHolder.lefttuserImageview).setErrorImagePath(R.mipmap.ic_no_pic_user).setPlaceholderImagePath(R.mipmap.ic_no_pic_user).build();
        }

        String Text = item.get("tMessage").toString();
        if (Text.length() == 1) {
            viewHolder.messageText.setText(" " + Text + " ");
        } else {
            viewHolder.messageText.setText(Text);
        }

        viewHolder.messageUser.setText(eUserType);
    }

    @Override
    public int getItemCount() {
        return list_item.size();
    }

    public void setOnItemClickList(OnItemClickList onItemClickList) {
        this.onItemClickList = onItemClickList;
    }

    public interface OnItemClickList {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageUser, left_message_time, right_message_time;
        public LinearLayout activity_main, mainlayout;
        ImageView rightshape, leftshap;
        ImageView lefttuserImageview, rightuserImageview;
        MTextView messageText;

        public ViewHolder(View view) {
            super(view);


            mainlayout = (LinearLayout) view.findViewById(R.id.mainlayout);
            rightshape = (ImageView) view.findViewById(R.id.rightshape);
            leftshap = (ImageView) view.findViewById(R.id.leftshap);

            lefttuserImageview = (ImageView) view.findViewById(R.id.lefttuserImageview);
            rightuserImageview = (ImageView) view.findViewById(R.id.rightuserImageview);


            messageText = (MTextView) view.findViewById(R.id.message_text);
            messageUser = (TextView) view.findViewById(R.id.message_user);
            left_message_time = (TextView) view.findViewById(R.id.left_message_time);
            right_message_time = (TextView) view.findViewById(R.id.right_message_time);
            activity_main = (LinearLayout) view.findViewById(R.id.activity_main);
        }
    }

}
