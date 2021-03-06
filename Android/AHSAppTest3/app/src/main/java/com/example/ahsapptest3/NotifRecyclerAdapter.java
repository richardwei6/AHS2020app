package com.example.ahsapptest3;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.util.ArrayList;

public class NotifRecyclerAdapter extends RecyclerView.Adapter<NotifRecyclerAdapter.ViewHolder>{
    private static final String TAG = "NotifRecyclerAdapter";

    private OnItemClick onItemClick;
    private SortedList<Notif_Data> dataSortedList;
    public NotifRecyclerAdapter(ArrayList<Notif_Data> dataList, OnItemClick onItemClick){
        this.onItemClick = onItemClick;
        dataSortedList = new SortedList<>(Notif_Data.class, new SortedList.Callback<Notif_Data>() {
            @Override
            public int compare(Notif_Data o1, Notif_Data o2) {
                if(o1.isNotified() && !o2.isNotified())
                    return 1;
                if(!o1.isNotified() && o2.isNotified())
                    return -1;

                long time_diff = o1.getTime() - o2.getTime();
                if(time_diff < 0) // o2 time greater than o1, so o2 after o1,
                    return 1;
                if(time_diff > 0)
                    return -1;
                return 0;
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
            dataSortedList.add(item);
        }
    }

    public void addItem(Notif_Data item){
        dataSortedList.add(item);
    }

    public void clearAll() {dataSortedList.clear();}

    public void updateReadItemPosition(int position)
    {
        dataSortedList.get(position).setNotified(true);
        notifyItemChanged(position);
        dataSortedList.recalculatePositionOfItemAt(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public NotifRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_template, parent, false);

        return new NotifRecyclerAdapter.ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(dataSortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSortedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleText, bodyText, dateText, typeText, newText;
        ImageView arrow;
        OnItemClick onItemClick;
        CardView outerLayout;
        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            this.onItemClick = onItemClick;
            outerLayout = itemView.findViewById(R.id.notif_template_CardView);
            titleText = itemView.findViewById(R.id.notif_template_TitleText);
            bodyText = itemView.findViewById(R.id.notif_template_BodyText);
            dateText = itemView.findViewById(R.id.notif_template_DateText);
            typeText = itemView.findViewById(R.id.notif_template_typeText);
            newText = itemView.findViewById(R.id.notif_template_newText);
            arrow = itemView.findViewById(R.id.notif_template_forward);

        }

        private Notif_Data notif_data;
        public void setDetails(Notif_Data data) {
            this.notif_data = data;
            titleText.setText(data.getTitle());
            bodyText.setText(data.getBody());
            Helper.setTimeText_toView(dateText, Helper.TimeFromNow(data.getTime()));
            typeText.setText(data.getType());
            if(data.isNotified()){
                newText.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outerLayout.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.LightGray_EAEAEA));
                }
            }
            if(data.getArticle() == null)
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
