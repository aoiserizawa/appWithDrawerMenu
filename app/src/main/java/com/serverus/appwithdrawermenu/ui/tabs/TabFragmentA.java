package com.serverus.appwithdrawermenu.ui.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.serverus.appwithdrawermenu.R;


/**
 * Created by alvinvaldez on 3/7/15.
 */
public class TabFragmentA extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_a, container, false);
    }
}
