package com.border.launcherfree.iconcolor;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.border.launcherfree.IconCache;
import com.border.launcherfree.InvariantDeviceProfile;
import com.border.launcherfree.R;

import java.util.List;

public class IconColorRecyclerViewAdapter extends RecyclerView.Adapter<IconColorRecyclerViewAdapter.ViewHolder> {

    private List<ActivityInfo> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private IconCache mIconCache;

    // data is passed into the constructor
    IconColorRecyclerViewAdapter(Context context, List<ActivityInfo> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;

        InvariantDeviceProfile mInvariantDeviceProfile = new InvariantDeviceProfile(mContext);
        mIconCache = new IconCache(mContext, mInvariantDeviceProfile);

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.icon_color_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityInfo info = mData.get(position);
        String appName = info.loadLabel(mContext.getPackageManager()).toString();
        holder.myTextView.setText(appName);
        Drawable logo = mIconCache.getFullResIcon(info);
        holder.logoView.setImageDrawable(logo);

        GradientDrawable background = (GradientDrawable) holder.itemView.getBackground();
        SharedPreferences preferences = mContext.getSharedPreferences
                (mContext.getString(R.string.preference_key), Context.MODE_PRIVATE);
        int intColor = preferences.getInt(appName, -1);
        background.setStroke(2, intColor);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView logoView;
        View itemView;

        ViewHolder(View view) {
            super(view);
            myTextView =  view.findViewById(R.id.app_name_color);
            logoView = view.findViewById(R.id.color_change_app_logo);
            itemView = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ActivityInfo getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}