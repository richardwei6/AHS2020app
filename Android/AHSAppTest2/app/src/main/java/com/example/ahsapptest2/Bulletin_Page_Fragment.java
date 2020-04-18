package com.example.ahsapptest2;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Bulletin_Page_Fragment extends Fragment {

    enum BulletinType
    {
        SENIORS, EVENTS, COLLEGE, REFERENCE, ATHLETICS, OTHER;
    }

    public Bulletin_Page_Fragment() {
        // Required empty public constructor
    }

    FrameLayout[] frameLayouts;
    LinearLayout seniors_layout, events_layout, college_layout, reference_layout, athletics_layout, other_layout;
    Bulletin_Template[] items;

    boolean[] is_active = new boolean[6]; //automatically all false

    public boolean is_Type_Layout_Active(BulletinType type) //"Translates" enum to is_active array location
    {
        switch(type)
        {
            case SENIORS:
                return is_active[0];
            case EVENTS:
                return is_active[1];
            case COLLEGE:
                return is_active[2];
            case REFERENCE:
                return is_active[3];
            case ATHLETICS:
                return is_active[4];
            case OTHER:
                return is_active[5];
            default:
                return false; //this really shouldn't happen
        }
    }

    public void switch_Type_Layout_Active(BulletinType type)
    {
        switch(type)
        {
            case SENIORS:
                is_active[0] = !is_active[0];
                break;
            case EVENTS:
                is_active[1] = !is_active[1];
                break;
            case COLLEGE:
                is_active[2] = !is_active[2];
                break;
            case REFERENCE:
                is_active[3] = !is_active[3];
                break;
            case ATHLETICS:
                is_active[4] = !is_active[4];
                break;
            case OTHER:
                is_active[5] = !is_active[5];
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bulletin__layout, container, false);

        seniors_layout =          view.findViewById(R.id.bulletin_SeniorsLinearLayout);
        events_layout =         view.findViewById(R.id.bulletin_EventsLinearLayout);
        college_layout =        view.findViewById(R.id.bulletin_CollegeLinearLayout);
        reference_layout =          view.findViewById(R.id.bulletin_ReferenceLinearLayout);
        athletics_layout =          view.findViewById(R.id.bulletin_AthleticsLinearLayout);
        other_layout =          view.findViewById(R.id.bulletin_OtherLinearLayout);


        final String [][] data = getInfo();
        if (data.length == 0) return view;


        frameLayouts = new FrameLayout[data.length];
        LinearLayout bulletin_item_LinearLayout = view.findViewById(R.id.bulletin_ComingUp_LinearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,0,(int)getResources().getDimension(R.dimen.Bulletin_Margin));
        for(int i = 0; i < frameLayouts.length; i++) {
            frameLayouts[i] = new FrameLayout(this.getContext());
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(getIdRange()+i);
            bulletin_item_LinearLayout.addView(frameLayouts[i],params);

            bulletin_item_LinearLayout.invalidate();
        }

        items = new Bulletin_Template[data.length];

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < items.length; i++)
                {
                    items[i] = Bulletin_Template.newInstance(data[i][0],data[i][1],data[i][2],getItemType(data[i][3]));
                    getFragmentManager()
                            .beginTransaction()
                            .add(frameLayouts[i].getId(),items[i])
                            .commit();

                }
            }
        }).start();



        return view;
    }

    private String[][] getInfo()
    {
        /*TODO: make this an Bulletin object[] return type rather than String[][] to accommodate more data types
            because having String type is fishy
        * */
        //TODO: implement this fully
        return new String[][]
                {
                        {"Title1", "Date1", "BodyText1", "Seniors"},
                        {"Title2, a very, very, very, very, very, very, very, very... long title", "Date2", "BodyText2", "College"},
                        {"Title3", "Date3", "Long body text. In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", "Events"},
                        {"Title4", "Date4", "Sign up today!", "Athletics"},
                        {"Title5", "Date4", "Robert'); DROP TABLE students;--", "Other"},
                        {"Title6", "Date5", "BodyText2. agogagaogagoagog", "College"},
                        {"Title7", "Date6", "BodyText2. Hello", "Events"},
                        {"NullPointerException", "Today", "Expected int, but got enum instead", "Other"},
                        {"Science Bowl Tryouts","April 24, 2020","Short answer written test on AP" +
                                "Bio, AP Chem, AP Physics, Earth & Space Sciences, Statistics, Math Analysis, Calculus. Candidates" +
                                "ideally should have taken an AP class and show subject mastery. No need to know all subjects. Team" +
                                "members typically show content mastery of a specific subject, not all of the subjects. Check out" +
                                "the link to watch a regional match, finals round. For questions email cmynster@ausd.net", "Reference"}


                };
    }

    public int getIdRange()
    {
        return 2000000; //Reserve 2000000-3000000 for bulletin page
    }

    public Bulletin_Page_Fragment.BulletinType getItemType(String type)
    {
        // TODO: fully implement this, because I don't know how it is currently sorted/ how the input works
        if(type.equals("Seniors"))
            return BulletinType.SENIORS;
        if(type.equals("Events"))
            return BulletinType.EVENTS;
        if(type.equals("College"))
            return BulletinType.COLLEGE;
        if(type.equals("Reference"))
            return BulletinType.REFERENCE;
        if(type.equals("Athletics"))
            return BulletinType.ATHLETICS;

        return BulletinType.OTHER;
    }

    public void onSeniorsClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.SENIORS))
            setLayoutTintActive(seniors_layout);
        else
            setLayoutTintInactive(seniors_layout);
        switch_Type_Layout_Active(BulletinType.SENIORS);
        filterItems();
    }

    public void onEventsClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.EVENTS))
            setLayoutTintActive(events_layout);
        else
            setLayoutTintInactive(events_layout);
        switch_Type_Layout_Active(BulletinType.EVENTS);
        filterItems();
    }

    public void onCollegeClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.COLLEGE))
            setLayoutTintActive(college_layout);
        else
            setLayoutTintInactive(college_layout);
        switch_Type_Layout_Active(BulletinType.COLLEGE);
        filterItems();
    }

    public void onReferenceClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.REFERENCE))
            setLayoutTintActive(reference_layout);
        else
            setLayoutTintInactive(reference_layout);
        switch_Type_Layout_Active(BulletinType.REFERENCE);
        filterItems();
    }

    public void onAthleticsClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.ATHLETICS))
            setLayoutTintActive(athletics_layout);
        else
            setLayoutTintInactive(athletics_layout);
        switch_Type_Layout_Active(BulletinType.ATHLETICS);
        filterItems();
    }

    public void onOtherClick(View view)
    {
        if(is_Type_Layout_Active(BulletinType.OTHER))
            setLayoutTintActive(other_layout);
        else
            setLayoutTintInactive(other_layout);
        switch_Type_Layout_Active(BulletinType.OTHER);
        filterItems();
    }

    public void setLayoutTintActive(View view)
    {
        ViewCompat.setBackgroundTintList(view,ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
    }

    public void setLayoutTintInactive(View view)
    {
        ViewCompat.setBackgroundTintList(view,ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
    }

    public void filterItems()
    {

        for(int i = 0; i < items.length; i++)
        {
            if(is_Type_Layout_Active(items[i].getType()))
                frameLayouts[i].setVisibility(View.VISIBLE);
            else
                frameLayouts[i].setVisibility(View.GONE);
            frameLayouts[i].invalidate();
        }

        boolean isallfalse = false;
        for(boolean b: is_active)
            isallfalse = isallfalse||b;

        if(!isallfalse)
        {
            for(FrameLayout i: frameLayouts)
                i.setVisibility(View.VISIBLE);
        }

    }

    public void hello(View view)
    {
        String string = "";
        for(int i = 0; i < frameLayouts.length; i++)
        {
            string += Integer.toString(frameLayouts[i].getHeight()) + "\n";
        }

        Toast.makeText(this.getContext(),string ,Toast.LENGTH_LONG).show();
    }
}
