package com.example.ahsapptest3;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.util.ArrayList;

public class NotifRecyclerAdapter extends RecyclerView.Adapter<NotifRecyclerAdapter.ViewHolder>{
    private static final String TAG = "NotifRecyclerAdapter";

    private Context context;
    private OnItemClick onItemClick;
    private static SortedList<Notif_Data> dataSortedList;
    public NotifRecyclerAdapter(Context context, ArrayList<Notif_Data> dataList, OnItemClick onItemClick){
        this.context = context;
        this.onItemClick = onItemClick;
        dataSortedList = new SortedList<>(Notif_Data.class, new SortedList.Callback<Notif_Data>() {
            @Override
            public int compare(Notif_Data o1, Notif_Data o2) {
                if(o1.getArticle() != null && o2.getArticle() != null)
                {if(o1.getArticle().isNotified() && !o2.getArticle().isNotified())
                    return 1;
                if(!o1.getArticle().isNotified() && o2.getArticle().isNotified())
                    return -1;}

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
                if(oldItem.getArticle() != null && newItem.getArticle() != null)
                {if (oldItem.getArticle().isNotified() != newItem.getArticle().isNotified())
                    return false;
                return oldItem.getArticle().getID().equals(newItem.getArticle().getID());}
                return oldItem.getTime() == newItem.getTime() && oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areItemsTheSame(Notif_Data item1, Notif_Data item2) {
                if(item1.getArticle() != null && item2.getArticle() != null)
                    return item1.getArticle().getID().equals(item2.getArticle().getID());
                return item1.getTime() == item2.getTime() && item1.getTitle().equals(item2.getTitle());
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
        Notif_Data data = dataSortedList.get(position);
        holder.titleText.setText(data.getTitle());
        holder.bodyText.setText(data.getBody());
        Helper.setTimeText_toView(holder.dateText, data.getTime());
        holder.typeText.setText(data.getType());
        if(data.getArticle() == null)
            holder.arrow.setVisibility(View.GONE);
        else if(data.getArticle().isNotified()){
            Log.d(TAG, data.getArticle().toString());
            holder.newText.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.outerLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.NEW_LightGray_EAEAEA__HOME));
            }
        }
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
            itemView.setOnClickListener(this);
            bodyText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(dataSortedList.get(getAdapterPosition()).getArticle(), getAdapterPosition());
        }
    }

    public interface OnItemClick
    {
        void onClick(@Nullable Article article, int position);
    }
}
