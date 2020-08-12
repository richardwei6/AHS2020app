package com.example.ahsapptest3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ahsapptest3.Misc.Helper;

import java.util.ArrayList;

public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.ViewHolder>{
    /*private static final String TAG = "BulletinRecyclerAdapter";*/

    private final SortedList<Bulletin_Article> sortedList;
    public final ArrayList<Bulletin_Article> bulletin_data;

    final boolean[] selectors_active = new boolean[Bulletin_Article.Type.values().length];
    private final Bulletin_Article.Type[] types = Bulletin_Article.Type.values();
    private final OnItemClick onItemClick;

    public BulletinRecyclerAdapter(ArrayList<Bulletin_Article> data, OnItemClick onItemClick) {
        this.bulletin_data = new ArrayList<>(data);
        this.onItemClick = onItemClick;

        sortedList = new SortedList<>(Bulletin_Article.class, new SortedList.Callback<Bulletin_Article>() {
            @Override
            public int compare(Bulletin_Article o1, Bulletin_Article o2) {
                // give priority to new vs not new articles
                /*if(o1.isAlready_read() && !o2.isAlready_read())
                    return 1;
                if(!o1.isAlready_read() && o2.isAlready_read())
                    return -1;*/

                // give priority to future vs past events
                if(Helper.TimeFromNow(o1.getTime()) > 0 && Helper.TimeFromNow(o2.getTime()) < 0)
                    return 1;
                if(Helper.TimeFromNow(o1.getTime()) < 0 && Helper.TimeFromNow(o2.getTime()) > 0)
                    return -1;

                long time_diff = o1.getTime()- o2.getTime();

                if(time_diff > 0) // o1 > o2, so o1 after o2; thus appears after
                    return 1;
                if(time_diff < 0)
                    return -1;
                return o1.getTitle().compareTo(o2.getTitle());

            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Bulletin_Article oldItem, Bulletin_Article newItem) {
                if(oldItem.isAlready_read() != newItem.isAlready_read())
                    return false;
                return oldItem.getID().equals(newItem.getID());
            }

            @Override
            public boolean areItemsTheSame(Bulletin_Article item1, Bulletin_Article item2) {
                return item1.getID().equals(item2.getID());
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

            @Override
            public void onChanged(int position, int count, Object payload) {
                notifyItemRangeChanged(position, count, payload);
            }
        });
        for(Bulletin_Article info: data)
        {
            sortedList.add(info);
        }
    }

    public void addItem(Bulletin_Article item)
    {
        bulletin_data.add(item);
        sortedList.add(item);
        /*Log.d(TAG, item.toString());*/
    }

    public void clearAll()
    {
        bulletin_data.clear();
        sortedList.clear();
    }

    public static final int READ = 0;
    @Override
    public int getItemViewType(int position) {
        if(sortedList.get(position).isAlready_read())
            return READ;
        return 1;
    }

    @NonNull
    @Override
    public BulletinRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate((viewType != READ) ? R.layout.bulletin_item_template: R.layout.bulletin_item_template_inactive, parent, false)
                ;
        return new BulletinRecyclerAdapter.ViewHolder(view, onItemClick);
    }
    /*private int lastPosition = -1;*/
    @Override
    public void onBindViewHolder(@NonNull BulletinRecyclerAdapter.ViewHolder holder, int position) {
        holder.setDetails(sortedList.get(position));
        /*if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }*/
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void filterItems()
    {
        sortedList.beginBatchedUpdates();

        // make a copy of the original data and filter it
        ArrayList<Bulletin_Article> copy = new ArrayList<>(bulletin_data);
        boolean hasTrue = false;
        for(boolean selector: selectors_active)
            hasTrue = hasTrue || selector;
        if(hasTrue)
            for(int i = copy.size() -1; i >= 0; i--)
            {
                Bulletin_Article.Type copyType = copy.get(i).getType();
                for(int i1 = 0; i1 < selectors_active.length; i1++)
                {
                    if(!selectors_active[i1] && copyType == types[i1])
                        copy.remove(i);
                }
            }

        //Log.d(TAG, copy.toString());

        for(int i = sortedList.size() - 1; i >= 0; i--)
        {
            boolean alreadyHas = false;
            for(Bulletin_Article info: copy)
                if(sortedList.get(i).getTitle().equals(info.getTitle()))
                    alreadyHas = true;
            if(!alreadyHas)
                sortedList.removeItemAt(i);

        }
        for(Bulletin_Article info: copy)
        {
            boolean alreadyHas = false;
            for(int i = sortedList.size() - 1; i >= 0; i--)
            {
                if(sortedList.get(i).getTitle().equals(info.getTitle()))
                    alreadyHas = true;
            }
            if(!alreadyHas)
                sortedList.add(info);
        }
        /*for(int i = 0; i < list.size(); i++)
        {
            Log.d(TAG, list.get(i).toString());
        }*/

        sortedList.endBatchedUpdates();
    }

    public boolean updateReadItemPosition(int position)
    {
        if(position < sortedList.size()) {
            sortedList.get(position).setAlready_read(true);
            notifyItemChanged(position);
            sortedList.recalculatePositionOfItemAt(position);
            return true;
        }
        return false;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final TextView titleText;
        final TextView bodyText;
        final TextView dateText;
        final TextView typeText/*, readText*/;
        final OnItemClick onItemClick;
        private Bulletin_Article bulletin_article;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
            titleText = itemView.findViewById(R.id.bulletin_template_TitleText);
            bodyText = itemView.findViewById(R.id.bulletin_template_BodyText);
            dateText = itemView.findViewById(R.id.bulletin_template_DateText);
            typeText = itemView.findViewById(R.id.bulletin_template_typeText);
            /*readText = itemView.findViewById(R.id.bulletin_template_newText);*/

            this.onItemClick = onItemClick;

        }
        
        public void setDetails(Bulletin_Article bulletin_article){
            this.bulletin_article = bulletin_article;
            titleText.setText(bulletin_article.getTitle());
            /*bodyText.setMovementMethod(LinkMovementMethod.getInstance());*/

            Helper.setHtmlParsedText_toView(bodyText, bulletin_article.getBodyText());
            /*Helper.setHtmlParsed_withRipple(bodyText, parentView, bulletin_article.getBodyText());*/

            Helper.setTimeText_toView(dateText, Helper.TimeFromNow(bulletin_article.getTime()));
            /*readText.setVisibility((bulletin_article.isAlready_read() ? View.GONE : View.VISIBLE));*/
            typeText.setText(bulletin_article.getType().getName());
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
            onItemClick.onClick(bulletin_article, getAdapterPosition());
        }
    }

    public interface OnItemClick
    {
        void onClick(Bulletin_Article data, int position);
    }
}
