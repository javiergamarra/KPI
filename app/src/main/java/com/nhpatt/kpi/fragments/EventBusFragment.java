package com.nhpatt.kpi.fragments;

import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;

/**
 * @author Javier Gamarra
 */
public class EventBusFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }
}
