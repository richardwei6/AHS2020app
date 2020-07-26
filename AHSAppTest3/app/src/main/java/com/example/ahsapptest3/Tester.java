package com.example.ahsapptest3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.ahsapptest3.Helper_Code.Bulletin_SelectorView;
import com.example.ahsapptest3.Helper_Code.Helper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Tester extends AppCompatActivity {

    private boolean[] selectors_active = new boolean[6];
    private static final String TAG = "Tester";

    final Bulletin.Type[] types = new Bulletin.Type[]
            {
                    Bulletin.Type.SENIORS,
                    Bulletin.Type.EVENTS,
                    Bulletin.Type.COLLEGES,
                    Bulletin.Type.REFERENCE,
                    Bulletin.Type.ATHLETICS,
                    Bulletin.Type.OTHERS,
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);

        for(int i = 0; i < selectors_active.length; i++)
        {
            selectors_active[i] = false;
        }

        final String[] categories = new String[]
                {
                        "seniors",
                        "events",
                        "colleges",
                        "reference",
                        "athletics",
                        "others",
                };


        Bulletin_SelectorView
                seniors_selector = findViewById(R.id.bulletin_seniors_selection),
                events_selector = findViewById(R.id.bulletin_events_selection),
                colleges_selector = findViewById(R.id.bulletin_colleges_selection),
                reference_selector = findViewById(R.id.bulletin_reference_selection),
                athletics_selector = findViewById(R.id.bulletin_athletics_selection),
                others_selector = findViewById(R.id.bulletin_others_selection);

        final Bulletin_SelectorView[] selectors =
                {
                        seniors_selector ,
                        events_selector ,
                        colleges_selector ,
                        reference_selector ,
                        athletics_selector ,
                        others_selector
                };
        final ArrayList<Bulletin_Data> data = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.bulletin_RecyclerView);
        final BulletinRecyclerAdapter adapter = new BulletinRecyclerAdapter(this, data);

        for(int i = 0; i < selectors.length; i++)
        {
            final int finalI = i;
            selectors[i].action = new Bulletin_SelectorView.OnClick() {
                @Override
                public void onClick() {
                    selectors_active[finalI] = !selectors_active[finalI];

                    adapter.filterItems();
                }
            };
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final int margin = (int) getResources().getDimension(R.dimen.EveryPage_Side_Padding);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
            }

            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = margin;
                outRect.right = margin;

                // account for special margins for first and last children
                int position = parent.getChildLayoutPosition(view);

                outRect.top = (position == 0) ? margin/2 : margin/8;
                outRect.bottom = (position == state.getItemCount()-1) ? margin/2 : margin/8;

            }
        });
        recyclerView.requestLayout();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("bulletin");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear(); // so it doesn't just keep adding should the data change
                adapter.clearAll();
                int pos = 0;
                for(int i = 0; i < categories.length; i++)
                {
                    Log.d(TAG, categories[i]);
                    DataSnapshot snapshot1 = snapshot.child(categories[i]);


                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                        String title = dataSnapshot.child("articleTitle").getValue().toString();
                        String body = dataSnapshot.child("articleBody").getValue().toString();
                        long time = (long) dataSnapshot.child("articleUnixEpoch").getValue();
                        Bulletin_Data info = new Bulletin_Data("huck", time, title, body, types[i], false);


                        data.add(info);
                        /*Log.d(TAG, data.toString());*/
                        adapter.addItem(info);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getDetails());
            }
        });
    }

    public class BulletinRecyclerAdapter extends RecyclerView.Adapter<BulletinRecyclerAdapter.ViewHolder> {
        private static final String TAG = "BulletinRecyclerAdapter";
        private SortedList<Bulletin_Data> list;

        public ArrayList<Bulletin_Data> bulletin_data;

        private Context context;

        public BulletinRecyclerAdapter(Context context, ArrayList<Bulletin_Data> data) {
            this.bulletin_data = new ArrayList<>(data);
            this.context = context;

            list = new SortedList<>(Bulletin_Data.class, new SortedList.Callback<Bulletin_Data>() {
                @Override
                public int compare(Bulletin_Data o1, Bulletin_Data o2) {
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
                    notifyItemMoved(fromPosition, toPosition);
                }
            });
            for (Bulletin_Data info : data) {
                list.add(info);
            }
        }

        public void addItem(Bulletin_Data item) {
            bulletin_data.add(item);
            list.add(item);
            /*Log.d(TAG, item.toString());*/
        }

        public void clearAll() {
            list.clear();
        }

        @NonNull
        @Override
        public BulletinRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.bulletin_item_template, parent, false);
            return new BulletinRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BulletinRecyclerAdapter.ViewHolder holder, int position) {
            Bulletin_Data info = list.get(position);
            holder.titleText.setText(info.getTitle());
            holder.bodyText.setMovementMethod(LinkMovementMethod.getInstance());
            holder.bodyText.setText(Html.fromHtml(info.getBodyText()));
            Helper.setTimeText_toView(holder.dateText, Helper.TimeFromNow(info.getTime()));

            String typeText;
            switch (info.getType()) {
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

        public void filterItems() {
            list.beginBatchedUpdates();
            ArrayList<Bulletin_Data> copy = new ArrayList<>(bulletin_data);
            boolean hasTrue = false;
            for (boolean selector : selectors_active)
                hasTrue = hasTrue || selector;
            if (hasTrue)
                for (int i = copy.size() - 1; i >= 0; i--) {
                    Bulletin.Type copyType = copy.get(i).getType();
                    for (int i1 = 0; i1 < selectors_active.length; i1++) {
                        if (!selectors_active[i1] && copyType == types[i1])
                            copy.remove(i);
                    }

                    /*if(!seniors_active && copy.get(i).getType() == Type.SENIORS)
                        copy.remove(i);
                    else if(!colleges_active && copy.get(i).getType() == Type.COLLEGES)
                        copy.remove(i);
                    else if(!athletics_active && copy.get(i).getType() == Type.ATHLETICS)
                        copy.remove(i);
                    else if(!others_active && copy.get(i).getType() == Type.OTHERS)
                        copy.remove(i);
                    else if(!reference_active && copy.get(i).getType() == Type.REFERENCE)
                        copy.remove(i);
                    else if(!events_active && copy.get(i).getType() == Type.EVENTS)
                        copy.remove(i);*/
                }

            //Log.d(TAG, copy.toString());

            for (int i = list.size() - 1; i >= 0; i--) {
                boolean alreadyHas = false;
                for (Bulletin_Data info : copy)
                    if (list.get(i).getTitle().equals(info.getTitle()))
                        alreadyHas = true;
                if (!alreadyHas)
                    list.removeItemAt(i);

            }
            for (Bulletin_Data info : copy) {
                boolean alreadyHas = false;
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i).getTitle().equals(info.getTitle()))
                        alreadyHas = true;
                }
                if (!alreadyHas)
                    list.add(info);
            }
            for (int i = 0; i < list.size(); i++) {
                Log.d(TAG, list.get(i).toString());
            }

            list.endBatchedUpdates();

/*
            for(int i = 0; i < copy.size() - 1; i++)
            {
                Log.d(TAG,copy.toString());
                Log.d(TAG, active_infos.toString());

                if(i >= active_infos.size())
                    active_infos.add(copy.get(i));
                else
                {
                    while(!active_infos.get(i).getTitle().equals(copy.get(i).getTitle()))
                    {

                        active_infos.remove(i);
                        if(i >= active_infos.size()) {
                            active_infos.add(copy.get(i));
                            break;
                        }
                    }
                }
            }
            if(active_infos.size() > copy.size())
                for(int i = active_infos.size() - 1; i > copy.size() -1; i--)
                {
                    active_infos.remove(i);
                }

            notifyDataSetChanged();*/
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleText, bodyText, dateText, typeText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.bulletin_template_TitleText);
                bodyText = itemView.findViewById(R.id.bulletin_template_BodyText);
                dateText = itemView.findViewById(R.id.bulletin_template_DateText);
                typeText = itemView.findViewById(R.id.bulletin_template_typeText);
            }
        }
    }
}
