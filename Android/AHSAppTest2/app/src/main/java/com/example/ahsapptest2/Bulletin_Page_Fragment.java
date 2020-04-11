package com.example.ahsapptest2;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
    boolean[] is_active = {false,false,false,false,false,false};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bulletin__layout, container, false);


         seniors_layout =          view.findViewById(R.id.bulletin_SeniorsLinearLayout);
         events_layout=         view.findViewById(R.id.bulletin_EventsLinearLayout);
         college_layout=        view.findViewById(R.id.bulletin_CollegeLinearLayout);
        reference_layout=          view.findViewById(R.id.bulletin_ReferenceLinearLayout);
        athletics_layout=          view.findViewById(R.id.bulletin_AthleticsLinearLayout);
        other_layout=          view.findViewById(R.id.bulletin_OtherLinearLayout)    ;


        String [][] data = getInfo();
        if (data.length == 0) return view;


        frameLayouts = new FrameLayout[data.length];
        LinearLayout bulletin_item_LinearLayout = view.findViewById(R.id.bulletin_ComingUp_LinearLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0,0,0,(int)getResources().getDimension(R.dimen.Bulletin_Margin));
    System.out.println("Margins successful");
        for(int i = 0; i < frameLayouts.length; i++) {
            frameLayouts[i] = new FrameLayout(this.getContext());
            frameLayouts[i].setLayoutParams(new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,  //width
                    FrameLayout.LayoutParams.WRAP_CONTENT   //height
            ));
            frameLayouts[i].setId(getIdRange()+i);
            bulletin_item_LinearLayout.addView(frameLayouts[i],params);

            System.out.println("Adding successful for fragment" + i);
            bulletin_item_LinearLayout.invalidate();
        }
System.out.println(bulletin_item_LinearLayout.getChildCount());
        items = new Bulletin_Template[data.length];

        for (int i = 0; i < items.length; i++)
        {
            items[i] = Bulletin_Template.newInstance(data[i][0],data[i][1],data[i][2],getItemType(data[i][3]));
            getFragmentManager()
                    .beginTransaction()
                    .add(frameLayouts[i].getId(),items[i])
                    .commit();
            System.out.println("Commit successful for fragment" + i);

        }


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
        if(is_active[0])
        {
            seniors_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[0] = false;
            filterItems();
        }
        else
        {
            seniors_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[0] = true;
            filterItems();
        }
    }

    public void onEventsClick(View view)
    {
        if(is_active[1])
        {
            events_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[1] = false;
            filterItems();
        }
        else
        {
            events_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[1] = true;
            filterItems();
        }
    }

    public void onCollegeClick(View view)
    {
        if(is_active[2])
        {
            college_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[2] = false;
            filterItems();
        }
        else
        {
            college_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[2] = true;
            filterItems();
        }
    }

    public void onReferenceClick(View view)
    {
        if(is_active[3])
        {
            reference_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[3] = false;
            filterItems();
        }
        else
        {
            reference_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[3] = true;
            filterItems();
        }
    }

    public void onAthleticsClick(View view)
    {
        if(is_active[4])
        {
            athletics_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[4] = false;
            filterItems();
        }
        else
        {
            athletics_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[4] = true;
            filterItems();
        }
    }

    public void onOtherClick(View view)
    {
        if(is_active[5])
        {
            other_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.Crimson_93424E__Bulletin));
            is_active[5] = false;
            filterItems();
        }
        else
        {
            other_layout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.GoldenYellow_E8BC66__Home_Bulletin));
            is_active[5] = true;
            filterItems();
        }
    }

    public void filterItems()
    {

        for(int i = 0; i < items.length; i++)
        {
            switch(items[i].getType())
            {
                case SENIORS:
                    if(is_active[0])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                    break;
                case EVENTS:
                    if(is_active[1])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                    break;
                case COLLEGE:
                    if(is_active[2])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                    break;
                case REFERENCE:
                    if(is_active[3])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                    break;
                case ATHLETICS:
                    if(is_active[4])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                    break;
                case OTHER:
                    if(is_active[5])
                        frameLayouts[i].setVisibility(View.VISIBLE);
                    else
                        frameLayouts[i].setVisibility(View.GONE);
                default:
                    break;
            }
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
