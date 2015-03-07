package com.serverus.appwithdrawermenu.activities;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.serverus.appwithdrawermenu.ui.NavigationDrawerFragment;
import com.serverus.appwithdrawermenu.R;
import com.serverus.appwithdrawermenu.ui.tabs.SlidingTabLayout;
import com.serverus.appwithdrawermenu.ui.tabs.TabFragmentA;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

                                                                //this interface is needed for the tab
public class MainActivity extends ActionBarActivity implements MaterialTabListener{
    private Toolbar toolbar;

    // variables for tab and pager
    private MaterialTabHost tabHost;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // getting the drawer fragment
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        // passing the values for the drawer
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        // ######################## TABS #######################
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        // eto ung mag aupdate ng tab position kapag nag swipe
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // eto ung mag lalabas ng tab sa may navigation
        for (int i = 0; i < viewPagerAdapter.getCount(); i++){
            tabHost.addTab(
                    tabHost.newTab()
                    .setText(viewPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        // ######################## END OF TABS #######################

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        if(id == R.id.navigate){
            startActivity(new Intent(this, SubActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    // eto ung mag ttrigger kpag nag select ka ng tabs
    public void onTabSelected(MaterialTab materialTab) {
        //tatawagin nya si setCurrentItem para ma set ung laman nung tab
        viewPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    //adapter for tabs
    class ViewPagerAdapter extends FragmentPagerAdapter{

        String[] tabs;

        //constructor
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            // kinuha mo ung items ng magiging title ng tabs
            // sa strings.xml
            tabs =  getResources().getStringArray(R.array.tabs);
        }


        @Override
        // eto ung tatawag sa magiging laman ng tabs
        public Fragment getItem(int position) {
            // isang example is tinawag mo ung TabFragment class
            // pra gumawa ng layout na may ibat ibang laman using 1 layout lang
            TabFragment tabFragment = TabFragment.getInstance(position);


            Fragment fragment = null;
            if(position == 0){
                // ang magging laman ng tab kung 0 is ung tab_fragment_a.xml
                fragment = new TabFragmentA();
                return fragment;

            }else{
                return tabFragment;
            }

        }

        @Override
        // kinuha mo ung page title galing sa tabs array
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        // number of tabs
        public int getCount() {
            return 6;
        }
    }


    // this class is to create a pages
    // eto ung ginamit sa tutorial kasi isang xml lang ang gamit pero nag babago ung laman
    // pwedeng hindi mo to gamitin kasi pwede mo ideclare ung fragment sa loob ng getItem()
    // kay ViewPagerAdapter na class
    public static  class TabFragment extends Fragment{
        private TextView textView;


        // kapag tinawag mo to at nag pasa ka ng position kay getInstance
        // gagawa sya ng panibagong page
        public static TabFragment getInstance(int position){
            TabFragment tabFragment = new TabFragment();

            // tinawag natin si bundle para mag pasa ng arguments
            Bundle args = new Bundle();

            // nag nag pasok ka kay args na instance na instance ni Bundle ng INT
            // key value pair sila
            args.putInt("position", position);

            tabFragment.setArguments(args);

            return tabFragment;
        }


        @Override
        // kapag tinawag mo si instance ng TabFragment na ganto
        // TabFragment tabFragment = TabFragment.getInstance(position);
        // gagawa sya ng panibagong layout
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // ang pag babatayan nya ng layout is fragment_tab.xml
            View layout = inflater.inflate(R.layout.fragment_tab, container, false );
            textView = (TextView) layout.findViewById(R.id.position);

            // kinuha mo ung arguments kay Bundle
            Bundle bundle = getArguments();

            if(bundle != null ) {
                textView.setText("The page selected is "+bundle.getInt("position"));
            }
            return layout;
        }
    }

}
