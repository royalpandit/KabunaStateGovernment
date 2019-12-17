package com.kapalert.kadunastategovernment.fragments;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kapalert.kadunastategovernment.R;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {


    private Context mContext;
    int tabCount;
    public SimpleFragmentPagerAdapter( int tabCount, FragmentManager fm) {
        super(fm);
        this.tabCount= tabCount;
     }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
               // Toast.makeText(SimpleFragmentPagerAdapter.this, "position"+ position , Toast.LENGTH_SHORT).show();

                CaseListFragment tab1 = new CaseListFragment(null);
                tab1.Tabselect="group";
                return tab1;
            case 1:
               // Toast.makeText(SimpleFragmentPagerAdapter.this, "position"+ position, Toast.LENGTH_SHORT).show();

                CaseListFragment tab2 = new CaseListFragment(null);
                tab2.Tabselect="personal";
                return tab2;

            default:
                return null;
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        String title = null;
       /* switch (position) {
            case 0:
                return mContext.getString(R.string.Gr);
            case 1:
                return mContext.getString(R.string.category_places);
            case 2:
                return mContext.getString(R.string.category_food);
            case 3:
                return mContext.getString(R.string.category_nature);
            default:
                return null;
        }*/


        if (position == 0)
        {
            title = "Group";
        }
        else if (position == 1)
        {
            title = "Personnal";
        }
        return title;
    }
}
