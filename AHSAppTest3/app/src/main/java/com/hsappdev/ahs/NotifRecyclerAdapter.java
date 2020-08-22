package com.hsappdev.ahs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.hsappdev.ahs.Misc.Helper;
import com.hsappdev.ahs.R;

import java.util.ArrayList;

public class NotifRecyclerAdapter extends RecyclerView.Adapter<NotifRecyclerAdapter.ViewHolder>{
    /*private static final String TAG = "NotifRecyclerAdapter";*/

    private final OnItemClick onItemClick;
    private final SortedList<Notif_Data> sortedList;
    public NotifRecyclerAdapter(ArrayList<Notif_Data> dataList, OnItemClick onItemClick){
        this.onItemClick = onItemClick;
        sortedList = new SortedList<>(Notif_Data.class, new SortedList.Callback<Notif_Data>() {
            @Override
            public int compare(Notif_Data o1, Notif_Data o2) {
                /*if(o1.isNotified() && !o2.isNotified())
                    return 1;
                if(!o1.isNotified() && o2.isNotified())
                    return -1;*/

                long time_diff = o1.getTime() - o2.getTime();
                if(time_diff < 0) // o2 time greater than o1, so o2 after o1,
                    return 1;
                if(time_diff > 0)
                    return -1;
                return o1.getTitle().compareTo(o2.getTitle());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Notif_Data oldItem, Notif_Data newItem) {
                if (oldItem.isNotified() != newItem.isNotified())
                    return false;
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Notif_Data item1, Notif_Data item2) {
                return item1.equals(item2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition,toPosition);
            }
        });
        for(Notif_Data item: dataList){
            sortedList.add(item);
        }
    }

    public void addItem(Notif_Data item){
        sortedList.add(item);
    }

    public void clearAll() {
        sortedList.clear();}

    public void updateReadItemPosition(int position) {
        if(position < sortedList.size()) {
            sortedList.get(position).setNotified(true);
            notifyItemChanged(position);
            sortedList.recalculatePositionOfItemAt(position);
        }
    }

    public static final int READ = 0;
    @Override
    public int getItemViewType(int position) {
        if(sortedList.get(position).isNotified())
            return READ;
        return 1;
    }

    @NonNull
    @Override
    public NotifRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate((viewType != READ) ? R.layout.notif_template: R.layout.notif_template_inactive, parent, false);

        return new NotifRecyclerAdapter.ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView titleText;
        final TextView bodyText;
        final TextView dateText;
        final TextView typeText;
        final ImageView arrow;
        final OnItemClick onItemClick;
        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            titleText = itemView.findViewById(R.id.notif_template_TitleText);
            bodyText = itemView.findViewById(R.id.notif_template_BodyText);
            dateText = itemView.findViewById(R.id.notif_template_DateText);
            typeText = itemView.findViewById(R.id.notif_template_typeText);
            arrow = itemView.findViewById(R.id.notif_template_forward);

        }

        private Notif_Data notif_data;
        public void setDetails(Notif_Data data) {
            this.notif_data = data;
            titleText.setText(data.getTitle());
            bodyText.setText(data.getBody());
            Helper.setTimeText_toView(dateText, Helper.TimeFromNow(data.getTime()));
            typeText.setText(data.getType());

            if(data.getHolder() == null)
                arrow.setVisibility(View.GONE);
            itemView.setOnClickListener(this);
            bodyText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(notif_data, getAdapterPosition());
        }
    }

    public interface OnItemClick
    {
        void onClick(@NonNull Notif_Data data, int position);
    }
}
