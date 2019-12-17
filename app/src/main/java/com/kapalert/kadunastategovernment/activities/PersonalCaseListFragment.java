package com.kapalert.kadunastategovernment.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.fragments.CaseListFragment;
import com.kapalert.kadunastategovernment.fragments.SimpleFragmentPagerAdapter;
import com.kapalert.kadunastategovernment.utils.Utils;

public class PersonalCaseListFragment extends Fragment  implements View.OnClickListener {
    ViewPager viewPager;
    String seltype="";
    Button group,persona;
CaseListFragment caseListFragment;

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_tab, container, false);
         group =  view.findViewById(R.id.group);
         persona =   view.findViewById(R.id.persona);


         group.setOnClickListener(this);
         persona.setOnClickListener(this);
         Utils.goToFragment(getActivity(), new CaseListFragment(null), R.id.frame_container, false);

         //loadFragment(new CaseListFragment());
       /* TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
         tabLayout.addTab(tabLayout.newTab().setText("Group"));
         tabLayout.addTab(tabLayout.newTab().setText("Personnal"));

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(tabLayout.getTabCount(), getFragmentManager());

        // Set the adapter onto the view pager
          viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
          tabLayout.setOnTabSelectedListener(this);*/
        return view;
    }
/*    http://kapalert.net/KadunaStateMOJ/api/caseblock/dpp_case_list?token=3faa80f591e69d25c753f90d22ad2da6&user_id=157&casetype=Civil&type=group */
    @Override
    public void onClick(View v) {
        if (v==group){
            group.setBackgroundColor(group.getContext().getResources().getColor(R.color.red));
            group.setClickable( false );
            persona.setBackgroundColor(group.getContext().getResources().getColor(R.color.grey));
            persona.setClickable( true );
            //group.setBackgroundColor(group.getContext().getResources().getColor(R.color.red));
            Bundle bundle = new Bundle();
            bundle.putString("seltype", "group");

            Utils.goToFragment(getActivity(), new CaseListFragment(bundle), R.id.frame_container, false);


           /* CaseListFragment tab1 = new CaseListFragment();
            tab1.Tabselect="group";*/

        }if (v==persona){
            group.setBackgroundColor(group.getContext().getResources().getColor(R.color.grey));
            group.setClickable( true );
            persona.setBackgroundColor(group.getContext().getResources().getColor(R.color.red));
            persona.setClickable( false );
            Bundle bundle = new Bundle();
            bundle.putString("seltype", "personal");
            Utils.goToFragment(getActivity(), new CaseListFragment(bundle), R.id.frame_container, false);

           /* if (getFragmentManager().findFragmentById(R.id.home_container) instanceof CaseListFragment) {
            } else {
                getFragmentManager().beginTransaction().replace(R.id.home_container, new CaseListFragment()).addToBackStack(null).commit();
            }*/
           /*

            CaseListFragment tab1 = new CaseListFragment();
            tab1.Tabselect="personal";
*/
        }
    }


   /* @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        String title = null;
        CaseListFragment tab1 = new CaseListFragment();
        if (tab.getPosition() == 0)
        {
            Toast.makeText(getActivity(), "positionif"+tab.getPosition(), Toast.LENGTH_SHORT).show();


            tab1.Tabselect="group";
            seltype="group";
            Log.d("1213","if");
tab1.setupList(true);



            tab1.setupclass(seltype);
        }else {
            Toast.makeText(getActivity(), "positionelse"+tab.getPosition(), Toast.LENGTH_SHORT).show();

           // CaseListFragment tab1 = new CaseListFragment();
            tab1.Tabselect="personal";
            seltype="personal";
          //  tab1.setupList(true);
            Log.d("1212","else");
            tab1.setupList(true);

        }

        tab1.setupList(true);



    //  Toast.makeText(getActivity(), "position"+tab.getPosition(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }*/
}
