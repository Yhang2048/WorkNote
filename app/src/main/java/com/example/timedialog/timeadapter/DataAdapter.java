package com.example.timedialog.timeadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.timedialog.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    public static final long LENGTH_OF_DAY_IN_MILLISECONDS = 86400000L;
    private OnItemClickListener mItemClickListener;
    private int mSelectedPos = 0;
    private List<Long> selectMillis = new ArrayList<>();
    private List<String> datas = new ArrayList<>();
    private List<String> ways = new ArrayList<>();

    public DataAdapter(Context context) {
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public Long getSelectedMillis() {
        return selectMillis.get(mSelectedPos);
    }

    public String getSelectedDate() {
        return datas.get(mSelectedPos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.date_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        long time = c.getTime().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");

        String[] dataNear = new String[]{"今天", "明天", "后天"};
        for (int i = 0; i < getItemCount(); i++) {
            Date date = new Date(time);
            String timeStr = simpleDateFormat.format(date);
            c.setTime(date);
            String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
            switch (mWay) {
                case "1":
                    mWay = "周日";
                    break;
                case "2":
                    mWay = "周一";
                    break;
                case "3":
                    mWay = "周二";
                    break;
                case "4":
                    mWay = "周三";
                    break;
                case "5":
                    mWay = "周四";
                    break;
                case "6":
                    mWay = "周五";
                    break;
                case "7":
                    mWay = "周六";
                    break;
            }
            ways.add(mWay);
            datas.add(timeStr);
            selectMillis.add(time);
            time += LENGTH_OF_DAY_IN_MILLISECONDS;
        }
        if (position == mSelectedPos) {
            mHolder.btnDateWeek.setBackgroundResource(R.drawable.time_btn_selector);
        } else {
            mHolder.btnDateWeek.setBackgroundResource(R.drawable.time_btn_unselector);
        }
        ways.set(0, dataNear[0]);
        ways.set(1, dataNear[1]);
        ways.set(2, dataNear[2]);
        mHolder.btnDateWeek.setText(datas.get(position) + "\n" + ways.get(position));
        mHolder.btnDateWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 14;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView btnDateWeek;

        ViewHolder(View view) {
            super(view);
            this.btnDateWeek =  view.findViewById(R.id.btn_date_week);
        }
    }

}
