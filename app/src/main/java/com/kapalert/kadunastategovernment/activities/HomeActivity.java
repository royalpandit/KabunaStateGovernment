package com.kapalert.kadunastategovernment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kapalert.kadunastategovernment.R;
import com.kapalert.kadunastategovernment.fragments.AnnouncementFragment;
import com.kapalert.kadunastategovernment.fragments.CalendarFragment;
import com.kapalert.kadunastategovernment.fragments.CaseListFragment;
import com.kapalert.kadunastategovernment.fragments.FileCaseListFragment;
import com.kapalert.kadunastategovernment.fragments.InboxFragment;
import com.kapalert.kadunastategovernment.templates.UserInfoJson;
import com.kapalert.kadunastategovernment.utils.AppBaseActivity;
import com.kapalert.kadunastategovernment.utils.Constants;
import com.kapalert.kadunastategovernment.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppBaseActivity implements View.OnClickListener {

    private DrawerLayout mDrawer;
    private LinearLayout mCaseManagement, mFileManagement, mCalendar, mAnnouncements, mChats;
    private CircleImageView mImage;
    private ImageView mLogout;
    private boolean mExit;
    private long BACK_PRESSED_MILLIS = 2000;

    private static HomeActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instance = this;
        mDrawer =  findViewById(R.id.drawer_layout);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.ic_nav_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else{
                    drawer.openDrawer(GravityCompat.START);}

            }
        });


        initViews();
    }

    private void initViews() {

        mCaseManagement = (LinearLayout) findViewById(R.id.case_management);
        mFileManagement = (LinearLayout) findViewById(R.id.file_management);
        mCalendar = (LinearLayout) findViewById(R.id.calendar);
        mAnnouncements = (LinearLayout) findViewById(R.id.announcements);
        mChats = (LinearLayout) findViewById(R.id.chats);

        mImage = (CircleImageView) findViewById(R.id.user_image);
        mLogout = (ImageView) findViewById(R.id.logout);

        mCaseManagement.setOnClickListener(this);
        mFileManagement.setOnClickListener(this);
        mCalendar.setOnClickListener(this);
        mAnnouncements.setOnClickListener(this);
        mChats.setOnClickListener(this);
        mImage.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        mCaseManagement.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
      ///  Utils.loadImage(mContext, userPOJO.getImageURL(), mImage, R.drawable.ic_user_placeholder, R.drawable.ic_user_placeholder);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.case_management:
                //new PersonalCaseListFragment();
                caseManagementClick();
                break;
            case R.id.calendar:
                Utils.goToFragment(mContext, new CalendarFragment(), R.id.home_container, false);
                break;
            case R.id.file_management:
                Utils.goToFragment(mContext, new FileCaseListFragment(), R.id.home_container, false);
                break;
            case R.id.announcements:
                Utils.goToFragment(mContext, new AnnouncementFragment(), R.id.home_container, false);
                break;
            case R.id.chats:
                Utils.goToFragment(mContext, new InboxFragment(), R.id.home_container, false);
                break;
            case R.id.user_image:
                Utils.intentToActivity(mContext, ProfileActivity.class);
                break;
            case R.id.logout:
                logoutClick();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
    }

    private void logoutClick() {
        Utils.showDialog(mContext, getString(R.string.logout_sure_text), getString(R.string.logout), getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Constants.setUser(mContext, null);
                dialogInterface.dismiss();
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, null, getString(R.string.no), null, null, true);
    }

    private void caseManagementClick() {

        UserInfoJson.UserData user = Constants.getUser(getApplicationContext());

        if (user.userRole.equalsIgnoreCase(Constants.USER_ROLE_DPP)) {

            Utils.goToFragment(mContext, new PersonalCaseListFragment(), R.id.home_container, false);

        }else
            Utils.goToFragment(mContext, new CaseListFragment(null), R.id.home_container, false);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mExit) {
                super.onBackPressed();
            } else {
                mExit = true;
                Utils.showToast(mContext, getString(R.string.press_again_to_exit));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mExit = false;
                    }
                }, BACK_PRESSED_MILLIS);
            }
        }
    }

    public static HomeActivity getInstance() {
        return instance;
    }

//    public void updateList() {
//        Fragment fragment = getVisibleFragment();
//        if (fragment != null) {
//            if (fragment instanceof CaseListFragment) {
//                CaseListFragment caseListFragment = (CaseListFragment) fragment;
//                caseListFragment.setupList();
//            }
//        }
//    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
