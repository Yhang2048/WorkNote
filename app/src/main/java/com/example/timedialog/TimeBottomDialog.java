package com.example.timedialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.example.timedialog.timeadapter.DataAdapter;
import com.example.timedialog.timeadapter.TimeAdapter;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by YHang on 18/5/28.
 */

public class TimeBottomDialog extends Dialog implements View.OnClickListener, DataAdapter.OnItemClickListener, TimeAdapter.OnTimeClickListener {
    private ImageView close;
    private Context mContext;
    private RecyclerView dateList, timeList;
    private DataAdapter mDataAdapter;
    private TimeAdapter mTimeAdapter;

    public void setMyTime(OnUpdateMyTime myTime) {
        this.myTime = myTime;
    }

    private OnUpdateMyTime myTime;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    private String orderNo;

    public TimeBottomDialog(Context context) {
        //重写dialog默认的主题
        this(context, R.style.bottom_dialog);
        this.mContext = context;
    }

    public TimeBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        View convertView = getLayoutInflater().inflate(R.layout.time_bottom_dialog, null);
        close = (ImageView) convertView.findViewById(R.id.iv_close);
        dateList = convertView.findViewById(R.id.date_list);
        timeList = convertView.findViewById(R.id.time_list);
        close.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(convertView);
        setCanceledOnTouchOutside(false);

        LinearLayoutManager ms = new LinearLayoutManager(mContext);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        dateList.setLayoutManager(ms);
        dateList.addItemDecoration(new SpacesItemDecoration(30));
        mDataAdapter = new DataAdapter(mContext);
        mDataAdapter.setItemClickListener(this);
        dateList.setAdapter(mDataAdapter);

        GridLayoutManager gm = new GridLayoutManager(mContext, 4);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mTimeAdapter.getItemViewType(position);
            }
        });
        timeList.setLayoutManager(gm);
        mTimeAdapter = new TimeAdapter(mContext);
        mTimeAdapter.setTimeClickListener(this);
        timeList.setAdapter(mTimeAdapter);
    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = dm.widthPixels;
        getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        mDataAdapter.setSelectedPos(position);
        if (mTimeAdapter != null) {
            mTimeAdapter.setSelectDateMillis(mDataAdapter.getSelectedMillis());
        }
    }

    @Override
    public void onTimeClick(int position) {
        mTimeAdapter.setSelectPosition(position);
        String selectedDate = mDataAdapter.getSelectedDate();
        String selectTime = mTimeAdapter.getSelectTime();
        
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(c.get(Calendar.YEAR));
        StringBuilder builder = new StringBuilder();
        builder.append(year)
                .append("-")
                .append(selectedDate)
                .append(" ")
                .append(selectTime);
        if (myTime != null) {
            myTime.updateMyTime(builder);
        }
        dismiss();
    }

    public interface OnUpdateMyTime {
        void updateMyTime(StringBuilder builder);
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View child,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.right = space;
        }
    }

}
