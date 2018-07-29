package com.example.timedialog.timeadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.timedialog.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final long LENGTH_OF_HALF_HOUR_IN_MILLISECONDS = 1800000L;
    private Context context;
    private List<String> times = new ArrayList<>();
    private List<Long> timesMillis = new ArrayList<>();
    private long dateMillis;
    private int selectPosition = 0;

    private OnTimeClickListener mItemClickListener;

    public interface OnTimeClickListener {
        void onTimeClick(int position);
    }

    public void setTimeClickListener(OnTimeClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public TimeAdapter(Context context) {
        this.context = context;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long time = dateToMilliseconds("08:30", sdf);
        for (int i = 0; i < 19; i++) {
            timesMillis.add(time);
            String timeStr = sdf.format(time);
            time += LENGTH_OF_HALF_HOUR_IN_MILLISECONDS;
            times.add(timeStr);
        }
    }

    public void setSelectDateMillis(long dateMillis) {
        this.dateMillis = dateMillis;
        notifyDataSetChanged();
    }

    public void setSelectPosition(int position) {
        selectPosition = position;
        notifyDataSetChanged();
    }

    public String getSelectTime() {
        return times.get(selectPosition);
    }

    /**
     * 获取指定时间的毫秒
     *
     * @param timeNow
     * @param sdf
     * @return
     */
    public long dateToMilliseconds(String timeNow, SimpleDateFormat sdf) {
        long time = 0L;
        try {
            Date date = sdf.parse(timeNow);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 4:
                return new TitleViewHolder(LayoutInflater.from(context).inflate(R.layout.time_list_item_title, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.time_list_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder mHolder = (ViewHolder) holder;
            if (position > 0) position--;
            if (position > 8) position--;
            mHolder.mTvTime.setText(times.get(position));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            String format = simpleDateFormat.format(new Date());
            long time = dateToMilliseconds(format, simpleDateFormat);
            if (dateMillis <= System.currentTimeMillis()) {
                if (timesMillis.get(position) < time) {
                    mHolder.mTvTime.setEnabled(false);
                } else {
                    mHolder.mTvTime.setEnabled(true);
                }
            } else {
                mHolder.mTvTime.setEnabled(true);
            }
            final int finalPosition = position;
            mHolder.mTvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onTimeClick(finalPosition);
                        selectPosition = finalPosition;
                    }
                }
            });
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder mHolder = (TitleViewHolder) holder;
            String time = null;
            if (position == 0) {
                time = "上午";
            } else if (position == 9) {
                time = "下午";
            }
            mHolder.mTvTimeTitle.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return 21;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTime;

        ViewHolder(View view) {
            super(view);
            this.mTvTime = view.findViewById(R.id.tv_time);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTimeTitle;

        TitleViewHolder(View view) {
            super(view);
            this.mTvTimeTitle = view.findViewById(R.id.tv_time_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
            case 9:
                return 4;
            default:
                return 1;
        }
    }
}
