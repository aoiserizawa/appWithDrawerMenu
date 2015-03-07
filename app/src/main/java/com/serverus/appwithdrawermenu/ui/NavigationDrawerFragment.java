package com.serverus.appwithdrawermenu.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.serverus.appwithdrawermenu.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME="testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private RecyclerView recyclerView;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    // adapter for recyclerview
    private MyAdapter adapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if(savedInstanceState != null ){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // get the recyclerview from xml fragment_navigation_drawer
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        // call MyAdapter class then pass the
        // context and getData() to it
        adapter = new MyAdapter(getActivity(), getData());

        // set an adapter to recyclerView
        recyclerView.setAdapter(adapter);

        // set a layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // this will let you add onItemListener on your recyclerView
        // you instantiate the RecyclerTouchListener then pass the:
        // 1. Context
        // 2. the recycler view
        // 3. the implementation of interface ClickListener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "onClick "+position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(getActivity(), "onLongClick "+position, Toast.LENGTH_LONG).show();
            }
        }));

        // Inflate the layout for this fragment
        return layout;
    }
    // this will ready the data in ArrayList for
    // the data to be bind to ViewHolder in MyAdapter.java
    // this will be pass to the constructor
    // ex.  adapter = new MyAdapter(getActivity(), getData());
    public static List<InformationRow> getData(){
        List<InformationRow> data = new ArrayList<>();

        int[] icon = {R.mipmap.ic_launcher};

        String[] titles = {"Sample 1", "Sample 2", "Sample 3", "Sample 4"};

        for (int i = 0; i < titles.length; i++) {
            InformationRow current = new InformationRow();

            current.iconId = icon[0];

            current.title = titles[i % titles.length];

            data.add(current);
        }

        return data;

    }


    public void setUp(int fragmentId,DrawerLayout drawerLayout, final Toolbar toolbar) {
        // we get the fragmentId of the drawer to call it in openDrawer()
        containerView = getActivity().findViewById(fragmentId); // the navigation drawer

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUserLearnedDrawer){ // check if the user never see the drawer before
                    mUserLearnedDrawer = true;

                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER,  mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //super.onDrawerSlide(drawerView, slideOffset);

                //slideOffset is 1-0, 1 when the drawer is fully open

                // set the dim of the action bar
                if(slideOffset < 0.6 ){
                    toolbar.setAlpha(1-slideOffset);
                }

            }
        };

        // if the user has never seen the drawer
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            // display the drawer
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                // Synchronize the state of the drawer indicator/affordance with the linked DrawerLayout.
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, preferenceValue);

        editor.apply();
    }


    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceName, defaultValue);
    }


    //we created the RecyclerTouchListener class to let the recyclerView process the onClick
    // then let the onLongClick processed by the view

    // the RecyclerView.OnItemTouchListener allows you to intercept recyclerView before each children do anything with it
    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;

        private ClickListener clickListener; // the interface that has onClick and onLongClick

        public  RecyclerTouchListener (Context context, final RecyclerView recyclerView,  final ClickListener clickListener){

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                // this will be called by the if condition in the onInterceptTouchEvent
                public boolean onSingleTapUp(MotionEvent e) {
                    // makes the onInterceptTouchEvent true
                    return true;
                }

                @Override
                // on the onLongPress we also pass it by the  gestureDetector.onTouchEvent(motionEvent)
                // but then it wont be handled by the onInterceptTouchEvent because by default it returns true
                // but it makes the event trigger by clickListener.onLongClick which will be handled by the view
                // not the recyclerView
                public void onLongPress(MotionEvent e) {
                    // find the childview on which long pressed
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if(childView != null && clickListener != null){
                        // makes the longClick trigger by the view
                        clickListener.onLongClick(childView, recyclerView.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }

        //Silently observe and/or take over touch events sent to the RecyclerView before they are handled by either the RecyclerView itself or its child views.
        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

            // this is the crucial part
            // we pass the motionEvent to gesture detector by gestureDetector.onTouchEvent(motionEvent)
            // which will be onSingleTapUp then return true and makes the onInterceptTouchEvent return true
            // then it will do the clickListener.onClick
            // because the if statement will be true on the gestureDetector part

            if(childView != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent))
            {
                // the click is now handled by the recyclerView
                clickListener.onClick(childView, recyclerView.getChildPosition(childView));
            }

            return false;
        }

        //Process a touch event as part of a gesture that was claimed by returning true from a previous call to
        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

        }
    }

    public static interface ClickListener{

        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }
}


