package com.example.ahsapptest3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ahsapptest3.Helper_Code.Helper;

import java.util.ArrayList;

public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.ViewHolder>{
    private static final String TAG = "BulletinRecyclerAdapter";

    private static SortedList<Bulletin_Data> list;
    public ArrayList<Bulletin_Data> bulletin_data;
    private static Context context;
    boolean[] selectors_active = new boolean[6];
    private final Bulletin.Type[] types = Bulletin.Type.values();

    private OnItemClick onItemClick;

    public BulletinRecyclerAdapter(Context context, ArrayList<Bulletin_Data> data, OnItemClick onItemClick) {
        this.bulletin_data = new ArrayList<>(data);
        BulletinRecyclerAdapter.context = context;
        this.onItemClick = onItemClick;

        list = new SortedList<>(Bulletin_Data.class, new SortedList.Callback<Bulletin_Data>() {
            @Override
            public int compare(Bulletin_Data o1, Bulletin_Data o2) {
                long time_diff = o1.getTime()- o2.getTime();
                if(time_diff < 0)
                    return -1;
                if(time_diff > 0)
                    return 1;
                return 0;

            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Bulletin_Data oldItem, Bulletin_Data newItem) {
                return oldItem.getTitle().equals(newItem.getTitle());
            }

            @Override
            public boolean areItemsTheSame(Bulletin_Data item1, Bulletin_Data item2) {
                return item1.getTitle().equals(item2.getTitle()); // sus but okay
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
        for(Bulletin_Data info: data)
        {
            list.add(info);
        }
    }

    public void addItem(Bulletin_Data item)
    {
        bulletin_data.add(item);
        list.add(item);
        /*Log.d(TAG, item.toString());*/
    }

    public void clearAll()
    {
        list.clear();
    }

    @NonNull
    @Override
    public BulletinRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bulletin_item_template, parent, false)
                ;
        return new BulletinRecyclerAdapter.ViewHolder(view, onItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinRecyclerAdapter.ViewHolder holder, int position) {
        Bulletin_Data info = list.get(position);
        holder.titleText.setText(info.getTitle());
        /*holder.bodyText.setMovementMethod(LinkMovementMethod.getInstance());*/

        Helper.setHtmlParsedText_toView(holder.bodyText, info.getBodyText());
        /*Helper.setHtmlParsed_withRipple(holder.bodyText, holder.parentView, info.getBodyText());*/

        Helper.setTimeText_toView(holder.dateText, Helper.TimeFromNow(info.getTime()));
        holder.readText.setVisibility((info.isAlready_read() ? View.GONE : View.VISIBLE));

        String typeText;
        switch(info.getType())
        {
            case SENIORS:
                typeText = "Seniors";
                break;
            case EVENTS:
                typeText = "Events";
                break;
            case COLLEGES:
                typeText = "Colleges";
                break;
            case REFERENCE:
                typeText = "Reference";
                break;
            case ATHLETICS:
                typeText = "Athletics";
                break;
            default:
                typeText = "Others";
                break;
        }
        holder.typeText.setText(typeText);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterItems()
    {
        list.beginBatchedUpdates();
        ArrayList<Bulletin_Data> copy = new ArrayList<>(bulletin_data);
        boolean hasTrue = false;
        for(boolean selector: selectors_active)
            hasTrue = hasTrue || selector;
        if(hasTrue)
            for(int i = copy.size() -1; i >= 0; i--)
            {
                Bulletin.Type copyType = copy.get(i).getType();
                for(int i1 = 0; i1 < selectors_active.length; i1++)
                {
                    if(!selectors_active[i1] && copyType == types[i1])
                        copy.remove(i);
                }
            }

        //Log.d(TAG, copy.toString());

        for(int i = list.size() - 1; i >= 0; i--)
        {
            boolean alreadyHas = false;
            for(Bulletin_Data info: copy)
                if(list.get(i).getTitle().equals(info.getTitle()))
                    alreadyHas = true;
            if(!alreadyHas)
                list.removeItemAt(i);

        }
        for(Bulletin_Data info: copy)
        {
            boolean alreadyHas = false;
            for(int i = list.size() - 1; i >= 0; i--)
            {
                if(list.get(i).getTitle().equals(info.getTitle()))
                    alreadyHas = true;
            }
            if(!alreadyHas)
                list.add(info);
        }
        /*for(int i = 0; i < list.size(); i++)
        {
            Log.d(TAG, list.get(i).toString());
        }*/

        list.endBatchedUpdates();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView titleText, bodyText, dateText, typeText, readText;
        OnItemClick onItemClick;
        /*View parentView;*/

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            titleText = itemView.findViewById(R.id.bulletin_template_TitleText);
            bodyText = itemView.findViewById(R.id.bulletin_template_BodyText);
            dateText = itemView.findViewById(R.id.bulletin_template_DateText);
            typeText = itemView.findViewById(R.id.bulletin_template_typeText);
            readText = itemView.findViewById(R.id.bulletin_template_newText);
            /*parentView = itemView.findViewById(R.id.bulletin_template_ConstraintLayout);*/

            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);
            bodyText.setOnClickListener(this); // so link movement method doesn't (fully) consume click events
            // however no ripple triggered
            //https://stackoverflow.com/questions/16792963/android-clickablespan-intercepts-the-click-event/34200772
            /*TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
            itemView.setBackgroundResource(outValue.resourceId);*/
        }

        @Override
        public void onClick(View v) {
            onItemClick.onClick(list.get(getAdapterPosition()));
        }
    }

    public interface OnItemClick
    {
        void onClick(Bulletin_Data data);
    }
}
