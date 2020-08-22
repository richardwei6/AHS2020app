package com.hsappdev.ahs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.hsappdev.ahs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NotifBtn extends Fragment implements View.OnClickListener{
    /*private static final String TAG = "NotifBtn";*/
    private Navigation navigation;

    @Override
    public void onClick(View v) {
        navigation.goToNotif();
    }

    public interface Navigation
    {
        void goToNotif();
    }

    public NotifBtn() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            navigation = (Navigation) context;
        } catch(ClassCastException e)
        {
            throw new ClassCastException("Context has not implemented Navigation interface!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notif_btn, container, false);

        final ImageView notifDot = view.findViewById(R.id.notif_dot);
        final NotifDatabase notifDatabase = NotifDatabase.getInstance(getContext());
        final Resources r = getResources();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                notifDot.setVisibility(View.INVISIBLE);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(r.getString(R.string.fb_notif_key));
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot child_sn: snapshot.getChildren())
                        {
                            String notifID = child_sn.getKey();
                            if(!notifDatabase.alreadyAdded(notifID)){ // not notified by default
                                notifDot.setVisibility(View.VISIBLE);
                                // leave adding the data and stuff for notif activity
                            } else if (!notifDatabase.getReadStatusByID(notifID)){
                                 notifDot.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        /*Log.e(TAG, error.getDetails());*/
                    }
                });
            }
        });

        /*TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground,outValue,true);
        view.setBackgroundResource(outValue.resourceId);*/

        view.setOnClickListener(this);
        return view;
    }
}