package com.example.jin.materialdesign.acctivities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jin.materialdesign.acctivities.auth.UserInfoManageActivity;
import com.example.jin.materialdesign.fragments.NavigationDrawerFragment;
import com.example.jin.materialdesign.R;
import com.example.jin.materialdesign.acctivities.auth.LoginActivity;
import com.example.jin.materialdesign.fragments.ListFragment;
import com.example.jin.materialdesign.fragments.MyBidListFragment;
import com.example.jin.materialdesign.fragments.TaskMapFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends ActionBarActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private ViewPager mPager;
    private MaterialTabHost tabHost;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.pager);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOffscreenPageLimit(5);
//        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
//        mTabs.setDistributeEvenly(true);
//
////        mTabs.setBackgroundColor(getResources().getColor(R.color.icons));
////        mTabs.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
//        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.accent);
//            }
//        });
//        mTabs.setCustomTabView(R.layout.custom_tab_view, R.id.tabText);
//        mTabs.setViewPager(mPager);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab().setText(adapter.getPageTitle(i))
            .setTabListener(this)
            );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SharedPreferences pref = getSharedPreferences("pref",
                MODE_PRIVATE);
        if(pref.getBoolean("is_logged_in", false)){
            MenuItem item = menu.findItem(R.id.login);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.signUp);
            item2.setVisible(false);
            this.invalidateOptionsMenu();

            MenuItem username = menu.findItem(R.id.username);
            username.setTitle(pref.getString("username",""));
        } else {
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(false);
            MenuItem item3 = menu.findItem(R.id.userInfo);
            item3.setVisible(false);
            this.invalidateOptionsMenu();

            MenuItem username = menu.findItem(R.id.username);
            username.setTitle(pref.getString("username", ""));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username", "");
            editor.putBoolean("is_logged_in", false);
            editor.commit();

            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

        if (id == R.id.login) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        if (id == R.id.userInfo) {
            Intent intent = new Intent(this, UserInfoManageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            overridePendingTransition(R.anim.slide_in_right, R.anim.hold);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        int icons[] = {R.drawable.ic_place_black_24dp, R.drawable.ic_local_library_black_24dp, R.drawable.ic_rate_review_black_24dp};
        String[] tabs;
        ImageButton.OnClickListener imgBtnClickListener;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0){
                return new TaskMapFragment().getInstance();
            } else if (position == 1){
                return new ListFragment().getInstance(position);
            } else {
                return new MyBidListFragment().getInstance();
            }


        }

        @Override
        public CharSequence getPageTitle(int position) {

//            Drawable drawable = getResources().getDrawable(icons[position]);
//            drawable.setBounds(0, 0, 36, 36);
//            ImageSpan imageSpan = new ImageSpan(drawable);
//            SpannableString spannableString = new SpannableString(" ");
//            spannableString.setSpan(imageSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return getResources().getStringArray(R.array.tabs)[position];
        }

        public Drawable getIcon(int possition) {
            return getResources().getDrawable(icons[possition]);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
