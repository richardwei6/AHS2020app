package com.example.ahsapptest3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifBtn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifBtn extends Fragment {

    private Navigation navigation;
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
            throw new ClassCastException("Context has not implemented Navigation!");
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifBtn.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifBtn newInstance(String param1, String param2) {
        NotifBtn fragment = new NotifBtn();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notif_btn, container, false);
        ImageView notifBtn = view.findViewById(R.id.notif_btn);
        
        notifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.goToNotif();
            }
        });
        return view;
    }
}